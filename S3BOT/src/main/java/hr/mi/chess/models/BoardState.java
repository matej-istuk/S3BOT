package hr.mi.chess.models;

import hr.mi.chess.constants.ChessBoardConstants;
import hr.mi.chess.models.support.ZobristNumbers;
import hr.mi.chess.util.ChessTranslator;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;

import java.util.Arrays;
import java.util.Stack;


/**
 * Represents the state of a chess board, holding all necessary information.
 * @author Matej Istuk
 */
public class BoardState {
    //the following 4 flags are for move encoding
    private final static int PROMOTION_FLAG = 8;
    private final static int CAPTURE_FLAG = 4;
    private final static int SPECIAL_1_FLAG = 2;
    private final static int SPECIAL_0_FLAG = 1;
    private int lastMovedPieceIndex = -1;
    /**
     * Bitboards in the Little-Endian Rank-File Mapping
     */
    private final long[] bitboards = new long[12];
    private boolean whiteKingSideCastling;
    private boolean whiteQueenSideCastling;
    private boolean blackKingSideCastling;
    private boolean blackQueenSideCastling;
    private int fullMoves;
    private int halfMoveClock;
    private boolean activeColour;
    private int enPassantTarget;
    private final Stack<Move> previousMoves;
    private long zobristHash = 0;

    /**
     * Creates the starting board-state of a standard chess game.
     */
    public BoardState() {
        this(ChessBoardConstants.STARTING_POSITION_FEN);
    }

    /**
     * Creates the board-state as described by the given fen string
     * @param fen a string describing a chess position. See
     *            <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Forsyth–Edwards Notation</a> for more detail
     */
    public BoardState(String fen){
        previousMoves = new Stack<>();
        loadFen(fen);
    }

    /**
     * Returns the bitboards
     * @return long array of length 12
     */
    public long[] getBitboards() {
        return bitboards;
    }

    /**
     * Returns if king side castling for white is allowed.
     * @return true if king side castling for white is allowed, false otherwise
     */
    public boolean isWhiteKingSideCastling() {
        return whiteKingSideCastling;
    }

    /**
     * Returns if queen side castling for white is allowed.
     * @return true if queen side castling for white is allowed, false otherwise
     */
    public boolean isWhiteQueenSideCastling() {
        return whiteQueenSideCastling;
    }

    /**
     * Returns if king side castling for black is allowed.
     * @return true if king side castling for black is allowed, false otherwise
     */
    public boolean isBlackKingSideCastling() {
        return blackKingSideCastling;
    }

    /**
     * Returns if queen side castling for black is allowed.
     * @return true if queen side castling for black is allowed, false otherwise
     */
    public boolean isBlackQueenSideCastling() {
        return blackQueenSideCastling;
    }

    /**
     * Returns how many full-moves has passed. A full-move is completed whenever black moves.
     * @return the number of full-moves
     */
    public int getFullMoves() {
        return fullMoves;
    }

    /**
     * Returns how many half-moves have passed since the last pawn move or piece capture.
     * @return the number of half-moves
     */
    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    /**
     * Returns the active colour as described in <code>ChessConstants</code> class.
     * @return true if the active colour is white, and false if the active colour is black
     */
    public boolean getActiveColour() {
        return activeColour;
    }

    /**
     * Returns the passive colour as described in <code>ChessConstants</code> class.
     * @return false if the active colour is white, and true if the active colour is black
     */
    public boolean getPassiveColour() {
        return !activeColour;
    }

    /**
     * Returns the LERF offset of the square on which the double pushed pawn can be captured.
     * @return integer describing the LERF offset
     */
    public int getEnPassantTarget() {
        return enPassantTarget;
    }

    public Stack<Move> getPreviousMoves() {
        return previousMoves;
    }

    /**
     * Returns the zobrist hash of the board-state.
     * @return long hash
     */
    public long getZobristHash() {
        return zobristHash;
    }

