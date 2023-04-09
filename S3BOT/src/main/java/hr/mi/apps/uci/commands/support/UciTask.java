package hr.mi.apps.uci.commands.support;

import hr.mi.apps.uci.support.Environment;

import java.util.concurrent.Callable;

public abstract class UciTask implements Callable<String[]> {
    protected final String[] arguments;
    protected final Environment environment;
    private final boolean longTask;

    public UciTask(String[] arguments, Environment environment, boolean longTask) {
        this.arguments = arguments;
        this.environment = environment;
        this.longTask = longTask;
    }

    public boolean isLongTask() {
        return longTask;
    }

}
