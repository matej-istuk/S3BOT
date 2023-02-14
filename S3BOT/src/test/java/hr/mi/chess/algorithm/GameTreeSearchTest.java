package hr.mi.chess.algorithm;

import hr.mi.chess.models.BoardState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTreeSearchTest {
    @Test
    void test1(){
        BoardState boardState = new BoardState("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b KQ - 3 2");
        assertEquals(8, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test2(){
        BoardState boardState = new BoardState("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 0 3");
        assertEquals(8, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test3(){
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 2 2");
        assertEquals(19, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test4(){
        BoardState boardState = new BoardState("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b KQkq - 3 2");
        assertEquals(5, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test5(){
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 2 2");
        assertEquals(19, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test6(){
        BoardState boardState = new BoardState("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b KQ - 3 2");
        assertEquals(44, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test7(){
        BoardState boardState = new BoardState("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w KQ - 3 9");
        assertEquals(39, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test8(){
        BoardState boardState = new BoardState("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4");
        assertEquals(9, GameTreeSearch.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test9(){
        BoardState boardState = new BoardState("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertEquals(62379, GameTreeSearch.countMovesAtDepth(boardState, 3));
    }
    @Test
    void test10(){
        BoardState boardState = new BoardState("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertEquals(89890, GameTreeSearch.countMovesAtDepth(boardState, 3));
    }
    @Test
    void test11(){
        BoardState boardState = new BoardState("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1");
        assertEquals(1134888, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test12(){
        BoardState boardState = new BoardState("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1");
        assertEquals(1015133, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test13(){
        BoardState boardState = new BoardState("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1");
        assertEquals(1440467, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test14(){
        BoardState boardState = new BoardState("5k2/8/8/8/8/8/8/4K2R w K - 0 1");
        assertEquals(661072, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test15(){
        BoardState boardState = new BoardState("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1");
        assertEquals(803711, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test16(){
        BoardState boardState = new BoardState("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1");
        assertEquals(1274206, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test17(){
        BoardState boardState = new BoardState("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1");
        assertEquals(1720476, GameTreeSearch.countMovesAtDepth(boardState, 4));
    }
    @Test
    void test18(){
        BoardState boardState = new BoardState("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1");
        assertEquals(3821001, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test19(){
        BoardState boardState = new BoardState("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1");
        assertEquals(1004658, GameTreeSearch.countMovesAtDepth(boardState, 5));
    }
    @Test
    void test20(){
        BoardState boardState = new BoardState("4k3/1P6/8/8/8/8/K7/8 w - - 0 1");
        assertEquals(217342, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test21(){
        BoardState boardState = new BoardState("8/P1k5/K7/8/8/8/8/8 w - - 0 1");
        assertEquals(92683, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test22(){
        BoardState boardState = new BoardState("K1k5/8/P7/8/8/8/8/8 w - - 0 1");
        assertEquals(2217, GameTreeSearch.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test23(){
        BoardState boardState = new BoardState("8/k1P5/8/1K6/8/8/8/8 w - - 0 1");
        assertEquals(567584, GameTreeSearch.countMovesAtDepth(boardState, 7));
    }
    @Test
    void test24(){
        BoardState boardState = new BoardState("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1");
        assertEquals(23527, GameTreeSearch.countMovesAtDepth(boardState, 4));
    }
}