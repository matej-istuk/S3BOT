package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * this is sent to the engine when the user wants to change the internal parameters
 * of the engine. Not implemented.
 * @author Matej Istuk
 */
public class SetOptionFunc extends UciTask {

    public SetOptionFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[0];
    }
}
