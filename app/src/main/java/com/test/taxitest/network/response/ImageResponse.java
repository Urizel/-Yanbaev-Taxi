package com.test.taxitest.network.response;

import android.content.Context;
import android.util.Log;

import com.test.taxitest.network.cache.LimitedTimeDiskCache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class ImageResponse extends Response {

    private final static String TAG = ImageResponse.class.getSimpleName();

    @Override
    public boolean save(Context context, ResponseBody body, String url) {
        try {
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(LimitedTimeDiskCache.getInstance(context).getFileName(url));
                int c;
                long start = System.nanoTime();
                // XXX Per-byte write
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                long end = System.nanoTime();
                Log.e(TAG, "Saved image in " + (end - start));

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                // XXX Causes result to be lost
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "SAVE FILE " + e.toString());
            return false;
        }
        return true;
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long totalCount = 0;

        int bytesRead;
        while (-1 != (bytesRead = input.read(buffer))) {
            output.write(buffer, 0, bytesRead);
            totalCount += bytesRead;
        }

        // Done
        return totalCount;
    }
}
