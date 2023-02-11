package hr.mi.chess.movegen.helpers;

import hr.mi.chess.movegen.helpers.interfaces.BitboardSearchAction;

public class BitboardSearch {
    public static void search(long bitboard, BitboardSearchAction bitboardSearchAction){
        while (bitboard != 0) {
            int bitIndex = Bitwise.findIndexOfMS1B(bitboard);
            bitboard &= ~(1L << bitIndex);

            bitboardSearchAction.applyForIndex(bitIndex);
        }
    }
}
