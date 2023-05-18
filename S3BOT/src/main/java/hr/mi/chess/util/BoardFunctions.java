package hr.mi.chess.util;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.movegen.helpers.MoveUtil;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;

/**
 * Class offering various static bitboard utility methods.
 * @author Matej Istuk
 */
public class BoardFunctions {
    private static final int[] offsets = {0, 1, 2, 3, 4, 5};
    private static final int[] blackOffsets = {6, 7, 8, 9, 10, 11};
    private static final int[] all = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    /**
     * Returns piece type by the bitboard of the piece.
     * @param bitboards bitboards of the boardstate
     * @param pieceBitboard bitboard of the piece whose type is wanted
     * @return ChessPiece
     */
    public static ChessPiece getPieceByBitboard(long[] bitboards, long pieceBitboard){
        for (ChessPiece chessPiece: ChessPiece.values()){
            if ((bitboards[chessPiece.getKey()] & pieceBitboard) != 0){
                return chessPiece;
            }
        }

        return null;
    }

    /**
     * Returns a bitboard of all spaces occupied by pieces of the requested colour.
     * @param bitboards bitboards of the boardstate
     * @param colour for which to return occupied squares
     * @return bitboard of all spaces occupied by pieces of the requested colour
     */
    public static long calculateOccupiedByColour(long[] bitboards, boolean colour) {
        return calculateOccupiedByIndexes(bitboards, offsets, colour == ChessConstants.WHITE ? 0 : 6);
    }

    /**
     * Returns bitboard of all occupied squares on the board.
     * @param bitboards bitboards of the boardstate
     * @return bitboard of all occupied squares on the board
     */
    public static long calculateOccupiedAll(long[] bitboards) {
        return calculateOccupiedByIndexes(bitboards, all, 0);
    }

    /**
     * Returns bitboard of all occupied squares by the pieces with the received indexes.
     * @param bitboards bitboards of the boardstate
     * @param pieceIndexes array of piece indexes
     * @param pieceColour colour of those pieces
     * @return bitboard of all occupied squares by the pieces with the received indexes
     */
    public static long calculateOccupiedByIndexes(long[] bitboards, int[] pieceIndexes, boolean pieceColour) {
        return calculateOccupiedByIndexes(bitboards, pieceIndexes, pieceColour == ChessConstants.WHITE ? 0 : 6);
    }

    /**
     * Returns bitboard of all occupied squares by the pieces with the received indexes.
     * @param bitboards bitboards of the boardstate
     * @param bitboardIndexes array of piece indexes
     * @param offset offset of the piece bitboards (0 for white, 6 for black)
     * @return bitboard of all occupied squares by the pieces with the received indexes
     */
    private static long calculateOccupiedByIndexes(long[] bitboards, int[] bitboardIndexes, int offset){
        long occupied = 0L;
        for (int i: bitboardIndexes){
            occupied |= bitboards[i + offset];
        }
        return occupied;
    }

    /**
     * Checks if the king of the chosen colour is in check.
     * @param bitboards bitboards of the boardstate
     * @param colour of the king whose being in check is being checked.
     * @return true if the king of the requested colour is in check, false otherwise
     */
    public static boolean determineCheckByColour(long[] bitboards, boolean colour){
        int offset = colour == ChessConstants.WHITE ? 0 : 6;
        long kingDangerSquares = MoveUtil.getKingDangerSquares(bitboards, colour);
        return (bitboards[ChessPieceConstants.KING + offset] & kingDangerSquares) != 0;
    }
}
