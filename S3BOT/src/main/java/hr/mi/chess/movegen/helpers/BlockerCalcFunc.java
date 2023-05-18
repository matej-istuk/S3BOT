package hr.mi.chess.movegen.helpers;

/**
 * Functional interface for calculating blocking pieces on the chess board.
 * @author Matej Istuk
 */
@FunctionalInterface
public interface BlockerCalcFunc {

    /**
     * Returns a bitmask of the squares that are blocking, depends on the implementation.
     * @param bitboards bitboards of the chess board
     * @return bitmask of the squares that are blocking
     */
    long calculateBlockers(long[] bitboards);
}
