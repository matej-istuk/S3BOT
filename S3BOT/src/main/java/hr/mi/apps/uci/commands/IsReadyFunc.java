package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * This command is used to synchronize the engine with the GUI. When the GUI has sent a command or
 * multiple commands that can take some time to complete,
 * this command can be used to wait for the engine to be ready again or
 * to ping the engine to find out if it is still alive.
 * In this chess engine, no set up is needed, so it always returns "readyok".
 * @author Matej Istuk
 */
public class IsReadyFunc extends UciTask {

    public IsReadyFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[] {"readyok"};
    }
}
