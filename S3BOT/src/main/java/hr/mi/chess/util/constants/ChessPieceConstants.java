package hr.mi.chess.util.constants;

import hr.mi.chess.models.ChessPiece;

import java.util.List;
import java.util.Map;

public class ChessPieceConstants {
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

    public static final int PAWN = 0;
    public static final int ROOK = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int QUEEN = 4;
    public static final int KING = 5;
    public static final List<ChessPiece> WHITE_PIECES = List.of(ChessPiece.WHITE_PAWN, ChessPiece.WHITE_ROOK, ChessPiece.WHITE_KNIGHT, ChessPiece.WHITE_BISHOP, ChessPiece.WHITE_QUEEN, ChessPiece.WHITE_KING);
    public static final List<ChessPiece> BLACK_PIECES = List.of(ChessPiece.BLACK_PAWN, ChessPiece.BLACK_ROOK, ChessPiece.BLACK_KNIGHT, ChessPiece.BLACK_BISHOP, ChessPiece.BLACK_QUEEN, ChessPiece.BLACK_KING);

    public static final List<ChessPiece> ATTACKERS_ON_WHITE_KING = List.of(ChessPiece.BLACK_PAWN, ChessPiece.BLACK_ROOK, ChessPiece.BLACK_KNIGHT, ChessPiece.BLACK_BISHOP, ChessPiece.BLACK_QUEEN);
    public static final List<ChessPiece> ATTACKERS_ON_BLACK_KING = List.of(ChessPiece.WHITE_PAWN, ChessPiece.WHITE_ROOK, ChessPiece.WHITE_KNIGHT, ChessPiece.WHITE_BISHOP, ChessPiece.WHITE_QUEEN);

}
