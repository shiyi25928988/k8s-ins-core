package yi.shi.ssh.shell;

import com.google.common.base.Strings;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.RandomStringUtils;

public class SshContext {

    private final Session session;

    private String hostname;

    private String hostIp;

    public SshContext(SshAuth sshAuth) throws Exception {
        this.hostname = sshAuth.getHostName();
        this.hostIp = sshAuth.getIp();
        this.session = SshUtil.getSession(sshAuth.getIp(), sshAuth.getUser(), sshAuth.getPasswd(), sshAuth.getPort());
    }

    public Session getSession(){
        return this.session;
    }

    public String getHostIp(){
        return this.hostIp;
    }

    /**
     * Returns the hostname.
     *
     * @return the hostname
     */
    public String getHostname(){
        if(Strings.isNullOrEmpty(this.hostname)){
            this.hostname = RandomStringUtils.randomAlphabetic(10);
        }
        return this.hostname;
    }
}
