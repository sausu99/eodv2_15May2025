/**
 * The ReferActivity class is an Android activity that allows users to refer friends and share their
 * referral code.
 */
package com.wowcodes.supreme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ReferActivity extends Activity {
    TextView txtRefercode, txtShare;
    ImageView imgBackk;
    TextView txtAucname;
    LinearLayout lvlEarn;
    String code;
    BindingService videoService;
    private SavePref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(ReferActivity.this);
        if(savePref.getLang() == null)
            savePref.setLang("en");
        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());

        setContentView(R.layout.activity_refer);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtRefercode = findViewById(R.id.txtRefercode);
        txtShare = findViewById(R.id.txtShare);
        lvlEarn = findViewById(R.id.linearlay);
        imgBackk = (ImageView) findViewById(R.id.imgBackk);
        txtAucname = (TextView) findViewById(R.id.txtAucname);
        txtAucname.setText(R.string.fifthtittle2);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String sAux = getText(R.string.string335) + code + "\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception ignore) {}
            }
        });

        getprofile();
    }

    public void getprofile() {
        lvlEarn.setVisibility(View.VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        lvlEarn.setVisibility(View.GONE);
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtRefercode.setText(arrayList.get(0).getCode());
                        code = arrayList.get(0).getCode();
                    } catch (Exception e) {
                        lvlEarn.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlEarn.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }
}