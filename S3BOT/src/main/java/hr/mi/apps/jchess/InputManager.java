package hr.mi.apps.jchess;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.support.FromToPair;
import hr.mi.chess.util.BoardFunctions;

import java.util.concurrent.BlockingQueue;

/**
 * Input manager for the <code>ChessGuiApp</code>. Functions as a final state machine. If the user clicks a legal piece
 * to move, it moves the user into a second state, and then if the user selects a tile to move to (doesn't have to be
 * legal), it sends the move forward.
 */
public class InputManager {
    private final BlockingQueue<FromToPair> blockingQueue;
    private final BoardState boardState;
    private int fromTile;

    /**
     * The constructor
     * @param blockingQueue blocking queue to which the moves will be sent
     * @param boardState the board-state.
     */
    public InputManager(BlockingQueue<FromToPair> blockingQueue, BoardState boardState) {
        this.blockingQueue = blockingQueue;
        this.boardState = boardState;
        fromTile = -1;
    }

    /**
     * Works the fsm. If the user clicks a legal piece
     * to move, it moves the user into a second state, and then if the user selects a tile to move to (doesn't have to be
     * legal), it sends the move forward
     * @param tileIndex which tile was pressed
     */
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
