package com.test.taxitest.network;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.taxitest.BuildConfig;
import com.test.taxitest.model.Order;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkFactory {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    public static final Gson GSON = new GsonBuilder()
            .setDateFormat(Order.DATE_FORMAT_PATTERN)
            .create();

    private static OkHttpClient CLIENT;

    @NonNull
    public static NetworkService getNetworkService() {
        return buildRetrofit().create(NetworkService.class);
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .build();
    }

    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = CLIENT;
        if (client == null) {
            synchronized (NetworkFactory.class) {
                client = CLIENT;
                if (client == null) {
                    client = CLIENT = buildClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
    }
}
