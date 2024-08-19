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

public class InstallContainerdAction extends AbstractAction {


    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        //cmds.add(Command.genCommand("yum install -y containerd.io"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/libcgroup-0.41-21.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/audit-libs-python-2.8.5-4.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/setools-libs-3.3.8-4.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/checkpolicy-2.5-8.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/libsemanage-python-2.5-14.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/python-IPy-0.75-6.el7.noarch.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/policycoreutils-python-2.5-34.el7.x86_64.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/container-selinux-2.119.2-1.911c772.el7_8.noarch.rpm"));
//        cmds.add(Command.genCommand("rpm -ivh /tmp/containerd.io-1.6.33-3.1.el7.x86_64.rpm"));
        cmds.add(Command.genCommand("yum erase -y \\\n" +
                "libnetfilter_queue-1.0.2-2.el7_2.x86_64 \\\n" +
                "libnetfilter_cttimeout-1.0.0-7.el7.x86_64 \\\n" +
                "libnetfilter_cthelper-1.0.0-11.el7.x86_64 \\\n" +
                "python-IPy-0.75-6.el7.noarch \\\n" +
                "libsemanage-python-2.5-14.el7.x86_64 \\\n" +
                "checkpolicy-2.5-8.el7.x86_64 \\\n" +
                "setools-libs-3.3.8-4.el7.x86_64 \\\n" +
                "audit-libs-python-2.8.5-4.el7.x86_64 \\\n" +
                "libcgroup-0.41-21.el7.x86_64 \\\n" +
                "policycoreutils-python-2.5-34.el7.x86_64 \\\n" +
                "container-selinux-2:2.119.2-1.911c772.el7_8.noarch \\\n" +
                "conntrack-tools-1.4.4-7.el7.x86_64 \\\n" +
                "containerd.io-1.6.33-3.1.el7.x86_64 \\\n" +
                "socat-1.7.3.2-2.el7.x86_64"));
        cmds.add(Command.genCommand("rpm -ivh \\\n" +
                "/tmp/libcgroup-0.41-21.el7.x86_64.rpm \\\n" +
                "/tmp/audit-libs-python-2.8.5-4.el7.x86_64.rpm \\\n" +
                "/tmp/setools-libs-3.3.8-4.el7.x86_64.rpm \\\n" +
                "/tmp/checkpolicy-2.5-8.el7.x86_64.rpm \\\n" +
                "/tmp/libsemanage-python-2.5-14.el7.x86_64.rpm \\\n" +
                "/tmp/python-IPy-0.75-6.el7.noarch.rpm \\\n" +
                "/tmp/policycoreutils-python-2.5-34.el7.x86_64.rpm \\\n" +
                "/tmp/container-selinux-2.119.2-1.911c772.el7_8.noarch.rpm \\\n" +
                "/tmp/containerd.io-1.6.33-3.1.el7.x86_64.rpm \\\n" +
                "/tmp/conntrack-tools-1.4.4-7.el7.x86_64.rpm \\\n" +
                "/tmp/libnetfilter_cthelper-1.0.0-11.el7.x86_64.rpm \\\n" +
                "/tmp/libnetfilter_cttimeout-1.0.0-7.el7.x86_64.rpm \\\n" +
                "/tmp/libnetfilter_queue-1.0.2-2.el7_2.x86_64.rpm \\\n" +
                "/tmp/socat-1.7.3.2-2.el7.x86_64.rpm"));

        cmds.add(Command.genCommand("mkdir -p /etc/containerd"));
        cmds.add(Command.genCommand("containerd config default > /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's/SystemdCgroup = false/SystemdCgroup = true/g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's#root = \"/var/lib/containerd\"#root = \"/usr/lib/containerd\"#g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("sed -i 's#sandbox_image = \"registry.k8s.io/pause:#sandbox_image = \"registry.aliyuncs.com/google_containers/pause:#g' /etc/containerd/config.toml"));
        cmds.add(Command.genCommand("systemctl daemon-reload"));
        cmds.add(Command.genCommand("systemctl restart containerd"));
    }

    public InstallContainerdAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }

    public InstallContainerdAction(SshContext sshContext){
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
        InputStream inputStream1 = this.getClass().getResourceAsStream("/rpm/containerd/libcgroup-0.41-21.el7.x86_64.rpm");
        InputStream inputStream2 = this.getClass().getResourceAsStream("/rpm/containerd/audit-libs-python-2.8.5-4.el7.x86_64.rpm");
        InputStream inputStream3 = this.getClass().getResourceAsStream("/rpm/containerd/setools-libs-3.3.8-4.el7.x86_64.rpm");
        InputStream inputStream4 = this.getClass().getResourceAsStream("/rpm/containerd/checkpolicy-2.5-8.el7.x86_64.rpm");
        InputStream inputStream5 = this.getClass().getResourceAsStream("/rpm/containerd/libsemanage-python-2.5-14.el7.x86_64.rpm");
        InputStream inputStream6 = this.getClass().getResourceAsStream("/rpm/containerd/python-IPy-0.75-6.el7.noarch.rpm");
        InputStream inputStream7 = this.getClass().getResourceAsStream("/rpm/containerd/policycoreutils-python-2.5-34.el7.x86_64.rpm");
        InputStream inputStream8 = this.getClass().getResourceAsStream("/rpm/containerd/container-selinux-2.119.2-1.911c772.el7_8.noarch.rpm");
        InputStream inputStream9 = this.getClass().getResourceAsStream("/rpm/containerd/containerd.io-1.6.33-3.1.el7.x86_64.rpm");

        InputStream inputStream10 = this.getClass().getResourceAsStream("/rpm/containerd/lib/conntrack-tools-1.4.4-7.el7.x86_64.rpm");
        InputStream inputStream11 = this.getClass().getResourceAsStream("/rpm/containerd/lib/libnetfilter_cthelper-1.0.0-11.el7.x86_64.rpm");
        InputStream inputStream12 = this.getClass().getResourceAsStream("/rpm/containerd/lib/libnetfilter_cttimeout-1.0.0-7.el7.x86_64.rpm");
        InputStream inputStream13 = this.getClass().getResourceAsStream("/rpm/containerd/lib/libnetfilter_queue-1.0.2-2.el7_2.x86_64.rpm");
        InputStream inputStream14 = this.getClass().getResourceAsStream("/rpm/containerd/lib/socat-1.7.3.2-2.el7.x86_64.rpm");

        SshUtil.upload(super.getSshContext().getSession(), inputStream1, "/tmp/libcgroup-0.41-21.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream2, "/tmp/audit-libs-python-2.8.5-4.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream3, "/tmp/setools-libs-3.3.8-4.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream4, "/tmp/checkpolicy-2.5-8.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream5, "/tmp/libsemanage-python-2.5-14.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream6, "/tmp/python-IPy-0.75-6.el7.noarch.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream7, "/tmp/policycoreutils-python-2.5-34.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream8, "/tmp/container-selinux-2.119.2-1.911c772.el7_8.noarch.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream9, "/tmp/containerd.io-1.6.33-3.1.el7.x86_64.rpm");

        SshUtil.upload(super.getSshContext().getSession(), inputStream10, "/tmp/conntrack-tools-1.4.4-7.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream11, "/tmp/libnetfilter_cthelper-1.0.0-11.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream12, "/tmp/libnetfilter_cttimeout-1.0.0-7.el7.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream13, "/tmp/libnetfilter_queue-1.0.2-2.el7_2.x86_64.rpm");
        SshUtil.upload(super.getSshContext().getSession(), inputStream14, "/tmp/socat-1.7.3.2-2.el7.x86_64.rpm");

    }
}
