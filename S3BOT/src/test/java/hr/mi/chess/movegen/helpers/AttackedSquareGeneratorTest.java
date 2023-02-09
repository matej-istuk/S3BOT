package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.ChessConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackedSquareGeneratorTest {
    private void visualiseBitboard(long bitboard){
        for (int i = 7; i >= 0; i--){
            System.out.println(new StringBuilder(String.format("%08d", Integer.parseInt(Long.toBinaryString((bitboard >>> i * 8) & 0xFF))).replace('0', '.').replace('1', 'X')).reverse().toString());
        }
    }
    @Test
    void testGenerateAttackByRook1(){
        //free rook
        BoardState boardState = new BoardState("1k6/8/8/4R3/8/8/8/7K w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(1157443723186933776L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook2(){
        //rook blocked by own queen
        BoardState boardState = new BoardState("1k6/8/8/2Q1R3/8/8/8/7K w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(1157443693122162704L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook3(){
        //free rook near top
        BoardState boardState = new BoardState("8/6R1/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(4665518383679160384L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook4(){
        //free rook left edge
        BoardState boardState = new BoardState("8/8/7R/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(-9187203049947365248L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook5(){
        //free rook bottom right
        BoardState boardState = new BoardState("8/8/8/4k3/8/4K3/8/R7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(72340172838076926L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook6(){
        // rook top right attacking black rook
        BoardState boardState = new BoardState("R3r3/8/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(2162010399937986817L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook7(){
        // rook top left attacking black rook and pawn
        BoardState boardState = new BoardState("4r2R/7p/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(8106479329266892800L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook8(){
        // rook center attacking three sides
        BoardState boardState = new BoardState("2r5/8/8/2R2k2/8/2p1K3/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(289360927575506944L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook9(){
        //two free rooks
        BoardState boardState = new BoardState("8/3R4/8/5k2/2K5/6R1/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(5257750565273684040L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook10(){
        //two rooks both attacking an enemy rook
        BoardState boardState = new BoardState("8/3R2r1/8/2K2k2/6R1/8/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(610035751816022088L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook11(){
        //three rooks test
        BoardState boardState = new BoardState("2k5/6N1/8/3R3R/8/2K5/8/4r2R w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(-8608480640745568136L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByBishop1(){
        //free bishop
        BoardState boardState = new BoardState("2k5/8/8/3B4/8/2K5/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(4693335752243822976L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop2(){
        //bishop attacking rook
        BoardState boardState = new BoardState("2k5/8/3B4/8/5r2/2K5/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2455587783293599744L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop3(){
        //free bishop on right edge
        BoardState boardState = new BoardState("2k5/8/7B/8/8/2K5/4r3/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2323857683139004420L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop4(){
        //bishop on left edge attacking rook
        BoardState boardState = new BoardState("2k5/8/B7/8/8/2K5/4r3/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(288793334762704896L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }
    @Test
    void testGenerateAttackByBishop5(){
        //bishop center blocked and attacking
        BoardState boardState = new BoardState("8/3r4/6k1/5B2/8/3K4/8/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2339762094473216L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop6(){
        //two free bishops
        BoardState boardState = new BoardState("5k2/8/2r5/5B2/2B5/8/3K4/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(4947293295409239330L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop7(){
        //two corner bishops attacking each-other
        BoardState boardState = new BoardState("5k1B/8/2r5/8/8/8/3K4/B7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(18049651735527936L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop8(){
        //three bishops
        BoardState boardState = new BoardState("5k1B/8/2r4B/8/8/8/3K4/B7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2323892936365048320L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByQueen1(){
        //free queen
        BoardState boardState = new BoardState("5k2/8/8/4Q3/8/8/3K4/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        assertEquals(-7902628846034972143L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
    }

    @Test
    void testGenerateAttackByQueen2(){
        //two queens on right edge
        BoardState boardState = new BoardState("5k2/7Q/8/8/7Q/8/3K4/8 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        assertEquals(-3999230689260559214L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
    }

    @Test
    void testGenerateAttackByBlackQueen(){
        //two queens on right edge
        BoardState boardState = new BoardState("5k2/8/7q/8/8/7q/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_QUEEN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_QUEEN));
        assertEquals(-8878706061545060188L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_QUEEN));
    }

    @Test
    void testGenerateAttackByWhitePawn1(){
        //free white pawn
        BoardState boardState = new BoardState("5k2/8/7q/8/8/1P3N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(83886080L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn2(){
        //blocked white pawn
        BoardState boardState = new BoardState("5k2/8/7q/8/2N5/1P3N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(16777216L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn3(){
        //free white pawn on edge
        BoardState boardState = new BoardState("5k2/8/7q/8/2N5/P4N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(33554432L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn4(){
        //white pawn attacking two targets
        BoardState boardState = new BoardState("5k2/8/3b1q2/4P3/2N5/5N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(43980465111040L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn5(){
        //multiple white pawns
        BoardState boardState = new BoardState("5k2/8/2Nb1q2/3P4/P5P1/3P1N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(18288306290688L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn1(){
        //free black pawn
        BoardState boardState = new BoardState("5K2/8/7Q/8/8/1p3n2/8/k7 b - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(1280L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn2(){
        //blocked black pawn
        BoardState boardState = new BoardState("5K2/8/7Q/8/8/1p3n2/2n5/k7 b - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(256L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn3(){
        //free black pawn on edge
        BoardState boardState = new BoardState("5K2/8/7Q/8/2n5/p4n2/8/k7 b - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(512L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn4(){
        //black pawn attacking two targets
        BoardState boardState = new BoardState("5K2/8/8/4p3/2nB1Q2/5n2/8/k7 b - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(671088640L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn5(){
        //multiple black pawns
        BoardState boardState = new BoardState("5k2/8/5q1p/3p4/p1N1b1p1/3p1N2/8/K7 w - - 0 1");
        //visualiseBitboard(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(274955637760L, AttackedSquareGenerator.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
    }
}