package hr.mi.apps.uci.support;

import hr.mi.chess.algorithm.GameStateSearch;
import hr.mi.chess.algorithm.SearchEndCondition;
import hr.mi.chess.evaluation.SimplePlusEvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;

import java.util.LinkedList;
import java.util.List;

public class SearchManager {
    private GameStateSearch gameStateSearch;
    private List<Move> searchMoves = new LinkedList<>();
    private boolean ponder = false;
    private int wTime = -1;
    private int bTime = -1;
    private int wInc = -1;
    private int bInc = -1;
    private int movesToGo = -1;
    private int depth = Integer.MAX_VALUE;
    private long nodes = Long.MAX_VALUE;
    private int mate = Integer.MAX_VALUE;
    private long moveTime = Long.MAX_VALUE;
    private boolean infinite;
    private final SearchEndCondition searchEndCondition;

    public SearchManager() {
        this.gameStateSearch = new GameStateSearch(new SimplePlusEvaluationFunction());
        this.searchEndCondition = new SearchEndCondition();
    }

    public Move findBestMove(BoardState boardState) {
        setSearchEndCondition();
        return gameStateSearch.getBestMove(boardState, searchEndCondition);
    }

    private void setSearchEndCondition() {
        searchEndCondition.setMaxDepth(depth);
        searchEndCondition.setMaxNodes(nodes);
        searchEndCondition.setMaxTime(moveTime);
    }

    private void stopSearch() {
        searchEndCondition.setManualStop(true);
    }

    public void setSearchMoves(List<Move> searchMoves) {
        this.searchMoves = searchMoves;
    }

    public void setPonder(boolean ponder) {
        this.ponder = ponder;
    }

    public void setwTime(int wTime) {
        this.wTime = wTime;
    }

    public void setbTime(int bTime) {
        this.bTime = bTime;
    }

    public void setwInc(int wInc) {
        this.wInc = wInc;
    }

    public void setbInc(int bInc) {
        this.bInc = bInc;
    }

    public void setMovesToGo(int movesToGo) {
        this.movesToGo = movesToGo;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setNodes(long nodes) {
        this.nodes = nodes;
    }

    public void setMate(int mate) {
        this.mate = mate;
    }

    public void setMoveTime(long moveTime) {
        this.moveTime = moveTime;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }
}
