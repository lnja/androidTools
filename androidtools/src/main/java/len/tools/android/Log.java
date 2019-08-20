package len.tools.android;

import android.support.annotation.Nullable;

public class Log {

    private static boolean enableLog = true;
    private static String globalTag = null;
    private static int level = android.util.Log.INFO;

    public static void enableLog(boolean enableLog) {
        Log.enableLog = enableLog;
    }

    /**
     * @param globalTag Used to identify the source of a log message.
     * @param level     The message level you would like logged. the value MUST BE {@link android.util.Log#VERBOSE} , {@link android.util.Log#DEBUG} , {@link android.util.Log#INFO} , {@link android.util.Log#ERROR} , {@link android.util.Log#WARN} or {@link android.util.Log#ASSERT}
     **/
    public static void init(@Nullable String globalTag, int level) {
        Log.globalTag = globalTag;
        Log.level = level;
    }


    public static void v(String msg) {
        log(android.util.Log.VERBOSE, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        log(android.util.Log.VERBOSE, msg, tr);
    }

    public static void d(String msg) {
        log(android.util.Log.DEBUG, msg, null);
    }

    public static void d(String msg, Throwable tr) {
        log(android.util.Log.DEBUG, msg, tr);
    }

    public static void i(String msg) {
        log(android.util.Log.INFO, msg, null);
    }

    public static void i(String msg, Throwable tr) {
        log(android.util.Log.INFO, msg, tr);
    }

    public static void e(String msg) {
        log(android.util.Log.ERROR, msg, null);
    }

    public static void e(String msg, Throwable tr) {
        log(android.util.Log.ERROR, msg, tr);
    }

    public static void w(String msg) {
        log(android.util.Log.WARN, msg, null);
    }

    public static void w(String msg, Throwable tr) {
        log(android.util.Log.WARN, msg, tr);
    }

    public static void wtf(String msg) {
        log(android.util.Log.ASSERT, msg, null);
    }

    public static void wtf(String msg, Throwable tr) {
        log(android.util.Log.ASSERT, msg, tr);
    }

    /**
     * The default level of any tag is set to INFO(Android system).so VERBOSE and DEBUG will be log more detail messages than other
     */
    private static void log(int level, String msg, Throwable tr) {
        if (!enableLog || level < Log.level) return;
        switch (level) {
            case android.util.Log.VERBOSE:
                logDetail(android.util.Log.VERBOSE, msg, tr);
                break;
            case android.util.Log.DEBUG:
                logDetail(android.util.Log.DEBUG, msg, tr);
                break;
            case android.util.Log.INFO:
                android.util.Log.i(globalTag, msg, tr);
                break;
            case android.util.Log.WARN:
                android.util.Log.w(globalTag, msg, tr);
                break;
            case android.util.Log.ERROR:
                android.util.Log.e(globalTag, msg, tr);
                break;
            case android.util.Log.ASSERT:
                android.util.Log.wtf(globalTag, msg, tr);
                break;
            default:
                break;
        }
    }

    private static void logDetail(int level, String msg, Throwable tr) {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        int stacksLen = stacks.length;
        if (stacksLen > 4) {
            StringBuffer tag = new StringBuffer(globalTag);
            tag.append("-").append(stacks[3].getFileName().split("\\.")[0]);
            StringBuffer extendMsg = new StringBuffer(msg);
            extendMsg.append(" LINE:").append(stacks[3].getLineNumber());
            switch (level) {
                case android.util.Log.VERBOSE:
                    android.util.Log.v(tag.toString(), extendMsg.toString(), tr);
                    break;
                case android.util.Log.DEBUG:
                    android.util.Log.d(tag.toString(), extendMsg.toString(), tr);
                    break;
                case android.util.Log.INFO:
                    android.util.Log.i(tag.toString(), extendMsg.toString(), tr);
                    break;
                case android.util.Log.WARN:
                    android.util.Log.w(tag.toString(), extendMsg.toString(), tr);
                    break;
                case android.util.Log.ERROR:
                    android.util.Log.e(tag.toString(), extendMsg.toString(), tr);
                    break;
                case android.util.Log.ASSERT:
                    android.util.Log.wtf(tag.toString(), extendMsg.toString(), tr);
                    break;
                default:
                    break;
            }
        } else {
            switch (level) {
                case android.util.Log.VERBOSE:
                    android.util.Log.v(globalTag, msg, tr);
                    break;
                case android.util.Log.DEBUG:
                    android.util.Log.d(globalTag, msg, tr);
                    break;
                case android.util.Log.INFO:
                    android.util.Log.i(globalTag, msg, tr);
                    break;
                case android.util.Log.WARN:
                    android.util.Log.w(globalTag, msg, tr);
                    break;
                case android.util.Log.ERROR:
                    android.util.Log.e(globalTag, msg, tr);
                    break;
                case android.util.Log.ASSERT:
                    android.util.Log.wtf(globalTag, msg, tr);
                    break;
                default:
                    break;
            }
        }
    }

}