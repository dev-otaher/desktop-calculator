class ThreadUtil {
    public static void printIfDaemon(Thread thread) {
        // implement logic
        System.out.println(thread.isDaemon() ? "daemon" : "not daemon");
    }
}