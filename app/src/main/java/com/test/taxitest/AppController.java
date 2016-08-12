package com.test.taxitest;

import android.app.Application;

import com.test.taxitest.network.cache.LimitedTimeDiskCache;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // XXX Cleaning files when low on RAM?
        LimitedTimeDiskCache.getInstance(getApplicationContext()).clear();
    }
}
