package yi.shi.ssh.cmd;

import com.google.common.base.Strings;
import com.jcraft.jsch.JSchException;
import org.apache.commons.io.IOUtils;
import yi.shi.ssh.exception.SshCmdExecException;
import yi.shi.ssh.log.Log;
import yi.shi.ssh.shell.SshContext;
import yi.shi.ssh.shell.SshOutputBuffer;
import yi.shi.ssh.shell.SshUtil;

import java.io.IOException;
import java.io.InputStream;


public class Command {

    //shell command
    String cmd;

    //parameter
    String param;

    //expect value returned by command
    String expectValueOrRegex;
    String result;

    public Command(){}

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getExpectValueOrRegex() {
        return expectValueOrRegex;
    }

    public void setExpectValueOrRegex(String expectValueOrRegex) {
        this.expectValueOrRegex = expectValueOrRegex;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCommand(){
        if(Strings.isNullOrEmpty(param)){
            return cmd;
        }
        return String.format(cmd, param);
    }

    public boolean isAccordToExpection(){
        if(Strings.isNullOrEmpty(result) || Strings.isNullOrEmpty(expectValueOrRegex)){
            return true;
        }
        if(result.contains(expectValueOrRegex)){
            return true;
        }
        if(result.matches(expectValueOrRegex)){
            return true;
        }
        return false;
    }

    public void exec(SshContext sshContext) throws JSchException, IOException, SshCmdExecException {
        if(Strings.isNullOrEmpty(this.getCommand())){
            return;
        }
        SshOutputBuffer.append(sshContext.getHostIp(), "=>".concat(this.getCommand()).concat("\n"));
        Log.info(sshContext,"==>".concat(this.getCommand()));
        InputStream in = SshUtil.exec(sshContext.getSession(), this.getCommand());
        Log.info(sshContext,"<==".concat(IOUtils.toString(in, "UTF-8")));
        CommandLineNumCount.deductCmdLineNum(1, sshContext);
        this.setResult(IOUtils.toString(in, "UTF-8"));
        if(!isAccordToExpection()){
            throw new SshCmdExecException(this.getResult());
        }
    }

    public static Command genCommand(String cmd, String param, String expectValueOrRegex){
        Command command = new Command();
        if(!Strings.isNullOrEmpty(cmd)){
            command.setCmd(cmd);
        }
        if(!Strings.isNullOrEmpty(param)){
            command.setParam(param);
        }
        if(!Strings.isNullOrEmpty(expectValueOrRegex)){
            command.setExpectValueOrRegex(expectValueOrRegex);
        }
        return command;
    }
    public static Command genCommand(String cmd, String param){
        Command command = new Command();
        if(!Strings.isNullOrEmpty(cmd)){
            command.setCmd(cmd);
        }
        if(!Strings.isNullOrEmpty(param)){
            command.setParam(param);
        }
        return command;
    }
    public static Command genCommand(String cmd){
        Command command = new Command();
        if(!Strings.isNullOrEmpty(cmd)){
            command.setCmd(cmd);
        }
        return command;
    }

}
