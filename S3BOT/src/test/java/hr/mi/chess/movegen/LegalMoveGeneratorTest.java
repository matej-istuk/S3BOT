package hr.mi.chess.movegen;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LegalMoveGeneratorTest {
    @Test
    void testKingMoves1() {
        //lone white king
        BoardState boardState = new BoardState("8/8/8/8/8/8/3K4/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(8, moves.size());
    }

    @Test
    void testKingMoves2() {
        //white king next to a black pawn
        BoardState boardState = new BoardState("8/8/8/8/8/8/3Kp3/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(7, moves.size());
    }

    @Test
    void testKingMoves3() {
        //white king next to a black pawn guarded by a black rook
        BoardState boardState = new BoardState("8/8/8/4r3/8/8/3Kp3/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(5, moves.size());
    }

    @Test
    void testKingMoves4() {
        //white king under check by black rook
        BoardState boardState = new BoardState("8/8/8/3r4/8/8/3K4/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(6, moves.size());
    }

    @Test
    void testKingMoves5() {
        //white king under check by black queen
        BoardState boardState = new BoardState("8/8/8/8/8/8/3q4/3K4 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(1, moves.size());
    }

    @Test
    void testKingMoves6() {
        //white king under check by black queen guarded by rook
        BoardState boardState = new BoardState("8/8/8/8/8/8/3q3r/3K4 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(0, moves.size());
    }

    @Test
    void testKingMoves7() {
        //white king blocked by enemy king
        BoardState boardState = new BoardState("1k1K4/7r/8/8/8/8/8/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
        assertEquals(1, moves.size());
    }

    @Test
    void testPin1() {
        BoardState boardState = new BoardState("5K2/8/5R2/5r2/8/8/8/8 w - - 0 1");
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);
    }
}