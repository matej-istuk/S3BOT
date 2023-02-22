package hr.mi.apps.jchess;

import hr.mi.apps.jchess.components.JChessBoard;
import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.GameStateEnum;
import hr.mi.chess.player.Player;
import hr.mi.chess.player.ai.PlayerAlan;
import hr.mi.chess.player.ai.PlayerRandy;

import javax.swing.*;
import java.awt.*;

public class ChessGuiApp extends JFrame {

    private final ChessGame game;

    public ChessGuiApp() {
        Player whitePlayer = new PlayerAlan(5);
        Player blackPlayer = new PlayerAlan(5);
        game = new ChessGame(whitePlayer, blackPlayer);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(700, 700);
        setTitle("JChess");
        initGui();
    }

    private void initGui(){
        this.getContentPane().setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        JChessBoard chessBoard = new JChessBoard(game);
        this.add(chessBoard);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGuiApp().setVisible(true));
    }
}
