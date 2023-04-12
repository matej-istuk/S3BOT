package hr.mi.chess.util;


import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.List;

public class ChessTranslator {
    public static int algebraicPosToLERF(String algebraicPos){
        if (!algebraicPos.matches("([a-h]|[A-H])[1-8]"))
            throw new IllegalArgumentException();

        return (algebraicPos.charAt(0) - 'a') + (algebraicPos.charAt(1) - '1') * 8;
    }

    public static String LERFToAlgebraicPos(int lerf){
        if (!(lerf <= 63 && lerf >= 0))
            throw new IllegalArgumentException();

        return String.format("%c%d",'a' + lerf%8, lerf/8 + 1);
    }

    public static String moveToAlgebraic(Move move){
        String promoPiece = "";
        if (move.isPromotion()) {
            promoPiece = switch (move.getFlags() & 3) {
                case 0 -> "r";
                case 1 -> "n";
                case 2 -> "b";
                case 3 -> "q";
                default -> throw new IllegalStateException("Unexpected value: " + (move.getFlags() & 3));
            };
        }

        return String.format("%s%s%s", LERFToAlgebraicPos(move.getFrom()), LERFToAlgebraicPos(move.getTo()), promoPiece);
    }


    public static Move algebraicToMove(String algebraicNotation, BoardState boardState) throws IllegalStateException {
        if (!algebraicNotation.matches("(([a-h]|[A-H])[1-8]){2}([rnbq]|[RNBQ])?"))
            throw new IllegalArgumentException();

        return findMatchingMove(LegalMoveGenerator.generateMoves(boardState), algebraicPosToLERF(algebraicNotation.substring(0, 2)),
                algebraicPosToLERF(algebraicNotation.substring(2, 4)), getPromotionOrNegOne(algebraicNotation));
    }

    private static int getPromotionOrNegOne (String algebraicNotation) {
        if (algebraicNotation.length() != 5){
            return -1;
        }

        return switch (algebraicNotation.toLowerCase().charAt(4)) {
            case 'r' -> 0;
            case 'n' -> 1;
            case 'b' -> 2;
            case 'q' -> 3;
            default -> throw new IllegalStateException();
        };
    }

    private static Move findMatchingMove(List<Move> moves, int from, int to, int promotion) {
        for (Move move: moves) {
            if (move.getFrom() == from && move.getTo() == to && (!move.isPromotion() || promotion == (move.getFlags() & 3))){
                return move;
            }
        }
        throw new IllegalStateException();
    }
}
