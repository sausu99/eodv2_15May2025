/**
 * The class `RetrofitVideoApiBaseUrl` is responsible for creating and configuring a Retrofit instance
 * with a base URL for making API calls.
 */
package com.wowcodes.supreme.RetrofitUtils;

import static com.wowcodes.supreme.Constants.retrobaseurl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitVideoApiBaseUrl {

    private static Retrofit retrofit = null;

    private static OkHttpClient buildClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }


    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(retrobaseurl)
                    .build();
        }
        return retrofit;
    }
    public static BindingService getService() {
        return new Retrofit.Builder()
                .client(buildClient())
                .baseUrl(retrobaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BindingService.class);
    }
}