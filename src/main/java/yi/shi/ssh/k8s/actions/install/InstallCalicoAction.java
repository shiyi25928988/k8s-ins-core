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

public class InstallCalicoAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        //cmds.add(Command.genCommand("cd /tmp"));

//        cmds.add(Command.genCommand("ctr i import /tmp/cni_v3_19_4.tar.gz"));
//        cmds.add(Command.genCommand("ctr i import /tmp/kube-controllers_v3_19_4.tar.gz"));
//        cmds.add(Command.genCommand("ctr i import /tmp/node_v3_19_4.tar.gz"));
//        cmds.add(Command.genCommand("ctr i import /tmp/pod2daemon-flexvol_v3_19_4.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/cni_v3_25_0.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/kube-controllers_v3_25_0.tar.gz"));
        cmds.add(Command.genCommand("ctr -n k8s.io i import /tmp/node_v3_25_0.tar.gz"));
        cmds.add(Command.genCommand("kubectl apply -f /tmp/calico.yaml"));
    }

    public InstallCalicoAction(SshContext sshContext, AbstractAction action) {
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallCalicoAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallCalicoAction() {
        super();
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

        InputStream inputStream = this.getClass().getResourceAsStream("/calico/3_25/calico.yaml");
        try {
            SshUtil.upload(super.getSshContext().getSession(), inputStream, "/tmp/calico.yaml");
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
        InputStream inputStream1 = this.getClass().getResourceAsStream("/images/calico/3_25/cni_v3_25_0.tar.gz");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/images/calico/3_25/kube-controllers_v3_25_0.tar.gz");
        InputStream inputStream3 = this.getClass().getResourceAsStream("/images/calico/3_25/node_v3_25_0.tar.gz");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/cni_v3_25_0.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/kube-controllers_v3_25_0.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/node_v3_25_0.tar.gz");

//        InputStream inputStream1 = this.getClass().getResourceAsStream("/images/calico/cni_v3_19_4.tar.gz");
//        InputStream inputStream2 = this.getClass().getResourceAsStream("/images/calico/kube-controllers_v3_19_4.tar.gz");
//        InputStream inputStream3 = this.getClass().getResourceAsStream("/images/calico/node_v3_19_4.tar.gz");
//        InputStream inputStream4 = this.getClass().getResourceAsStream("/images/calico/pod2daemon-flexvol_v3_19_4.tar.gz");
//
//        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/cni_v3_19_4.tar.gz");
//        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/kube-controllers_v3_19_4.tar.gz");
//        SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/node_v3_19_4.tar.gz");
//        SshUtil.upload(super.getSshContext().getSession(), inputStream4, "/tmp/pod2daemon-flexvol_v3_19_4.tar.gz");
    }
}
