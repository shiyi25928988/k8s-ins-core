package yi.shi.ssh.k8s.actions.join;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Stream;

public class JoinAsNodeAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("docker pull registry.aliyuncs.com/google_containers/coredns:v1.8.0"));
        cmds.add(Command.genCommand("docker tag registry.aliyuncs.com/google_containers/coredns:v1.8.0 registry.aliyuncs.com/google_containers/coredns/coredns:v1.8.0"));
        cmds.add(Command.genCommand("kubeadm reset -f"));
    }

    public JoinAsNodeAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    public JoinAsNodeAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    public JoinAsNodeAction addCmd(String...commands){
        Stream.of(commands).forEach(command -> {
            cmds.add(Command.genCommand(command));
        });
        return this;
    }

    @Override
    public void execute() {
        cmds.forEach(cmd ->{
            try {
                cmd.exec(this.getSshContext());
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
