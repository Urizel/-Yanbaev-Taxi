package com.test.taxitest.network.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.test.taxitest.network.NetworkFactory;
import com.test.taxitest.network.NetworkService;
import com.test.taxitest.network.cache.LimitedTimeDiskCache;
import com.test.taxitest.network.response.ImageResponse;
import com.test.taxitest.network.response.Response;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ImageLoader extends AsyncTaskLoader<Response> {

    public static final String ARG_IMAGE_NAME = "image_name";

    public static Bundle getBundle(String imageName) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_IMAGE_NAME, imageName);
        return arguments;
    }

    private String imageName;

    public ImageLoader(Context context, Bundle arguments) {
        super(context);
        imageName = arguments.getString(ARG_IMAGE_NAME);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Response loadInBackground() {
        try {
            return apiCall();
        } catch (IOException e) {
            return new ImageResponse();
        }
    }

    protected Response apiCall() throws IOException {
        NetworkService service = NetworkFactory.getNetworkService();
        Call<ResponseBody> call = service.getImage(imageName);
        String url = call.request().url().uri().toString();
        Bitmap bitmap = LimitedTimeDiskCache.getInstance(getContext()).get(url);
        if (bitmap != null) {
            return new ImageResponse()
                    .setRequestResult(Response.OK)
                    .setAnswer(bitmap);
        }
        retrofit2.Response<ResponseBody> responseResult = call.execute();
        @Response.RequestResult int code = Response.code(responseResult.code());
        ResponseBody body = responseResult.body();
        if (body == null) {
            return new ImageResponse();
        }
        Response response = new ImageResponse()
                .setRequestResult(code);
        if (response.save(getContext(), body, url)) {
            bitmap = LimitedTimeDiskCache.getInstance(getContext()).put(url);
        }
        return response.setAnswer(bitmap);
    }
}