package hr.mi.chess.movegen.helpers;

/**
 * Class containing utility methods for bitboards.
 * @author Matej Istuk
 */
public class BitboardUtil {
    /**
     * Executes the received strategy for each bit set to one in the received bitboard.
     * @param bitboard bitboard
     * @param bitboardSearchAction action which will be preformed on each "one" bit
     */
    public static void forEachOneBit(long bitboard, BitboardSearchAction bitboardSearchAction){
        while (bitboard != 0) {
            int bitIndex = Bitwise.findIndexOfMS1B(bitboard);
            bitboard &= ~(1L << bitIndex);

            bitboardSearchAction.applyForIndex(bitIndex);
        }
    }

    /**
     * Action to be performed when a bit with the value of one is found in the <code>forEachOneBit</code> method
     */
    public interface BitboardSearchAction {
        void applyForIndex (int bitIndex);
    }

}