    /**
     * Parses the received string as FEN, loading the represented chess board state into itself.
     * @param fen FEN string
     */
    public void loadFen(String fen) {
        Arrays.fill(bitboards, 0L);

        String[] fenArr = fen.split(" ");
        if(fenArr.length != 6) {
            throw new IllegalArgumentException();
        }

        //Piece Placement
        String[] rows = fenArr[0].split("/");
        if (rows.length != 8)
            throw new IllegalArgumentException();

        int index = 56;
        for (String row: rows){
            int offset = 0;
            for (char c: row.toCharArray()){
                if (Character.isDigit(c)){
                    offset += c - '0';
                }
                else {
                    bitboards[ChessPieceConstants.PIECE_INT_MAPPING.get(c)] = bitboards[ChessPieceConstants.PIECE_INT_MAPPING.get(c)] | (1L << (index + offset));
                    offset++;
                }
            }
            index -= 8;
        }

        //Active Color
        activeColour = fenArr[1].equals("w");

        //Castling Rights
        whiteKingSideCastling = fenArr[2].contains("K");
        whiteQueenSideCastling = fenArr[2].contains("Q");
        blackKingSideCastling = fenArr[2].contains("k");
        blackQueenSideCastling = fenArr[2].contains("q");

        //Possible En Passant Targets
        if (!fenArr[3].equals("-")){
            enPassantTarget = ChessTranslator.algebraicPosToLERF(fenArr[3]);
        }
        else {
            enPassantTarget = -1;
        }

        //Halfmove Clock
        halfMoveClock = Integer.parseInt(fenArr[4]);

        //Fullmove Number
        fullMoves = Integer.parseInt(fenArr[5]);
        lastMovedPieceIndex = -1;
        previousMoves.clear();

        calculateZobrist();
    }

    public void loadStartingPosition(){
        loadFen(ChessBoardConstants.STARTING_POSITION_FEN);
    }
    /**
     * This move method does not check the legality of the moves, it just executes them, so it should only be used by
     * something that checks the move legality beforehand
     * @param move the move to be executed
     * <tbody><tr>
     * <th> code
     * </th>
     * <th> promotion
     * </th>
     * <th> capture
     * </th>
     * <th> special 1
     * </th>
     * <th> special 0
     * </th>
     * <th> kind of move
     * </th></tr>
     * <tr>
     * <th> 0
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> quiet moves
     * </td></tr>
     * <tr>
     * <th> 1
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> double pawn push
     * </td></tr>
     * <tr>
     * <th> 2
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> king castle
     * </td></tr>
     * <tr>
     * <th> 3
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> queen castle
     * </td></tr>
     * <tr>
     * <th> 4
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> captures
     * </td></tr>
     * <tr>
     * <th> 5
     * </th>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> ep-capture
     * </td></tr>
     * <tr>
     * <th> 8
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> rook-promotion
     * </td></tr>
     * <tr>
     * <th> 9
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> knight-promotion
     * </td></tr>
     * <tr>
     * <th> 10
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> bishop-promotion
     * </td></tr>
     * <tr>
     * <th> 11
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> queen-promotion
     * </td></tr>
     * <tr>
     * <th> 12
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> rook-promo capture
     * </td></tr>
     * <tr>
     * <th> 13
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> knight-promo capture
     * </td></tr>
     * <tr>
     * <th> 14
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 0
     * </td>
     * <td style="text-align:center;"> bishop-promo capture
     * </td></tr>
     * <tr>
     * <th> 15
     * </th>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> 1
     * </td>
     * <td style="text-align:center;"> queen-promo capture
     * </td></tr></tbody>
     */
    public void makeMove(Move move){
        //set last moved piece
        lastMovedPieceIndex = move.getTo();

        move.setOldWhiteKingSideCastling(whiteKingSideCastling);
        move.setOldWhiteQueenSideCastling(whiteQueenSideCastling);
        move.setOldBlackKingSideCastling(blackKingSideCastling);
        move.setOldBlackQueenSideCastling(blackQueenSideCastling);
        move.setOldFullMoves(fullMoves);
        move.setOldHalfMoveClock(halfMoveClock);
        move.setOldActiveColour(activeColour);
        move.setOldEnPassantTarget(enPassantTarget);

        //resolve the move
        bitboardsRemovePiece(move.getPiece(), move.getFrom());
        bitboardsAddPiece(move.getPiece(), move.getTo());

        //clears en passant targets
        enPassantTarget = -1;

        //deal with halfmoves, resets when a pawn is moved or a unit is captured
        if ((move.getFlags() & 4) != 0 || move.getPiece() == 0 || move.getPiece() == 6){
            halfMoveClock = 0;
        }
        else {
            halfMoveClock++;
        }

        //increase move counter, if black just moved
        if (activeColour == ChessConstants.BLACK){
            fullMoves++;
        }

        //update castling legality changes
        whiteKingSideCastling = whiteKingSideCastling && move.getFrom() != 4 && move.getFrom() != 7 && move.getTo() != 7;
        whiteQueenSideCastling = whiteQueenSideCastling && move.getFrom() != 4 && move.getFrom() != 0 && move.getTo() != 0;
        blackKingSideCastling = blackKingSideCastling && move.getFrom() != 60 && move.getFrom() != 63 && move.getTo() != 63;
        blackQueenSideCastling = blackQueenSideCastling && move.getFrom() != 60 && move.getFrom() != 56 && move.getTo() != 56;

        //handle the flags
        //due to their special nature, cases 1, 2, 3, and 5 are handled separately
        switch (move.getFlags()) {
            //double pawn push
            case 1 -> {
                if (activeColour == ChessConstants.WHITE)
                    enPassantTarget = move.getTo() - 8;
                else
                    enPassantTarget = move.getTo() + 8;
            }
            //king castle
            case 2 -> {
                //have to move the rooks to their new positions
                if (activeColour == ChessConstants.WHITE) {
                    //White king-side rook on the bitboard index 1 7th digit
                    bitboardsRemovePiece(1, 7);
                    bitboardsAddPiece(1, 5);
                } else {
                    //Black king-side rook on the bitboard index 7 63rd digit
                    bitboardsRemovePiece(7, 63);
                    bitboardsAddPiece(7, 61);
                }
            }
            //queen castle
            case 3 -> {
                if (activeColour == ChessConstants.WHITE) {
                    //White queen-side rook on the bitboard index 1 0th digit
                    bitboardsRemovePiece(1, 0);
                    bitboardsAddPiece(1, 3);
                } else {
                    //Black queen-side rook on the bitboard index 7 56th digit
                    bitboardsRemovePiece(7, 56);
                    bitboardsAddPiece(7, 59);
                }
            }
            //ep-capture
            case 5 -> {
                //this "if" will run only on tests, move generation sets the captured piece index
                if (move.getCapturedPieceIndex() == -1) {
                    if (activeColour == ChessConstants.WHITE) {
                        move.setCapturedPieceIndex(6);
                    }
                    else {
                        move.setCapturedPieceIndex(0);
                    }
                }
                if (activeColour == ChessConstants.WHITE) {
                    //Black pawns are on bitboard index 6
                    bitboardsRemovePiece(6, move.getTo() - 8);
                } else {
                    //White pawns are on bitboard index 0
                    bitboardsRemovePiece(0, move.getTo() + 8);
                }
            }
            //the other cases share a lot of functionality, so they're bundled together
            default -> {
                //the move is a capture if the third flag is active
                if ((move.getFlags() & CAPTURE_FLAG) != 0){
                    //this "if" will run only on tests, move generation sets the captured piece index
                    if (move.getCapturedPieceIndex() == -1){
                        for (int i = ((activeColour == ChessConstants.WHITE) ? 6 : 0); i < ((activeColour == ChessConstants.WHITE) ? 11 : 5); i++){
                            if ((bitboards[i] & (1L << move.getTo())) != 0){
                                move.setCapturedPieceIndex(i);
                                break;
                            }
                        }
                    }
                    bitboardsRemovePiece(move.getCapturedPieceIndex(), move.getTo());
                }

                //the move is a promotion if the fourth flag is active
                if ((move.getFlags() & PROMOTION_FLAG) != 0){
                    bitboardsRemovePiece(move.getPiece(), move.getTo());
                    //possible because the same ordering is used in the bitboards and flags
                    bitboardsAddPiece(move.getPiece() + (move.getFlags() & 3) + 1, move.getTo());
                }
            }
        }

        //change active player
        activeColour = !activeColour;

        zobristChangeMoveSpecial(move);

        //push the move onto the stack
        previousMoves.push(move);
    }

