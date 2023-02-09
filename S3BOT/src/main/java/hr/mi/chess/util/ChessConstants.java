package hr.mi.chess.util;

import java.util.List;
import java.util.Map;

public class ChessConstants {
    public static final long FILE_A = 0x0101010101010101L;
    public static final long FILE_B = 0x0202020202020202L;
    public static final long FILE_C = 0x0404040404040404L;
    public static final long FILE_D = 0x0808080808080808L;
    public static final long FILE_E = 0x1010101010101010L;
    public static final long FILE_F = 0x2020202020202020L;
    public static final long FILE_G = 0x4040404040404040L;
    public static final long FILE_H = 0x8080808080808080L;

    public static final long RANK_1 = 0x00000000000000FFL;
    public static final long RANK_2 = 0x000000000000FF00L;
    public static final long RANK_3 = 0x0000000000FF0000L;
    public static final long RANK_4 = 0x00000000FF000000L;
    public static final long RANK_5 = 0x000000FF00000000L;
    public static final long RANK_6 = 0x0000FF0000000000L;
    public static final long RANK_7 = 0x00FF000000000000L;
    public static final long RANK_8 = 0xFF00000000000000L;

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

    public static final int WHITE_PAWN = 0;
    public static final int WHITE_ROOK = 1;
    public static final int WHITE_KNIGHT = 2;
    public static final int WHITE_BISHOP = 3;
    public static final int WHITE_QUEEN = 4;
    public static final int WHITE_KING = 5;
    public static final int BLACK_PAWN = 6;
    public static final int BLACK_ROOK = 7;
    public static final int BLACK_KNIGHT = 8;
    public static final int BLACK_BISHOP = 9;
    public static final int BLACK_QUEEN = 10;
    public static final int BLACK_KING = 11;

    public static final int NORTH = 8;
    public static final int EAST = 1;
    public static final int SOUTH = -8;
    public static final int WEST = -8;
    public static final int NORTH_WEST = 7;
    public static final int NORTH_EAST = 9;
    public static final int SOUTH_EAST = -7;
    public static final int SOUTH_WEST = -9;

    /**
     * Two north one west
     */
    public static final int KNIGHT_NORTH_WEST = 15;
    /**
     * Two north one east
     */
    public static final int KNIGHT_NORTH_EAST = 17;
    /**
     * Two east one north
     */
    public static final int KNIGHT_EAST_NORTH = 10;
    /**
     * Two east one south
     */
    public static final int KNIGHT_EAST_SOUTH = -6;
    /**
     * Two south one east
     */
    public static final int KNIGHT_SOUTH_EAST= -15;
    /**
     * Two south one west
     */
    public static final int KNIGHT_SOUTH_WEST = -17;
    /**
     * Two west one south
     */
    public static final int KNIGHT_WEST_SOUTH = -10;
    /**
     * Two west one north
     */
    public static final int KNIGHT_WEST_NORTH = 6;

    /**
     * No move mask starting positions for two north one west
     */
    public static final long NO_MOVE_KNIGHT_NORTH_WEST = RANK_8 | RANK_7 | FILE_A;
    /**
     * No move mask starting positions for two north one east
     */
    public static final long NO_MOVE_KNIGHT_NORTH_EAST = RANK_8 | RANK_7 | FILE_H;
    /**
     * No move mask starting positions for two east one north
     */
    public static final long NO_MOVE_KNIGHT_EAST_NORTH = FILE_H | FILE_F | RANK_8;
    /**
     * No move mask starting positions for two east one south
     */
    public static final long NO_MOVE_KNIGHT_EAST_SOUTH = FILE_H | FILE_F | RANK_1;
    /**
     * No move mask starting positions for two south one east
     */
    public static final long NO_MOVE_KNIGHT_SOUTH_EAST= RANK_1 | RANK_3 | FILE_H;
    /**
     * No move mask starting positions for two south one west
     */
    public static final long NO_MOVE_KNIGHT_SOUTH_WEST = RANK_1 | RANK_3 | FILE_A;
    /**
     * No move mask starting positions for two west one south
     */
    public static final long NO_MOVE_KNIGHT_WEST_SOUTH = FILE_A | FILE_B | RANK_1;
    /**
     * No move mask starting positions for two west one north
     */
    public static final long NO_MOVE_KNIGHT_WEST_NORTH = FILE_A | FILE_B | RANK_8;

    public static final long[] KNIGHT_MOVES = {132096L, 329728L, 659712L, 1319424L, 2638848L, 5277696L, 10489856L, 4202496L, 33816580L, 84410376L, 168886289L, 337772578L, 675545156L, 1351090312L, 2685403152L, 1075839008L, 8657044482L, 21609056261L, 43234889994L, 86469779988L, 172939559976L, 345879119952L, 687463207072L, 275414786112L, 2216203387392L, 5531918402816L, 11068131838464L, 22136263676928L, 44272527353856L, 88545054707712L, 175990581010432L, 70506185244672L, 567348067172352L, 1416171111120896L, 2833441750646784L, 5666883501293568L, 11333767002587136L, 22667534005174272L, 45053588738670592L, 18049583422636032L, 145241105196122112L, 362539804446949376L, 725361088165576704L, 1450722176331153408L, 2901444352662306816L, 5802888705324613632L, -6913025356609880064L, 4620693356194824192L, 288234782788157440L, 576469569871282176L, 1224997833292120064L, 2449995666584240128L, 4899991333168480256L, -8646761407372591104L, 1152939783987658752L, 2305878468463689728L, 1128098930098176L, 2257297371824128L, 4796069720358912L, 9592139440717824L, 19184278881435648L, 38368557762871296L, 4679521487814656L, 9077567998918656L};


    public static final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
}
