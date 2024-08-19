package yi.shi.ssh.k8s.actions.install;

import com.google.common.io.CharStreams;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;
import yi.shi.ssh.shell.SshUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class InstallMetallbAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/controller_v0_12_1.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/speaker_v0_12_1.tar.gz"));
        cmds.add(Command.genCommand("kubectl apply -f /tmp/namespace.yaml"));
        cmds.add(Command.genCommand("kubectl apply -f /tmp/metallb.yaml"));
        cmds.add(Command.genCommand("kubectl apply -f /tmp/layer2-config.yaml"));
    }

    public InstallMetallbAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallMetallbAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallMetallbAction() {
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

        InputStream inputStream = this.getClass().getResourceAsStream("/metallb/namespace.yaml");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/metallb/metallb.yaml");
        InputStream inputStream3 = this.getClass().getResourceAsStream("/metallb/layer2-config.yaml");
        try {
            String str = CharStreams.toString(new InputStreamReader(inputStream3, StandardCharsets.UTF_8)).replace("- 0.0.0.0/0", "- " + this.getSshContext().getHostIp() + "/26");
            inputStream3 = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            SshUtil.upload(super.getSshContext().getSession(), inputStream, "/tmp/namespace.yaml");
            SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/metallb.yaml");
            SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/layer2-config.yaml");
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
        InputStream inputStream1 = this.getClass().getResourceAsStream("/images/metallb/controller_v0_12_1.tar.gz");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/images/metallb/speaker_v0_12_1.tar.gz");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/controller_v0_12_1.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/speaker_v0_12_1.tar.gz");
    }
}
