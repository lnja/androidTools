package len.tools.android;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Provides application storage files and paths
 */
public final class StorageUtils {

    private StorageUtils() {
    }

    /**
     * Returns the file which the absolute path to the application specific cache directory.
     * where the cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted.
     * Else - Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return cacheFile {@link File File},which holding application cache files
     */
    public static File getCacheDir(Context context) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    /**
     * Returns the file which the absolute path to the directory on the SD card or on the filesystem,
     * where the "files" directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/files")</i> if card is mounted.
     * Else - Android defines “files” directory on device's file system.
     *
     * @param context Application context
     * @return filesFile {@link File File},which holding application files.
     */
    public static File getFilesDir(Context context) {
        File appFilesDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            appFilesDir = getExternalFilesDir(context);
        }
        if (appFilesDir == null) {
            appFilesDir = context.getFilesDir();
        }
        return appFilesDir;
    }

    /**
     * Returns the file which the absolute path to the application specific cache's custom directory.
     * where the cache's custom directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache/customDirName")</i> if card is mounted.
     * Else - Android defines cache directory on device's file system.
     *
     * @param context       Application context
     * @param customDirName custom directory name
     * @return cacheCustomFile {@link File File},which holding application cache's custom files.
     */
    public static File getCacheCustomDir(Context context,
                                         String customDirName) {
        File cacheDir = getCacheDir(context);
        File cacheCustomDir = new File(cacheDir, customDirName);
        if (!cacheCustomDir.exists()) {
            if (!cacheCustomDir.mkdirs()) {
                cacheCustomDir = cacheDir;
            }
        }
        return cacheCustomDir;
    }

    /**
     * Returns the file which the absolute path to the directory on the SD card or on the filesystem,
     * where the "files" custom directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/files/customDirName")</i> if card is mounted.
     * Else - as a child file in the "files" directory on device's file system defined by Android.
     *
     * @param context       Application context
     * @param customDirName custom directory name
     * @return filesCustomFile {@link File File},which holding application custom files.
     */
    public static File getFilesCustomDir(Context context,
                                         String customDirName) {
        File filesDir = getFilesDir(context);
        File filesCustomDir = new File(filesDir, customDirName);
        if (!filesCustomDir.exists()) {
            if (!filesCustomDir.mkdirs()) {
                filesCustomDir = filesDir;
            }
        }
        return filesCustomDir;
    }

    /**
     * Returns the file which the absolute path to the application specific extend directory.
     * where the extend file directory will be created on
     * SD card <i>("/Android/data/[app_package_name]/extendDirName")</i> if card is mounted.
     * Else - as a child file in the file return by {@link #getFilesCustomDir(Context, String)}
     *
     * @param context       Application context
     * @param extendDirName extend directory name
     * @return extendFile {@link File file},which holding application extend files.
     */
    public static File getExtendDir(Context context, String extendDirName) {
        File appExtendDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            appExtendDir = getExternalDir(context, extendDirName);
        }
        if (appExtendDir == null) {
            appExtendDir = getFilesCustomDir(context, extendDirName);
        }
        return appExtendDir;
    }

    /**
     * Returns the file which the absolute path to the application specific extend directory,
     * and which will not be deleted when the application uninstalled.
     * where the extend file directory will be created on
     * SD card <i>("/lnja/chat/image")</i>(e.g customPath = "lnja/chat/image") if card is mounted.
     * Else - will not be create on SD card.
     *
     * @param customPath the absolute custom path,e.g customPath = "lnja/chat/image"
     * @return extendFile {@link File file},which holding application extend files.
     */
    public static File getExtendDir(String customPath) {
        File appExtendDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), customPath);
        if (appExtendDir != null && !appExtendDir.exists()) {
            if (!appExtendDir.mkdirs()) {
                return null;
            }
        }
        return appExtendDir;
    }

    private static File getExternalCacheDir(Context context) {
        return getExternalDir(context, "cache");
    }

    private static File getExternalFilesDir(Context context) {
        return getExternalDir(context, "files");
    }

    private static File getExternalDir(Context context, String customDirName) {
        File dataDir = new File(new File(
                Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCustomDir = new File(
                new File(dataDir, context.getPackageName()), customDirName);
        if (!appCustomDir.exists()) {
            if (!appCustomDir.mkdirs()) {
                Log.w("StorageUtils",
                        "Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCustomDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.w("StorageUtils",
                        "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCustomDir;
    }
}
