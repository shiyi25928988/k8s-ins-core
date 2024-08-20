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

public class InstallCrictlAction extends AbstractAction {

    static LinkedList<Command> cmds;


    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("cd /tmp"));
        cmds.add(Command.genCommand("tar -zxvf crictl-v1.31.1-linux-amd64.tar.gz -C /usr/local/bin"));
        cmds.add(Command.genCommand("cat > /etc/crictl.yaml <<EOF\n" +
                "runtime-endpoint: unix:///var/run/containerd/containerd.sock\n" +
                "image-endpoint: unix:///var/run/containerd/containerd.sock\n" +
                "timeout: 10\n" +
                "debug: false\n" +
                "pull-image-on-create: false\n" +
                "EOF"));
        cmds.add(Command.genCommand("systemctl daemon-reload"));
    }

    public InstallCrictlAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    @Override
    public void execute() {
        try {
            uploadPackage();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cmds.forEach(cmd ->{
            try {
                //System.out.println(cmd.getCmd());
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

    private void uploadPackage() throws JSchException, SftpException, IOException {
        InputStream inputStream1 = this.getClass().getResourceAsStream("/rpm/containerd/crictl/crictl-v1.31.1-linux-amd64.tar.gz");
        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/crictl-v1.31.1-linux-amd64.tar.gz");
    }
}
