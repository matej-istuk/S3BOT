package hr.mi.chess.movegen.helpers.interfaces;

import hr.mi.chess.models.BoardState;

@FunctionalInterface
public interface BlockerCalcFunc {
    long calculateBlockers(BoardState boardState);
}
