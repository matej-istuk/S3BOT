package hr.mi.apps.uci.support;

import hr.mi.chess.models.BoardState;

/**
 * Environment variable for the UCI terminal app, contains the board-state and the search manager.
 * @author Matej Istuk
 */
public class Environment {
    private final BoardState boardstate;
    private volatile boolean engineBusy = false;
    private final SearchManager searchManager;

    /**
     * The Constructor.
     */
    public Environment() {
        this.boardstate = new BoardState();
        this.searchManager = new SearchManager();
    }

    public BoardState getBoardstate() {
        return boardstate;
    }

    public boolean isEngineBusy() {
        return engineBusy;
    }

    public void setEngineBusy(boolean engineBusy) {
        this.engineBusy = engineBusy;
    }

    public SearchManager getSearchManager() {
        return searchManager;
    }
}
