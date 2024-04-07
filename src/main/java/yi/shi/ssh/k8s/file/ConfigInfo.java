package yi.shi.ssh.k8s.file;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ConfigInfo {
    @JSONField(name = "NODES")
    List<NodeInfo> list;
}
