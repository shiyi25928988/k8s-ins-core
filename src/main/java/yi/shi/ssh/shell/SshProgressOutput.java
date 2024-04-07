package yi.shi.ssh.shell;

import yi.shi.ssh.cmd.CommandLineNumCount;

public class SshProgressOutput {


    static int maxIndex = 100;    //控制输出的进度条宽度

//    public static void main(String[] args) {
//        begin();
//    }

    public static void asyncOutput(int total, SshContext sshContext){
        new Thread(()->{
            begin(total, sshContext);
        }).start();
    }

    private static void begin(int total, SshContext sshContext){

        StringBuffer kg = new StringBuffer();
        for(int i=0;i<maxIndex;i++){
            kg.append(" ");
        }
        System.out.println("kubernetes install progress:");
        System.out.print("00%[>"+kg.toString()+"]");

        boolean flag = true;
        while (flag){
            printCurrentNum(100-CommandLineNumCount.getCmdLineNum(sshContext)*100/total);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void focusGoto(){
        for(int i=maxIndex+6;i>0;i--){
            System.out.print('\b');
        }
    }

    private static void printCurrentNum(int i) {
        System.out.print('\r');
        String num = "000"+i;
        num = num.substring(num.length()-3);
        StringBuffer s = new StringBuffer(num+"%[");
        focusGoto();
        int prec = (i*100)/100;
        for(int index=0;index<maxIndex;index++){
            int c = (index*100)/maxIndex;
            if(c<prec){
                s.append("|");
            }else{
                s.append(" ");
            }
        }
        s.append("]");
        System.out.print(s.toString());
    }

}
