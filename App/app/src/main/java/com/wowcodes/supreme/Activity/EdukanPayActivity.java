package com.wowcodes.supreme.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
public class EdukanPayActivity extends AppCompatActivity {
    WebView web;
    Button butt;
    SavePref savePref;
    BindingService videoService;
    EditText name ,email,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edokanpay);
        name= findViewById(R.id.name);
        email= findViewById(R.id.email);
        amount= findViewById(R.id.amount);
        butt= findViewById(R.id.button);
        web= findViewById(R.id.web);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name .setVisibility(View.INVISIBLE);
                email .setVisibility(View.INVISIBLE);
                amount .setVisibility(View.INVISIBLE);
                butt .setVisibility(View.INVISIBLE);
                web.setVisibility(View.VISIBLE);
                web.setWebViewClient(new WebViewClient());
                web.getSettings().setJavaScriptEnabled(true);
                String loadUrl = "https://pay2.edokanpay.com/woocommerce_checkout.php?api=63cab36ec7ed0&client=63cab36ec8108&secret=1586664048&amount=\"+am+\"&position=http://android.liveblog365.com&success_url=https://pay2.edokanpay.com/android/after_success.php&cancel_url=https://pay2.edokanpay.com/android/cancled.php&cus_name=\"+na+\"&cus_email=\"+em+\"&done" ;
                web.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        if (url.equals("https://pay2.edokanpay.com/android/success.php")) {
                            Toast.makeText(EdukanPayActivity.this, "success", Toast.LENGTH_SHORT).show();
                            name .setVisibility(View.VISIBLE);
                            email .setVisibility(View.VISIBLE);
                            amount .setVisibility(View.VISIBLE);
                            butt .setVisibility(View.VISIBLE);
                            web.setVisibility(View.INVISIBLE);
                        } else if (url.equals("https://pay2.edokanpay.com/android/cancel.php")) {
                                Toast.makeText(EdukanPayActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();
                                name .setVisibility(View.VISIBLE);
                                email .setVisibility(View.VISIBLE);
                                amount .setVisibility(View.VISIBLE);
                                butt .setVisibility(View.VISIBLE);
                                web.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                web.loadUrl(loadUrl);
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void getprofile() {
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        name.setText(arrayList.get(0).getName());
                        email.setText(arrayList.get(0).getEmail());
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
}