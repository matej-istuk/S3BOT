package hr.mi.chess.util;


public class ChessTranslator {
    public static int AlgebraicToLERF (String algebraicNotation){
        if (algebraicNotation.length() != 2 || algebraicNotation.charAt(0) < 'a' || algebraicNotation.charAt(0) > 'h' || algebraicNotation.charAt(1) < '1' || algebraicNotation.charAt(1) > '8'){
            throw new IllegalArgumentException();
        }
        return (algebraicNotation.charAt(0) - 'a') + (algebraicNotation.charAt(1) - '1') * 8;
    }
}
