package hr.mi.chess.algorithm.support;

import hr.mi.chess.models.Move;

import java.util.Arrays;
import java.util.Objects;

public class SearchInfo {
    private final static int MAX_PLY = 125;
    private final static int MAX_KILLER_MOVES = 2;
    private final Move[][] killerMoves = new Move[MAX_PLY][MAX_KILLER_MOVES];

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

    public boolean checkIfKiller(int currentPly, Move move) {
        for (Move killerMove: killerMoves[currentPly]) {
            if (Objects.equals(move, killerMove)){
                return true;
            }
        }
        return false;
    }

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
        if (Integer.bitCount(size) != 1) {
            size = Integer.highestOneBit(size);
        }

        tTable = new TTEntry[size];

        indexMask = size - 1;
    }

    /**
     * Stores the ttEntry into the table
     * @param ttEntry
     */
    public void ttStore(TTEntry ttEntry) {
        int index = (int) (ttEntry.zobristHash()&indexMask);

        //if the zobrist hash is the same, keep the one which was explored to a greater depth
        if (tTable[index] != null && tTable[index].zobristHash() == ttEntry.zobristHash() && tTable[index].depth() >= ttEntry.depth()){
            return;
        }
        tTable[index] = ttEntry;
    }

    /**
     *
     * Returns the entry, if any, for the received zobrist hash
     * @param zobristHash
     * @return TTEntry
     */
    public TTEntry ttGet(long zobristHash) {
        return tTable[(int) (zobristHash&indexMask)];
    }

    public record TTEntry (long zobristHash, double value, int type, Move bestMove, int depth) {}

}
