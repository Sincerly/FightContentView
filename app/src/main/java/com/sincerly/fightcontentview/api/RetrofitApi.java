package com.sincerly.fightcontentview.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/4/15 0015.
 */

public interface RetrofitApi {

    @GET
    Call<ResponseBody> getText1(@Url String url);
}
