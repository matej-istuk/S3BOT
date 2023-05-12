package hr.mi.chess.algorithm;

import hr.mi.chess.algorithm.support.MVV_LVA;
import hr.mi.chess.algorithm.support.SearchInfo;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.evaluation.EvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.movegen.helpers.MoveUtil;

import java.util.*;

public class GameStateSearch {
    private final EvaluationFunction evaluationFunction;
    private final SearchInfo searchInfo;
    //private static final MoveComparator moveComparator = new MoveComparator();
    private long statesSearched;
    private int quiescenceStatesSearched;
    private long searchStartTime;
    private SearchEndCondition searchEndCondition;

    public GameStateSearch(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        this.searchInfo = new SearchInfo();
    }

    public Move getBestMove(BoardState boardState, SearchEndCondition searchEndCondition){
        searchInfo.clearKillerMoves();
        this.searchEndCondition = searchEndCondition;
        this.searchStartTime = System.currentTimeMillis();
        evaluationFunction.setPerspective(boardState.getActiveColour());

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        if (moves.isEmpty()){
            return null;
        }

        Move bestMove = moves.get(0);
        for (int i = 1; i < searchEndCondition.getMaxDepth(); i++) {
            statesSearched = 0;
            quiescenceStatesSearched = 0;
            MoveValuePair mvp = getBestMoveRec(boardState, 0, i, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            Move bestMoveCandidate = mvp.move;
            //System.out.println(i + ": " + bestMoveCandidate);
            if ((statesSearched + quiescenceStatesSearched) >= searchEndCondition.getMaxNodes()) {
                break;
            }

            if ((System.currentTimeMillis() - searchStartTime) > searchEndCondition.getMaxTime() || searchEndCondition.isManualStop()) {
                break;
            }
            bestMove = bestMoveCandidate;
        }
        System.out.println(System.currentTimeMillis() - searchStartTime);
        return bestMove;
    }


    private MoveValuePair getBestMoveRec(BoardState boardState, int ply, int searchDepth, double alpha, double beta, int colour){
        double originalAlpha = alpha;

        //search termination conditions
        if (ply >= searchDepth) {
            return new MoveValuePair(null, getQuiescenceEvaluation(boardState, ply, alpha, beta, colour));
        }

        if (searchEndCondition.isManualStop()){
            return new MoveValuePair(null, 0);
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

        //tt stuff
        SearchInfo.TTEntry ttEntry = searchInfo.ttGet(boardState.getZobristHash());

        if (ttEntry != null && ttEntry.zobristHash() == boardState.getZobristHash() && ttEntry.depth() == (searchDepth - ply) && moves.contains(ttEntry.bestMove())){
            switch (ttEntry.type()) {
                case SearchInfo.EXACT -> {
                    return new MoveValuePair(ttEntry.bestMove(), ttEntry.value());
                }
                case SearchInfo.LOWER_BOUND -> {
                    alpha = Math.max(alpha, ttEntry.value());
                }
                case SearchInfo.UPPER_BOUND -> {
                    beta = Math.min(beta, ttEntry.value());
                }
            }

            if (alpha >= beta) {
                return new MoveValuePair(ttEntry.bestMove(), ttEntry.value());
            }
        }

        statesSearched++;
        orderMoves(moves, ply, boardState.getLastMovedPieceIndex(), ttEntry != null ? ttEntry.bestMove() : null);
        double value = -Double.MAX_VALUE;
        Move bestMove = null;

        for (Move move: moves){
            boardState.makeMove(move);
            MoveValuePair result = this.getBestMoveRec(boardState, ply + 1, searchDepth, -beta, -alpha, -colour);
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

        int ttType = SearchInfo.EXACT;

        if (value <= originalAlpha){
            ttType = SearchInfo.UPPER_BOUND;
        } else if (value >= beta){
            ttType = SearchInfo.LOWER_BOUND;
        }

        searchInfo.ttStore(new SearchInfo.TTEntry(boardState.getZobristHash(), value, ttType, bestMove, searchDepth - ply));

        return new MoveValuePair(bestMove, value);
    }

    private double getQuiescenceEvaluation(BoardState boardState, int ply, double alpha, double beta, int colour) {
        quiescenceStatesSearched++;
        double standingPat = colour*evaluationFunction.evaluate(boardState);

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
        orderMoves(moves, -1, boardState.getLastMovedPieceIndex(), null);
        double value;

        for (Move move: moves) {
            boardState.makeMove(move);
            value = -this.getQuiescenceEvaluation(boardState, ply + 1, -beta, -alpha, -colour);
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

    private void orderMoves(List<Move> moves, int ply, int lastMovedPieceIndex, Move ttMove) {
        Map<Move, Integer> scores = new HashMap<>();

        moves.forEach(move -> scores.put(move, scoreMove(move, ply, lastMovedPieceIndex, ttMove)));
        moves.sort(new MoveComparator(scores));
    }

    private int scoreMove(Move move, int ply, int lastMovedPieceIndex, Move ttMove){
        int score = 0;
        if (ply != -1 && searchInfo.checkIfKiller(ply, move)){
            score += 50;
        }

        if (Objects.equals(move, ttMove)){
            score += 1000;
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
            return (double) Integer.MAX_VALUE /2;
        }
        return 0;
    }
    private static record MoveValuePair (Move move, double value) {}

    private static class MoveComparator implements Comparator<Move> {

        private final Map<Move, Integer> scores;
        public MoveComparator(Map<Move, Integer> scores) {
            this.scores = scores;
        }

        @Override
        public int compare(Move m1, Move m2) {
            return scores.get(m2) - scores.get(m1);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
