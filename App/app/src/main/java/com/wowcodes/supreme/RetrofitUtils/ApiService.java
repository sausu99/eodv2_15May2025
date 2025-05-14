// The code you provided is defining an interface called `ApiService` in the package
// `com.wowcodes.prizex.RetrofitUtils`. This interface is used for making API calls using Retrofit
// library in Java.
package com.wowcodes.supreme.RetrofitUtils;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String API_KEY = "Q2xpZW50X0lkXzFlZDJhN2Q5LTlmYjUtNGVmZi1hOTJhLWYwNjY2NDNiMGMyMDpDbGllbnRfU2VjcmV0X0NheW44bXgxd3k4TmNJTU11Znh5ak0rMVl4eVQrWFBkMnhvUUlSMEZCY2s9";
    @Headers({
            "Authorization: "+API_KEY,
            "Content-Type: application/json"
    })
    @POST("charge?return_existing=true")
    Call<ChargeResponse> createCharge(@Body ChargeRequest chargeRequest);
    @Headers({
            "Authorization: "+API_KEY,
            "Content-Type: application/json"
    })
    @GET("charge/{id}")
    Call<ChargeResponse> getCharge(@Path("id") String chargeId);










}
