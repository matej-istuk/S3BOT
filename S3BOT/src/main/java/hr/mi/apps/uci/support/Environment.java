package hr.mi.apps.uci.support;

import hr.mi.chess.models.BoardState;

public class Environment {
    private final BoardState boardstate;

    public Environment() {
        this.boardstate = new BoardState();
    }

    public BoardState getBoardstate() {
        return boardstate;
    }

}
