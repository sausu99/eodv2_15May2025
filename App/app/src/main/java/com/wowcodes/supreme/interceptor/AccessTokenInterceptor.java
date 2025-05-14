/**
 * The AccessTokenInterceptor class is an OkHttp interceptor that adds an Authorization header to the
 * request with a Basic authentication token.
 */
package com.wowcodes.supreme.interceptor;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {
    public AccessTokenInterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String Consumer_key = MainActivity.mpesa_key;
        String Consumer_secret = MainActivity.mpesa_code;
        String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;
String test = "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP);
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);
    }
}