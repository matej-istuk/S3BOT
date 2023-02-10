package hr.mi.chess.movegen;

import hr.mi.chess.models.BoardState;

public class BoardFunctions {
    public static long calculateActiveColourOccupied(BoardState boardState) {
        int offset = boardState.isWhiteActive() ? 0 : 6;
        return calculateOccupiedByIndexRange(boardState, offset, 6 + offset);
    }

    public static long calculateNotActiveColourOccupied(BoardState boardState) {
        int offset = boardState.isWhiteActive() ? 6 : 0;
        return calculateOccupiedByIndexRange(boardState, offset, 6 + offset);
    }

    public static long calculateWhiteOccupied(BoardState boardState){
        return calculateOccupiedByIndexRange(boardState, 0, 6);
    }

    public static long calculateBlackOccupied(BoardState boardState){
        return calculateOccupiedByIndexRange(boardState, 6, 12);
    }

    private static long calculateOccupiedByIndexRange(BoardState boardState, int from, int to){
        long occupied = 0L;
        for (int i = from; i < to; i++){
            occupied |= boardState.getBitboards()[i];
        }
        return occupied;
    }
}
