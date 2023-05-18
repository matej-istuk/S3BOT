package hr.mi.chess.util;


import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;

import java.util.List;

/**
 * Class offering various static positional notation methods.
 * <p>
 *     Useful terminology for understanding this "library"
 *      <ul>
 *         <li>
 *             algebraic position - square coordinates written as a letter indicating column and number indicating row
 *         </li>
 *         <li>
 *             algebraic - move notation consisting of two algebraic positions, describing to and from, and a potential
 *             character at the en to clarify promotion
 *         </li>
 *         <li>
 *             LERF - index of the square according to the
 *             <a href="https://www.chessprogramming.org/Square_Mapping_Considerations#Little-Endian_File-Rank_Mapping">LERF notation</a>.
 *         </li>
 *     </ul>
 * </p>
 * @author Matej Istuk
 */
public class ChessTranslator {

    /**
     * Turns algebraic position into a LERF index
     * @param algebraicPos algebraic position
     * @return LERF index of the square
     */
    public static int algebraicPosToLERF(String algebraicPos){
        if (!algebraicPos.matches("([a-h]|[A-H])[1-8]"))
            throw new IllegalArgumentException();

        return (algebraicPos.charAt(0) - 'a') + (algebraicPos.charAt(1) - '1') * 8;
    }

    /**
     * Turns LERF index into an algebraic position
     * @param lerf LERF index of a square
     * @return algebraic position of the square
     */
    public static String LERFToAlgebraicPos(int lerf){
        if (!(lerf <= 63 && lerf >= 0))
            throw new IllegalArgumentException();

        return String.format("%c%d",'a' + lerf%8, lerf/8 + 1);
    }

    /**
     * Transforms a move into algebraic notation.
     * @param move Move
     * @return algebraic representation of the move
     */
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

    /**
     * Transforms algebraic notation into a move, needs the boardstate because the class <code>Move</code> stores more
     * information.
     * @param algebraicNotation algebraic notation of the move
     * @param boardState boardstate the which the algebraic move belongs
     * @return Move
     */
    public static Move algebraicToMove(String algebraicNotation, BoardState boardState) throws IllegalStateException {
        if (!algebraicNotation.matches("(([a-h]|[A-H])[1-8]){2}([rnbq]|[RNBQ])?"))
            throw new IllegalArgumentException();

        return findMatchingMove(LegalMoveGenerator.generateMoves(boardState), algebraicPosToLERF(algebraicNotation.substring(0, 2)),
                algebraicPosToLERF(algebraicNotation.substring(2, 4)), getPromotion(algebraicNotation));
    }

    /**
     * Returns if the algebraic notation of the move contains a promotion specification.
     * @param algebraicNotation move in algebraic notation
     * @return 0 for rook promotion, 1 for knight promotion, 2 for bishop promotion, 3 for queen promotion, -1 for no
     * promotion
     */
    private static int getPromotion(String algebraicNotation) {
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

    /**
     * Finds a matching move from the list of given moves.
     * @param moves list of moves
     * @param from from where is the move
     * @param to to where is the move
     * @param promotion promotion info about the move
     * @return corresponding Move
     * @throws IllegalStateException if no corresponding move was found
     */
    private static Move findMatchingMove(List<Move> moves, int from, int to, int promotion) {
        for (Move move: moves) {
            if (move.getFrom() == from && move.getTo() == to && (!move.isPromotion() || promotion == (move.getFlags() & 3))){
                return move;
            }
        }
        throw new IllegalStateException();
    }
}
