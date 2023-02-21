package hr.mi.chess.constants;

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

    public static final int[] NON_SLIDER_ATTACKERS = {0, 2};

    public static final int[] COMPASS_ROSE = {7, 8, 9, 1, -7, -8, -9, -1};
    public static final int[] DIAGONAL_SLIDING_ATTACKERS = {3, 4};
    public static final int[] STRAIGHT_SLIDING_ATTACKERS = {1, 4};

    public static final Map<Integer, int[]> POSSIBLE_ATTACKERS_BY_OFFSET = Map.ofEntries(
            Map.entry(7, DIAGONAL_SLIDING_ATTACKERS),
            Map.entry(8, STRAIGHT_SLIDING_ATTACKERS),
            Map.entry(9, DIAGONAL_SLIDING_ATTACKERS),
            Map.entry(1, STRAIGHT_SLIDING_ATTACKERS),
            Map.entry(-7, DIAGONAL_SLIDING_ATTACKERS),
            Map.entry(-8, STRAIGHT_SLIDING_ATTACKERS),
            Map.entry(-9, DIAGONAL_SLIDING_ATTACKERS),
            Map.entry(-1, STRAIGHT_SLIDING_ATTACKERS)
        );

    public static final long[] KNIGHT_MOVES = {132096L, 329728L, 659712L, 1319424L, 2638848L, 5277696L, 10489856L, 4202496L, 33816580L, 84410376L, 168886289L, 337772578L, 675545156L, 1351090312L, 2685403152L, 1075839008L, 8657044482L, 21609056261L, 43234889994L, 86469779988L, 172939559976L, 345879119952L, 687463207072L, 275414786112L, 2216203387392L, 5531918402816L, 11068131838464L, 22136263676928L, 44272527353856L, 88545054707712L, 175990581010432L, 70506185244672L, 567348067172352L, 1416171111120896L, 2833441750646784L, 5666883501293568L, 11333767002587136L, 22667534005174272L, 45053588738670592L, 18049583422636032L, 145241105196122112L, 362539804446949376L, 725361088165576704L, 1450722176331153408L, 2901444352662306816L, 5802888705324613632L, -6913025356609880064L, 4620693356194824192L, 288234782788157440L, 576469569871282176L, 1224997833292120064L, 2449995666584240128L, 4899991333168480256L, -8646761407372591104L, 1152939783987658752L, 2305878468463689728L, 1128098930098176L, 2257297371824128L, 4796069720358912L, 9592139440717824L, 19184278881435648L, 38368557762871296L, 4679521487814656L, 9077567998918656L};
    public static final long[] KING_MOVES = {770L, 1797L, 3594L, 7188L, 14376L, 28752L, 57504L, 49216L, 197123L, 460039L, 920078L, 1840156L, 3680312L, 7360624L, 14721248L, 12599488L, 50463488L, 117769984L, 235539968L, 471079936L, 942159872L, 1884319744L, 3768639488L, 3225468928L, 12918652928L, 30149115904L, 60298231808L, 120596463616L, 241192927232L, 482385854464L, 964771708928L, 825720045568L, 3307175149568L, 7718173671424L, 15436347342848L, 30872694685696L, 61745389371392L, 123490778742784L, 246981557485568L, 211384331665408L, 846636838289408L, 1975852459884544L, 3951704919769088L, 7903409839538176L, 15806819679076352L, 31613639358152704L, 63227278716305408L, 54114388906344448L, 216739030602088448L, 505818229730443264L, 1011636459460886528L, 2023272918921773056L, 4046545837843546112L, 8093091675687092224L, -2260560722335367168L, -4593460513685372928L, 144959613005987840L, 362258295026614272L, 724516590053228544L, 1449033180106457088L, 2898066360212914176L, 5796132720425828352L, -6854478632857894912L, 4665729213955833856L};

    public static final Map<ChessPiece, long[]> NON_SLIDING_PIECE_MOVES = Map.ofEntries(
            Map.entry(ChessPiece.WHITE_KNIGHT, KNIGHT_MOVES),
            Map.entry(ChessPiece.WHITE_KING, KING_MOVES),
            Map.entry(ChessPiece.BLACK_KNIGHT, KNIGHT_MOVES),
            Map.entry(ChessPiece.BLACK_KING, KING_MOVES)
    );
}