package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * Command for stopping calculation as soon as possible.
 * @author Matej Istuk
 */
public class StopFunc extends UciTask {

    public StopFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        environment.getSearchManager().stopSearch();
        return new String[0];
    }
}
