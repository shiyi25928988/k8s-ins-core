package yi.shi.ssh.k8s.actions.install;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class InstallKube_1_21_1_Action extends AbstractAction {
    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("kubectl delete node --all"));
        cmds.add(Command.genCommand("yum erase -y kubelet kubectl kubeadm kubernetes-cni"));
        cmds.add(Command.genCommand("yum install -y kubelet-1.21.1-0 kubeadm-1.21.1-0 kubectl-1.21.1-0 --disableexcludes=kubernetes"));
        cmds.add(Command.genCommand("systemctl restart kubelet"));
        cmds.add(Command.genCommand("systemctl enable kubelet"));
    }

    public InstallKube_1_21_1_Action(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public InstallKube_1_21_1_Action(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public InstallKube_1_21_1_Action(){
        super();
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
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
