package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.GetReferrals;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralsActivity extends AppCompatActivity {

    ListView referralview;
    ImageView imgbackk;
    LinearLayout noreferrals;
    TextView yourreferrals;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_referrals);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(getText(R.string.string367));
        ImageView imgBackk=findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent p = getIntent();
        userid = p.getStringExtra("userid");
        noreferrals = findViewById(R.id.noreferrals);
        yourreferrals = findViewById(R.id.yourreferrals);
        imgbackk = findViewById(R.id.imgBackk);
        referralview = findViewById(R.id.referralView);
        imgbackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        BindingService apiService = RetrofitVideoApiBaseUrl.getService();
        Call<GetReferrals> call = apiService.getReferrals(userid);
        call.enqueue(new Callback<GetReferrals>() {
            @Override
            public void onResponse(Call<GetReferrals> call, Response<GetReferrals> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(!response.body().toString().equals("ClassPojo [JSON_DATA = []]")) {
                            noreferrals.setVisibility(View.GONE);
                            referralview.setAdapter(new referralAdapter(getApplicationContext(), response.body().getJSON_DATA()));
                        }
                    }
                } else {
                    Toast.makeText(ReferralsActivity.this, "Error: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override public void onFailure(Call<GetReferrals> call, Throwable t) {
                Toast.makeText(ReferralsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}