package yi.shi.ssh.k8s.actions.install;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class InstallContainerdAction extends AbstractAction {


    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("yum install -y containerd.io"));
        cmds.add(Command.genCommand("mkdir -p /etc/containerd"));
        cmds.add(Command.genCommand("containerd config default > /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's/SystemdCgroup = false/SystemdCgroup = true/g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's#root = \"/var/lib/containerd\"#root = \"/usr/lib/containerd\"#g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's#sandbox_image = \"registry.k8s.io/pause:#sandbox_image = \"registry.aliyuncs.com/google_containers/pause:#g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("systemctl daemon-reload"));
        cmds.add(Command.genCommand("systemctl restart containerd"));
    }

    public InstallContainerdAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallContainerdAction(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
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
