package yi.shi.ssh.actions.executor;

import yi.shi.ssh.actions.AbstractAction;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ActionExecutors {
    private static Executor executor = Executors.newCachedThreadPool();

    /**
     * Executes the given action using the executor.
     *
     * @param action the action to execute
     */
    public static void execute(AbstractAction action){
        executor.execute(action);
    }

}
