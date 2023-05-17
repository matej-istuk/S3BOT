package hr.mi.apps.jchess.components;

import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.GameStateEnum;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.models.Move;
import hr.mi.chess.util.BoardFunctions;

import javax.swing.*;
import java.awt.*;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing component which acts as a chess board. Encapsulates a chess game and provides a way of communicating with the
 * game.
 */
public class JChessBoard extends JComponent {

    private final JChessTile[] tiles;
    private final ChessGame game;
    private final List<TileClickListener> listeners;

    /**
     * The constructor.
     * @param game the game which is shown on the Board.
     * @param flip is the board flipped (for example if the human player is playing black)
     */
    public JChessBoard(ChessGame game, boolean flip){
        this.tiles = new JChessTile[64];
        this.game = game;
        this.listeners = new ArrayList<>();
        this.setLayout(new GridLayout(8, 8, 0, 0));
        for (int i = 7; i >= 0; i--){
            for (int j = 0; j < 8; j++){
                addTile(flip ? 7-i : i, flip ? 7-j : j);
            }
        }

        repaintTiles();
        this.game.addGameListener(this::repaintTiles);

        SwingWorker<GameStateEnum, Object> swingWorker = new SwingWorker<>() {

            /**
             * Computes a result, or throws an exception if unable to do so.
             *
             * <p>
             * Note that this method is executed only once.
             *
             * <p>
             * Note: this method is executed in a background thread.
             *
             * @return the computed result
             * @throws Exception if unable to compute a result
             */
            @Override
            protected GameStateEnum doInBackground() throws Exception {
                try {
                    GameStateEnum gameState;


                    do {
                        gameState = game.advance();
                    } while (gameState == GameStateEnum.IN_PROGRESS);

                    JOptionPane.showMessageDialog(JChessBoard.this, "Game result: " + gameState.name());

                    return gameState;
                } catch (Exception e){
                    System.out.println(e.toString());
                    System.out.println(e.getMessage());

                }
                return null;
            }
        };

        swingWorker.execute();

    }

    /**
     * Adds a tile at the requested row and column of the board.
     * @param row the row
     * @param column the column
     */
    private void addTile(int row, int column){
        JChessTile tile = new JChessTile(8*row + column);
        tile.setBorder(BorderFactory.createEmptyBorder());
        setTileColour(tile);
        this.add(tile);
        this.tiles[8*row+column] = tile;
        tile.addActionListener(o -> fireListeners(tile.getLERFIndex()));
    }

    /**
     * Repaints the tiles and the pieces on them to represent the current board state.
     */
    private void repaintTiles(){
        int moveFrom = -1;
        int moveTo = -1;
        if (game.getBoardState().getPreviousMoves().size() != 0){
            Move previousMove = game.getBoardState().getPreviousMoves().get(game.getBoardState().getPreviousMoves().size()-1);
            moveFrom = previousMove.getFrom();
            moveTo = previousMove.getTo();
        }

        for (JChessTile tile: tiles){
            setTileColour(tile, tile.getLERFIndex() == moveTo || tile.getLERFIndex() == moveFrom);

            ChessPiece piece = BoardFunctions.getPieceByBitboard(game.getBoardState().getBitboards(), (1L << tile.getLERFIndex()));
            tile.setVisible(true);

            if (piece == null){
                tile.clearTile();
            }
            else {
                tile.drawPiece(piece);
            }
        }
    }

    /**
     * Sets the colour of the tile (black or white).
     * @param tile the tile whose colour is being set
     */
    private void setTileColour(JChessTile tile){
        setTileColour(tile, false);
    }

    /**
     * Sets the colour of the tile, and adds an option of tile highlighting.
     * @param tile the tile whose colour is being set
     * @param isHighlighted should the tile be highlighted (for example if a piece was moved to/from this tile)
     */
    private void setTileColour(JChessTile tile, boolean isHighlighted){
        int row = tile.getLERFIndex()/8;
        int column = tile.getLERFIndex()%8;
        if (isHighlighted){
            tile.setBackground((row % 2 == column % 2) ? new Color(0xAAA23A) : new Color(0xCDD26A));
        } else {
            tile.setBackground((row % 2 == column % 2) ? new Color(0xB58863) : new Color(0xF0D9B5));
        }
    }

    /**
     * Activates every subscribed listener that a tile has been clicked
     * @param tileIndex the LERF index of the tile clicked
     */

    private void fireListeners(int tileIndex){
        listeners.forEach(o -> o.tileClicked(tileIndex));
    }

    /**
     * Subscribes a listener.
     * @param listener listener
     */
    public void addListener(TileClickListener listener){
        listeners.add(listener);
    }

    /**
     * Unsubscribes a listener
     * @param listener listener
     */
    public void removeListener(TileClickListener listener){
        listeners.remove(listener);
    }
}
