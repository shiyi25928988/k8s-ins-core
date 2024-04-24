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

public class InstallHelmAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("tar -zxvf helm-v3.14.4-linux-amd64.tar.gz"));
        cmds.add(Command.genCommand("mv linux-amd64/helm /usr/local/bin/helm"));
        cmds.add(Command.genCommand("helm version"));
    }

    public InstallHelmAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    public InstallHelmAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    public InstallHelmAction() {
        super();
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    @Override
    public void execute() {
        InputStream inputStream = this.getClass().getResourceAsStream("/helm/helm-v3.14.4-linux-amd64.tar.gz");
        try {
            SshUtil.upload(super.getSshContext().getSession(), inputStream, "/root/helm-v3.14.4-linux-amd64.tar.gz");
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
