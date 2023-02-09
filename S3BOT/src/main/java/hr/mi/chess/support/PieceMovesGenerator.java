package hr.mi.chess.support;

import hr.mi.chess.models.ChessPiece;

import java.util.*;

public class PieceMovesGenerator {
    public static void main(String[] args) {
        generateKnightMoves();
    }

    private static void generateKnightMoves(){
        long[] moves = new long[64];
        for (int i = 0; i < 64; i++){
            Set<Integer> legalJumpSet = new HashSet<>();
            Arrays.stream(ChessPiece.WHITE_KNIGHT.getMoveOffsets()).forEach(legalJumpSet::add);

            //bottom row
            if (i <= 7){
                legalJumpSet.remove(-6);
                legalJumpSet.remove(-10);
            }
            //bottom two row
            if (i <= 15){
                legalJumpSet.remove(-17);
                legalJumpSet.remove(-15);
            }

            //top row
            if (i >= 56){
                legalJumpSet.remove(6);
                legalJumpSet.remove(10);
            }
            //top two row
            if (i >= 48){
                legalJumpSet.remove(17);
                legalJumpSet.remove(15);
            }

            //leftmost column
            if (i % 8 == 0){
                legalJumpSet.remove(-17);
                legalJumpSet.remove(15);
            }
            //two leftmost columns
            if (i % 8 == 0 || i % 8 == 1){
                legalJumpSet.remove(6);
                legalJumpSet.remove(-10);
            }

            //rightmost column
            if (i % 8 == 7){
                legalJumpSet.remove(17);
                legalJumpSet.remove(-15);
            }
            //two rightmost columns
            if (i % 8 == 7 || i % 8 == 6){
                legalJumpSet.remove(-6);
                legalJumpSet.remove(10);
            }
            long bitmask = 0L;
            for (int offset: legalJumpSet){
                bitmask |= (1L << (offset + i));
            }
            moves[i] = bitmask;
        }
        Arrays.stream(moves).forEach(o -> System.out.printf("%dL, ", o));
    }

}
