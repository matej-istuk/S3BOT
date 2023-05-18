package hr.mi.chess.algorithm.support;

import hr.mi.chess.models.Move;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class which acts as storage for the <code>GameStateSearch</code>, storing killer moves and transposition tables.
 * @author Matej Istuk
 */
public class SearchInfo {
    private final static int MAX_PLY = 125;
    private final static int MAX_KILLER_MOVES = 2;
    private final Move[][] killerMoves = new Move[MAX_PLY][MAX_KILLER_MOVES];

    /**
     * Adds a killer move to the received ply
     * @param currentPly depth of search at which the killer move was found
     * @param killerMove the killer move
     */
    public void addKillerMove(int currentPly, Move killerMove){
        Move firstKillerMove = killerMoves[currentPly][0];

        //The new killer move can't be the same as the most recent one added
        if (!Objects.equals(firstKillerMove, killerMove)){
            for (int i = MAX_KILLER_MOVES - 1; i > 0; i--) {
                killerMoves[currentPly][i] = killerMoves[currentPly][i-1];
            }

            killerMoves[currentPly][0] = killerMove;
        }
    }

    /**
     * Checks if the received move is killer.
     * @param currentPly current depth of search
     * @param move candidate move
     * @return true if the move is a killer move, false if not
     */
    public boolean checkIfKiller(int currentPly, Move move) {
        for (Move killerMove: killerMoves[currentPly]) {
            if (Objects.equals(move, killerMove)){
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all stored killer moves.
     */
    public void clearKillerMoves() {
        Arrays.stream(killerMoves).forEach(arr -> Arrays.fill(arr, null));
    }

    public static final int EXACT = 1;
    public static final int LOWER_BOUND = 2;
    public static final int UPPER_BOUND = 3;
    private final TTEntry[] tTable;
    private final long indexMask;


    /**
     * Constructor, sets the transposition table size to 33554432 entries
     */
    public SearchInfo(){
        this(33554432);
    }

    /**
     * Constructor, the size parameter will be set to the lowest or equal power of 2.
     * @param size int, must be positive
     */
    public SearchInfo(int size) {
        //check if size is power of 2, if it isn't set it to the highest power of 2 lower than the received one
        if (Integer.bitCount(size) != 1 || size != 0) {
            size = Integer.highestOneBit(size);
        }

        tTable = new TTEntry[size];

        indexMask = size - 1;
    }

    /**
     * Stores the ttEntry into the table
     * @param ttEntry ttEntry
     */
    public void ttStore(TTEntry ttEntry) {
        if (tTable.length == 0){
            return;
        }

        int index = (int) (ttEntry.zobristHash&indexMask);

        //keep the one which was explored to a greater depth
        if (tTable[index] != null && tTable[index].creationMove >= ttEntry.creationMove && tTable[index].depth >= ttEntry.depth){
            return;
        }
        tTable[index] = ttEntry;
    }

    /**
     *
     * Returns the entry, if any, for the received zobrist hash
     * @param zobristHash long
     * @return TTEntry
     */
    public TTEntry ttGet(long zobristHash) {
        if (tTable.length == 0){
            return null;
        }
        return tTable[(int) (zobristHash&indexMask)];
    }

    /**
     * Entry for the transposition table
     * @param zobristHash of the board
     * @param value of the board-state from the perspective of the active player at the beginning of the search
     * @param type of node, either exact, upper bound or lower bound
     * @param bestMove the best move found in the node
     * @param depth the depth to which the board-state was searched to
     * @param creationMove on which move was the search started
     */
    public record TTEntry (long zobristHash, double value, int type, Move bestMove, int depth, int creationMove) {
    }

}
