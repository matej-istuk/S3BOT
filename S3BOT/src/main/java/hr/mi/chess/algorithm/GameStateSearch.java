package hr.mi.chess.algorithm;

import hr.mi.chess.algorithm.support.MVV_LVA;
import hr.mi.chess.algorithm.support.SearchInfo;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.evaluation.EvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.movegen.helpers.MoveUtil;

import java.util.Comparator;
import java.util.List;

public class GameStateSearch {
    private final EvaluationFunction evaluationFunction;
    private SearchInfo searchInfo;
    //private static final MoveComparator moveComparator = new MoveComparator();
    private long statesSearched;
    private int quiescenceStatesSearched;
    private long searchStartTime;
    private SearchEndCondition searchEndCondition;

    public GameStateSearch(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }



    public Move getBestMove(BoardState boardState, SearchEndCondition searchEndCondition){
        this.searchInfo = new SearchInfo();
        this.searchEndCondition = searchEndCondition;
        this.searchStartTime = System.currentTimeMillis();
        evaluationFunction.setPerspective(boardState.getActiveColour());
        Move bestMove = LegalMoveGenerator.generateMoves(boardState).get(0);
        for (int i = 1; i < searchEndCondition.getMaxDepth(); i++) {
            statesSearched = 0;
            quiescenceStatesSearched = 0;
            MoveValuePair mvp = getBestMoveRec(boardState, 0, i, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            Move bestMoveCandidate = mvp.move;
            if ((statesSearched + quiescenceStatesSearched) >= searchEndCondition.getMaxNodes()) {
                break;
            }

            if ((System.currentTimeMillis() - searchStartTime) > searchEndCondition.getMaxTime() || searchEndCondition.isManualStop()) {
                break;
            }
            bestMove = bestMoveCandidate;
        }
        return bestMove;
    }


    private MoveValuePair getBestMoveRec(BoardState boardState, int ply, int searchDepth, double alpha, double beta, int level){

        if (ply >= searchDepth) {
            return new MoveValuePair(null, getQuiescenceEvaluation(boardState, alpha, beta, level));
        }

        if ((statesSearched + quiescenceStatesSearched) >= searchEndCondition.getMaxNodes()){
            return new MoveValuePair(null, 0);
        }

        if (((statesSearched + quiescenceStatesSearched) & 1023) == 0){
            if ((System.currentTimeMillis() - searchStartTime) > searchEndCondition.getMaxTime() || searchEndCondition.isManualStop()){
                return new MoveValuePair(null, 0);
            }
        }

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (moves.size() == 0) {
            return new MoveValuePair(null, -evaluateNoMoveBoard(boardState));
        }

        statesSearched++;
        orderMoves(moves, ply, boardState.getLastMovedPieceIndex());
        double value = -Double.MAX_VALUE;
        Move bestMove = null;

        for (Move move: moves){
            boardState.makeMove(move);
            MoveValuePair result = this.getBestMoveRec(boardState, ply + 1, searchDepth, -beta, -alpha, -level);
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

    private double getQuiescenceEvaluation(BoardState boardState, double alpha, double beta, int level) {
        quiescenceStatesSearched++;
        double standingPat = level*evaluationFunction.evaluate(boardState);

        if (standingPat >= beta) {
            return beta;
        }

        if (alpha < standingPat) {
            alpha = standingPat;
        }

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        if (moves.isEmpty()) {
            return -evaluateNoMoveBoard(boardState);
        }
        moves.removeIf(m -> !m.isCapture());
        orderMoves(moves, -1, boardState.getLastMovedPieceIndex());
        double value;

        for (Move move: moves) {
            boardState.makeMove(move);
            value = -this.getQuiescenceEvaluation(boardState, -beta, -alpha, -level);
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

    private double evaluateNoMoveBoard(BoardState boardState) {
        if ((MoveUtil.getKingDangerSquares(boardState.getBitboards(), boardState.getActiveColour()) & boardState.getBitboards()[(boardState.getActiveColour() == ChessConstants.WHITE) ? 5 : 11]) != 0L){
            return Integer.MAX_VALUE;
        }
        return 0;
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
