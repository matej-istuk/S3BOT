package hr.mi.apps.jchess;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.support.FromToPair;
import hr.mi.chess.util.BoardFunctions;

import java.util.concurrent.BlockingQueue;

public class InputManager {
    private final BlockingQueue<FromToPair> blockingQueue;
    private final BoardState boardState;
    private int fromTile;

    public InputManager(BlockingQueue<FromToPair> blockingQueue, BoardState boardState) {
        this.blockingQueue = blockingQueue;
        this.boardState = boardState;
        fromTile = -1;
    }

    public void tileSelected(int tileIndex){
        if (tileIndex < 0 || tileIndex > 63){
            throw new IllegalArgumentException();
        }

        ChessPiece piece = BoardFunctions.getPieceByBitboard(boardState.getBitboards(), (1L << tileIndex));

        //if the selected tile contains a piece of the active colour, it becomes the fromTile
        if (piece != null && piece.getColour() == boardState.getActiveColour()){
            fromTile = tileIndex;
        }
        //otherwise, if a from tile already exists, send the fromToPair
        else if (fromTile != -1){
            blockingQueue.clear();

            FromToPair fromToPair = new FromToPair(fromTile, tileIndex);
            boolean putSuccess = false;
            do {
                try {
                    blockingQueue.put(fromToPair);
                    putSuccess = true;
                } catch (InterruptedException ignored){}
            } while (!putSuccess);

            fromTile = -1;
        }
    }
}
