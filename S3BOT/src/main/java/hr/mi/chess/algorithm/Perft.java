package hr.mi.chess.algorithm;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.List;

/**
 * Class preforming performance test, move path enumeration. Used for debugging the move generator, counts possible
 * chess board states.
 * @author Matej Istuk
 */
public class Perft {

    /**
     * Searches the game tree fully to the requested depth and returns how many states it found.
     * @param boardState the boardstate
     * @param depth depth to which to search
     * @return number of found states
     */
    public static long countMovesAtDepth(BoardState boardState, int depth){
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        if (depth == 1){
            return moves.size();
        }

        long result = 0;

        for (Move move: moves){
            boardState.makeMove(move);
            long temp = countMovesAtDepth(boardState, depth - 1);
            result += temp;
            boardState.unmakeLastMove();
        }

        return result;
    }
}
