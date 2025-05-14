package com.wowcodes.supreme.Activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Adapter.CoinAdapter;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class GetCoinActivity extends AppCompatActivity {
    RecyclerView recyclerGetCoin;
    TextView txtGetCoin,txtGetCoinHis;
    LinearLayout lvlGetCoin;
    TextView txtShare;
    SavePref savePref;
    TextView txtTran;
    String referalCode;
    BindingService videoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.fragment_get_coin);

        txtTran =findViewById(R.id.text_tran);
        txtGetCoin=findViewById(R.id.txtGetCoin);
        txtShare=findViewById(R.id.txtShare);
        txtGetCoinHis=findViewById(R.id.txtGetCoinHis);
        savePref=new SavePref(GetCoinActivity.this);
        ImageView imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(R.string.string119);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerGetCoin =findViewById(R.id.recycler);
        lvlGetCoin =findViewById(R.id.linearlay);
        recyclerGetCoin.setLayoutManager(new LinearLayoutManager(GetCoinActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetCoinActivity.this, WalletPassbookActivity.class);
                startActivity(i);

            }
        });

        txtGetCoinHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GetCoinActivity.this, GetCoinTraActivity.class);
                startActivity(i);
            }
        });

        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String sAux = "Invite your friends and share code  " + referalCode + "\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        getcoinapi();
        getprofile();
    }

    public void getcoinapi() {
        lvlGetCoin.setVisibility(View.VISIBLE);
        try {
            callcoinApi().enqueue(new Callback<GetCoin>() {
                @Override
                public void onResponse(Call<GetCoin> call, retrofit2.Response<GetCoin> response) {
                    try {
                        lvlGetCoin.setVisibility(View.GONE);
                        ArrayList<GetCoin.Get_Coin_Inner> arrayList = response.body().getJSON_DATA();
                        recyclerGetCoin.setAdapter(new CoinAdapter(GetCoinActivity.this, arrayList));
                    }catch (Exception e){
                        lvlGetCoin.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetCoin> call, Throwable t) {
                    lvlGetCoin.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<GetCoin> callcoinApi() {
        return videoService.get_coin_list();
    }

    public void getprofile() {
        lvlGetCoin.setVisibility(View.VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        lvlGetCoin.setVisibility(View.GONE);
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtGetCoin.setText(arrayList.get(0).getWallet() + getText(R.string.string46) );
                        referalCode =arrayList.get(0).getCode();
                    }catch (Exception e){
                        lvlGetCoin.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlGetCoin.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
}