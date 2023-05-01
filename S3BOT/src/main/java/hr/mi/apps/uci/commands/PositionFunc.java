package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;
import hr.mi.chess.util.ChessTranslator;

public class PositionFunc extends UciTask {

    public PositionFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }


    /*
        position [fen  | startpos ]  moves  ....
     */
    @Override
    public String[] call() throws Exception {
        if (arguments.length < 1){
            return new String[0];
        }

        int argStart = 0;
        if (arguments[0].equals("startpos")){
            environment.getBoardstate().loadStartingPosition();
            argStart = 1;
        }
        else {
            try {
                if (arguments.length < 6) {
                    throw new IllegalArgumentException();
                }
                environment.getBoardstate().loadFen(String.format("%s %s %s %s %s %s", arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]));
                argStart = 6;
            } catch (Exception e) {
                environment.getBoardstate().loadStartingPosition();
                return new String[] {"Received string not fen, board set to starting position"};
            }
        }

        boolean expectMoves = false;
        for (int i = argStart; i < arguments.length; i++){
            if (arguments[i].equals("moves")){
                expectMoves = true;
                continue;
            }

            if (expectMoves){
                try {
                    environment.getBoardstate().makeMove(ChessTranslator.algebraicToMove(arguments[i], environment.getBoardstate()));
                } catch (IllegalStateException ignored){}
            }
        }
        return new String[0];
    }
}
