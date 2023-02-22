package hr.mi.apps.bridges;

import hr.mi.chess.player.human.FromToPair;
import hr.mi.chess.player.human.UserBridge;
import hr.mi.chess.util.ChessTranslator;

import java.util.Scanner;

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

    private FromToPair parseInput(String input){
        int from;
        int to;
        String[] squares = input.split("\\s+");

        try {
            if (squares.length != 2){
                throw new IllegalArgumentException();
            }

            from = ChessTranslator.AlgebraicToLERF(squares[0]);
            to = ChessTranslator.AlgebraicToLERF(squares[1]);
        }catch (IllegalArgumentException e){
            return null;
        }

        return new FromToPair(from, to);
    }
}
