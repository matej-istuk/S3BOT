package hr.mi.apps.jchess.components;

import hr.mi.apps.jchess.util.IconProvider;
import hr.mi.chess.models.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class JChessTile extends JButton {
    private final int LERFIndex;

    /**
     * Creates a button with no set text or icon.
     */
    public JChessTile(int LERFIndex) {
        this.LERFIndex = LERFIndex;
    }

    public int getLERFIndex() {
        return LERFIndex;
    }

    public void clearTile(){
        this.setIcon(null);
    }

    public void drawPiece(ChessPiece chessPiece){
        Image image = IconProvider.getInstance().getPieceIcon(chessPiece);
        this.setIcon(new ImageIcon(image.getScaledInstance(85, 85, Image.SCALE_SMOOTH)));
    }
}
