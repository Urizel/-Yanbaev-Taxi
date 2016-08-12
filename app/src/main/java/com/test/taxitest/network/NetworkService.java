package com.test.taxitest.network;


import com.test.taxitest.model.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkService {

    @GET("orders.json")
    Call<List<Order>> orders();

    @GET("images/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);
}
