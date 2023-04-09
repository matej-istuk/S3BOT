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
