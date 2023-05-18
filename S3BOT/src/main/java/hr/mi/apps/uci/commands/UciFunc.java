package hr.mi.apps.uci.commands;

import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

/**
 * tell engine to use the uci (universal chess interface),
 * this will be send once as a first command after program boot
 * to tell the engine to switch to uci mode.
 * After receiving the uci command the engine must identify itself with the "id" command
 * and sent the "option" commands to tell the GUI which engine settings the engine supports if any.
 * After that the engine should sent "uciok" to acknowledge the uci mode.
 * If no uciok is sent within a certain time period, the engine task will be killed by the GUI.
 * @author Matej Istuk
 */
public class UciFunc extends UciTask {

    public UciFunc(String[] arguments, Environment environment) {
        super(arguments, environment, false);
    }

    @Override
    public String[] call() throws Exception {
        return new String[] {"id name S3BOT 0.1", "id author Matej Istuk"};
    }
}
