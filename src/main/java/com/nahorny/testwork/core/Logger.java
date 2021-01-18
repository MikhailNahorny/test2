package com.nahorny.testwork.core;

import java.time.format.DateTimeFormatter;

public final class Logger {
    /**
     * thread-safety logger
     * call: Logger.getLogger().log("message"); or Logger.getLogger().log("message", true); to write to error stream
     * Logger.getLogger().log("message", false); to write to out stream
     */
    private int counter;
    private static Logger logger;

    private Logger() {
        if (logger != null) throw new RuntimeException();
    }

    public void log(String str, boolean isErr) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        final String methodName = (stack[2].getMethodName().equalsIgnoreCase("log")) ? stack[3].getMethodName() : stack[2].getMethodName();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
        String dateStr = java.time.LocalTime.now().format(dtf);
        dateStr = dateStr.substring(0, dateStr.indexOf(' ')) + ":" + dateStr.substring(dateStr.indexOf(' ') + 1);
        String threadName = Thread.currentThread().getName();
        if (isErr) {
            System.err.printf("%d) %s [%s] [%s]: %s\n", ++counter, dateStr, threadName, methodName, str);
        } else {
            System.out.printf("%d) %s [%s] [%s]: %s\n", ++counter, dateStr, threadName, methodName, str);
        }
    }

    public void log(String str){
        log(str, true);
    }

    public static Logger getLogger() {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) logger = new Logger();
            }
        }
        return logger;
    }

}
