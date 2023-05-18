package hr.mi.apps.uci.commands;

import hr.mi.apps.tchess.ChessTerminalApp;
import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;
import hr.mi.apps.uci.support.SearchManager;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;

/**
 * Starts calculating on the current position set up with the "position" command, prints the best move found. Currently
 * doesn't receive any arguments. A long task.
 * @author Matej Istuk
 */
public class GoFunc extends UciTask {

    public GoFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        if (environment.isEngineBusy()){
            throw new IllegalStateException("Engine called when busy.");
        }

        SearchManager searchManager = environment.getSearchManager();

        searchManager.setMoveTime(10000);
        long sTime = System.currentTimeMillis();
        environment.setEngineBusy(true);
        Move bestmove = searchManager.findBestMove(environment.getBoardstate());
        environment.setEngineBusy(false);
        environment.getSearchManager().resetStop();

        System.out.println(System.currentTimeMillis() - sTime);
        if (bestmove != null)
            return new String[] {"bestmove " + bestmove.toString()};
        else
            return new String[]{"No move"};
    }
}
