package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * this is the command to try to register an engine or to tell the engine that registration
 * will be done later. This command should always be sent if the engine	has sent "registration error"
 * @author Matej Istuk
 */
public class RegisterFunc extends UciTask {

    public RegisterFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[] {"Registration not supported."};
    }
}
