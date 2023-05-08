package hr.mi.chess.models.support;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
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
}