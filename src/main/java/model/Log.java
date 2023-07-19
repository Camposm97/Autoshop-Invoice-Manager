package model;

public class Log {
    public static void debug(Object arg) {
        StackTraceElement[] arr = Thread.currentThread().getStackTrace();
        if (arr.length >= 4) {
            StackTraceElement caller = arr[3];
            String className = caller.getClassName();
            String methodName = caller.getMethodName();
            System.err.printf("DEBUG: %s/%s: %s\n", className, methodName, arg);
        } else System.err.println("DEBUG: ERR");
    }

    public static void info(Object arg) {
        StackTraceElement[] arr = Thread.currentThread().getStackTrace();
        if (arr.length >= 4) {
            StackTraceElement caller = arr[3];
            String className= caller.getClassName();
            String methodName = caller.getMethodName();
            System.out.printf("INFO: %s/%s: %s\n", className, methodName, arg);
        } else System.out.println("INFO: ERR");
    }
}
