package hr.mi.chess.evaluation;

import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.models.BoardState;

/**
 * Very simple evaluation function, only takes in material value of each side.
 * @author Matej Istuk
 */
public class SimpleEvaluationFunction implements EvaluationFunction{

    private boolean perspective;
    private static final int[] pieceValues = {1, 3, 3, 5, 9, 0};

    @Override
    public int evaluate(BoardState boardState) {
        return (perspective == ChessConstants.WHITE ? 1 : -1) * (calculateBoardValueByColour(boardState.getBitboards(), ChessConstants.WHITE)
                - calculateBoardValueByColour(boardState.getBitboards(), ChessConstants.BLACK));
    }

    @Override
    public void setPerspective(boolean perspective) {
        this.perspective = perspective;
    }

    /**
     * Calculates the value of the board for the received colour
     * @param bitboards bitboards of the boardstate
     * @param colour colour which is being scored
     * @return material value of the colour
     */
    private int calculateBoardValueByColour(long[] bitboards, boolean colour){
        int offset = colour == ChessConstants.WHITE ? 0 : 6;
        int res = 0;

        for (int i = 0; i < 6; i++){
            res += Long.bitCount(bitboards[i + offset]) * pieceValues[i];
        }

        return res;
    }
}
