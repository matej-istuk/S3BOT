package hr.mi.chess.evaluation;

import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.movegen.helpers.BitboardUtil;
import hr.mi.chess.movegen.helpers.Bitwise;
import hr.mi.chess.util.BoardFunctions;

/**
 * Improvement upon the <code>SimpleEvaluationFunction</code>.
 * Source:
 * <a href="https://www.chessprogramming.org/Simplified_Evaluation_Function">link</a>
 */
public class SimplePlusEvaluationFunction implements EvaluationFunction{

    private final int[] pieceValues = {
            100,
            320,
            330,
            500,
            900,
            200000
    };

    private final int[][] pieceSquareTables = {
            //White Pawn
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    5, 10, 10,-20,-20, 10, 10,  5,
                    5, -5,-10,  0,  0,-10, -5,  5,
                    0,  0,  0, 20, 20,  0,  0,  0,
                    5,  5, 10, 25, 25, 10,  5,  5,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    0,  0,  0,  0,  0,  0,  0,  0
            },
            //White Rook
            {
                    0,  0,  0,  5,  5,  0,  0,  0,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    5, 10, 10, 10, 10, 10, 10,  5,
                    0,  0,  0,  0,  0,  0,  0,  0
            },
            //White Knight
            {
                    -50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -30,  0, 15, 20, 20, 15,  0,-30,
                    -30,  5, 15, 20, 20, 15,  5,-30,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -50,-40,-30,-30,-30,-30,-40,-50
            },
            //White Bishop
            {
                    -20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10,  5,  5, 10, 10,  5,  5,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -20,-10,-10,-10,-10,-10,-10,-20
            },
            //White Queen
            {
                    -20,-10,-10, -5, -5,-10,-10,-20,
                    -10,  0,  5,  0,  0,  0,  0,-10,
                    -10,  5,  5,  5,  5,  5,  0,-10,
                    0,  0,  5,  5,  5,  5,  0, -5,
                    -5,  0,  5,  5,  5,  5,  0, -5,
                    -10,  0,  5,  5,  5,  5,  0,-10,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -20,-10,-10, -5, -5,-10,-10,-20
            },
            //White King
            {
                    20, 30, 10,  0,  0, 10, 30, 20,
                    20, 20,  0,  0,  0,  0, 20, 20,
                    -10,-20,-20,-20,-20,-20,-20,-10,
                    -20,-30,-30,-40,-40,-30,-30,-20,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30
            },
            //Black Pawn
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    5,  5, 10, 25, 25, 10,  5,  5,
                    0,  0,  0, 20, 20,  0,  0,  0,
                    5, -5,-10,  0,  0,-10, -5,  5,
                    5, 10, 10,-20,-20, 10, 10,  5,
                    0,  0,  0,  0,  0,  0,  0,  0
            },
            //Black Rook
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    5, 10, 10, 10, 10, 10, 10,  5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    0,  0,  0,  5,  5,  0,  0,  0
            },
            //Black Knight
            {
                    -50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -30,  5, 15, 20, 20, 15,  5,-30,
                    -30,  0, 15, 20, 20, 15,  0,-30,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -50,-40,-30,-30,-30,-30,-40,-50
            },
            //Black Bishop
            {
                    -20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -10,  5,  5, 10, 10,  5,  5,-10,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -20,-10,-10,-10,-10,-10,-10,-20
            },
            //Black Queen
            {
                    -20,-10,-10, -5, -5,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5,  5,  5,  5,  0,-10,
                    -5,  0,  5,  5,  5,  5,  0, -5,
                    0,  0,  5,  5,  5,  5,  0, -5,
                    -10,  5,  5,  5,  5,  5,  0,-10,
                    -10,  0,  5,  0,  0,  0,  0,-10,
                    -20,-10,-10, -5, -5,-10,-10,-20
            },
            //Black King
            {
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -30,-40,-40,-50,-50,-40,-40,-30,
                    -20,-30,-30,-40,-40,-30,-30,-20,
                    -10,-20,-20,-20,-20,-20,-20,-10,
                    20, 20,  0,  0,  0,  0, 20, 20,
                    20, 30, 10,  0,  0, 10, 30, 20
            }

    };

    /**
     * Evaluates the given board-state, returning its "goodness"
     *
     * @param boardState the board-state to be evaluates
     * @return a double representing the absolute "goodness" of the board-state
     */
    @Override
    public int evaluate(BoardState boardState) {
        return calculateValueByColour(boardState, ChessConstants.WHITE) -
                calculateValueByColour(boardState, ChessConstants.BLACK);
    }

    private int calculateValueByColour(BoardState boardState, boolean colour){
        int result = 0;
        int offset = colour == ChessConstants.WHITE ? 0 : 6;

        for (int i = 0; i < 6; i++){
            long bitboard = boardState.getBitboards()[i + offset];

            while (bitboard != 0){
                int index = Bitwise.findIndexOfMS1B(bitboard);
                bitboard &= ~(1L << index);

                result += pieceValues[i] + pieceSquareTables[i+offset][index];
            }
        }

        return result;
    }

}
