package com.test.taxitest.network.response;

import android.content.Context;
import android.util.Log;

import com.test.taxitest.network.cache.LimitedTimeDiskCache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
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
}
