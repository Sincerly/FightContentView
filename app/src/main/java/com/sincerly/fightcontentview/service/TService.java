package com.sincerly.fightcontentview.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sincerly.fightcontentview.api.RetrofitApi;
import com.sincerly.fightcontentview.bean.DataSource;
import com.sincerly.fightcontentview.config.AppConfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class TService extends Service {

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}


	/**
	 * 获取重庆的
	 */
	private void getText1() {
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl("http://data.917500.cn/");
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
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
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


	/**
	 * 获取新疆的
	 */
	private void getText2() {
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl("http://data.917500.cn/");
		RetrofitApi retrofit = retrofitBuilder
				.client(new OkHttpClient())
				.build().create(RetrofitApi.class);


		retrofit2.Call<ResponseBody> call = retrofit.getText1("xjssc_2000.txt");
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				try {
					ResponseBody is = response.body();
					InputStream inputStream = is.byteStream();

					File file = new File(AppConfig.PATH, "text2.txt");
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
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


	private void getText() {
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl("http://data.917500.cn/");
		RetrofitApi retrofit = retrofitBuilder
				.client(new OkHttpClient())
				.build().create(RetrofitApi.class);


		retrofit2.Call<ResponseBody> call = retrofit.getText1("tjssc_2000.txt");
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				try {
					ResponseBody is = response.body();
					InputStream inputStream = is.byteStream();

					File file = new File(AppConfig.PATH, "text3.txt");
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
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
