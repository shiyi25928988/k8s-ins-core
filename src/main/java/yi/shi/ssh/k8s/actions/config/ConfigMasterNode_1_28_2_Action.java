package yi.shi.ssh.k8s.actions.config;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class ConfigMasterNode_1_28_2_Action extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/kube-apiserver:v1.28.5"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/kube-controller-manager:v1.28.5"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/kube-scheduler:v1.28.5"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/kube-proxy:v1.28.5"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/pause:3.9"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/etcd:3.5.9-0"));
        cmds.add(Command.genCommand("ctr images pull registry.aliyuncs.com/google_containers/coredns:v1.10.1"));
        cmds.add(Command.genCommand("ctr i tag registry.aliyuncs.com/google_containers/coredns:v1.10.1 registry.aliyuncs.com/google_containers/coredns/coredns:v1.10.1"));
    }


    public ConfigMasterNode_1_28_2_Action(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        cmds.add(Command.genCommand("kubeadm init --image-repository registry.aliyuncs.com/google_containers --kubernetes-version=v1.28.5 --pod-network-cidr=10.244.0.0/16 --control-plane-endpoint=%s:6443 --upload-certs", this.getSshContext().getHostIp()));
        cmds.add(Command.genCommand("echo export KUBECONFIG=/etc/kubernetes/admin.conf >> /etc/profile"));
        cmds.add(Command.genCommand("source /etc/profile"));
        cmds.add(Command.genCommand("mkdir -p $HOME/.kube"));
        cmds.add(Command.genCommand("sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config"));
        cmds.add(Command.genCommand("sudo chown $(id -u):$(id -g) $HOME/.kube/config"));
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public ConfigMasterNode_1_28_2_Action(SshContext sshContext){
        super(sshContext);
        cmds.add(Command.genCommand("kubeadm init --image-repository registry.aliyuncs.com/google_containers --kubernetes-version=v1.28.5 --pod-network-cidr=10.244.0.0/16 --control-plane-endpoint=%s:6443 --upload-certs", this.getSshContext().getHostIp()));
        cmds.add(Command.genCommand("echo export KUBECONFIG=/etc/kubernetes/admin.conf >> /etc/profile"));
        cmds.add(Command.genCommand("source /etc/profile"));
        cmds.add(Command.genCommand("mkdir -p $HOME/.kube"));
        cmds.add(Command.genCommand("sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config"));
        cmds.add(Command.genCommand("sudo chown $(id -u):$(id -g) $HOME/.kube/config"));
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
