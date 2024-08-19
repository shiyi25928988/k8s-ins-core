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

public class InstallIngressAction extends AbstractAction {
    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/kube-webhook-certgen_v1_1_1.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/nginx-ingress-controller_v1_1_0.tar.gz"));
        cmds.add(Command.genCommand("kubectl apply -f /tmp/deploy.yaml"));
    }

    public InstallIngressAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallIngressAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallIngressAction() {
        super();
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
    }

    @Override
    public void execute() {
        try {
            uploadImage();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/ingress/deploy.yaml");
        try {
            SshUtil.upload(super.getSshContext().getSession(), inputStream, "/tmp/deploy.yaml");
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

    private void uploadImage() throws JSchException, SftpException, IOException {
        InputStream inputStream1 = this.getClass().getResourceAsStream("/images/ingress/kube-webhook-certgen_v1_1_1.tar.gz");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/images/ingress/nginx-ingress-controller_v1_1_0.tar.gz");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/kube-webhook-certgen_v1_1_1.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/nginx-ingress-controller_v1_1_0.tar.gz");
    }
}
