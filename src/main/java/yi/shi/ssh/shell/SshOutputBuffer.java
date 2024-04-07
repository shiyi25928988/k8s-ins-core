package yi.shi.ssh.shell;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class SshOutputBuffer {
    private static Map<String, StringBuffer> bufferMap = new ConcurrentHashMap<>();

    public static void append(String host, Object object){
        if(Objects.isNull(bufferMap.get(host))){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(object);
            bufferMap.put(host, stringBuffer);
        }else{
            bufferMap.get(host).append(object);
        }
    }

    public static String getConsoles(String host){
        if(Objects.isNull(bufferMap.get(host))){
            return "";
        }else{
            return bufferMap.get(host).toString();
        }
    }

    public static void clear(String host){
        if(Objects.isNull(bufferMap.get(host))){
            return;
        }else{
            bufferMap.get(host).setLength(0);
        }
    }

    public static void clear(){
        bufferMap.clear();
    }

    public static void asyncOutput(String host){

        Runnable runnable = () -> {
            while(true){
                System.out.println(SshOutputBuffer.getConsoles(host));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

    }
}
