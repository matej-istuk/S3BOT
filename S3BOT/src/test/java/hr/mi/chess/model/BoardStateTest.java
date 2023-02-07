package hr.mi.chess.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardStateTest {
    @Test
    void testStartingPosition(){
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        long[] expectedBitboards = {65280L, 129L, 66L, 36L, 8L, 16L, 71776119061217280L, -9151314442816847872L, 4755801206503243776L, 2594073385365405696L, 576460752303423488L, 1152921504606846976L};

        assertArrayEquals(expectedBitboards, boardState.getBitboards());

        assertTrue(boardState.isWhiteKingSideCastling());
        assertTrue(boardState.isWhiteQueenSideCastling());
        assertTrue(boardState.isBlackKingSideCastling());
        assertTrue(boardState.isBlackQueenSideCastling());

        assertEquals(1, boardState.getFullMoves());

        assertEquals(0, boardState.getHalfMoveClock());

        assertTrue(boardState.isWhiteActive());

        assertEquals(-1, boardState.getEnPassantTarget());
    }

    @Test
    void testPosition1(){
        BoardState boardState = new BoardState("r1b1kbnr/ppp1pppp/n2q4/3p4/1P1P1B2/5N2/P1P1PPPP/RNQ1KBR1 b Q b3 0 6");
        long[] expectedBitboards = {167834880L, 65L, 2097154L, 536870944L, 4L, 16L, 69524353607270400L, -9151314442816847872L, 4611687117939015680L, 2594073385365405696L, 8796093022208L, 1152921504606846976L};

        assertArrayEquals(expectedBitboards, boardState.getBitboards());

        assertFalse(boardState.isWhiteKingSideCastling());
        assertTrue(boardState.isWhiteQueenSideCastling());
        assertFalse(boardState.isBlackKingSideCastling());
        assertFalse(boardState.isBlackQueenSideCastling());

        assertEquals(6, boardState.getFullMoves());

        assertEquals(0, boardState.getHalfMoveClock());

        assertFalse(boardState.isWhiteActive());

        assertEquals(17, boardState.getEnPassantTarget());
    }

    @Test
    void testPosition2(){
        BoardState boardState = new BoardState("rnbqkbNr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBnR w KQkq - 22 12");
        long[] expectedBitboards = {65280L, 129L, 4611686018427387906L, 36L, 8L, 16L, 71776119061217280L, -9151314442816847872L, 144115188075855936L, 2594073385365405696L, 576460752303423488L, 1152921504606846976L};

        assertArrayEquals(expectedBitboards, boardState.getBitboards());

        assertTrue(boardState.isWhiteKingSideCastling());
        assertTrue(boardState.isWhiteQueenSideCastling());
        assertTrue(boardState.isBlackKingSideCastling());
        assertTrue(boardState.isBlackQueenSideCastling());

        assertEquals(12, boardState.getFullMoves());

        assertEquals(22, boardState.getHalfMoveClock());

        assertTrue(boardState.isWhiteActive());

        assertEquals(-1, boardState.getEnPassantTarget());
    }

    @Test
    void testMove1(){
        //tests knight jump from starting position
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('N'), 6, 21, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove2(){
        //pawn single move from starting position
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('P'), 11, 19, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove3(){
        //pawn double move from starting position
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('P'), 11, 27, 1);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove4(){
        //tests white king side rook moving castling right loss
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R w KQkq d6 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKBR1 b Qkq - 1 2");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('R'), 7, 6, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove5(){
        //tests white queen side rook moving castling right loss
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/R1BQKBNR w KQkq d6 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/1RBQKBNR b Kkq - 1 2");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('R'), 0, 1, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove6(){
        //tests white king moving castling right loss
        BoardState boardState = new BoardState("rnbqkbnr/ppp2ppp/8/3pp3/3P4/4B3/PPP1PPPP/RN1QKBNR w KQkq e6 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp2ppp/8/3pp3/3P4/4B3/PPPKPPPP/RN1Q1BNR b kq - 1 3");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('K'), 4, 11, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove7(){
        //tests black king side rook moving castling right loss
        BoardState boardState = new BoardState("rnbqkb1r/pppppppp/5n2/8/3P4/5P2/PPP1P1PP/RNBQKBNR b KQkq - 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbr1/pppppppp/5n2/8/3P4/5P2/PPP1P1PP/RNBQKBNR w KQq - 1 3");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('r'), 63, 62, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove8(){
        //tests black queen side rook moving castling right loss
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/2n5/8/2P5/1P6/P2PPPPP/RNBQKBNR b KQkq - 0 2");
        BoardState expectedBoardState = new BoardState("1rbqkbnr/pppppppp/2n5/8/2P5/1P6/P2PPPPP/RNBQKBNR w KQk - 1 3");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('r'), 56, 57, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove9(){
        //tests black king moving castling right loss
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/3p4/8/4P3/3P4/PPP2PPP/RNBQKBNR b KQkq e3 0 2");
        BoardState expectedBoardState = new BoardState("rnbq1bnr/pppkpppp/3p4/8/4P3/3P4/PPP2PPP/RNBQKBNR w KQ - 1 3");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('k'), 60, 51, 0);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove10(){
        //tests white king side castling
        BoardState boardState = new BoardState("rnbqkbnr/ppp3pp/5p2/3pp3/4P3/3B1N2/PPPP1PPP/RNBQK2R w KQkq d6 0 4");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp3pp/5p2/3pp3/4P3/3B1N2/PPPP1PPP/RNBQ1RK1 b kq - 1 4");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('K'), 4, 6, 2);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove11(){
        //tests white queen side castling
        BoardState boardState = new BoardState("rnbqkbnr/p4ppp/4p3/1ppp4/3P1B2/2NQ4/PPP1PPPP/R3KBNR w KQkq b6 0 5");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/p4ppp/4p3/1ppp4/3P1B2/2NQ4/PPP1PPPP/2KR1BNR b kq - 1 5");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('K'), 4, 2, 3);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove12(){
        //tests black king side castling
        BoardState boardState = new BoardState("rnbqk2r/pppp1ppp/3b1n2/4p3/4P1P1/3P1P2/PPP4P/RNBQKBNR b KQkq g3 0 4");
        BoardState expectedBoardState = new BoardState("rnbq1rk1/pppp1ppp/3b1n2/4p3/4P1P1/3P1P2/PPP4P/RNBQKBNR w KQ - 1 5");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('k'), 60, 62, 2);

        assertEquals(expectedBoardState, boardState);
    }

    @Test
    void testMove13(){
        //tests black queen side castling
        BoardState boardState = new BoardState("r3kbnr/pbqppppp/1pn5/2p5/2P5/1P1PPPP1/P6P/RNBQKBNR b KQkq - 0 6");
        BoardState expectedBoardState = new BoardState("2kr1bnr/pbqppppp/1pn5/2p5/2P5/1P1PPPP1/P6P/RNBQKBNR w KQ - 1 7");

        boardState.move(BoardState.PIECE_INT_MAPPING.get('k'), 60, 58, 3);

        assertEquals(expectedBoardState, boardState);
    }
}