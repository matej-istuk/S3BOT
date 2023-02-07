package hr.mi.chess.model;

import hr.mi.apps.ChessModelAdapter;
import hr.mi.chess.util.ChessTranslator;

import java.util.Arrays;
import java.util.Map;

public class BoardState {
    /*
    Legend:
        P - white pawn
        R - white rook
        N - white knight
        B - white bishop
        Q - white queen
        K - white king

        p - black pawn
        r - black rook
        n - black knight
        b - black bishop
        q - black queen
        k - black king
     */
    public static final Map<Character, Integer> PIECE_INT_MAPPING = Map.ofEntries(
            Map.entry('P', 0), Map.entry('R', 1), Map.entry('N', 2), Map.entry('B', 3), Map.entry('Q', 4), Map.entry('K', 5),
            Map.entry('p', 6), Map.entry('r', 7), Map.entry('n', 8), Map.entry('b', 9), Map.entry('q', 10), Map.entry('k', 11)
            );

    private static final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

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
    private boolean isWhiteActive;
    private int enPassantTarget;

    public BoardState() {
        this(STARTING_POSITION_FEN);
    }

    public BoardState(String fen){
        loadFen(fen);
    }


    public long[] getBitboards() {
        return bitboards;
    }

    public boolean isWhiteKingSideCastling() {
        return whiteKingSideCastling;
    }

    public boolean isWhiteQueenSideCastling() {
        return whiteQueenSideCastling;
    }

    public boolean isBlackKingSideCastling() {
        return blackKingSideCastling;
    }

    public boolean isBlackQueenSideCastling() {
        return blackQueenSideCastling;
    }

