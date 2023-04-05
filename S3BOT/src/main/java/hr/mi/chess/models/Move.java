package hr.mi.chess.models;

/**
 * Represents a chess move
 *
 * @author Matej Istuk
 */
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

    /**
     * Creates a move with the given values
     *
     * @param piece piece which is moved
     * @param from  from where the piece is moved (LERF offset index)
     * @param to    to where the piece is moved (LERF offset index)
     * @param flags flags for encoding various types of moves
     */
    public Move(int piece, int from, int to, int flags) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.flags = flags;
        this.capturedPieceIndex = -1;
    }

    /**
     * Returns which piece is moved.
     *
     * @return key of the moved piece
     */
    public int getPiece() {
        return piece;
    }

    /**
     * Returns from where the piece is moved.
     *
     * @return LERF offset index
     */
    public int getFrom() {
        return from;
    }

    /**
     * Returns to where the piece is moved.
     *
     * @return LERF offset index
     */
    public int getTo() {
        return to;
    }

    /**
     * Returns the move encoding flags.
     *
     * @return the move encoding flags
     */
    public int getFlags() {
        return flags;
    }

    //the following methods are used for reversing the move because some information is lost after making the move


    /**
     * Gets old white king side castling.
     *
     * @return the old white king side castling
     */
    public boolean getOldWhiteKingSideCastling() {
        return oldWhiteKingSideCastling;
    }

    /**
     * Sets old white king side castling.
     *
     * @param oldWhiteKingSideCastling the old white king side castling
     */
    public void setOldWhiteKingSideCastling(boolean oldWhiteKingSideCastling) {
        this.oldWhiteKingSideCastling = oldWhiteKingSideCastling;
    }

    /**
     * Gets old white queen side castling.
     *
     * @return the old white queen side castling
     */
    public boolean getOldWhiteQueenSideCastling() {
        return oldWhiteQueenSideCastling;
    }

    /**
     * Sets old white queen side castling.
     *
     * @param oldWhiteQueenSideCastling the old white queen side castling
     */
    public void setOldWhiteQueenSideCastling(boolean oldWhiteQueenSideCastling) {
        this.oldWhiteQueenSideCastling = oldWhiteQueenSideCastling;
    }

    /**
     * Gets old black king side castling.
     *
     * @return the old black king side castling
     */
    public boolean getOldBlackKingSideCastling() {
        return oldBlackKingSideCastling;
    }

    /**
     * Sets old black king side castling.
     *
     * @param oldBlackKingSideCastling the old black king side castling
     */
    public void setOldBlackKingSideCastling(boolean oldBlackKingSideCastling) {
        this.oldBlackKingSideCastling = oldBlackKingSideCastling;
    }

    /**
     * Gets old black queen side castling.
     *
     * @return the old black queen side castling
     */
    public boolean getOldBlackQueenSideCastling() {
        return oldBlackQueenSideCastling;
    }

    /**
     * Sets old black queen side castling.
     *
     * @param oldBlackQueenSideCastling the old black queen side castling
     */
    public void setOldBlackQueenSideCastling(boolean oldBlackQueenSideCastling) {
        this.oldBlackQueenSideCastling = oldBlackQueenSideCastling;
    }

    /**
     * Gets old full moves.
     *
     * @return the old full moves
     */
    public int getOldFullMoves() {
        return oldFullMoves;
    }

    /**
     * Sets old full moves.
     *
     * @param oldFullMoves the old full moves
     */
    public void setOldFullMoves(int oldFullMoves) {
        this.oldFullMoves = oldFullMoves;
    }

    /**
     * Gets old half move clock.
     *
     * @return the old half move clock
     */
    public int getOldHalfMoveClock() {
        return oldHalfMoveClock;
    }

    /**
     * Sets old half move clock.
     *
     * @param oldHalfMoveClock the old half move clock
     */
    public void setOldHalfMoveClock(int oldHalfMoveClock) {
        this.oldHalfMoveClock = oldHalfMoveClock;
    }

    /**
     * Gets old active colour.
     *
     * @return the old active colour
     */
    public boolean getOldActiveColour() {
        return oldActiveColour;
    }

    /**
     * Sets old active colour.
     *
     * @param oldActiveColour the old active colour
     */
    public void setOldActiveColour(boolean oldActiveColour) {
        this.oldActiveColour = oldActiveColour;
    }

    /**
     * Gets old en passant target.
     *
     * @return the old en passant target
     */
    public int getOldEnPassantTarget() {
        return oldEnPassantTarget;
    }

    /**
     * Sets old en passant target.
     *
     * @param oldEnPassantTarget the old en passant target
     */
    public void setOldEnPassantTarget(int oldEnPassantTarget) {
        this.oldEnPassantTarget = oldEnPassantTarget;
    }

    /**
     * Gets captured piece index.
     *
     * @return the captured piece index
     */
    public int getCapturedPieceIndex() {
        return capturedPieceIndex;
    }

    /**
     * Sets captured piece index.
     *
     * @param capturedPieceIndex the captured piece index
     */
    public void setCapturedPieceIndex(int capturedPieceIndex) {
        this.capturedPieceIndex = capturedPieceIndex;
    }

    /**
     * @return returns true if the move is a capture.
     */
    public boolean isCapture() {
        return (flags & 4) == 4;
    }

    /**
     * @return returns true if the move is a ep.
     */
    public boolean isEp() {
        return flags == 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (piece != move.piece) return false;
        if (from != move.from) return false;
        if (to != move.to) return false;
        return flags == move.flags;
    }

    @Override
    public int hashCode() {
        int result = piece;
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + flags;
        return result;
    }
}
