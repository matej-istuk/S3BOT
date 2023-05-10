package hr.mi.apps.uci.commands;

import hr.mi.apps.tchess.ChessTerminalApp;
import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;
import hr.mi.apps.uci.support.SearchManager;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;

public class GoFunc extends UciTask {

    public GoFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        SearchManager searchManager = new SearchManager();

        searchManager.setMoveTime(10000);
        long sTime = System.currentTimeMillis();
        Move bestmove = searchManager.findBestMove(environment.getBoardstate());

        System.out.println(System.currentTimeMillis() - sTime);
        if (bestmove != null)
            return new String[] {"bestmove " + bestmove.toString()};
        else
            return new String[]{"No move"};
    }
}