    public int getFullMoves() {
        return fullMoves;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public boolean isWhiteActive() {
        return isWhiteActive;
    }

    public int getEnPassantTarget() {
        return enPassantTarget;
    }

    /**
     * Parses the received string as FEN, loading the represented chess board state into itself.
     * @param fen FEN string
     */
    private void loadFen(String fen) {
        String[] fenArr = fen.split(" ");
        assert fenArr.length == 6;

        //Piece Placement
        String[] rows = fenArr[0].split("/");
        assert rows.length == 8;
        int index = 56;
        for (String row: rows){
            int offset = 0;
            for (char c: row.toCharArray()){
                if (Character.isDigit(c)){
                    offset += c - '0';
                }
                else {
                    bitboards[PIECE_INT_MAPPING.get(c)] = bitboards[PIECE_INT_MAPPING.get(c)] | (1L << (index + offset));
                    offset++;
                }
            }
            index -= 8;
        }

        //Active Color
        isWhiteActive = fenArr[1].equals("w");

        //Castling Rights
        whiteKingSideCastling = fenArr[2].contains("K");
        whiteQueenSideCastling = fenArr[2].contains("Q");
        blackKingSideCastling = fenArr[2].contains("k");
        blackQueenSideCastling = fenArr[2].contains("q");

        //Possible En Passant Targets
        if (!fenArr[3].equals("-")){
            enPassantTarget = ChessTranslator.AlgebraicToLERF(fenArr[3]);
        }
        else {
            enPassantTarget = -1;
        }

        //Halfmove Clock
        halfMoveClock = Integer.parseInt(fenArr[4]);

        //Fullmove Number
        fullMoves = Integer.parseInt(fenArr[5]);
    }


    /**
     * This move method does not check the legality of the moves, it just executes them, so it should only be used by
     * something that checks the move legality beforehand
     * @param piece the piece that is moved
     * @param from the LERF coordinate from which the piece is moved
     * @param to the LERF coordinate to which the piece is moved
     * @param flags 4 flags describing the move, as shown here:
     *              <tbody><tr>
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
    public void move(int piece, int from, int to, int flags){
        //resolve the move
        bitboards[piece] = bitboards[piece] & ~(1L << from);
        bitboards[piece] = bitboards[piece] | (1L << to);
        //clears en passant targets
        enPassantTarget = -1;

        //deal with halfmoves, resets when a pawn is moved or a unit is captured
        if ((flags & 4) != 0 || piece == 0 || piece == 6){
            halfMoveClock = 0;
        }
        else {
            halfMoveClock++;
        }

        //increase move counter, if black just moved
        if (!isWhiteActive){
            fullMoves++;
        }

        //update castling legality changes
        whiteKingSideCastling = whiteKingSideCastling && from != 4 && from != 7;
        whiteQueenSideCastling = whiteQueenSideCastling && from != 4 && from != 0;
        blackKingSideCastling = blackKingSideCastling && from != 60 && from != 63;
        blackQueenSideCastling = blackQueenSideCastling && from != 60 && from != 56;

        //handle the flags
        //due to their special nature, cases 1, 2, 3, and 5 are handled separately
        switch (flags) {
            //double pawn push
            case 1 -> {
                if (isWhiteActive)
                    enPassantTarget = to - 8;
                else
                    enPassantTarget = to + 8;
            }
            //king castle
            case 2 -> {
                //have to move the rooks to their new positions
                if (isWhiteActive) {
                    //White king-side rook on the bitboard index 1 7th digit
                    bitboards[1] = bitboards[1] & ~(1L << 7);
                    bitboards[1] = bitboards[1] | (1L << 5);
                } else {
                    //Black king-side rook on the bitboard index 7 63rd digit
                    bitboards[7] = bitboards[7] & ~(1L << 63);
                    bitboards[7] = bitboards[7] | (1L << 61);
                }
            }
            //queen castle
            case 3 -> {
                if (isWhiteActive) {
                    //White queen-side rook on the bitboard index 1 0th digit
                    bitboards[1] = bitboards[1] & ~(1L);
                    bitboards[1] = bitboards[1] | (1L << 3);
                } else {
                    //Black queen-side rook on the bitboard index 7 56th digit
                    bitboards[7] = bitboards[7] & ~(1L << 56);
                    bitboards[7] = bitboards[7] | (1L << 59);
                }
            }
            //ep-capture
            case 5 -> {
                if (isWhiteActive) {
                    //Black pawns are on bitboard index 6
                    bitboards[6] = bitboards[6] & ~(1L << (to - 8));
                } else {
                    //White pawns are on bitboard index 0
                    bitboards[0] = bitboards[0] & ~(1L << (to + 8));
                }
            }
            //the following cases share a lot of functionality, so they're bundled together
            default -> {
                //the move is a capture if the third flag is active
                if ((flags & 4) != 0){
                    if (isWhiteActive) {
                        for (int i = 6; i < 11; i++) {
                            bitboards[i] = bitboards[i] & ~(1L << to);
                        }
                    } else {
                        for (int i = 0; i < 5; i++) {
                            bitboards[i] = bitboards[i] & ~(1L << to);
                        }
                    }
                }

                //the move is a promotion if the fourth flag is active
                if ((flags & 8) != 0){
                    if (isWhiteActive) {
                        //White pawns are on bitboard index 0
                        bitboards[0] = bitboards[0] & ~(1L << to);
                        //possible because the same ordering is used in the bitboards and flags
                        int promotedPiece = 1 + flags & 3;
                        bitboards[promotedPiece] = bitboards[promotedPiece] | (1L << to);
                    } else {
                        //Black pawns are on bitboard index 6
                        bitboards[6] = bitboards[6] & ~(1L << to);
                        //possible because the same ordering is used in the bitboards and flags
                        int promotedPiece = 7 + flags & 3;
                        bitboards[promotedPiece] = bitboards[promotedPiece] | (1L << to);
                    }
                }
            }
        }
        //change active player
        isWhiteActive = !isWhiteActive;
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
        if (isWhiteActive != that.isWhiteActive) return false;
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
        result = 31 * result + (isWhiteActive ? 1 : 0);
        result = 31 * result + enPassantTarget;
        return result;
    }
}
