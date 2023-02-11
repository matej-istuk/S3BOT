package hr.mi.chess.movegen;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.constants.ChessConstants;
import hr.mi.chess.util.constants.ChessPieceConstants;

public class BoardFunctions {
    private static final int[] offsets = {0, 1, 2, 3, 4, 5};
    private static final int[] blackOffsets = {6, 7, 8, 9, 10, 11};

    public static ChessPiece getPieceByBitboard(long[] bitboards, long checkerBitboard){
        for (ChessPiece chessPiece: ChessPiece.values()){
            if ((bitboards[chessPiece.getKey()] & checkerBitboard) != 0){
                return chessPiece;
            }
        }

        return null;
    }

    public static long calculateOccupiedByColour(long[] bitboards, boolean colour) {
        return calculateOccupiedByIndexes(bitboards, offsets, colour == ChessConstants.WHITE ? 0 : 6);
    }

    public static long calculateOccupiedByIndexes(long[] bitboards, int[] pieceIndexes, boolean pieceColour) {
        return calculateOccupiedByIndexes(bitboards, pieceIndexes, pieceColour == ChessConstants.WHITE ? 0 : 6);
    }

    private static long calculateOccupiedByIndexes(long[] bitboards, int[] bitboardIndexes, int offset){
        long occupied = 0L;
        for (int i: bitboardIndexes){
            occupied |= bitboards[i + offset];
        }
        return occupied;
    }
}
