package hr.mi.chess.models;

public enum ChessPiece {
    WHITE_PAWN(0, new int[] {8}, new int[] {7, 9}, false),
    WHITE_ROOK(1, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, true),
    WHITE_KNIGHT(2, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, false),
    WHITE_BISHOP(3, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, true),
    WHITE_QUEEN(4, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, true),
    WHITE_KING(5, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, false),
    BLACK_PAWN(6, new int[] {-8}, new int[] {-7, -9}, false),
    BLACK_ROOK(7, new int[] {8, 1, -8, -1}, new int[] {8, 1, -8, -1}, true),
    BLACK_KNIGHT(8, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, new int[] {17, 15, 10, 6, -17, -15, -10, -6}, false),
    BLACK_BISHOP(9, new int[] {7, 9, -7, -9}, new int[] {7, 9, -7, -9}, true),
    BLACK_QUEEN(10, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, true),
    BLACK_KING(11, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, new int[] {8, 1, -8, -1, 7, 9, -7, -9}, false);

    private final int key;
    private final int[] attackOffsets;
    private final int[] moveOffsets;
    private final boolean sliding;

    ChessPiece(int key, int[] moveOffsets, int[] attackOffsets, boolean sliding) {
        this.key = key;
        this.moveOffsets = moveOffsets;
        this.attackOffsets = attackOffsets;
        this.sliding = sliding;
    }

    public int getKey() {
        return key;
    }

    public int[] getAttackOffsets() {
        return attackOffsets;
    }

    public int[] getMoveOffsets() {
        return moveOffsets;
    }

    public boolean isSliding() {
        return sliding;
    }
}
