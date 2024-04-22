package yi.shi.ssh.k8s.file;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.actions.NullAction;
import yi.shi.ssh.actions.executor.ActionExecutors;
import yi.shi.ssh.cmd.CommandLineNumCount;
import yi.shi.ssh.k8s.KubernetesActionFactory;
import yi.shi.ssh.k8s.KubernetesActionType;
import yi.shi.ssh.shell.SshAuth;
import yi.shi.ssh.shell.SshContext;
import yi.shi.ssh.shell.SshProgressOutput;
import yi.shi.ssh.utils.Timer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ConfigFileProcessor {

    //加载File,

    public static ConfigInfo loadConfigFile(File file) throws IOException {

        if(file.exists()) {
            String content = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
            ConfigInfo configInfo = JSON.parseObject(content, ConfigInfo.class);
            return configInfo;
        }
        throw new RuntimeException("配置文件不存在");
    }

    public static void processConfig(ConfigInfo configInfo) {
        configInfo.getList().forEach(nodeInfo -> {
            SshAuth sshAuth = new SshAuth();
            sshAuth.setIp(nodeInfo.getHostIp());
            sshAuth.setUser(nodeInfo.getUser());
            sshAuth.setHostName(nodeInfo.getHostName());
            sshAuth.setPort(Integer.parseInt(nodeInfo.getPort()));
            sshAuth.setPasswd(nodeInfo.getPassword());
            //SshOutputBuffer.asyncOutput(config.getHostIp());
            AbstractAction action = new NullAction();
            try {
                SshContext sshContext = new SshContext(sshAuth);
                if(Strings.isNullOrEmpty(nodeInfo.getJoinCmd())){
                    action = KubernetesActionFactory.getAction(false, sshContext);
                    SshProgressOutput.asyncOutput(CommandLineNumCount.getCmdLineNum(sshContext), sshContext);
                    Timer.start();
                    ActionExecutors.execute(action);
                }else if(!Strings.isNullOrEmpty(nodeInfo.getJoinCmd()) && nodeInfo.getJoinCmd().startsWith("kubeadm join")){
                    action = KubernetesActionFactory.getAction(true, sshContext, nodeInfo.getJoinCmd());
                    SshProgressOutput.asyncOutput(CommandLineNumCount.getCmdLineNum(sshContext), sshContext);
                    Timer.start();
                    ActionExecutors.execute(action);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
