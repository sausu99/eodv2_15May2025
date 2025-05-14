package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.CoinAdapter;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PurchaseCoinsActivity extends AppCompatActivity {
    BindingService videoService;
    RecyclerView recycler;
    TextView cur_bal,aucname,rate;
    LinearLayout amt200,amt500,nodata,submit;
    EditText amt;
    ImageView imgBackk;
    SavePref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        setContentView(R.layout.activity_purchase_coins);

        cur_bal=findViewById(R.id.bal);
        aucname=findViewById(R.id.txtAucname);
        amt200=findViewById(R.id.amt200);
        amt500=findViewById(R.id.amt500);
        amt=findViewById(R.id.amt);
        imgBackk=findViewById(R.id.imgBackk);
        recycler=findViewById(R.id.offers);
        nodata=findViewById(R.id.nodata);
        submit=findViewById(R.id.submit);
        rate=findViewById(R.id.rate);
        savePref=new SavePref(this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        aucname.setText(getResources().getString(R.string.purchase_coins));
        rate.setText(getString(R.string.coinvalue)+" "+MainActivity.currency+MainActivity.coinvalue);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                    cur_bal.setText(arrayList.get(0).getWallet());
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}

        try {
            ArrayList<GetCoin.Get_Coin_Inner> arrayList=StaticData.coinfragmentList;
            if(arrayList.isEmpty()){
                recycler.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
            else {
                recycler.setVisibility(View.VISIBLE);
                recycler.setAdapter(new CoinAdapter(this,arrayList));
                nodata.setVisibility(View.GONE);
            }
        } catch (Exception ignore) {}

        amt200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amt.setText("200");
            }
        });

        amt500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amt.setText("500");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (TextUtils.isEmpty(amt.getText().toString()) || Integer.parseInt(amt.getText().toString())==0) {
                        Toast.makeText(PurchaseCoinsActivity.this, getString(R.string.enter_amt), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PurchaseCoinsActivity.this, RazorpayActivity.class);
                        intent.putExtra("activity", "PurchaseCoin");
                        intent.putExtra("amount", String.valueOf(Integer.parseInt(amt.getText().toString())*Double.parseDouble(MainActivity.coinvalue)));
                        intent.putExtra("coins", amt.getText().toString());
                        startActivity(intent);
                    }
                }catch (Exception ignore){}
            }
        });
    }
}