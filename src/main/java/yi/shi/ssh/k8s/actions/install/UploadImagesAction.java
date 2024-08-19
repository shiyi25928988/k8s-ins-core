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

public class UploadImagesAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/dashboard_v2_7_0.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/metrics-scraper_v1_0_8.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/controller_v0_12_1.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/speaker_v0_12_1.tar.gz"));

    }

    public UploadImagesAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public UploadImagesAction(SshContext sshContext) {
        super(sshContext);
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
        InputStream inputStream1 = this.getClass().getResourceAsStream("/images/dashboard/dashboard_v2_7_0.tar.gz");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/images/dashboard/metrics-scraper_v1_0_8.tar.gz");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/dashboard_v2_7_0.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/metrics-scraper_v1_0_8.tar.gz");

        InputStream inputStream3 = this.getClass().getResourceAsStream("/images/metallb/controller_v0_12_1.tar.gz");
        InputStream inputStream4 = this.getClass().getResourceAsStream("/images/metallb/speaker_v0_12_1.tar.gz");

        SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/controller_v0_12_1.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream4, "/tmp/speaker_v0_12_1.tar.gz");
    }
}
