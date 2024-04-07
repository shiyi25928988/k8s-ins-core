package yi.shi.ssh.k8s.actions.install;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;
import yi.shi.ssh.shell.SshUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class InstallDashboardAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("kubectl apply -f /root/recommended.yaml"));
        cmds.add(Command.genCommand("kubectl apply -f /root/dashboard-user.yaml"));
        cmds.add(Command.genCommand("kubectl -n kubernetes-dashboard create token admin-user"));
    }

    public InstallDashboardAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallDashboardAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallDashboardAction() {
        super();
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }



    @Override
    public void execute() {
        InputStream inputStream = this.getClass().getResourceAsStream("/dashboard/recommended.yaml");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/dashboard/dashboard-user.yaml");
        try {
            SshUtil.upload(super.getSshContext().getSession(), inputStream, "/root/recommended.yaml");
            SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/root/dashboard-user.yaml");
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cmds.forEach(cmd -> {
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
