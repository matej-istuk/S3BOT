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

/**
 * Class which preforms the search of the game tree. Uses an iterative depth first search and the negamax algorithm
 * to find the best move in accordance with the limitations put in place by the <code>SearchEndCondition</code>.
 * @author Matej Istuk
 */
public class GameStateSearch {
    private final EvaluationFunction evaluationFunction;
    private final SearchInfo searchInfo;
    //private static final MoveComparator moveComparator = new MoveComparator();
    private long statesSearched;
    private int quiescenceStatesSearched;
    private long searchStartTime;
    private SearchEndCondition searchEndCondition;
    private int searchStartMove;
    private long ttHit;

    /**
     * The constructor, sets the transposition table size to 33554432 entries.
     * @param evaluationFunction the evaluation function which the search will use
     */
    public GameStateSearch(EvaluationFunction evaluationFunction) {
        this(evaluationFunction, 33554432);
    }

    /**
     * The constructor.
     * @param evaluationFunction the evaluation function which the search will use
     * @param ttSize size of the transposition table (if the size isn't a power of two, it will be rounded down to the
     *               highest power of two less than the received number)
     */
    public GameStateSearch(EvaluationFunction evaluationFunction, int ttSize) {
        this.evaluationFunction = evaluationFunction;
        this.searchInfo = new SearchInfo(ttSize);
    }

