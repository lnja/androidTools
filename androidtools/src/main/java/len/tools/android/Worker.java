package len.tools.android;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class Worker {
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private String name;
    private volatile boolean isStarted = false;

    public Worker(String name) {
        this.name = name;
        initHandlerThread();
    }

    private void initHandlerThread() {
        mHandlerThread = new HandlerThread("name");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj instanceof Runnable) {
                    work(((Runnable) msg.obj));
                }
            }
        };
        isStarted = true;
    }

    protected void work(Runnable runnable) {
        runnable.run();
    }

    public boolean sendTask(Runnable task) {
        if (!isStarted) {
            Log.w(name + "is quited,task is not be executed!");
            return false;
        }
        return mHandler.sendMessage(mHandler.obtainMessage(0, task));
    }

    public boolean sendTaskToFront(Runnable task) {
        if (!isStarted) {
            Log.w(name + "is quited,task is not be executed!");
            return false;
        }
        return mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(0, task));
    }

    public boolean sendTaskDelayed(Runnable task, long delayMillis) {
        if (!isStarted) {
            Log.w(name + "is quited,task is not be executed!");
            return false;
        }
        return mHandler.sendMessageDelayed(mHandler.obtainMessage(0, task), delayMillis);
    }

    public void quit() {
        if (!isStarted) {
            Log.w(name + "is quited!");
        }
        mHandlerThread.interrupt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mHandlerThread.quitSafely();
        } else {
            mHandlerThread.quit();
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
