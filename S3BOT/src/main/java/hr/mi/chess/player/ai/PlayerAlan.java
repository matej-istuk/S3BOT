package hr.mi.chess.player.ai;

import hr.mi.chess.algorithm.Negamax;
import hr.mi.chess.evaluation.SimpleEvaluationFunction;
import hr.mi.chess.evaluation.SimplePlusEvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;

import java.util.List;

public class PlayerAlan implements Player {

    private int depth;

    public PlayerAlan(int depth) {
        this.depth = depth;
    }

    @Override
    public Move requestMove(BoardState boardState) {
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        Negamax negamax = new Negamax(new SimplePlusEvaluationFunction());

        Move bestMove = null;
        double maxValue = -Double.MAX_VALUE;
        for (Move move: moves){
            boardState.makeMove(move);
            double moveValue = negamax.getValue(boardState, depth - 1);
            boardState.unmakeLastMove();
            if (moveValue >= maxValue){
                maxValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }
}
