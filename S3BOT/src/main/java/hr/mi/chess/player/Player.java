package hr.mi.chess.player;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;

/**
 * Interface defining a chess game player. A chess player can take a move request and be made to stop.
 * @author Matej Istuk
 */
public interface Player {
    /**
     * Requests a Move for the given boardstate from the Player
     * @param boardState boardstate
     * @return Move
     */
    Move requestMove(BoardState boardState);

    /**
     * Forces the player to stop
     */
    void stop();
}
