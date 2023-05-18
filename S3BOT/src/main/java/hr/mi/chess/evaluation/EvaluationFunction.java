package hr.mi.chess.evaluation;

import hr.mi.chess.models.BoardState;

/**
 * Model of a class that evaluates a board position, it's only function returning a double representing the board states "goodness"
 * @author Matej Istuk
 */
public interface EvaluationFunction {

    /**
     * Evaluates the given board-state, returning its "goodness"
     *
     * @param boardState the board-state to be evaluates
     * @return a double representing the absolute "goodness" of the board-state
     */
    int evaluate(BoardState boardState);

    /**
     * Sets the perspective from which the board will be evaluated for the evaluation function.
     * @param perspective chess colour
     */
    void setPerspective(boolean perspective);
}