    /**
     * Reverts the <code>BoardState</code> object to the state before the last move was made
     */
    public void unmakeLastMove(){
        if (previousMoves.isEmpty()){
            throw new IllegalStateException();
        }

        //set local variables
        Move move = previousMoves.pop();

        //set last moved piece index
        this.lastMovedPieceIndex = move.getTo();

        zobristChangeMoveSpecial(move);

        this.whiteKingSideCastling = move.getOldWhiteKingSideCastling();
        this.whiteQueenSideCastling = move.getOldWhiteQueenSideCastling();
        this.blackKingSideCastling = move.getOldBlackKingSideCastling();
        this.blackQueenSideCastling = move.getOldBlackQueenSideCastling();
        this.halfMoveClock = move.getOldHalfMoveClock();
        this.fullMoves = move.getOldFullMoves();
        this.activeColour = move.getOldActiveColour();
        this.enPassantTarget = move.getOldEnPassantTarget();

        //undo promotion if promotion
        if ((move.getFlags() & PROMOTION_FLAG) != 0){
            int promotedPiece = (activeColour == ChessConstants.WHITE ? 1 : 7)  + (move.getFlags() & 3);
            bitboardsRemovePiece(promotedPiece, move.getTo());
        }
        //else remove piece as usual
        else {
            bitboardsRemovePiece(move.getPiece(), move.getTo());
        }

        //add piece back
        bitboardsAddPiece(move.getPiece(), move.getFrom());


        //undo king castle
        if (move.getFlags() == 2){
            //have to move the rooks to their new positions
            if (activeColour == ChessConstants.WHITE) {
                //White king-side rook on the bitboard index 1 7th digit
                bitboardsRemovePiece(1, 5);
                bitboardsAddPiece(1, 7);
            } else {
                //Black king-side rook on the bitboard index 7 63rd digit
                bitboardsRemovePiece(7, 61);
                bitboardsAddPiece(7, 63);
            }
        }

        //undo queen castle
        if (move.getFlags() == 3){
            //have to move the rooks to their new positions
            if (activeColour == ChessConstants.WHITE) {
                //White queen-side rook on the bitboard index 1 0th digit
                bitboardsRemovePiece(1, 3);
                bitboardsAddPiece(1, 0);
            } else {
                //Black queen-side rook on the bitboard index 7 56th digit
                bitboardsRemovePiece(7, 59);
                bitboardsAddPiece(7, 56);
            }
        }

        //undo capture
        if ((move.getFlags() & CAPTURE_FLAG) != 0){
            int epOffset = 0;

            //check if en passant
            if (move.getFlags() == 5){
                if (move.getOldActiveColour() == ChessConstants.WHITE){
                    epOffset = -8;
                } else {
                    epOffset = 8;
                }
            }

            bitboardsAddPiece(move.getCapturedPieceIndex(), move.getTo() + epOffset);
        }
    }

