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

public class InstallKube_1_28_2_Action extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        //cmds.add(Command.genCommand("kubectl delete node --all"));
        cmds.add(Command.genCommand("yum erase -y kubelet kubectl kubeadm kubernetes-cni"));
        //cmds.add(Command.genCommand("yum install -y kubelet-1.28.2-0 kubeadm-1.28.2-0 kubectl-1.28.2-0 --disableexcludes=kubernetes"));
        cmds.add(Command.genCommand("rpm -ivh /tmp/a24e42254b5a14b67b58c4633d29c27370c28ed6796a80c455a65acc813ff374-kubectl-1.28.2-0.x86_64.rpm " +
                "/tmp/cee73f8035d734e86f722f77f1bf4e7d643e78d36646fd000148deb8af98b61c-kubeadm-1.28.2-0.x86_64.rpm " +
                "/tmp/e1cae938e231bffa3618f5934a096bd85372ee9b1293081f5682a22fe873add8-kubelet-1.28.2-0.x86_64.rpm " +
                "/tmp/0f2a2afd740d476ad77c508847bad1f559afc2425816c1f2ce4432a62dfe0b9d-kubernetes-cni-1.2.0-0.x86_64.rpm " +
                "/tmp/3f5ba2b53701ac9102ea7c7ab2ca6616a8cd5966591a77577585fde1c434ef74-cri-tools-1.26.0-0.x86_64.rpm"));
        cmds.add(Command.genCommand("systemctl restart kubelet"));
        cmds.add(Command.genCommand("systemctl enable kubelet"));
    }

    public InstallKube_1_28_2_Action(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallKube_1_28_2_Action(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    @Override
    public void execute() {
        try {
            uploadRpm();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cmds.forEach(cmd ->{
            try {
                System.out.println(cmd.getCmd());
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

    private void uploadRpm() throws JSchException, SftpException, IOException {
        InputStream inputStream1 = this.getClass().getResourceAsStream("/rpm/kube/a24e42254b5a14b67b58c4633d29c27370c28ed6796a80c455a65acc813ff374-kubectl-1.28.2-0.x86_64.rpm");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/rpm/kube/cee73f8035d734e86f722f77f1bf4e7d643e78d36646fd000148deb8af98b61c-kubeadm-1.28.2-0.x86_64.rpm");
        InputStream inputStream3 = this.getClass().getResourceAsStream("/rpm/kube/e1cae938e231bffa3618f5934a096bd85372ee9b1293081f5682a22fe873add8-kubelet-1.28.2-0.x86_64.rpm");
        InputStream inputStream4 = this.getClass().getResourceAsStream("/rpm/kube/0f2a2afd740d476ad77c508847bad1f559afc2425816c1f2ce4432a62dfe0b9d-kubernetes-cni-1.2.0-0.x86_64.rpm");
        InputStream inputStream5 = this.getClass().getResourceAsStream("/rpm/kube/3f5ba2b53701ac9102ea7c7ab2ca6616a8cd5966591a77577585fde1c434ef74-cri-tools-1.26.0-0.x86_64.rpm");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/a24e42254b5a14b67b58c4633d29c27370c28ed6796a80c455a65acc813ff374-kubectl-1.28.2-0.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/cee73f8035d734e86f722f77f1bf4e7d643e78d36646fd000148deb8af98b61c-kubeadm-1.28.2-0.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/e1cae938e231bffa3618f5934a096bd85372ee9b1293081f5682a22fe873add8-kubelet-1.28.2-0.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream4, "/tmp/0f2a2afd740d476ad77c508847bad1f559afc2425816c1f2ce4432a62dfe0b9d-kubernetes-cni-1.2.0-0.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream5, "/tmp/3f5ba2b53701ac9102ea7c7ab2ca6616a8cd5966591a77577585fde1c434ef74-cri-tools-1.26.0-0.x86_64.rpm");
    }
}
