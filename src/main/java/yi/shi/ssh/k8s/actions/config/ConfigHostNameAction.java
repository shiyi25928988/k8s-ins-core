package yi.shi.ssh.k8s.actions.config;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class ConfigHostNameAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
    }

    public ConfigHostNameAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        cmds.add(Command.genCommand("hostnamectl set-hostname %s", sshContext.getHostname()));
        cmds.add(Command.genCommand("echo \"" + sshContext.getHostIp() + " %s\">> /etc/hosts", sshContext.getHostname()));
        cmds.add(Command.genCommand("echo \"127.0.0.1  %s\">> /etc/hosts", sshContext.getHostname()));
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public ConfigHostNameAction(SshContext sshContext){
        super(sshContext);
        cmds.add(Command.genCommand("hostnamectl set-hostname %s", sshContext.getHostname()));
        cmds.add(Command.genCommand("echo \"" + sshContext.getHostIp() + " %s\">> /etc/hosts", sshContext.getHostname()));
        cmds.add(Command.genCommand("echo \"127.0.0.1  %s\">> /etc/hosts", sshContext.getHostname()));
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public ConfigHostNameAction(){super();}

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

    public static void main(String...strings){
        String.format("echo \" %s  %s \">> /etc/hosts", "2123", "123");
    }
}
