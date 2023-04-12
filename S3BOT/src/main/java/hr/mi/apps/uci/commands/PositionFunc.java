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
        if (arguments[0].equals("startpos")){
            environment.getBoardstate().loadStartingPosition();
        }
        else {
            try {
                if (arguments.length < 6) {
                    throw new IllegalArgumentException();
                }
                environment.getBoardstate().loadFen(String.format("%s %s %s %s %s %s", arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]));
            } catch (Exception e) {
                environment.getBoardstate().loadStartingPosition();
                return new String[] {"Received string not fen, board set to starting position"};
            }
        }

        for (int i = 6; i < arguments.length; i++) {
            try {
                environment.getBoardstate().makeMove(ChessTranslator.algebraicToMove(arguments[i], environment.getBoardstate()));
            } catch (IllegalStateException ignored){}
        }
        return new String[0];
    }
}
