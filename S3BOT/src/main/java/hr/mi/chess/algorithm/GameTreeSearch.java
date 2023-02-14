package hr.mi.chess.algorithm;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.List;

public class GameTreeSearch {
    public static long countMovesAtDepth(BoardState boardState, int depth){
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (depth == 1){
            return moves.size();
        }

        long result = 0;

        for (Move move: moves){
            boardState.makeMove(move);
            result += countMovesAtDepth(boardState, depth - 1);
            boardState.unmakeLastMove();
        }

        return result;
    }
}