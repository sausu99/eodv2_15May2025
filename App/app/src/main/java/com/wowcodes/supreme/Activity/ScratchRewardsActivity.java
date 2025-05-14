package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowcodes.supreme.Adapter.ScratchRewardsAdapter;
import com.wowcodes.supreme.Modelclas.GetConsolation;
import com.wowcodes.supreme.Modelclas.GetProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ScratchRewardsActivity extends AppCompatActivity {
    ImageView imgBackk;
    TextView txtAucname,total;
    RecyclerView scratchCardsRecycler;
    BindingService videoService;
    LinearLayout noitems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_rewards);
        imgBackk=findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtAucname=findViewById(R.id.txtAucname);
        total=findViewById(R.id.total);
        noitems=findViewById(R.id.noitems);
        scratchCardsRecycler=findViewById(R.id.scratchCards_recycler);
        scratchCardsRecycler.setLayoutManager(new GridLayoutManager(this,2));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        get_wallet();
        get_cards();
    }

    public void get_wallet(){
        try {
            videoService.get_profile(new SavePref(ScratchRewardsActivity.this).getUserId()).enqueue(new Callback<GetProfile>() {
                @Override
                public void onResponse(Call<GetProfile> call, retrofit2.Response<GetProfile> response) {
                        total.setText(response.body().getJSON_DATA().get(0).getCoins_earned());
                }
                @Override public void onFailure(Call<GetProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    public void get_cards(){
        try {
            videoService.get_consolation(new SavePref(ScratchRewardsActivity.this).getUserId()).enqueue(new Callback<GetConsolation>() {
                @Override
                public void onResponse(Call<GetConsolation> call, retrofit2.Response<GetConsolation> response) {
                    ArrayList<GetConsolation.Get_consolation_Inner> arrayList=response.body().getJSON_DATA();

                    if(arrayList.size()>0) {
                        scratchCardsRecycler.setAdapter(new ScratchRewardsAdapter(ScratchRewardsActivity.this, arrayList));
                        noitems.setVisibility(View.GONE);
                    }
                    else
                        noitems.setVisibility(View.VISIBLE);
                }
                @Override public void onFailure(Call<GetConsolation> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
}