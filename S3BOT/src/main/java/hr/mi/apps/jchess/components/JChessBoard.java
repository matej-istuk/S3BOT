package hr.mi.apps.jchess.components;

import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.GameStateEnum;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.BoardFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JChessBoard extends JComponent {

    private final JChessTile[] tiles;
    private final ChessGame game;
    private final List<TileClickListener> listeners;

    public JChessBoard(ChessGame game){
        this.tiles = new JChessTile[64];
        this.game = game;
        this.listeners = new ArrayList<>();
        this.setLayout(new GridLayout(8, 8, 0, 0));
        for (int i = 7; i >= 0; i--){
            for (int j = 0; j < 8; j++){
                addTile(i, j);
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
                GameStateEnum gameState;
                do {
                    gameState = game.advance();
                } while (gameState == GameStateEnum.IN_PROGRESS);

                return gameState;
            }
        };

        swingWorker.execute();

    }

    private void addTile(int row, int column){
        JChessTile tile = new JChessTile(8*row + column);
        tile.setBorder(BorderFactory.createEmptyBorder());
        tile.setBackground((row % 2 == column % 2) ? new Color(207, 138, 70) : new Color(0xFDCC9D));
        this.add(tile);
        this.tiles[8*row+column] = tile;
        tile.addActionListener(o -> fireListeners(tile.getLERFIndex()));
    }

    private void repaintTiles(){
        for (JChessTile tile: tiles){
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

    private void fireListeners(int tileIndex){
        listeners.forEach(o -> o.tileClicked(tileIndex));
    }

    public void addListener(TileClickListener listener){
        listeners.add(listener);
    }

    public void removeListener(TileClickListener listener){
        listeners.remove(listener);
    }
}
