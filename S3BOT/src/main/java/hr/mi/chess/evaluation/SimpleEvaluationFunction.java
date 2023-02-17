package hr.mi.chess.evaluation;

import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.models.BoardState;

public class SimpleEvaluationFunction implements EvaluationFunction{

    private static final int[] pieceValues = {1, 3, 3, 5, 9, 0};

    /**
     * Evaluates the given board-state, returning its "goodness"
     *
     * @param boardState the board-state to be evaluates
     * @return a double representing the absolute "goodness" of the board-state
     */
    @Override
    public double evaluate(BoardState boardState) {
        return calculateBoardValueByColour(boardState.getBitboards(), ChessConstants.WHITE)
                - calculateBoardValueByColour(boardState.getBitboards(), ChessConstants.BLACK);
    }

    private double calculateBoardValueByColour(long[] bitboards, boolean colour){
        int offset = colour == ChessConstants.WHITE ? 0 : 6;
        double res = 0;

        for (int i = 0; i < 6; i++){
            res += Long.bitCount(bitboards[i + offset]) * pieceValues[i];
        }

        return res;
    }
}
