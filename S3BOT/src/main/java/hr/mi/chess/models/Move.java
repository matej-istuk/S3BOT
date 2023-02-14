package hr.mi.chess.models;

public class Move {
    private final int piece;
    private final int from;
    private final int to;
    private final int flags;
    private boolean oldWhiteKingSideCastling;
    private boolean oldWhiteQueenSideCastling;
    private boolean oldBlackKingSideCastling;
    private boolean oldBlackQueenSideCastling;
    private int oldFullMoves;
    private int oldHalfMoveClock;
    private boolean oldActiveColour;
    private int oldEnPassantTarget;
    private int capturedPieceIndex;

    public Move(int piece, int from, int to, int flags) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.flags = flags;
        this.capturedPieceIndex = -1;
    }

    public int getPiece() {
        return piece;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getFlags() {
        return flags;
    }


    public boolean getOldWhiteKingSideCastling() {
        return oldWhiteKingSideCastling;
    }

    public void setOldWhiteKingSideCastling(boolean oldWhiteKingSideCastling) {
        this.oldWhiteKingSideCastling = oldWhiteKingSideCastling;
    }

    public boolean getOldWhiteQueenSideCastling() {
        return oldWhiteQueenSideCastling;
    }

    public void setOldWhiteQueenSideCastling(boolean oldWhiteQueenSideCastling) {
        this.oldWhiteQueenSideCastling = oldWhiteQueenSideCastling;
    }

    public boolean getOldBlackKingSideCastling() {
        return oldBlackKingSideCastling;
    }

    public void setOldBlackKingSideCastling(boolean oldBlackKingSideCastling) {
        this.oldBlackKingSideCastling = oldBlackKingSideCastling;
    }

    public boolean getOldBlackQueenSideCastling() {
        return oldBlackQueenSideCastling;
    }

    public void setOldBlackQueenSideCastling(boolean oldBlackQueenSideCastling) {
        this.oldBlackQueenSideCastling = oldBlackQueenSideCastling;
    }

    public int getOldFullMoves() {
        return oldFullMoves;
    }

    public void setOldFullMoves(int oldFullMoves) {
        this.oldFullMoves = oldFullMoves;
    }

    public int getOldHalfMoveClock() {
        return oldHalfMoveClock;
    }

    public void setOldHalfMoveClock(int oldHalfMoveClock) {
        this.oldHalfMoveClock = oldHalfMoveClock;
    }

    public boolean getOldActiveColour() {
        return oldActiveColour;
    }

    public void setOldActiveColour(boolean oldActiveColour) {
        this.oldActiveColour = oldActiveColour;
    }

    public int getOldEnPassantTarget() {
        return oldEnPassantTarget;
    }

    public void setOldEnPassantTarget(int oldEnPassantTarget) {
        this.oldEnPassantTarget = oldEnPassantTarget;
    }

    public int getCapturedPieceIndex() {
        return capturedPieceIndex;
    }

    public void setCapturedPieceIndex(int capturedPieceIndex) {
        this.capturedPieceIndex = capturedPieceIndex;
    }
}
