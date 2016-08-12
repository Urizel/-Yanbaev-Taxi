package com.test.taxitest.network.response;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.ResponseBody;

public class Response {
    @Nullable private Object mAnswer;

    public static final int ERROR_CONNECTION = 0;
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;
    @IntDef({OK, ERROR_CONNECTION, CREATED, BAD_REQUEST, UNAUTHORIZED, NOT_FOUND, SERVER_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestResult {}

    public static @RequestResult int code(int code) {
        if (code == Response.CREATED) {
            return Response.CREATED;
        }
        if (Response.isBetween(code, 200, 299)) {
            return Response.OK;
        }
        if (Response.isBetween(code, 500, 599)) {
            return Response.SERVER_ERROR;
        }
        if (code == Response.UNAUTHORIZED) {
            return Response.UNAUTHORIZED;
        }
        if (code == Response.NOT_FOUND) {
            return Response.NOT_FOUND;
        }
        if (Response.isBetween(code, 400, 499)) {
            return Response.BAD_REQUEST;
        }
        return Response.ERROR_CONNECTION;
    }

    private @RequestResult int mRequestResult;

    public Response() {
        mRequestResult = ERROR_CONNECTION;
    }

    public @RequestResult int getRequestResult() {
        return mRequestResult;
    }

    public Response setRequestResult(@RequestResult int requestResult) {
        mRequestResult = requestResult;
        return this;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public boolean save(Context context, ResponseBody body, String url) {
        return true;
    }

    @Nullable
    public <T> T getTypedAnswer() {
        if (mAnswer == null) {
            return null;
        }
        //noinspection unchecked
        return (T) mAnswer;
    }

    @NonNull
    public Response setAnswer(@Nullable Object answer) {
        mAnswer = answer;
        return this;
    }
}
