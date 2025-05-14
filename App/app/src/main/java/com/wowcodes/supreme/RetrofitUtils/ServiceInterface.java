// The code provided is defining an interface called `ServiceInterface` in the package
// `com.wowcodes.prizex.RetrofitUtils`. This interface is used for making API calls using Retrofit
// library in Java.
package com.wowcodes.supreme.RetrofitUtils;



import com.wowcodes.supreme.Modelclas.Token_Res;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

    // method,, return type ,, secondary url
    @Multipart
    @POST("paytmallinone/init_Transaction.php")
    Call<Token_Res> generateTokenCall(
            @Part("code") RequestBody language,
            @Part("MID") RequestBody mid,
            @Part("ORDER_ID") RequestBody order_id,
            @Part("AMOUNT") RequestBody amount
    );


}