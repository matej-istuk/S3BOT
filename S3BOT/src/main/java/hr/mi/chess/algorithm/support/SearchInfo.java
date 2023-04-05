package hr.mi.chess.algorithm.support;

import hr.mi.chess.models.Move;

import java.util.Objects;

public class SearchInfo {
    private final static int MAX_PLY = 125;
    private final static int MAX_KILLER_MOVES = 2;
    private final Move[][] killerMoves = new Move[MAX_PLY][MAX_KILLER_MOVES];

    public void addKillerMove(int currentPly, Move killerMove){
        Move firstKillerMove = killerMoves[currentPly][0];

        //The new killer move can't be the same as the most recent one added
        if (!Objects.equals(firstKillerMove, killerMove)){
            for (int i = MAX_KILLER_MOVES - 1; i > 0; i--) {
                killerMoves[currentPly][i] = killerMoves[currentPly][i-1];
            }

            killerMoves[currentPly][0] = killerMove;
        }
    }

    public boolean checkIfKiller(int currentPly, Move move) {
        for (Move killerMove: killerMoves[currentPly]) {
            if (Objects.equals(move, killerMove)){
                return true;
            }
        }
        return false;
    }
}
