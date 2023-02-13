package hr.mi.chess.models;

public class Move {
    private final int piece;
    private final int from;
    private final int to;
    private final int flags;
    private boolean whiteKingSideCastling;
    private boolean whiteQueenSideCastling;
    private boolean blackKingSideCastling;
    private boolean blackQueenSideCastling;


    public Move(int piece, int from, int to, int flags) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.flags = flags;
    }

    public int piece() {
        return piece;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public int flags() {
        return flags;
    }
}
