package com.test.taxitest.network.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.test.taxitest.model.Order;
import com.test.taxitest.model.comparator.OrderDescendComparator;
import com.test.taxitest.network.NetworkFactory;
import com.test.taxitest.network.NetworkService;
import com.test.taxitest.network.response.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

public class OrdersLoader extends AsyncTaskLoader<Response> {

    public OrdersLoader(Context context) {
        super(context);
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
            return new Response();
        }
    }

    // XXX protected
    protected Response apiCall() throws IOException {
        NetworkService service = NetworkFactory.getNetworkService();
        Call<List<Order>> call = service.orders();
        retrofit2.Response<List<Order>> responseResult = call.execute();
        @Response.RequestResult int code = Response.code(responseResult.code());
        List<Order> orders = responseResult.body();
        if (orders != null) {
            Collections.sort(orders, new OrderDescendComparator());
        }
        return new Response()
                .setRequestResult(code)
                .setAnswer(orders);
    }
}