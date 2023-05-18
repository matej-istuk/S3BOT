package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * Switches the debug mode of the engine on and off. Not implemented. Not a long command.
 * @author Matej Istuk
 */
public class DebugFunc extends UciTask {

    public DebugFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[] {"Debug not implemented."};
    }
}
