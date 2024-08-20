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
        nodeInfo.setHostIp("192.168.1.102");
        nodeInfo.setPort("22");
        nodeInfo.setHostName("master");
        nodeInfo.setPassword("1234567890");
        //nodeInfo.setJoinCmd("kubeadm join 192.168.1.102:6443 --token f6m2b8.61jp2lme8n1kep9r --discovery-token-ca-cert-hash sha256:db54b36086081b30aebec28ce1a78afb605ae232d7e8bb09daa37a3ff7e35762");
        list.add(nodeInfo);
        configInfo.setList(list);
        ConfigFileProcessor.processConfig(configInfo);
    }
}
