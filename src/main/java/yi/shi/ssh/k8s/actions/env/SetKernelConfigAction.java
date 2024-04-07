package yi.shi.ssh.k8s.actions.env;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class SetKernelConfigAction extends AbstractAction {
    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("cat <<EOF > /etc/sysctl.d/k8s.conf \n" +
                "net.bridge.bridge-nf-call-ip6tables = 1 \n" +
                "net.bridge.bridge-nf-call-iptables = 1 \n" +
                "net.ipv4.ip_forward = 1 \n" +
                "EOF"));
        cmds.add(Command.genCommand("sysctl -p /etc/sysctl.d/k8s.conf "));
        cmds.add(Command.genCommand("modprobe br_netfilter"));
        cmds.add(Command.genCommand("lsmod | grep br_netfilter"));
        cmds.add(Command.genCommand("sysctl net.bridge.bridge-nf-call-iptables=1"));
    }

    public SetKernelConfigAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public SetKernelConfigAction(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public SetKernelConfigAction(){
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
