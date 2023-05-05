package hr.mi.apps.jchess.components;

import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.GameStateEnum;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.models.Move;
import hr.mi.chess.util.BoardFunctions;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JChessBoard extends JComponent {

    private final JChessTile[] tiles;
    private final ChessGame game;
    private final List<TileClickListener> listeners;

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
                GameStateEnum gameState;

                LocalDateTime now = LocalDateTime.now();
                String startTime = String.format("%d-%d-%d_%d:%d:%d", now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());

                do {
                    System.out.println(game.getBoardState().getZobristHash());
                    gameState = game.advance();
                } while (gameState == GameStateEnum.IN_PROGRESS);

                String error = "";
                Path gamePath = Path.of("games" + File.separator + "chess_game_" + startTime);
                gamePath.getParent().toFile().mkdirs();
                try (BufferedWriter writer = Files.newBufferedWriter(gamePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                    for (Move move: game.getBoardState().getPreviousMoves()){
                        writer.write(move.toString() + " ");
                    }
                } catch (Exception e) {
                    error = e.toString();
                }

                JOptionPane.showMessageDialog(JChessBoard.this, "Game result: " + gameState.name() + error);

                return gameState;
            }
        };

        swingWorker.execute();

    }

    private void addTile(int row, int column){
        JChessTile tile = new JChessTile(8*row + column);
        tile.setBorder(BorderFactory.createEmptyBorder());
        setTileColour(tile);
        this.add(tile);
        this.tiles[8*row+column] = tile;
        tile.addActionListener(o -> fireListeners(tile.getLERFIndex()));
    }

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

    private void setTileColour(JChessTile tile){
        setTileColour(tile, false);
    }

    private void setTileColour(JChessTile tile, boolean isHighlighted){
        int row = tile.getLERFIndex()/8;
        int column = tile.getLERFIndex()%8;
        if (isHighlighted){
            tile.setBackground((row % 2 == column % 2) ? new Color(0xAAA23A) : new Color(0xCDD26A));
        } else {
            tile.setBackground((row % 2 == column % 2) ? new Color(0xB58863) : new Color(0xF0D9B5));
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
