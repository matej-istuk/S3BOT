package hr.mi.apps.jchess;

import hr.mi.apps.jchess.components.JChessBoard;

import javax.swing.*;
import java.awt.*;

public class ChessGuiApp extends JFrame {
    public ChessGuiApp() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(700, 700);
        setTitle("JChess");
        initGui();
    }

    private void initGui(){
        this.getContentPane().setLayout(new BorderLayout());
        this.add(new JChessBoard(), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGuiApp().setVisible(true));
    }
}
