package yi.shi.ssh.k8s.actions.install;

import com.jcraft.jsch.JSchException;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.cmd.Command;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.shell.SshContext;

import java.io.IOException;
import java.util.LinkedList;

public class InstallDockerAction extends AbstractAction {

    static LinkedList<Command> cmds;

    static {
        cmds = new LinkedList<>();
        cmds.add(Command.genCommand("curl -o docker-24.0.5.tgz https://download.docker.com/linux/static/stable/x86_64/docker-24.0.5.tgz"));
        cmds.add(Command.genCommand("tar -xvf docker-24.0.5.tgz"));
        cmds.add(Command.genCommand("cp docker/* /usr/bin/"));
        cmds.add(Command.genCommand("cat <<EOF > /etc/systemd/system/docker.service\n" +
                "[Unit]\n" +
                "Description=Docker Application Container Engine\n" +
                "Documentation=https://docs.docker.com\n" +
                "After=network-online.target firewalld.service\n" +
                "Wants=network-online.target\n" +
                " \n" +
                "[Service]\n" +
                "Type=notify\n" +
                "# the default is not to use systemd for cgroups because the delegate issues still\n" +
                "# exists and systemd currently does not support the cgroup feature set required\n" +
                "# for containers run by docker\n" +
                "ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock\n" +
                "ExecReload=/bin/kill -s HUP $MAINPID\n" +
                "# Having non-zero Limit*s causes performance problems due to accounting overhead\n" +
                "# in the kernel. We recommend using cgroups to do container-local accounting.\n" +
                "LimitNOFILE=infinity\n" +
                "LimitNPROC=infinity\n" +
                "LimitCORE=infinity\n" +
                "# Uncomment TasksMax if your systemd version supports it.\n" +
                "# Only systemd 226 and above support this version.\n" +
                "#TasksMax=infinity\n" +
                "TimeoutStartSec=0\n" +
                "# set delegate yes so that systemd does not reset the cgroups of docker containers\n" +
                "Delegate=yes\n" +
                "# kill only the docker process, not all processes in the cgroup\n" +
                "KillMode=process\n" +
                "# restart the docker process if it exits prematurely\n" +
                "Restart=on-failure\n" +
                "StartLimitBurst=3\n" +
                "StartLimitInterval=60s\n" +
                " \n" +
                "[Install]\n" +
                "WantedBy=multi-user.target\n" +
                "EOF"));
        cmds.add(Command.genCommand("chmod +x /etc/systemd/system/docker.service"));

        cmds.add(Command.genCommand("{\n" +
                "  \"registry-mirrors\": [],\n" +
                "  \"insecure-registries\": [\n" +
                "  ],\n" +
                "  \"debug\": false,\n" +
                "  \"experimental\": false,\n" +
                "  \"features\": {\n" +
                "    \"buildkit\": true\n" +
                "  },\n" +
                "  \"exec-opts\":[\"native.cgroupdriver=systemd\"]\n" +
                "}"));
        cmds.add(Command.genCommand("systemctl daemon-reload"));
        cmds.add(Command.genCommand("systemctl start docker"));
        cmds.add(Command.genCommand("systemctl enable docker.service"));
        cmds.add(Command.genCommand("docker -v", "", "Docker version"));
    }

    public InstallDockerAction(SshContext sshContext, AbstractAction action){
        super(sshContext, action);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public InstallDockerAction(SshContext sshContext){
        super(sshContext);
        CommandLineNumCount.updateCmdLineNum(cmds.size(), sshContext);
    }
    public InstallDockerAction(){
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
