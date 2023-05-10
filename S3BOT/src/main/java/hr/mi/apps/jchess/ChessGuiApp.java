package hr.mi.apps.jchess;

import hr.mi.apps.bridges.GUIUserBridge;
import hr.mi.apps.jchess.components.JChessBoard;
import hr.mi.chess.algorithm.support.OpeningBook;
import hr.mi.chess.game.ChessGame;
import hr.mi.chess.player.Player;
import hr.mi.chess.player.ai.PlayerAlan;
import hr.mi.support.FromToPair;
import hr.mi.chess.player.human.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChessGuiApp extends JFrame {

    private final ChessGame game;

    public ChessGuiApp() {
        BlockingQueue<FromToPair> blockingQueue = new ArrayBlockingQueue<>(1);

        Object[] options = {"AI vs AI",
                "Play as White",
                "Play as Black",
                "Human vs Human"};
        int choseSide = JOptionPane.showOptionDialog(this,
                "Chose your side!",
                "Chose side",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        Player blackPlayer = null;
        Player whitePlayer = null;

        if ((choseSide & 1) != 0){
            whitePlayer = new HumanPlayer(new GUIUserBridge(blockingQueue));
        } else {
            whitePlayer = new PlayerAlan("baron30.bin", OpeningBook.WEIGHTED_RANDOM_MOVE);
        }

        if ((choseSide & 2) != 0){
            blackPlayer = new HumanPlayer(new GUIUserBridge(blockingQueue));
        } else {
            blackPlayer = new PlayerAlan("baron30.bin", OpeningBook.WEIGHTED_RANDOM_MOVE);
        }

        game = new ChessGame(whitePlayer, blackPlayer);
        InputManager inputManager = new InputManager(blockingQueue, game.getBoardState());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(700, 700);
        setTitle("JChess");

        this.getContentPane().setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        JChessBoard chessBoard = new JChessBoard(game, choseSide == 2);
        this.add(chessBoard);
        chessBoard.addListener(inputManager::tileSelected);
    }

    private void close() {
        game.saveToDisc();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGuiApp().setVisible(true));
    }
}
