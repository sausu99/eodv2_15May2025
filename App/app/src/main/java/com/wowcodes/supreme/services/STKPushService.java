// The code you provided is defining an interface called `STKPushService` in the
// `com.wowcodes.prizex.services` package.
package com.wowcodes.supreme.services;
import com.wowcodes.supreme.model.AccessToken;
import com.wowcodes.supreme.model.STKPush;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface STKPushService {

    @POST("mpesa/stkpush/v1/processrequest")
    Call<STKPush.Mpesa_Push_Response> sendPush(@Body STKPush stkPush);

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken();

}