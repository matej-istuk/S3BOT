package hr.mi.chess.player;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;

public interface Player {
    Move requestMove(BoardState boardState);
    void stop();
}
