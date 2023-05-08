package hr.mi.chess.model;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.constants.ChessPieceConstants;
import hr.mi.chess.movegen.LegalMoveGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        assertTrue(boardState.getActiveColour());

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

        assertFalse(boardState.getActiveColour());

        assertEquals(-1, boardState.getEnPassantTarget());
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

        assertTrue(boardState.getActiveColour());

        assertEquals(-1, boardState.getEnPassantTarget());
    }

    @Test
    void testMove1(){
        //tests knight jump from starting position
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('N'), 6, 21, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove2(){
        //pawn single move from starting position
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 11, 19, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove3(){
        //pawn double move from starting position
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 11, 27, 1));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove4(){
        //tests white king side rook moving castling right loss
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R w KQkq d6 0 2");
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R w KQkq d6 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKBR1 b Qkq - 1 2");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('R'), 7, 6, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove5(){
        //tests white queen side rook moving castling right loss
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/R1BQKBNR w KQkq d6 0 2");
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/R1BQKBNR w KQkq d6 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/1RBQKBNR b Kkq - 1 2");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('R'), 0, 1, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove6(){
        //tests white king moving castling right loss
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp2ppp/8/3pp3/3P4/4B3/PPP1PPPP/RN1QKBNR w KQkq e6 0 3");
        BoardState boardState = new BoardState("rnbqkbnr/ppp2ppp/8/3pp3/3P4/4B3/PPP1PPPP/RN1QKBNR w KQkq e6 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp2ppp/8/3pp3/3P4/4B3/PPPKPPPP/RN1Q1BNR b kq - 1 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('K'), 4, 11, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove7(){
        //tests black king side rook moving castling right loss
        BoardState originalBoardState = new BoardState("rnbqkb1r/pppppppp/5n2/8/3P4/5P2/PPP1P1PP/RNBQKBNR b KQkq - 0 2");
        BoardState boardState = new BoardState("rnbqkb1r/pppppppp/5n2/8/3P4/5P2/PPP1P1PP/RNBQKBNR b KQkq - 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbr1/pppppppp/5n2/8/3P4/5P2/PPP1P1PP/RNBQKBNR w KQq - 1 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('r'), 63, 62, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove8(){
        //tests black queen side rook moving castling right loss
        BoardState originalBoardState = new BoardState("r1bqkbnr/pppppppp/2n5/8/2P5/1P6/P2PPPPP/RNBQKBNR b KQkq - 0 2");
        BoardState boardState = new BoardState("r1bqkbnr/pppppppp/2n5/8/2P5/1P6/P2PPPPP/RNBQKBNR b KQkq - 0 2");
        BoardState expectedBoardState = new BoardState("1rbqkbnr/pppppppp/2n5/8/2P5/1P6/P2PPPPP/RNBQKBNR w KQk - 1 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('r'), 56, 57, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove9(){
        //tests black king moving castling right loss
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp1pppp/3p4/8/4P3/3P4/PPP2PPP/RNBQKBNR b KQkq e3 0 2");
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/3p4/8/4P3/3P4/PPP2PPP/RNBQKBNR b KQkq e3 0 2");
        BoardState expectedBoardState = new BoardState("rnbq1bnr/pppkpppp/3p4/8/4P3/3P4/PPP2PPP/RNBQKBNR w KQ - 1 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('k'), 60, 51, 0));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove10(){
        //tests white king side castling
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp3pp/5p2/3pp3/4P3/3B1N2/PPPP1PPP/RNBQK2R w KQkq d6 0 4");
        BoardState boardState = new BoardState("rnbqkbnr/ppp3pp/5p2/3pp3/4P3/3B1N2/PPPP1PPP/RNBQK2R w KQkq d6 0 4");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp3pp/5p2/3pp3/4P3/3B1N2/PPPP1PPP/RNBQ1RK1 b kq - 1 4");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('K'), 4, 6, 2));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove11(){
        //tests white queen side castling
        BoardState originalBoardState = new BoardState("rnbqkbnr/p4ppp/4p3/1ppp4/3P1B2/2NQ4/PPP1PPPP/R3KBNR w KQkq b6 0 5");
        BoardState boardState = new BoardState("rnbqkbnr/p4ppp/4p3/1ppp4/3P1B2/2NQ4/PPP1PPPP/R3KBNR w KQkq b6 0 5");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/p4ppp/4p3/1ppp4/3P1B2/2NQ4/PPP1PPPP/2KR1BNR b kq - 1 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('K'), 4, 2, 3));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove12(){
        //tests black king side castling
        BoardState originalBoardState = new BoardState("rnbqk2r/pppp1ppp/3b1n2/4p3/4P1P1/3P1P2/PPP4P/RNBQKBNR b KQkq g3 0 4");
        BoardState boardState = new BoardState("rnbqk2r/pppp1ppp/3b1n2/4p3/4P1P1/3P1P2/PPP4P/RNBQKBNR b KQkq g3 0 4");
        BoardState expectedBoardState = new BoardState("rnbq1rk1/pppp1ppp/3b1n2/4p3/4P1P1/3P1P2/PPP4P/RNBQKBNR w KQ - 1 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('k'), 60, 62, 2));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove13(){
        //tests black queen side castling
        BoardState originalBoardState = new BoardState("r3kbnr/pbqppppp/1pn5/2p5/2P5/1P1PPPP1/P6P/RNBQKBNR b KQkq - 0 6");
        BoardState boardState = new BoardState("r3kbnr/pbqppppp/1pn5/2p5/2P5/1P1PPPP1/P6P/RNBQKBNR b KQkq - 0 6");
        BoardState expectedBoardState = new BoardState("2kr1bnr/pbqppppp/1pn5/2p5/2P5/1P1PPPP1/P6P/RNBQKBNR w KQ - 1 7");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('k'), 60, 58, 3));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove14(){
        //tests white pawn capture pawn
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2");
        BoardState boardState = new BoardState("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/4P3/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 2");

        Move move = new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 27, 36, 4);
        move.setCapturedPieceIndex(6);
        boardState.makeMove(move);
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove15(){
        //tests white knight capture queen
        BoardState originalBoardState = new BoardState("rnb1kbnr/ppp2ppp/5q2/3p4/6N1/8/PPPPPPPP/RNBQKB1R w KQkq d6 0 4");
        BoardState boardState = new BoardState("rnb1kbnr/ppp2ppp/5q2/3p4/6N1/8/PPPPPPPP/RNBQKB1R w KQkq d6 0 4");
        BoardState expectedBoardState = new BoardState("rnb1kbnr/ppp2ppp/5N2/3p4/8/8/PPPPPPPP/RNBQKB1R b KQkq - 0 4");

        Move move = new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('N'), 30, 45, 4);
        move.setCapturedPieceIndex(10);
        boardState.makeMove(move);
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove16(){
        //tests black pawn capture pawn
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/4p3/3P4/5P2/PPP1P1PP/RNBQKBNR b KQkq - 0 2");
        BoardState boardState = new BoardState("rnbqkbnr/pppp1ppp/8/4p3/3P4/5P2/PPP1P1PP/RNBQKBNR b KQkq - 0 2");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/8/3p4/5P2/PPP1P1PP/RNBQKBNR w KQkq - 0 3");


        Move move = new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 36, 27, 4);
        move.setCapturedPieceIndex(0);
        boardState.makeMove(move);
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    /*
    @Test
    void testMove17(){
        //tests black bishop capture bishop
        BoardState originalBoardState = new BoardState("rnbqk1nr/ppppbppp/8/4p1B1/3P4/5N2/PPP1PPPP/RN1QKB1R b KQkq - 3 3");
        BoardState boardState = new BoardState("rnbqk1nr/ppppbppp/8/4p1B1/3P4/5N2/PPP1PPPP/RN1QKB1R b KQkq - 3 3");
        BoardState expectedBoardState = new BoardState("rnbqk1nr/pppp1ppp/8/4p1b1/3P4/5N2/PPP1PPPP/RN1QKB1R w KQkq - 0 4");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 52, 38, 4));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove19(){
        //ep white capture black from left
        BoardState originalBoardState = new BoardState("rnbqkb1r/pppp1ppp/7n/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3");
        BoardState boardState = new BoardState("rnbqkb1r/pppp1ppp/7n/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkb1r/pppp1ppp/4P2n/8/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 35, 44, 5));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove20(){
        //ep black capture white from right
        BoardState originalBoardState = new BoardState("rnbqkb1r/pppp1ppp/7n/4pP2/8/8/PPPPP1PP/RNBQKBNR w KQkq e6 0 3");
        BoardState boardState = new BoardState("rnbqkb1r/pppp1ppp/7n/4pP2/8/8/PPPPP1PP/RNBQKBNR w KQkq e6 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkb1r/pppp1ppp/4P2n/8/8/8/PPPPP1PP/RNBQKBNR b KQkq - 0 3");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 37, 44, 5));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove21(){
        //ep black capture white from left
        BoardState originalBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 3");
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/ppp1pppp/8/8/8/4p3/PPPP1PPP/RNBQKBNR w KQkq - 0 4");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 27, 20, 5));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove22(){
        //ep white capture black from right
        BoardState originalBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 3");
        BoardState boardState = new BoardState("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 3");
        BoardState expectedBoardState = new BoardState("rnbqkbnr/pppp1ppp/8/8/8/3p4/PPP1PPPP/RNBQKBNR w KQkq - 0 4");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 28, 19, 5));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

     */

    @Test
    void testMove23(){
        //white rook promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkbRr/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQkq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 62, 8));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove24(){
        //white knight promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkbNr/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQkq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 62, 9));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove25(){
        //white bishop promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkbBr/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQkq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 62, 10));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    @Test
    void testMove26(){
        //white queen promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkbQr/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQkq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 62, 11));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);

    }

    /*
    @Test
    void testMove27(){
        //white capture rook promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkb1R/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 63, 12));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove28(){
        //white capture knight promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkb1N/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 63, 13));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove29(){
        //white capture bishop promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkb1B/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 63, 14));
        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

    @Test
    void testMove30(){
        //white capture queen promotion
        BoardState originalBoardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState boardState = new BoardState("rnbqkb1r/ppppp1Pp/8/7n/8/8/PPPPPP1P/RNBQKBNR w KQkq - 1 5");
        BoardState expectedBoardState = new BoardState("rnbqkb1Q/ppppp2p/8/7n/8/8/PPPPPP1P/RNBQKBNR b KQq - 0 5");

        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 54, 63, 15));

        assertEquals(expectedBoardState, boardState);

        boardState.unmakeLastMove();
        assertEquals(originalBoardState, boardState);
    }

     */

    @Test
    void testZobrist1(){
        //two pawn double pushes
        BoardState boardState = new BoardState();
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 12, 28, 1));
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 52, 36, 1));
        boardState.unmakeLastMove();
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist2(){
        //two pawn by one
        BoardState boardState = new BoardState();
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 12, 20, 0));
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 52, 44, 0));
        boardState.unmakeLastMove();
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist3(){
        //test capture
        BoardState boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 28, 35, 4));
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('q'), 59, 35, 4));
        boardState.unmakeLastMove();
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist4(){
        //test castle
        BoardState boardState = new BoardState("rn1qkb1r/ppp1p2p/5np1/3p4/2P1PB2/2NP4/PP2QPPP/R3K2R w KQkq - 0 2");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('K'), 4, 6, 2));
        assertEquals((new BoardState("rn1qkb1r/ppp1p2p/5np1/3p4/2P1PB2/2NP4/PP2QPPP/R4RK1 b kq - 1 2")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('r'), 63, 62, 0));
        assertEquals((new BoardState("rn1qkbr1/ppp1p2p/5np1/3p4/2P1PB2/2NP4/PP2QPPP/R4RK1 w q - 2 3")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkb1r/ppp1p2p/5np1/3p4/2P1PB2/2NP4/PP2QPPP/R4RK1 b kq - 1 2")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkb1r/ppp1p2p/5np1/3p4/2P1PB2/2NP4/PP2QPPP/R3K2R w KQkq - 0 2")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist5(){
        //test castle
        BoardState boardState = new BoardState("rn1qkb1r/ppp1p2p/6p1/3p2P1/2P1nB2/2NP4/PP2QP1P/R3K2R b KQkq - 0 3");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('p'), 55, 39, 1));
        assertEquals((new BoardState("rn1qkb1r/ppp1p3/6p1/3p2Pp/2P1nB2/2NP4/PP2QP1P/R3K2R w KQkq h6 0 4")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 38, 47, 5));
        assertEquals((new BoardState("rn1qkb1r/ppp1p3/6pP/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQkq - 0 4")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkb1r/ppp1p3/6p1/3p2Pp/2P1nB2/2NP4/PP2QP1P/R3K2R w KQkq h6 0 4")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkb1r/ppp1p2p/6p1/3p2P1/2P1nB2/2NP4/PP2QP1P/R3K2R b KQkq - 0 3")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist6(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 63, 8));
        assertEquals((new BoardState("rn1qk1rR/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist7(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 63, 9));
        assertEquals((new BoardState("rn1qk1rN/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist8(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 63, 10));
        assertEquals((new BoardState("rn1qk1rB/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist9(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 63, 11));
        assertEquals((new BoardState("rn1qk1rQ/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist10(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 62, 12));
        assertEquals((new BoardState("rn1qk1R1/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist11(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 62, 13));
        assertEquals((new BoardState("rn1qk1N1/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist12(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 62, 14));
        assertEquals((new BoardState("rn1qk1B1/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobrist13(){
        //test promotion
        BoardState boardState = new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5");
        long zHash0 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('b'), 61, 54, 0));
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        long zHash1 = boardState.getZobristHash();
        boardState.makeMove(new Move(ChessPieceConstants.PIECE_INT_MAPPING.get('P'), 55, 62, 15));
        assertEquals((new BoardState("rn1qk1Q1/ppp1p1b1/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 6")).getZobristHash(), boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qk1r1/ppp1p1bP/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R w KQq - 1 6")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash1, boardState.getZobristHash());
        boardState.unmakeLastMove();
        assertEquals((new BoardState("rn1qkbr1/ppp1p2P/6p1/3p4/2P1nB2/2NP4/PP2QP1P/R3K2R b KQq - 0 5")).getZobristHash(), boardState.getZobristHash());
        assertEquals(zHash0, boardState.getZobristHash());
    }

    @Test
    void testZobristPolyglot1() {
        BoardState boardState = new BoardState();
        assertEquals(0x463b96181691fc9cL, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        assertEquals(0x823c9b50fd114196L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2");
        assertEquals(0x0756b94461c50fb0L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3");
        assertEquals(0x22a48b5a8e47ff78L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2");
        assertEquals(0x662fafb965db29d4L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR b kq - 0 3");
        assertEquals(0x652a607ca3f242c1L, boardState.getZobristHash());
        boardState = new BoardState("rnbq1bnr/ppp1pkpp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR w - - 0 4");
        assertEquals(0x00fdd303c946bdd9L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/p1pppppp/8/8/PpP4P/8/1P1PPPP1/RNBQKBNR b KQkq c3 0 3");
        assertEquals(0x3c8123ea7b067637L, boardState.getZobristHash());
        boardState = new BoardState("rnbqkbnr/p1pppppp/8/8/P6P/R1p5/1P1PPPP1/1NBQKBNR b Kkq - 0 4");
        assertEquals(0x5c3f9b829b279560L, boardState.getZobristHash());
    }

    @Test
    void testZobristPolyglot2() {
        BoardState boardState = new BoardState();
        assertEquals(0x463b96181691fc9cL, boardState.getZobristHash());
        boardState.makeMove("e2e4");
        assertEquals(0x823c9b50fd114196L, boardState.getZobristHash());
        boardState.makeMove("d7d5");
        assertEquals(0x0756b94461c50fb0L, boardState.getZobristHash());
        boardState.makeMove("e4e5");
        assertEquals(0x662fafb965db29d4L, boardState.getZobristHash());
        boardState.makeMove("f7f5");
        assertEquals(0x22a48b5a8e47ff78L, boardState.getZobristHash());
        boardState.makeMove("e1e2");
        assertEquals(0x652a607ca3f242c1L, boardState.getZobristHash());
        boardState.makeMove("e8f7");
        assertEquals(0x00fdd303c946bdd9L, boardState.getZobristHash());
        boardState = new BoardState();
        boardState.makeMoves("a2a4", "b7b5", "h2h4", "b5b4", "c2c4");
        assertEquals(0x3c8123ea7b067637L, boardState.getZobristHash());
        boardState.makeMoves("b4c3", "a1a3");
        assertEquals(0x5c3f9b829b279560L, boardState.getZobristHash());
    }
}