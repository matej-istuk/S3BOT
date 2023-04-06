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
    private SearchInfo searchInfo;
    //private static final MoveComparator moveComparator = new MoveComparator();
    private int statesSearched;
    private int quiescenceStatesSearched;

    public GameStateSearch(EvaluationFunction evaluationFunction, int searchPly) {
        this.evaluationFunction = evaluationFunction;
        this.searchPly = searchPly;
    }

    public Move getBestMove(BoardState boardState){
        this.searchInfo = new SearchInfo();
        long searchStartTime = System.currentTimeMillis();
        statesSearched = 0;
        quiescenceStatesSearched = 0;
        Move bestMove = getValueRec(boardState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, boardState.getActiveColour() == ChessConstants.WHITE ? 1 : -1).move;
        double totalTime = System.currentTimeMillis() - searchStartTime;
        System.out.printf("Standard: %d%nQuiescence: %d%nTotal: %d%nTime spent: %.2fs%nNodes per second: %.0f%n%n", statesSearched, quiescenceStatesSearched, statesSearched + quiescenceStatesSearched, totalTime/1000, 1000L * (statesSearched + quiescenceStatesSearched) / totalTime);
        return bestMove;
    }


    private MoveValuePair getValueRec(BoardState boardState, int ply, double alpha, double beta, int colour){
        statesSearched++;

        if (ply >= searchPly) {
            return new MoveValuePair(null, getQuiescenceEvaluation(boardState, alpha, beta, colour));
        }

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (moves.size() == 0)
            return new MoveValuePair(null,-colour * Double.MAX_VALUE);

        orderMoves(moves, ply, boardState.getLastMovedPieceIndex());
        double value = -Double.MAX_VALUE;
        Move bestMove = null;

        for (Move move: moves){
            boardState.makeMove(move);
            MoveValuePair result = this.getValueRec(boardState, ply + 1, -beta, -alpha, -colour);
            if (-result.value > value){
                value = -result.value;
                bestMove = move;
            }
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

    private double getQuiescenceEvaluation(BoardState boardState, double alpha, double beta, int colour) {
        quiescenceStatesSearched++;
        double standingPat = colour * evaluationFunction.evaluate(boardState);

        if (standingPat >= beta) {
            return beta;
        }

        if (alpha < standingPat) {
            alpha = standingPat;
        }

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        moves.removeIf(m -> !m.isCapture());
        orderMoves(moves, -1, boardState.getLastMovedPieceIndex());
        double value;

        for (Move move: moves) {
            boardState.makeMove(move);
            value = -this.getQuiescenceEvaluation(boardState, -beta, -alpha, -colour);
            boardState.unmakeLastMove();

            if (value >= beta){
                return beta;
            }

            if (value > alpha) {
                alpha = value;
            }
        }

        return alpha;
    }

    private void orderMoves(List<Move> moves, int ply, int lastMovedPieceIndex) {
        moves.sort(new MoveComparator(ply, lastMovedPieceIndex).reversed());
    }

    private int scoreMove(Move move, int ply, int lastMovedPieceIndex){
        int score = 0;
        if (ply != -1 && searchInfo.checkIfKiller(ply, move)){
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
