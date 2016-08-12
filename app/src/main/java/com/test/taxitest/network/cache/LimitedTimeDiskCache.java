package com.test.taxitest.network.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.test.taxitest.network.cache.generator.FileNameGenerator;
import com.test.taxitest.network.cache.generator.Md5FileNameGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class LimitedTimeDiskCache {

    private final static String TAG = LimitedTimeDiskCache.class.getSimpleName();

    private final static long TIMEOUT = 10 * 60 * 1000;
    private final static String IMAGES_FOLDER = "/images";
    private volatile static LimitedTimeDiskCache instance;

    private FileNameGenerator fileNameGenerator;
    private final File cacheDir;
    private final HashMap<String, Long> loadingDates = new HashMap<>();

    public static LimitedTimeDiskCache getInstance(Context context) {
        LimitedTimeDiskCache diskCache = instance;
        if (diskCache == null) {
            // XXX double cache creation
            synchronized (LimitedTimeDiskCache.class) {
                diskCache = instance;
                if (diskCache == null) {
                    // XXX using inner cache
                    instance = new LimitedTimeDiskCache(new File(context.getCacheDir(), IMAGES_FOLDER), new Md5FileNameGenerator());
                }
            }
        }
        return instance;
    }

    protected LimitedTimeDiskCache(File cacheDir, FileNameGenerator fileNameGenerator) {
        this.cacheDir = cacheDir;
        this.fileNameGenerator = fileNameGenerator;
        // XXX IO on main thread
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                Log.d(TAG, "Unable to create cache dir "+cacheDir.getAbsolutePath());
            }
            return;
        }
        readLoadingDates();
    }

    public String getFileName(String url) {
        return new File(cacheDir, File.separator+fileNameGenerator.generate(url)).getAbsolutePath();
    }

    /*
    * Benchmark data
    * 1578334ns (1.6ms)
    * 1134531ns (1.1ms)
    * 445572ns  (0.4ms)
    * 100+files
    * 45093282ns(45ms)
    */
    private void readLoadingDates() {
        long start = System.nanoTime();
        File[] cachedFiles = cacheDir.listFiles();
        for (File cachedFile : cachedFiles) {
            if (cachedFile.isFile()) {
                String absolutePath = cachedFile.getAbsolutePath();
                String nameOfFile = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, absolutePath.length());
                // XXX using last modified https://code.google.com/p/android/issues/detail?id=1699
                if (System.currentTimeMillis() - cachedFile.lastModified() > TIMEOUT) {
                    if (!cachedFile.delete()) {
                        Log.d(TAG, "Unable to delete file "+cachedFile.getAbsolutePath());
                    }
                } else {
                    loadingDates.put(nameOfFile, cachedFile.lastModified());
                }
            }
        }
        long end = System.nanoTime();

        Log.e(TAG, "loading done in " + (end - start));
    }

    // XXX PUT?!
    public Bitmap put(String url) throws FileNotFoundException {
        synchronized (this) {
            String nameOfFile = fileNameGenerator.generate(url);
            File file = new File(cacheDir, nameOfFile);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            loadingDates.put(nameOfFile, file.lastModified());
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
    }

    public Bitmap get(String url) {
        synchronized (this) {
            String nameOfFile = fileNameGenerator.generate(url);
            Long loadingDate = loadingDates.get(nameOfFile);
            if (loadingDate == null) {
                return null;
            }
            File file = new File(cacheDir, nameOfFile);
            if (System.currentTimeMillis() - loadingDate > TIMEOUT) {
                if (file.exists()) {
                    if (!file.delete()) {
                        Log.d(TAG, "Unable to delete file "+file.getAbsolutePath());
                    }
                }
                loadingDates.remove(nameOfFile);
                return null;
            }
            if (!file.exists()) {
                loadingDates.remove(nameOfFile);
                return null;
            }
            long currentTime = System.currentTimeMillis();
            if (file.setLastModified(currentTime)) {
                loadingDates.put(nameOfFile, currentTime);
            }
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
    }

    public void clear() {
        synchronized (this) {
            loadingDates.clear();
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            Log.d(TAG, "Unable to delete file "+file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

}