    /**
     * Returns the best move found for the received <code>BoardState</code>. The search is limited by the
     * <code>SearchEndCondition</code>
     * @param boardState the board-state
     * @param searchEndCondition search limit
     * @return the best found move
     */
    public Move getBestMove(BoardState boardState, SearchEndCondition searchEndCondition){
        try {

            this.searchStartMove = boardState.getFullMoves();
            searchInfo.clearKillerMoves();
            this.searchEndCondition = searchEndCondition;
            this.searchStartTime = System.currentTimeMillis();
            evaluationFunction.setPerspective(boardState.getActiveColour());

            List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
            if (moves.isEmpty()) {
                return null;
            }

            Move bestMove = moves.get(0);
            int maxDepth = 0;
            for (int i = 1; i < searchEndCondition.getMaxDepth(); i++) {
                ttHit = 0;
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
                if (bestMoveCandidate != null) {
                    bestMove = bestMoveCandidate;
                    maxDepth = i;
                    //System.out.printf("TtHit at %d: %d%n", i, ttHit);
                }
            }
            System.out.println(System.currentTimeMillis() - searchStartTime + " " + maxDepth + " " + boardState.getFEN());
            return bestMove;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Recursive function which navigates the game tree depth first. Implementation of the negamax algorithm with
     * alpha-beta pruning and transposition tables
     * @param boardState the boardstate, central object of the search
     * @param ply current depth of search
     * @param maxSearchDepth max search depth
     * @param alpha alpha used in alpha beta pruning
     * @param beta beta used in alpha beta pruning
     * @param colour represents if the level of search is even or odd (necessary for negamax)
     * @return the best move for the node and the value of the searched node
     */
    private MoveValuePair getBestMoveRec(BoardState boardState, int ply, int maxSearchDepth, double alpha, double beta, int colour){

        //search termination conditions
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

        //check if the boardstate is a draw (by the 50 move rule or by repetition, draw by no moves is checked later on)
        if (boardState.isDraw()) {
            return new MoveValuePair(null, 0);
        }

        //Necessary for transposition tables, since alpha changes through the search
        double originalAlpha = alpha;

        //The most important search end condition, if this is met, the boardstate is evaluated by a further quiescence
        //search. Counters the horizon effect.
        if (ply >= maxSearchDepth) {
            return new MoveValuePair(null, getQuiescenceEvaluation(boardState, ply, alpha, beta, colour));
        }


        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (moves.size() == 0) {
            return new MoveValuePair(null, -evaluateNoMoveBoard(boardState, ply));
        }


        //Check if the current boardstate has appeared before, and if it is ok to use (searched to a greater or equal
        //depth...). If so, use it.
        SearchInfo.TTEntry ttEntry = searchInfo.ttGet(boardState.getZobristHash());

        if (ttEntry != null && ttEntry.zobristHash() == boardState.getZobristHash() && ttEntry.creationMove() == this.searchStartMove && ttEntry.depth() >= (maxSearchDepth - ply) && moves.contains(ttEntry.bestMove())){
            ttHit++;
            //Since the algorithm is using alpha beta pruning, we don't always know the exact value of the node, only
            //the upper or lower bound.
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
        //Orders moves to help with cutoffs
        orderMoves(moves, ply, boardState.getLastMovedPieceIndex(), ttEntry != null ? ttEntry.bestMove() : null);
        double value = -Double.MAX_VALUE;
        Move bestMove = null;

        //search through possible moves to find the best
        for (Move move: moves){
            boardState.makeMove(move);
            MoveValuePair result = this.getBestMoveRec(boardState, ply + 1, maxSearchDepth, -beta, -alpha, -colour);
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

        //save the search results to the transposition table
        int ttType = SearchInfo.EXACT;

        if (value <= originalAlpha){
            ttType = SearchInfo.UPPER_BOUND;
        } else if (value >= beta){
            ttType = SearchInfo.LOWER_BOUND;
        }

        SearchInfo.TTEntry newEntry = new SearchInfo.TTEntry(boardState.getZobristHash(), value, ttType, bestMove, maxSearchDepth - ply, this.searchStartMove);
        searchInfo.ttStore(newEntry);

        return new MoveValuePair(bestMove, value);
    }

    /**
     * Searches the boardstate recursively to stabilize it (make all possible trades, to get a more accurate evaluation
     * of the state). Counter to the horizon effect.
     * @param boardState the boardstate, central object of the search
     * @param ply current depth of search
     * @param alpha alpha used in alpha beta pruning
     * @param beta beta used in alpha beta pruning
     * @param colour represents if the level of search is even or odd (necessary for negamax)
     * @return the value of the search
     */
    private double getQuiescenceEvaluation(BoardState boardState, int ply, double alpha, double beta, int colour) {
        quiescenceStatesSearched++;
        //theoretical lowest limit, unless the game is in Zugzwang. We're assuming that at least one capture leads to a
        //better position
        double standingPat = colour*evaluationFunction.evaluate(boardState);

        if (standingPat >= beta) {
            return beta;
        }

        if (alpha < standingPat) {
            alpha = standingPat;
        }

        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (moves.isEmpty()) {
            return -evaluateNoMoveBoard(boardState, ply);
        }

        //remove non captures
        moves.removeIf(m -> !m.isCapture());
        orderMoves(moves, -1, boardState.getLastMovedPieceIndex(), null);
        double value;

        //check moves
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

    /**
     * Orders the received moves according to various heuristics, see <code>scoreMove</code> for more.
     * @param moves the move list to be ordered.
     * @param ply current depth of search
     * @param lastMovedPieceIndex index of the tile to which the last moved piece was moved to
     * @param ttMove move from the transposition table, can be null
     */
    private void orderMoves(List<Move> moves, int ply, int lastMovedPieceIndex, Move ttMove) {
        Map<Move, Integer> scores = new HashMap<>();

        moves.forEach(move -> scores.put(move, scoreMove(move, ply, lastMovedPieceIndex, ttMove)));
        moves.sort(new MoveComparator(scores));
    }

    /**
     * Gives a score to the move, used in move ordering. Applies the following heuristics for scoring:
     * <ul>
     *     <li><a href="https://www.chessprogramming.org/Killer_Move">Killer Moves</a></li>
     *     <li><a href="https://www.chessprogramming.org/MVV-LVA">MVV_LVA</a></li>
     *     Move stored in the transposition table
     * </ul>
     * @param move move being scored
     * @param ply depth of search in which the move was found
     * @param lastMovedPieceIndex index of the tile to which the last moved piece was moved to
     * @param ttMove move from the transposition table, can be null
     * @return move score
     */
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

    /**
     * Used to evaluate a board with no legal moves. Returns (Integer.MAX_VALUE / 2) - ply for a mate, and 0 for a
     * draw
     * @param boardState boardstate being evaluated
     * @param ply depth at which the no move board was found
     * @return score.
     */
    private double evaluateNoMoveBoard(BoardState boardState, int ply) {
        if ((MoveUtil.getKingDangerSquares(boardState.getBitboards(), boardState.getActiveColour()) & boardState.getBitboards()[(boardState.getActiveColour() == ChessConstants.WHITE) ? 5 : 11]) != 0L){
            return ((double) Integer.MAX_VALUE / 2) - ply;
        }
        return 0;
    }

    /**
     * Pair of a move and a value, result of the search algorithm
     * @param move
     * @param value
     */
    private  record MoveValuePair (Move move, double value) {}

    /**
     * Comparator for a Move, sorts moves on heuristic values described in the method <code>scoreMove</code>
     */
    private static class MoveComparator implements Comparator<Move> {

        private final Map<Move, Integer> scores;

        /**
         * @param scores Map of precalculated moves and heuristic values
         */
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
