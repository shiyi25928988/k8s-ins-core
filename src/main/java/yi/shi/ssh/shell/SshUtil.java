package yi.shi.ssh.shell;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SshUtil {

    public static final Integer DEFAULT_SSH_PORT = 22;

    public static void shell(Session session, InputStream in, OutputStream out) throws JSchException {
        Channel channel=session.openChannel("shell");
        channel.setInputStream(in);
        channel.setOutputStream(out);
    }

    public static InputStream exec(Session session, String cmd) throws JSchException, IOException {
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(cmd);
        ((ChannelExec)channel).setErrStream(System.err);
        InputStream inputStream=channel.getInputStream();
        channel.connect();
        InputStream out = IOUtils.toBufferedInputStream(inputStream);
        channel.disconnect();
        return out;
    }

    public static void upload(Session session, InputStream inputStream, String dst) throws SftpException, JSchException, IOException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ((ChannelSftp)channel).put((inputStream), dst);
        ((ChannelSftp)channel).quit();
    }

    public static void download(Session session, String src, OutputStream outputStream) throws SftpException, JSchException, IOException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ((ChannelSftp)channel).get(src, outputStream);
        ((ChannelSftp)channel).quit();
    }

    public static Session getSession(String host, String user, String passwd) throws Exception {
        return getSession(host, user, passwd, DEFAULT_SSH_PORT);
    }

    public static Session getSession(String host, String user, String passwd, Integer port) throws Exception {
        JSch jsch=new JSch();
        Session session=jsch.getSession(user, host, port);
        session.setPassword(passwd);

        session.setUserInfo(new UserInfo() {
            @Override
            public String getPassphrase() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public boolean promptPassword(String message) {
                return false;
            }

            @Override
            public boolean promptPassphrase(String message) {
                return false;
            }

            @Override
            public boolean promptYesNo(String message) {
                return true;
            }

            @Override
            public void showMessage(String message) {

            }
        });

        session.connect(30000);   // making a connection with timeout.
        return session;

    }



}
