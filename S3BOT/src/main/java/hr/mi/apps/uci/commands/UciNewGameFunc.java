package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * This command is sent to the engine when the next search (started with "position" and "go") will be from
 * a different game. This can be a new game the engine should play or a new game it should analyse but
 * also the next position from a testsuite with positions only.
 * @author Matej Istuk
 */
public class UciNewGameFunc extends UciTask {

    public UciNewGameFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[0];
    }
}
