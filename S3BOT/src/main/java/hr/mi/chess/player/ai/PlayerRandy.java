package hr.mi.chess.player.ai;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;

import java.util.List;
import java.util.Random;

public class PlayerRandy implements Player {
    private final Random random;
    private final boolean pretendToThink;

    public PlayerRandy(int seed, boolean pretendToThink) {
        this.random = new Random(seed);
        this.pretendToThink = pretendToThink;
    }

    public PlayerRandy(boolean pretendToThink){
        this((new Random()).nextInt(), pretendToThink);
    }


    @Override
    public Move requestMove(BoardState boardState) {
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        if (pretendToThink){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return moves.get(random.nextInt(moves.size()));
    }
}
