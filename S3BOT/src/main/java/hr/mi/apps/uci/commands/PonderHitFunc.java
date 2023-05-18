package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * Command sent if the user has played the expected move. This will be sent if the engine was told to ponder on the
 * same move the user has played. The engine should continue searching but switch from pondering to normal search. Not
 * implemented yet.
 * @author Matej Istuk
 */
public class PonderHitFunc extends UciTask {

    public PonderHitFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[] {"Ponder hit not implemented yet"};
    }
}
