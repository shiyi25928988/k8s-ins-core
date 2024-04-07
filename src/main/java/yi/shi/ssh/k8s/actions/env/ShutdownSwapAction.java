package yi.shi.ssh.k8s.actions.env;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class ShutdownSwapAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static{
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("swapoff -a"));
        cmds.add(Command.genCommand("swapon -s"));
        cmds.add(Command.genCommand("sed -ri 's/.*swap.*/#&/' /etc/fstab"));
        cmds.add(Command.genCommand("echo vm.swappiness=0 >> /etc/sysctl.conf"));
    }

    public ShutdownSwapAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public ShutdownSwapAction(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public ShutdownSwapAction(){
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
