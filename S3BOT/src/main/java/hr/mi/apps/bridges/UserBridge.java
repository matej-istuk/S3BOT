package hr.mi.apps.bridges;

import hr.mi.support.FromToPair;

/**
 * Interface which describes a way for an input method to communicate with a chess game.
 * @author Matej Istuk
 */
public interface UserBridge {
    /**
     * Requests an input move.
     * @return returns a from to pair describing the received move.
     */
    FromToPair requestMoveInput();

    /**
     * Called when a pawn promotion move is made. The user must further choose to which piece the pawn is promoted to.
     * @return int assigned to the chosen promotion piece (Rook - 0, Knight - 1, Bishop - 2, Queen - 3)
     */
    int requestPromotedPiece();

    /**
     * Interrupts the game.
     */
    void stop();
}
