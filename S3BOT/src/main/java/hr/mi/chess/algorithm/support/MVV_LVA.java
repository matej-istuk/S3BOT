package hr.mi.chess.algorithm.support;

public class MVV_LVA {

    private final int[] pieceValues = {
            100,
            500,
            320,
            330,
            900,
            200000
    };

    /**
     * indexed by [victim][attacker]
     */
    public static final int[][] MVV_LVA_TABLE= {
            {100 - 10, 100 - 50, 100 - 32, 100 - 33, 100 - 90, 100 - 90}, // victim - P, attackers - P, R, N, B, Q, K
            {500 - 10, 500 - 50, 500 - 32, 500 - 33, 500 - 90, 500 - 90}, // victim - P, attackers - P, R, N, B, Q, K
            {320 - 10, 320 - 50, 320 - 32, 320 - 33, 320 - 90, 320 - 90}, // victim - P, attackers - P, R, N, B, Q, K
            {330 - 10, 330 - 50, 330 - 32, 330 - 33, 330 - 90, 330 - 90}, // victim - P, attackers - P, R, N, B, Q, K
            {900 - 10, 900 - 50, 900 - 32, 900 - 33, 900 - 90, 900 - 90}, // victim - P, attackers - P, R, N, B, Q, K
    };
}
