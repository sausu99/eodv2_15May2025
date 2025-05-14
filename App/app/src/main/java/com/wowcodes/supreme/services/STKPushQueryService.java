// The code snippet is defining a Java interface called `STKPushQueryService` that declares two methods
// for making API requests using Retrofit library.
package com.wowcodes.supreme.services;
import com.wowcodes.supreme.model.AccessToken;
import com.wowcodes.supreme.model.STKPushQuery;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface STKPushQueryService {
    @POST("mpesa/stkpushquery/v1/query")
    Call<STKPushQuery.Mpesa_Push_Response> sendPushQuery(@Body STKPushQuery stkPushQuery);
    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken();
}