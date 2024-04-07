package yi.shi.ssh.k8s.actions;

import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.log.Log;
import yi.shi.ssh.shell.SshContext;
import yi.shi.ssh.utils.Timer;

public class CompleteAction extends AbstractAction {
    public CompleteAction(SshContext sshContext, AbstractAction action) {
        //super(sshContext, action);
    }

    public CompleteAction(SshContext sshContext) {

    }

    public CompleteAction() {

    }

    @Override
    public void execute() {
        Runtime.getRuntime().addShutdownHook((new Thread(() -> {
            Log.writeToLogFile();
            System.out.println("\r\n Kubernetes Installation Complete. Total consumed time: " + Timer.getConsumedTime());
        })));
        System.exit(0);
    }
}
