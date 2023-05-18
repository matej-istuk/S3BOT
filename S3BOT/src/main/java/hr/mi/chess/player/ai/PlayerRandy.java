package hr.mi.chess.player.ai;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;

import java.util.List;
import java.util.Random;

/**
 * "AI" chess player. Plays randomly
 * @author Matej Istuk
 */
public class PlayerRandy implements Player {
    private final Random random;
    private final boolean pretendToThink;
    private boolean isStopped;

    /**
     * Constructor.
     * @param seed seed for the random number generator
     * @param pretendToThink if the player waits a second before returning a move
     */
    public PlayerRandy(int seed, boolean pretendToThink) {
        this.random = new Random(seed);
        this.pretendToThink = pretendToThink;
    }

    /**
     * Constructor. Selects random seed
     * @param pretendToThink if the player waits a second before returning a move
     */
    public PlayerRandy(boolean pretendToThink){
        this((new Random()).nextInt(), pretendToThink);
    }


    @Override
    public Move requestMove(BoardState boardState) {
        if (isStopped){
            throw new IllegalStateException();
        }
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

    @Override
    public void stop() {
        isStopped = true;
    }
}
