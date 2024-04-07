package yi.shi.ssh.cmd;

import yi.shi.ssh.shell.SshContext;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class CommandLineNumCount {

    private static final Map<String, AtomicInteger> cmdLineNumMap = new ConcurrentHashMap<>();

    public static int updateCmdLineNum(int num, SshContext sshContext){
        if(Objects.nonNull(cmdLineNumMap.get(sshContext.getHostIp()))){
            AtomicInteger cmdLineNum = cmdLineNumMap.get(sshContext.getHostIp());
            return cmdLineNum.addAndGet(num);
        }else{
            cmdLineNumMap.put(sshContext.getHostIp(), new AtomicInteger(num));
            return num;
        }
    }

    public static int deductCmdLineNum(int num, SshContext sshContext){
        if(Objects.nonNull(cmdLineNumMap.get(sshContext.getHostIp()))){
            AtomicInteger cmdLineNum = cmdLineNumMap.get(sshContext.getHostIp());
            return cmdLineNum.addAndGet(-num);
        }else{
            cmdLineNumMap.put(sshContext.getHostIp(), new AtomicInteger(0));
            return 0;
        }
    }

    public static int getCmdLineNum(SshContext sshContext){
        if(Objects.nonNull(cmdLineNumMap.get(sshContext.getHostIp()))){
            AtomicInteger cmdLineNum = cmdLineNumMap.get(sshContext.getHostIp());
            return cmdLineNum.get();
        }else{
            cmdLineNumMap.put(sshContext.getHostIp(), new AtomicInteger(0));
            return 0;
        }
    }
}
