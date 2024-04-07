package yi.shi.ssh.shell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SshAuth {
    String ip;
    String user;
    String passwd;
    Integer port;
    String hostName;
    Boolean isMaster;

}
