package hr.mi.apps.bridges;

import hr.mi.support.FromToPair;
import hr.mi.chess.util.ChessTranslator;

import java.util.Scanner;

/**
 * User bridge for the terminal chess application.
 * @author Matej Istuk
 */
public class TerminalUserBridge implements UserBridge {

    @Override
    public FromToPair requestMoveInput() {
        Scanner scanner = new Scanner(System.in);

        FromToPair fromTo;
        do {
            System.out.printf("Please input your move:%n");
            fromTo = parseInput(scanner.nextLine().toLowerCase());
            if (fromTo == null){
                System.out.printf("Incorrect input format, please input the origin and destination squares in algebraic notation (example: a2 a4):%n");
            }
        } while (fromTo == null);

        return fromTo;
    }

    @Override
    public int requestPromotedPiece() {
        return 0;
    }

    @Override
    public void stop() {
        //the terminal app is synchronous, so it can be exited safely at any point
    }

    /**
     * Parses the input string into a <code>FromToPair</code>.
     * @param input the input string from the terminal app
     * @return FromToPair
     */
    private FromToPair parseInput(String input){
        int from;
        int to;
        String[] squares = input.split("\\s+");

        try {
            if (squares.length != 2){
                throw new IllegalArgumentException();
            }

            from = ChessTranslator.algebraicPosToLERF(squares[0]);
            to = ChessTranslator.algebraicPosToLERF(squares[1]);
        }catch (IllegalArgumentException e){
            return null;
        }

        return new FromToPair(from, to);
    }
}
