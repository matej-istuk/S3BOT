package hr.mi.chess.algorithm;

import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.evaluation.EvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.Collections;
import java.util.List;

public class Negamax {
    private final EvaluationFunction evaluationFunction;

    public Negamax(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    public double getValue(BoardState boardState, int depth){
        return -getValueRec(boardState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, boardState.getActiveColour() == ChessConstants.WHITE ? 1 : -1);
    }


    private double getValueRec(BoardState boardState, int depth, double alpha, double beta, int colour){
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (depth == 0){
            return colour * evaluationFunction.evaluate(boardState);
        }

        if (moves.size() == 0){
            return -colour * Double.MAX_VALUE;
        }

        //TODO order moves for better alpha beta pruning

        double value = -Double.MAX_VALUE;

        for (Move move: moves){
            boardState.makeMove(move);
            value = Math.max(value, -this.getValueRec(boardState, depth - 1, -beta, -alpha, -colour));
            boardState.unmakeLastMove();
            alpha = Math.max(alpha, value);
            if (alpha >= beta){
                break;
            }
        }

        return value;
    }
}
