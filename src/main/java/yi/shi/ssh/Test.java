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
        nodeInfo.setHostIp("192.168.3.240");
        nodeInfo.setPort("22");
        nodeInfo.setHostName("worker01");
        nodeInfo.setPassword("1234567890");
        nodeInfo.setJoinCmd("kubeadm join 192.168.3.137:6443 --token tnx28f.ym9hjz4gba9736y8 --discovery-token-ca-cert-hash sha256:53e305e425c1157a202dfa3bcdf7897c8415cb1210eff81139b561296d1ed93e");
        list.add(nodeInfo);
        configInfo.setList(list);
        ConfigFileProcessor.processConfig(configInfo);
    }
}
