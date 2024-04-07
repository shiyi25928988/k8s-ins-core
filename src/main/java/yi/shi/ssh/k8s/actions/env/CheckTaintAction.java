package yi.shi.ssh.k8s.actions.env;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class CheckTaintAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("kubectl describe nodes | grep -E '(Taints)' "));
    }

    public CheckTaintAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(1, sshContext);
    }

    @Override
    public void execute() {
        cmds.forEach(cmd ->{
            try {
                cmd.exec(this.getSshContext());
                //Command.genCommand("kubectl taint nodes ".concat(this.getSshContext().getHostname()).concat(" ").concat(cmd.getResult().replace("Taints:", "")).concat("-")).exec(this.getSshContext());
            } catch (JSchException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SshCmdExecException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
