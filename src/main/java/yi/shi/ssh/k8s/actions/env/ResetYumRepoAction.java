package yi.shi.ssh.k8s.actions.env;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class ResetYumRepoAction extends AbstractAction {
    static LinkedList<Command> cmds;

    static{
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("rm -f /etc/yum.repos.d/*"));
        cmds.add(Command.genCommand("curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo"));
        cmds.add(Command.genCommand("curl -o /etc/yum.repos.d/docker-ce.repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo"));
        cmds.add(Command.genCommand("cat <<EOF > /etc/yum.repos.d/kubernetes.repo\n" +
                "[kubernetes]\n" +
                "name=Kubernetes\n" +
                "baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/\n" +
                "enabled=1\n" +
                "gpgcheck=1\n" +
                "repo_gpgcheck=1\n" +
                "gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg\n" +
                "EOF"));
        cmds.add(Command.genCommand("yum clean all"));
        cmds.add(Command.genCommand("yum install -y wget.x86_64"));
    }

    public ResetYumRepoAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public ResetYumRepoAction(SshContext sshContext) {
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public ResetYumRepoAction(){
        super();
        CommandLineNumCount.updateCmdLineNum(cmds.size(), this.getSshContext());
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