    /**
     * Changes zobrist castling, colour and ep.
     * @param move the move being made or unmade
     */
    private void zobristChangeMoveSpecial(Move move) {
        zobristHash ^= ZobristNumbers.getBlackActive();

        if (move.getOldEnPassantTarget() != -1)
            zobristHash ^= ZobristNumbers.getEnPassant(move.getOldEnPassantTarget()%8);

        if (enPassantTarget != -1)
            zobristHash ^= ZobristNumbers.getEnPassant(enPassantTarget%8);

        if (move.getOldWhiteKingSideCastling() != whiteKingSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(0);

        if (move.getOldWhiteQueenSideCastling() != whiteQueenSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(1);

        if (move.getOldBlackKingSideCastling() != blackKingSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(2);

        if (move.getOldBlackQueenSideCastling() != blackQueenSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(3);
    }

    private void bitboardsAddPiece(int piece, int squareIndex) {
        bitboards[piece] = bitboards[piece] | (1L << squareIndex);
        zobristHash ^= ZobristNumbers.getPieceOnTile(piece, squareIndex);

    }

    private void bitboardsRemovePiece(int piece, int squareIndex) {
        bitboards[piece] = bitboards[piece] & ~(1L << squareIndex);
        zobristHash ^= ZobristNumbers.getPieceOnTile(piece, squareIndex);
    }

    public int getLastMovedPieceIndex() {
        return lastMovedPieceIndex;
    }

    private void calculateZobrist(){
        for (int i = 0; i < bitboards.length; i++)
            for (int j = 0; j < 64; j++)
                if ((bitboards[i] & (1L<<j)) != 0)
                    zobristHash ^= ZobristNumbers.getPieceOnTile(i, j);


        if (whiteKingSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(0);

        if (whiteQueenSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(1);

        if (blackKingSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(2);

        if (blackQueenSideCastling)
            zobristHash ^= ZobristNumbers.getCastling(3);

        if (enPassantTarget != -1)
            zobristHash ^= ZobristNumbers.getEnPassant(enPassantTarget%8);

        if (activeColour == ChessConstants.BLACK)
            zobristHash ^= ZobristNumbers.getBlackActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardState that = (BoardState) o;

        if (whiteKingSideCastling != that.whiteKingSideCastling) return false;
        if (whiteQueenSideCastling != that.whiteQueenSideCastling) return false;
        if (blackKingSideCastling != that.blackKingSideCastling) return false;
        if (blackQueenSideCastling != that.blackQueenSideCastling) return false;
        if (fullMoves != that.fullMoves) return false;
        if (halfMoveClock != that.halfMoveClock) return false;
        if (activeColour != that.activeColour) return false;
        if (enPassantTarget != that.enPassantTarget) return false;
        return Arrays.equals(bitboards, that.bitboards);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(bitboards);
        result = 31 * result + (whiteKingSideCastling ? 1 : 0);
        result = 31 * result + (whiteQueenSideCastling ? 1 : 0);
        result = 31 * result + (blackKingSideCastling ? 1 : 0);
        result = 31 * result + (blackQueenSideCastling ? 1 : 0);
        result = 31 * result + fullMoves;
        result = 31 * result + halfMoveClock;
        result = 31 * result + (activeColour ? 1 : 0);
        result = 31 * result + enPassantTarget;
        return result;
    }
}
