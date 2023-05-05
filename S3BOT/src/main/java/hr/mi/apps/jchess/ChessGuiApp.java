package hr.mi.apps.jchess;

import hr.mi.apps.bridges.GUIUserBridge;
import hr.mi.apps.jchess.components.JChessBoard;
import hr.mi.chess.game.ChessGame;
import hr.mi.chess.player.Player;
import hr.mi.chess.player.ai.PlayerAlan;
import hr.mi.chess.player.ai.PlayerRandy;
import hr.mi.chess.player.human.FromToPair;
import hr.mi.chess.player.human.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChessGuiApp extends JFrame {

    private ChessGame game;

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
            whitePlayer = new PlayerAlan();
        }

        if ((choseSide & 2) != 0){
            blackPlayer = new HumanPlayer(new GUIUserBridge(blockingQueue));
        } else {
            blackPlayer = new PlayerAlan();
        }

        game = new ChessGame(whitePlayer, blackPlayer);
        InputManager inputManager = new InputManager(blockingQueue, game.getBoardState());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(700, 700);
        setTitle("JChess");

        this.getContentPane().setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        JChessBoard chessBoard = new JChessBoard(game, choseSide == 2);
        this.add(chessBoard);
        chessBoard.addListener(inputManager::tileSelected);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGuiApp().setVisible(true));
    }
}
