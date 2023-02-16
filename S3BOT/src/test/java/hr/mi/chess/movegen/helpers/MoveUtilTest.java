package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.constants.ChessConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveUtilTest {
    private void visualiseBitboard(long bitboard){
        for (int i = 7; i >= 0; i--){
            System.out.println(new StringBuilder(String.format("%08d", Integer.parseInt(Long.toBinaryString((bitboard >>> i * 8) & 0xFF))).replace('0', '.').replace('1', 'X')).reverse().toString());
        }
    }

    @Test
    void testGenerateAttackByRook1(){
        //free rook
        BoardState boardState = new BoardState("1k6/8/8/4R3/8/8/8/7K w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(1157443723186933776L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook2(){
        //rook blocked by own queen
        BoardState boardState = new BoardState("1k6/8/8/2Q1R3/8/8/8/7K w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(1157443693122162704L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook3(){
        //free rook near top
        BoardState boardState = new BoardState("8/6R1/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(4665518383679160384L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook4(){
        //free rook left edge
        BoardState boardState = new BoardState("8/8/7R/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(-9187203049947365248L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook5(){
        //free rook bottom right
        BoardState boardState = new BoardState("8/8/8/4k3/8/4K3/8/R7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(72340172838076926L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook6(){
        // rook top right attacking black rook
        BoardState boardState = new BoardState("R3r3/8/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(2162010399937986817L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook7(){
        // rook top left attacking black rook and pawn
        BoardState boardState = new BoardState("4r2R/7p/8/4k3/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(8106479329266892800L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook8(){
        // rook center attacking three sides
        BoardState boardState = new BoardState("2r5/8/8/2R2k2/8/2p1K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(289360927575506944L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook9(){
        //two free rooks
        BoardState boardState = new BoardState("8/3R4/8/5k2/2K5/6R1/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(5257750565273684040L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook10(){
        //two rooks both attacking an enemy rook
        BoardState boardState = new BoardState("8/3R2r1/8/2K2k2/6R1/8/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(610035751816022088L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByRook11(){
        //three rooks test
        BoardState boardState = new BoardState("2k5/6N1/8/3R3R/8/2K5/8/4r2R w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_ROOK));
        assertEquals(-8608480640745568136L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK));
    }

    @Test
    void testGenerateAttackByBishop1(){
        //free bishop
        BoardState boardState = new BoardState("2k5/8/8/3B4/8/2K5/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(4693335752243822976L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop2(){
        //bishop attacking rook
        BoardState boardState = new BoardState("2k5/8/3B4/8/5r2/2K5/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2455587783293599744L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop3(){
        //free bishop on right edge
        BoardState boardState = new BoardState("2k5/8/7B/8/8/2K5/4r3/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2323857683139004420L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop4(){
        //bishop on left edge attacking rook
        BoardState boardState = new BoardState("2k5/8/B7/8/8/2K5/4r3/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(288793334762704896L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }
    @Test
    void testGenerateAttackByBishop5(){
        //bishop center blocked and attacking
        BoardState boardState = new BoardState("8/3r4/6k1/5B2/8/3K4/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2339762094473216L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop6(){
        //two free bishops
        BoardState boardState = new BoardState("5k2/8/2r5/5B2/2B5/8/3K4/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(4947293295409239330L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop7(){
        //two corner bishops attacking each-other
        BoardState boardState = new BoardState("5k1B/8/2r5/8/8/8/3K4/B7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(18049651735527936L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByBishop8(){
        //three bishops
        BoardState boardState = new BoardState("5k1B/8/2r4B/8/8/8/3K4/B7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_BISHOP));
        assertEquals(2323892936365048320L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_BISHOP));
    }

    @Test
    void testGenerateAttackByQueen1(){
        //free queen
        BoardState boardState = new BoardState("5k2/8/8/4Q3/8/8/3K4/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        assertEquals(-7902628846034972143L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_QUEEN));
    }

    @Test
    void testGenerateAttackByQueen2(){
        //two queens on right edge
        BoardState boardState = new BoardState("5k2/7Q/8/8/7Q/8/3K4/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_QUEEN));
        assertEquals(-3999230689260559214L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_QUEEN));
    }

    @Test
    void testGenerateAttackByBlackQueen(){
        //two queens on right edge
        BoardState boardState = new BoardState("5k2/8/7q/8/8/7q/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_QUEEN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_QUEEN));
        assertEquals(-8878706061545060188L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.BLACK_QUEEN));
    }

    @Test
    void testGenerateAttackByWhitePawn1(){
        //free white pawn
        BoardState boardState = new BoardState("5k2/8/7q/8/8/1P3N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(83886080L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn2(){
        //blocked white pawn
        BoardState boardState = new BoardState("5k2/8/7q/8/2N5/1P3N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(16777216L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn3(){
        //free white pawn on edge
        BoardState boardState = new BoardState("5k2/8/7q/8/2N5/P4N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(33554432L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn4(){
        //white pawn attacking two targets
        BoardState boardState = new BoardState("5k2/8/3b1q2/4P3/2N5/5N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(43980465111040L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByWhitePawn5(){
        //multiple white pawns
        BoardState boardState = new BoardState("5k2/8/2Nb1q2/3P4/P5P1/3P1N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.WHITE_PAWN));
        assertEquals(18288306290688L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.WHITE_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn1(){
        //free black pawn
        BoardState boardState = new BoardState("5K2/8/7Q/8/8/1p3n2/8/k7 b - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(1280L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn2(){
        //blocked black pawn
        BoardState boardState = new BoardState("5K2/8/7Q/8/8/1p3n2/2n5/k7 b - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(256L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn3(){
        //free black pawn on edge
        BoardState boardState = new BoardState("5K2/8/7Q/8/2n5/p4n2/8/k7 b - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(512L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn4(){
        //black pawn attacking two targets
        BoardState boardState = new BoardState("5K2/8/8/4p3/2nB1Q2/5n2/8/k7 b - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(671088640L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByBlackPawn5(){
        //multiple black pawns
        BoardState boardState = new BoardState("5k2/8/5q1p/3p4/p1N1b1p1/3p1N2/8/K7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        //System.out.println(MoveUtil.generateAttackBySlidingPiece(boardState, ChessPiece.BLACK_PAWN));
        assertEquals(274955637760L, MoveUtil.typeCaptures(boardState.getBitboards(), ChessPiece.BLACK_PAWN));
    }

    @Test
    void testGenerateAttackByWhiteKnight1(){
        //free white knight
        BoardState boardState = new BoardState("8/7K/8/8/4N3/8/2k5/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(44272527353856L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight2(){
        //free white knight near left edge
        BoardState boardState = new BoardState("8/7K/8/8/6N1/8/2k5/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(175990581010432L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight3(){
        //free white knight near bottom left
        BoardState boardState = new BoardState("8/7K/8/8/8/8/1k4N1/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(2685403152L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight4(){
        //free white knight near bottom edge
        BoardState boardState = new BoardState("8/7K/8/8/8/8/4k3/3N4 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(1319424L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight5(){
        //free white knight bottom left
        BoardState boardState = new BoardState("8/7K/8/8/8/8/4k3/N7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(132096L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight6(){
        //two white knights right edge
        BoardState boardState = new BoardState("8/N6K/8/8/1N6/8/4k3/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(288235916660049152L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight7(){
        //two attacking white knights top edge
        BoardState boardState = new BoardState("2N1N3/7K/3n4/8/8/8/4k3/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(23971552508772352L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testGenerateAttackByWhiteKnight8(){
        //two white knights near corners attacking and being blocked
        BoardState boardState = new BoardState("7N/N7/2n3K1/8/8/8/4k3/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        //System.out.println(MoveUtil.generateAttackByPieceType(boardState, ChessPiece.WHITE_KNIGHT));
        assertEquals(297241982042898432L, MoveUtil.typePushes(boardState.getBitboards(), ChessPiece.WHITE_KNIGHT));
    }

    @Test
    void testKingDangerSquares1(){
        //rook not attacking king
        BoardState boardState = new BoardState("8/8/5r2/8/8/4K3/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(2315095537539358752L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares2(){
        //rook attacking king
        BoardState boardState = new BoardState("8/8/5r2/8/8/5K2/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(2315095537539358752L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares3(){
        //rook and queen attacking king
        BoardState boardState = new BoardState("8/8/5r2/8/8/5K2/8/3q4 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(2893808674997419255L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares4(){
        //queen attacking king blocked by rook
        BoardState boardState = new BoardState("8/8/8/8/8/1q1R1K2/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(4765391189988673290L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares5(){
        //knight near king
        BoardState boardState = new BoardState("8/8/8/5n2/8/4RK2/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(22667534005174272L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares6(){
        //
        BoardState boardState = new BoardState("8/8/8/8/4r1r1/5K2/8/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(5787213829982146640L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares7(){
        //two rooks covering eachother
        BoardState boardState = new BoardState("8/8/8/8/7r/6K1/7r/8 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(-9187201948304998528L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares8(){
        //rook in the corner
        BoardState boardState = new BoardState("8/8/8/8/8/8/6K1/7r w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(-9187201950435737473L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares9(){
        //queen in the corner
        BoardState boardState = new BoardState("8/8/8/8/8/8/6K1/7q w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(-9114576973767589761L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testKingDangerSquares10(){
        //queen in the corner and bishop covering each-other
        BoardState boardState = new BoardState("8/8/2b5/8/8/8/6K1/7q w - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState));
        assertEquals(-7959403660740345601L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.WHITE));
    }

    @Test
    void testBlackKingDangerSquares(){
        //queen in the corner and bishop covering each-other
        BoardState boardState = new BoardState("8/8/8/8/8/5k1R/8/8 b - - 0 1");
        //visualiseBitboard(MoveUtil.generateActivePlayerKingDangerSquares(boardState.getBitboards()));
        //System.out.println(MoveUtil.generateActivePlayerKingDangerSquares(boardState.getBitboards()));
        assertEquals(-9187201950435803008L, MoveUtil.getKingDangerSquares(boardState.getBitboards(), ChessConstants.BLACK));
    }

    @Test
    void testSinglePieceAttack1(){
        //queen in the corner and bishop covering each-other
        BoardState boardState = new BoardState("8/8/8/2R5/5k2/8/8/R7 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
        //System.out.println(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
        assertEquals(72340172838076926L, MoveUtil.piecePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
    }

    @Test
    void testSinglePieceAttack2(){
        //queen in the corner and bishop covering each-other
        BoardState boardState = new BoardState("8/8/8/R3R3/8/2k5/8/R3R3 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
        //System.out.println(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
        assertEquals(16843022L, MoveUtil.piecePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 1));
    }

    @Test
    void testSinglePieceAttack4(){
        //queen in the corner and bishop covering each-other
        BoardState boardState = new BoardState("8/8/8/p3R3/8/2k5/8/R3R3 w - - 0 1");
        //visualiseBitboard(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 0x1000000000L));
        //System.out.println(MoveUtil.generateMovesForPiece(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 0x1000000000L));
        assertEquals(1157443723186933760L, MoveUtil.piecePushes(boardState.getBitboards(), ChessPiece.WHITE_ROOK, 0x1000000000L));
    }
}