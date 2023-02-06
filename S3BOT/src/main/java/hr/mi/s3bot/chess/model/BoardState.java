package hr.mi.s3bot.chess.model;

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
    private char activeColour;
    private String enPassantTarget;

    public BoardState() {
        this(STARTING_POSITION_FEN);
    }

    public BoardState(String fen){
        loadFen(fen);
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
        if (fenArr[1].equals("b")){
            this.activeColour = 'b';
        } else {
            this.activeColour = 'w';
        }

        //Castling Rights
        whiteKingSideCastling = fenArr[2].contains("K");
        whiteQueenSideCastling = fenArr[2].contains("Q");
        blackKingSideCastling = fenArr[2].contains("k");
        blackQueenSideCastling = fenArr[2].contains("q");

        //Possible En Passant Targets
        if (!fenArr[3].equals("-")){
            enPassantTarget = fenArr[3];
        }

        //Halfmove Clock
        halfMoveClock = Integer.parseInt(fenArr[4]);

        //Fullmove Number
        fullMoves = Integer.parseInt(fenArr[5]);
    }

    /**
     * @return returns the bitboards
     */
    public long[] getBitboards() {
        return bitboards;
    }
}
