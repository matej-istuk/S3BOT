package hr.mi.apps.jchess.components;

import hr.mi.apps.jchess.util.IconProvider;
import hr.mi.chess.models.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Chessboard tile, extends a button so that the user can click it to move pieces. Can draw a piece on itself.
 */
public class JChessTile extends JButton {
    private final int LERFIndex;

    /**
     * Creates a button with no set text or icon.
     */
    public JChessTile(int LERFIndex) {
        this.LERFIndex = LERFIndex;
    }

    /**
     * Returns the LERF index of the tile
     * @return int
     */
    public int getLERFIndex() {
        return LERFIndex;
    }

    /**
     * Clears any image on the tile.
     */
    public void clearTile(){
        this.setIcon(null);
    }

    /**
     * Draws a piece on the tile
     * @param chessPiece
     */
    public void drawPiece(ChessPiece chessPiece){
        Image image = IconProvider.getInstance().getPieceIcon(chessPiece);
        this.setIcon(new ImageIcon(image.getScaledInstance(85, 85, Image.SCALE_SMOOTH)));
    }
}
