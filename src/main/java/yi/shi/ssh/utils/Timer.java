package yi.shi.ssh.utils;

public class Timer {

    static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static long end() {
        return System.currentTimeMillis() - startTime;
    }

    public static String getConsumedTime() {
        return String.valueOf(end()/1000) + "s";
    }
}
