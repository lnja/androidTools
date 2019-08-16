package len.tools.android;

public class Log {

    private static boolean enableLog = false;

    public static void d(String message) {
        getLog("d", message);
    }

    public static void i(String message) {
        getLog("i", message);

    }

    public static void e(String message) {
        getLog("e", message);

    }

    public static void v(String message) {
        getLog("v", message);

    }

    public static void w(String message) {
        getLog("w", message);

    }

    private static void getLog(String type, String message) {
        if (enableLog) {
            StringBuffer sb = new StringBuffer();
            StackTraceElement[] stacks = new Throwable().getStackTrace();
            int stacksLen = stacks.length;
            if (stacksLen > 3) {
                sb.append(" LINE:").append(stacks[2].getLineNumber());
                switch (type) {
                    case "d":
                        android.util.Log.d(stacks[2].getFileName().split("\\.")[0],
                                message + sb.toString());
                        break;
                    case "i":
                        android.util.Log.i(stacks[2].getFileName().split("\\.")[0],
                                message + sb.toString());
                        break;
                    case "e":
                        android.util.Log.e(stacks[2].getFileName().split("\\.")[0],
                                message + sb.toString());
                        break;
                    case "v":
                        android.util.Log.v(stacks[2].getFileName().split("\\.")[0],
                                message + sb.toString());
                        break;
                    case "w":
                        android.util.Log.w(stacks[2].getFileName().split("\\.")[0],
                                message + sb.toString());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static void enableLog(boolean enableLog) {
        Log.enableLog = enableLog;
    }

}