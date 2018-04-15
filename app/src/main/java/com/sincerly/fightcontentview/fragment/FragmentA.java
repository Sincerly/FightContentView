package com.sincerly.fightcontentview.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sincerly.fightcontentview.R;
import com.sincerly.fightcontentview.api.RetrofitApi;
import com.sincerly.fightcontentview.config.AppConfig;
import com.sincerly.fightcontentview.lottery.LottoTrendActivity;
import com.sincerly.fightcontentview.ui.DDTrendChart;
import com.sincerly.fightcontentview.ui.LottoTrendView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/15 0015.
 */

public class FragmentA extends Fragment {
    private LottoTrendView mTrendView;
    final int maxSignleNum = 9;
    private DDTrendChart mTrendChart;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        initChart(view);
        getText();
        return view;
    }

    private void initChart(View view) {
        this.mTrendView = (LottoTrendView) view.findViewById(R.id.ltv_trendView);
        this.mTrendChart = new DDTrendChart(getActivity(), this.mTrendView);
        this.mTrendView.setChart(this.mTrendChart);
        this.mTrendChart.setShowYilou(true);
        this.mTrendChart.setDrawLine(true);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            mTrendChart.updateData("01", (ArrayList) paramMessage.obj);
        }
    };

    private void refresh() {
        String url = "http://23.252.161.83:8666/shishicai/ajax?ajaxhandler=getcqsscawarddata&t=0.5901237616961141";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Log.e("tag", "5");
            }
        });
    }

    private void getText() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://data.917500.cn");
        RetrofitApi retrofit = retrofitBuilder
                .client(new OkHttpClient())
                .build().create(RetrofitApi.class);

        retrofit2.Call<ResponseBody> call = retrofit.getText1("cqssc_2000.txt");
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    ResponseBody is = response.body();
                    InputStream inputStream = is.byteStream();

                    File file = new File(AppConfig.PATH, "text1.txt");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
