package hr.mi.chess.models.support;

import java.util.Random;

public class ZobristNumbers {
    private static final long[] zobristNumbers = generateZobristNumbers();

    private static long[] generateZobristNumbers() {
        Random rand = new Random(36535040);
        long[] zobristNumbers = new long[12*64 + 4 + 8 + 1]; //each piece on each square + castling + en passant + is black active

        for (int i = 0; i < zobristNumbers.length; i++) {
            zobristNumbers[i] = rand.nextLong();
        }

        return zobristNumbers;
    }

    /**
     * Returns number based on piece and square.
     * @param piece int from 0 to 11
     * @param square int from 0 to 63
     * @return number assigned to specific piece and square
     */
    public static long getPieceOnTile(int piece, int square) {
        return zobristNumbers[piece * 64 + square];
    }

    /**
     * Type goes from 0 to 3.
     *  0 - white king side
     *  1 - white queen side
     *  2 - black king side
     *  3 - black queen side
     * @param type which specific castle
     * @return number assigned to specific castle
     */
    public static long getCastling(int type) {
        return zobristNumbers[12 * 64 + type];
    }

    /**
     * Flank describes on which flank is the en passant available. Goes from 0 to 7
     * @param flank int from 0 to 7
     * @return number assigned to specific en passant
     */
    public static long getEnPassant(int flank){
        return zobristNumbers[12*64 + 4 + flank];
    }

    /**
     * Flank describes on which flank is the en passant available. Goes from 0 to 7
     * @return number assigned to specific en passant
     */
    public static long getBlackActive(){
        return zobristNumbers[12*64 + 4 + 8];
    }
}

