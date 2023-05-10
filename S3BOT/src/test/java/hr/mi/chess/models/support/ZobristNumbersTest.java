package hr.mi.chess.models.support;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZobristNumbersTest {
    @Test
    void test(){
        Set<Long> set = new HashSet<>();
        int counter = 0;
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 64; j++){
                set.add(ZobristNumbers.getPieceOnTile(i, j));
                counter++;
            }
        }

        for (int i = 0; i < 4; i++){
            set.add(ZobristNumbers.getCastling(i));
            counter++;
        }

        for (int i = 0; i < 8; i++){
            set.add(ZobristNumbers.getEnPassant(i));
            counter++;
        }

        set.add(ZobristNumbers.getWhiteActive());
        counter++;

        assertEquals(counter, set.size());
    }

    @Test
    void test2() {
        Results results = new Results();
        collisionRec(new BoardState(), 7, new HashMap<>(), results);

        System.out.printf("States visited: %d%n", results.statesVisited);
        System.out.printf("Hits: %d%n", results.hits);
        System.out.printf("Misses: %d%n", results.misses);
        System.out.printf("True Positives: %d%n", results.truePositives);
        System.out.printf("False Positives: %d%n", results.falsePositives);
        System.out.printf("Hit frequency: %f%n", ((double)results.hits)/results.statesVisited);
        System.out.printf("Collision frequency: %f%n", ((double)results.falsePositives)/results.hits);
    }

    private void collisionRec(BoardState boardState, int depth, Map<Long, String> positionMap, Results results) {
        if (depth == 0){
            return;
        }

        results.statesVisited++;
        String cFEN = cutOffFEN(boardState.getFEN());

        if (positionMap.containsKey(boardState.getZobristHash())){
            results.hits++;
            if (positionMap.get(boardState.getZobristHash()).equals(cFEN)){
                results.truePositives++;

            }
            else {
                results.falsePositives++;
                System.out.printf("saved: %s    new: %s%n", positionMap.get(boardState.getZobristHash()), cFEN);
            }
        } else {
            positionMap.put(boardState.getZobristHash(), cFEN);
            results.misses++;
        }

        for (Move move: LegalMoveGenerator.generateMoves(boardState)){
            boardState.makeMove(move);
            collisionRec(boardState, depth - 1, positionMap, results);
            boardState.unmakeLastMove();
        }
    }

    private String cutOffFEN(String fen){
        int index = 0;
        int counter = 0;

        for (int i = fen.length() - 1; i >= 0; i--){
            if (fen.charAt(i) == ' '){
                counter++;
            }

            if (counter == 2){
                index = i;
            }
        }
        return fen.substring(0, index);
    }

    private static class Results {
        long statesVisited = 0L;
        long hits = 0L;
        long misses = 0L;
        long truePositives = 0L;
        long falsePositives = 0L;
    }
}