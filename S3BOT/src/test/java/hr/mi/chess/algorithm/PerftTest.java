package hr.mi.chess.algorithm;

import hr.mi.chess.models.BoardState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerftTest {
    @Test
    void test1(){
        BoardState boardState = new BoardState("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b KQ - 3 2");
        assertEquals(8, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test2(){
        BoardState boardState = new BoardState("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 0 3");
        assertEquals(8, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test3(){
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 2 2");
        assertEquals(19, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test4(){
        BoardState boardState = new BoardState("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b KQkq - 3 2");
        assertEquals(5, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test5(){
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 2 2");
        assertEquals(19, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test6(){
        BoardState boardState = new BoardState("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b KQ - 3 2");
        assertEquals(44, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test7(){
        BoardState boardState = new BoardState("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w KQ - 3 9");
        assertEquals(39, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test8(){
        BoardState boardState = new BoardState("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4");
        assertEquals(9, Perft.countMovesAtDepth(boardState, 1));
    }
    @Test
    void test9(){
        BoardState boardState = new BoardState("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertEquals(62379, Perft.countMovesAtDepth(boardState, 3));
    }
    @Test
    void test10(){
        BoardState boardState = new BoardState("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertEquals(89890, Perft.countMovesAtDepth(boardState, 3));
    }
    @Test
    void test11(){
        BoardState boardState = new BoardState("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1");
        assertEquals(1134888, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test12(){
        BoardState boardState = new BoardState("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1");
        assertEquals(1015133, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test13(){
        BoardState boardState = new BoardState("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1");
        assertEquals(1440467, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test14(){
        BoardState boardState = new BoardState("5k2/8/8/8/8/8/8/4K2R w K - 0 1");
        assertEquals(661072, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test15(){
        BoardState boardState = new BoardState("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1");
        assertEquals(803711, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test16(){
        BoardState boardState = new BoardState("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1");
        assertEquals(1274206, Perft.countMovesAtDepth(boardState, 4));
    }
    @Test
    void test17(){
        BoardState boardState = new BoardState("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1");
        assertEquals(1720476, Perft.countMovesAtDepth(boardState, 4));
    }
    @Test
    void test18(){
        BoardState boardState = new BoardState("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1");
        assertEquals(3821001, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test19(){
        BoardState boardState = new BoardState("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1");
        assertEquals(1004658, Perft.countMovesAtDepth(boardState, 5));
    }
    @Test
    void test20(){
        BoardState boardState = new BoardState("4k3/1P6/8/8/8/8/K7/8 w - - 0 1");
        assertEquals(217342, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test21(){
        BoardState boardState = new BoardState("8/P1k5/K7/8/8/8/8/8 w - - 0 1");
        assertEquals(92683, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test22(){
        BoardState boardState = new BoardState("K1k5/8/P7/8/8/8/8/8 w - - 0 1");
        assertEquals(2217, Perft.countMovesAtDepth(boardState, 6));
    }
    @Test
    void test23(){
        BoardState boardState = new BoardState("8/k1P5/8/1K6/8/8/8/8 w - - 0 1");
        assertEquals(567584, Perft.countMovesAtDepth(boardState, 7));
    }
    @Test
    void test24(){
        BoardState boardState = new BoardState("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1");
        assertEquals(23527, Perft.countMovesAtDepth(boardState, 4));
    }

    @Test
    void test25(){
        BoardState boardState = new BoardState("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1");
        assertEquals(10276, Perft.countMovesAtDepth(boardState, 4));
    }

    @Test
    void test26(){
        BoardState boardState = new BoardState("8/8/4k3/8/2p5/8/3P2K1/1B6 b - - 0 1");
        assertEquals(1105, Perft.countMovesAtDepth(boardState, 3));
    }

    @Test
    void test27(){
        BoardState boardState = new BoardState("8/8/4k3/8/8/2p5/3P2K1/1B6 w - - 0 1");
        assertEquals(150, Perft.countMovesAtDepth(boardState, 2));
    }

    @Test
    void test28(){
        BoardState boardState = new BoardState("8/8/4k3/8/3P4/2p5/6K1/1B6 b - d3 0 1");
        assertEquals(7, Perft.countMovesAtDepth(boardState, 1));
    }

    @Test
    void test29(){
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(4865609, Perft.countMovesAtDepth(boardState, 5));
    }

    @Test
    void test30(){
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(119060324, Perft.countMovesAtDepth(boardState, 6));
    }

    //@Test
    void test31(){
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(3195901860L, Perft.countMovesAtDepth(boardState, 7));
    }

    @Test
    void test32(){
        BoardState boardState = new BoardState("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertEquals(193690690L, Perft.countMovesAtDepth(boardState, 5));
    }

    //@Test
    void test33(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertEquals(178633661L, Perft.countMovesAtDepth(boardState, 7));
    }

    @Test
    void test34(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertEquals(11030083L, Perft.countMovesAtDepth(boardState, 6));
    }

    @Test
    void test35(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertEquals(674624, Perft.countMovesAtDepth(boardState, 5));
    }
    @Test
    void test36(){
        BoardState boardState = new BoardState("8/8/3p4/KPp4r/1R3p1k/8/4P1P1/8 w - c6 0 2");
        assertEquals(16, Perft.countMovesAtDepth(boardState, 1));
    }

    @Test
    void test37(){
        BoardState boardState = new BoardState("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertEquals(706045033L, Perft.countMovesAtDepth(boardState, 6));
    }

    @Test
    void test38(){
        BoardState boardState = new BoardState("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertEquals(89941194, Perft.countMovesAtDepth(boardState, 5));
    }

    @Test
    void test39(){
        BoardState boardState = new BoardState("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertEquals(6923051137L, Perft.countMovesAtDepth(boardState, 6));
    }

    @Test
    void test40(){
        BoardState boardState = new BoardState("1N6/6k1/8/8/7B/8/8/4K3 w - - 19 103");
        assertEquals(14, Perft.countMovesAtDepth(boardState, 1));
    }

    @Test
    void test41(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP5r/5p1k/8/4P1P1/1R6 b - - 1 1");
        assertEquals(1160678, Perft.countMovesAtDepth(boardState, 5));
    }

    @Test
    void test42(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP4kr/5p2/8/4P1P1/1R6 w - - 2 2");
        assertEquals(104371, Perft.countMovesAtDepth(boardState, 4));
    }

    @Test
    void test43(){
        BoardState boardState = new BoardState("8/2p5/3p4/KP4kr/5p2/8/4P1P1/6R1 b - - 3 2");
        assertEquals(5007, Perft.countMovesAtDepth(boardState, 3));
    }

    @Test
    void test44(){
        BoardState boardState = new BoardState("8/2p5/3p2k1/KP5r/5p2/8/4P1P1/6R1 w - - 4 3");
        assertEquals(338, Perft.countMovesAtDepth(boardState, 2));
    }

    @Test
    void test45(){
        BoardState boardState = new BoardState("8/2p5/3p2k1/KP5r/5pP1/8/4P3/6R1 b - g3 0 3");
        assertEquals(24, Perft.countMovesAtDepth(boardState, 1));
    }

    //@Test
    void test46(){
        BoardState boardState = new BoardState();
        assertEquals(84998978956L, Perft.countMovesAtDepth(boardState, 8));
    }
}