package yi.shi.ssh.k8s.file;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

//节点信息的pojo类
@Data
public class NodeInfo {
    @JSONField(name = "HOST_IP")
    String hostIp = "127.0.0.1";
    @JSONField(name = "HOST_NAME")
    String hostName = "HOST_NAME";
    @JSONField(name = "PORT")
    String port = "22";
    @JSONField(name = "USER")
    String user = "root";
    @JSONField(name = "PASSWORD")
    String password = "";
    @JSONField(name = "JOIN_CMD")
    String joinCmd = "";
}
