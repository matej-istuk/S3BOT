package hr.mi.chess.util;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.movegen.helpers.MoveUtil;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;

public class BoardFunctions {
    private static final int[] offsets = {0, 1, 2, 3, 4, 5};
    private static final int[] blackOffsets = {6, 7, 8, 9, 10, 11};
    private static final int[] all = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

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
    public static long calculateOccupiedAll(long[] bitboards) {
        return calculateOccupiedByIndexes(bitboards, all, 0);
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

    public static boolean determineCheckByColour(long[] bitboards, boolean colour){
        int offset = colour == ChessConstants.WHITE ? 0 : 6;
        long kingDangerSquares = MoveUtil.getKingDangerSquares(bitboards, colour);
        return (bitboards[ChessPieceConstants.KING + offset] & kingDangerSquares) != 0;
    }
}
