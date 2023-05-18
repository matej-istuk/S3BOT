package hr.mi.apps.uci.commands.support;

import hr.mi.apps.uci.support.Environment;

import java.util.concurrent.Callable;

/**
 * Model of an abstract UCI command. <a href="https://www.wbec-ridderkerk.nl/html/UCIProtocol.html">UCI protocol</a>
 * @author Matej Istuk
 */
public abstract class UciTask implements Callable<String[]> {
    protected final String[] arguments;
    protected final Environment environment;
    private final boolean longTask;

    /**
     * The Constructor.
     * @param arguments the command arguments
     * @param environment the environment variable
     * @param longTask if the task takes a long time to execute (is not pretty much instant)
     */
    public UciTask(String[] arguments, Environment environment, boolean longTask) {
        this.arguments = arguments;
        this.environment = environment;
        this.longTask = longTask;
    }

    /**
     * @return true if the task is not long
     */
    public boolean isLongTask() {
        return longTask;
    }

}
