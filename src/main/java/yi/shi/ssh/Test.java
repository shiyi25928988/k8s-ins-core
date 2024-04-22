package yi.shi.ssh;

import yi.shi.ssh.k8s.file.ConfigFileProcessor;
import yi.shi.ssh.k8s.file.ConfigInfo;
import yi.shi.ssh.k8s.file.NodeInfo;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void  main(String[] args) {
        ConfigInfo configInfo = new ConfigInfo();
        List<NodeInfo> list = new ArrayList<>();
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setUser("root");
        nodeInfo.setHostIp("192.168.3.137");
        nodeInfo.setPort("22");
        nodeInfo.setHostName("master01");
        nodeInfo.setPassword("1234567890");
        nodeInfo.setJoinCmd("kubeadm join 192.168.3.137:6443 --token 621avo.pr2cein6c0u8ci42 --discovery-token-ca-cert-hash sha256:1530e8c81fa283bf6ada446ec7f35e7de9209c264013dab6240f8feb393158e6 ");
        list.add(nodeInfo);
        configInfo.setList(list);
        ConfigFileProcessor.processConfig(configInfo);
    }
}
