package hr.mi.apps.uci;

import hr.mi.apps.uci.commands.*;
import hr.mi.apps.uci.commands.support.Commands;
import hr.mi.apps.uci.commands.support.UciTask;
import hr.mi.apps.uci.support.Environment;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UCI {
    private static final Environment environment = new Environment();
    static final Map<String, BiFunction<String[], Environment, UciTask>> commandMap = Map.ofEntries(
            Map.entry(Commands.UCI.getText(), UciFunc::new),
            Map.entry(Commands.DEBUG.getText(), DebugFunc::new),
            Map.entry(Commands.IS_READY.getText(), IsReadyFunc::new),
            Map.entry(Commands.SET_OPTION.getText(), SetOptionFunc::new),
            Map.entry(Commands.REGISTER.getText(), RegisterFunc::new),
            Map.entry(Commands.UCI_NEW_GAME.getText(), UciNewGameFunc::new),
            Map.entry(Commands.POSITION.getText(), PositionFunc::new),
            Map.entry(Commands.GO.getText(), GoFunc::new),
            Map.entry(Commands.STOP.getText(), StopFunc::new),
            Map.entry(Commands.PONDER_HIT.getText(), PonderHitFunc::new)
            );

    static BlockingQueue<UciTask> workQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();
    static private boolean isOn = true;

    public static void main(String[] args) {
        System.out.println("S3BOT by Matej Istuk");
        new Thread(new OutputWriter()).start();
        new Thread(new EngineLoop()).start();
        new Thread(new InputParser()).start();

        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            command = scanner.nextLine();
            while (true) {
                try {
                    inputQueue.put(command);
                    break;
                } catch (InterruptedException ignored) {}
            }
        } while (!command.equals("quit"));
    }

    private static void executeTask(UciTask task) {
        try {
            String[] output = task.call();
            for (String s : output) {
                while (true) {
                    try {
                        outputQueue.put(s);
                        break;
                    } catch (InterruptedException ignored){}
                }
            }
        } catch (Exception e) {
            while (true) {
                try {
                    outputQueue.put("Something went wrong while executing task.\n" + Arrays.toString(e.getStackTrace()));
                    break;
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private static void parseGUICommand(String command, String[] arguments) {
        if (command.equals("quit")){
            isOn = false;
            return;
        }
        if (!commandMap.containsKey(command)) {
            while (true) {
                try {
                    outputQueue.put(String.format("Unknown command: '%s'.", command));
                    return;
                } catch (InterruptedException ignored) {
                }
            }
        }

        UciTask task = commandMap.get(command).apply(arguments, environment);

        if (task.isLongTask()){
            while (true) {
                try {
                    workQueue.put(task);
                    break;
                } catch (InterruptedException ignored) {}
            }
        }
        else {
            executeTask(task);
        }
    }

    private static class EngineLoop implements Runnable {

        @Override
        public void run() {
            while (isOn || !workQueue.isEmpty()) {
                UciTask uciTask;
                while(true) {
                    try {
                        uciTask = workQueue.poll(5, TimeUnit.SECONDS);
                        break;
                    } catch (InterruptedException ignored) {}
                }

                if (uciTask != null){
                    executeTask(uciTask);
                }
            }
        }
    }

    private static class InputParser implements Runnable {

        @Override
        public void run() {
            while (isOn) {
                try {
                    String command = inputQueue.take();

                    String[] commandSplit = command.split("\\s+");

                    if (commandSplit.length < 1) {
                        continue;
                    }

                    parseGUICommand(commandSplit[0], Arrays.copyOfRange(commandSplit, 1, commandSplit.length));
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private static class OutputWriter implements Runnable {

        @Override
        public void run() {
            while (isOn || !outputQueue.isEmpty()) {
                try {
                    System.out.println(outputQueue.take());
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
