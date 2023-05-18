package hr.mi.chess.constants;

import hr.mi.chess.models.ChessPiece;

import java.util.List;
import java.util.Map;

/**
 * Class of constants related to the chess board itself.
 * @author Matej Istuk
 */
public class ChessBoardConstants {

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

    public static final int NORTH = 8;
    public static final int EAST = 1;
    public static final int SOUTH = -8;
    public static final int WEST = -1;
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
    public static final long NO_MOVE_KNIGHT_EAST_NORTH = FILE_H | FILE_G | RANK_8;
    /**
     * No move mask starting positions for two east one south
     */
    public static final long NO_MOVE_KNIGHT_EAST_SOUTH = FILE_H | FILE_G | RANK_1;
    /**
     * No move mask starting positions for two south one east
     */
    public static final long NO_MOVE_KNIGHT_SOUTH_EAST= RANK_1 | RANK_2 | FILE_H;
    /**
     * No move mask starting positions for two south one west
     */
    public static final long NO_MOVE_KNIGHT_SOUTH_WEST = RANK_1 | RANK_2 | FILE_A;
    /**
     * No move mask starting positions for two west one south
     */
    public static final long NO_MOVE_KNIGHT_WEST_SOUTH = FILE_A | FILE_B | RANK_1;
    /**
     * No move mask starting positions for two west one north
     */
    public static final long NO_MOVE_KNIGHT_WEST_NORTH = FILE_A | FILE_B | RANK_8;
    public static final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static final long WHITE_KING_SIDE_CASTLING_MASK_BLOCKERS = 96L;
    public static final long WHITE_QUEEN_SIDE_CASTLING_MASK_BLOCKERS = 14L;
    public static final long BLACK_KING_SIDE_CASTLING_MASK_BLOCKERS = 6917529027641081856L;
    public static final long BLACK_QUEEN_SIDE_CASTLING_MASK_BLOCKERS = 1008806316530991104L;
    public static final long WHITE_KING_SIDE_CASTLING_MASK_ATTACKED_SQUARES = 96L;
    public static final long WHITE_QUEEN_SIDE_CASTLING_MASK_ATTACKED_SQUARES = 12L;
    public static final long BLACK_KING_SIDE_CASTLING_MASK_ATTACKED_SQUARES = 6917529027641081856L;
    public static final long BLACK_QUEEN_SIDE_CASTLING_MASK_ATTACKED_SQUARES = 864691128455135232L;

}
