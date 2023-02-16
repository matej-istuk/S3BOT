package hr.mi.chess.models;

import hr.mi.chess.constants.ChessConstants;

/**
 * Enum containing all chess pieces, each containing the following information:
 * <ul>
 *     <li>key - corresponding to the bitboard index of the piece</li>
 *     <li>colour</li>
 *     <li>pushOffsets</li>
 *     <li>captureOffsets</li>
 *     <li>moveOffsets</li>
 *     <li>sliding - if the piece is sliding</li>
 *     <li>pawn - if the piece is a pawn</li>
 * </ul>
 * @author Matej Istuk
 */
public enum ChessPiece {
    WHITE_PAWN(0, ChessConstants.WHITE, new int[] {8}, new int[] {7, 9}, new int[] {7, 8, 9}, false, true),
    WHITE_ROOK(1, ChessConstants.WHITE, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, true, false),
    WHITE_KNIGHT(2, ChessConstants.WHITE, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, false, false),
    WHITE_BISHOP(3, ChessConstants.WHITE, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, true, false),
    WHITE_QUEEN(4, ChessConstants.WHITE, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, true, false),
    WHITE_KING(5, ChessConstants.WHITE, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, false, false),
    BLACK_PAWN(6, ChessConstants.BLACK, new int[] {-8}, new int[] {-7, -9}, new int[] {-7, -8, -9}, false, true),
    BLACK_ROOK(7, ChessConstants.BLACK, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, true, false),
    BLACK_KNIGHT(8, ChessConstants.BLACK, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, false, false),
    BLACK_BISHOP(9, ChessConstants.BLACK, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, true, false),
    BLACK_QUEEN(10, ChessConstants.BLACK, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, true, false),
    BLACK_KING(11, ChessConstants.BLACK, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, false, false);

    private final int key;
    private final boolean colour;
    private final int[] pushOffsets;
    private final int[] captureOffsets;
    private final int[] moveOffsets;
    private final boolean sliding;
    private final boolean pawn;

    ChessPiece(int key, boolean colour, int[] pushOffsets, int[] captureOffsets, int[] moveOffsets, boolean sliding, boolean pawn) {
        this.key = key;
        this.colour = colour;
        this.pushOffsets = pushOffsets;
        this.captureOffsets = captureOffsets;
        this.moveOffsets = moveOffsets;
        this.sliding = sliding;
        this.pawn = pawn;
    }

    public int getKey() {
        return key;
    }

    public boolean getColour() {
        return colour;
    }

    public int[] getCaptureOffsets() {
        return captureOffsets;
    }

    public int[] getPushOffsets() {
        return pushOffsets;
    }

    public int[] getMoveOffsets() {
        return moveOffsets;
    }

    public boolean isSliding() {
        return sliding;
    }

    public boolean isPawn() {
        return pawn;
    }
}
