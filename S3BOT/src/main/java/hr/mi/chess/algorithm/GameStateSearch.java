package hr.mi.chess.algorithm;

import hr.mi.chess.algorithm.support.MVV_LVA;
import hr.mi.chess.algorithm.support.SearchInfo;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.evaluation.EvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GameStateSearch {
    private final EvaluationFunction evaluationFunction;
    private final int searchPly;
    private final SearchInfo searchInfo;
    //private static final MoveComparator moveComparator = new MoveComparator();
    private int statesSearched;

    public GameStateSearch(EvaluationFunction evaluationFunction, int searchPly) {
        this.evaluationFunction = evaluationFunction;
        this.searchPly = searchPly;
        this.searchInfo = new SearchInfo();
    }

    public Move getBestMove(BoardState boardState){
        statesSearched = 0;
        Move bestMove = getValueRec(boardState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, boardState.getActiveColour() == ChessConstants.WHITE ? 1 : -1).move;
        System.out.println(statesSearched);
        return bestMove;
    }


    private MoveValuePair getValueRec(BoardState boardState, int ply, double alpha, double beta, int colour){
        statesSearched++;
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        Move bestMove = null;
        if (ply >= searchPly){
            return new MoveValuePair(null,colour * evaluationFunction.evaluate(boardState));
        }

        if (moves.size() == 0){
            return new MoveValuePair(null,-colour * Double.MAX_VALUE);
        }

        orderMoves(moves, ply, boardState.getLastMovedPieceIndex());
        double value = -Double.MAX_VALUE;

        for (Move move: moves){
            boardState.makeMove(move);
            MoveValuePair result = this.getValueRec(boardState, ply + 1, -beta, -alpha, -colour);
            if (-result.value > value){
                value = -result.value;
                bestMove = move;
            }
            value = Math.max(value, -result.value);
            boardState.unmakeLastMove();
            alpha = Math.max(alpha, value);
            if (alpha >= beta){
                if (!move.isCapture()){
                    searchInfo.addKillerMove(ply, move);
                }
                break;
            }
        }

        return new MoveValuePair(bestMove, value);
    }


    private void orderMoves(List<Move> moves, int ply, int lastMovedPieceIndex) {
        moves.sort(new MoveComparator(ply, lastMovedPieceIndex).reversed());
    }

    private int scoreMove(Move move, int ply, int lastMovedPieceIndex){
        int score = 0;
        if (searchInfo.checkIfKiller(ply, move)){
            score += 50;
        }

        if (move.isCapture()){
            score += MVV_LVA.MVV_LVA_TABLE[move.getCapturedPieceIndex() % 6][move.getPiece() % 6];

            if (move.getTo() == lastMovedPieceIndex)
                score += 1001;
        }
        return score;
    }

    private static record MoveValuePair (Move move, double value) {}

    private class MoveComparator implements Comparator<Move> {

        private final int currentPly;
        private final int lastMovedPieceIndex;

        public MoveComparator(int currentPly, int lastMovedPieceIndex) {
            this.currentPly = currentPly;
            this.lastMovedPieceIndex = lastMovedPieceIndex;
        }

        @Override
        public int compare(Move m1, Move m2) {
            return scoreMove(m1, currentPly, lastMovedPieceIndex) - scoreMove(m2, currentPly, lastMovedPieceIndex);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
