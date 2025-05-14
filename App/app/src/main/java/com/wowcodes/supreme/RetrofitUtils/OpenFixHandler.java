/**
 * The OpenFixHandler class is a Retrofit client that handles API requests to create and retrieve
 * charges from the OpenPix API.
 */
package com.wowcodes.supreme.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenFixHandler {
    private static final String BASE_URL = "https://api.openpix.com.br/api/v1/";

    private ApiService apiService;

    public OpenFixHandler() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void createCharge(String correlationID, int value, String comment, Callback<ChargeResponse> callback) {
        ChargeRequest chargeRequest = new ChargeRequest(correlationID, value, comment);
        Call<ChargeResponse> call = apiService.createCharge(chargeRequest);
        call.enqueue(callback);
    }

    public void getCharge(String chargeId, Callback<ChargeResponse> callback) {
        Call<ChargeResponse> call = apiService.getCharge(chargeId);
        call.enqueue(callback);
    }


}
