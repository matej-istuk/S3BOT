package hr.mi.chess.game;

/**
 * Listener for <code>ChessGame</code> related events.
 * @author Matej Istuk
 */
public interface GameListener {
    /**
     * Will be executed when the game state is updated or the game is saved
     */
    void gameStateUpdated();
}
