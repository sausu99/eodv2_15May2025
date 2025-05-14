package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowcodes.supreme.Adapter.AllAchievementsAdapter;
import com.wowcodes.supreme.Modelclas.GetAchievements;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewAllAchievementsActivity extends AppCompatActivity {

    ImageView imgBackk;
    TextView txtAucname;
    RecyclerView recycler;
    BindingService videoService;
    SavePref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_achievements);
        imgBackk=findViewById(R.id.imgBackk);
        txtAucname=findViewById(R.id.txtAucname);
        recycler=findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this,2));
        savePref=new SavePref(this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtAucname.setText(getString(R.string.achievements));
        try {
            videoService.get_achievements(savePref.getUserId()).enqueue(new Callback<GetAchievements>() {
                @Override
                public void onResponse(Call<GetAchievements> call, retrofit2.Response<GetAchievements> response) {
                    ArrayList<GetAchievements.Get_Achievements_Inner> arrayList = response.body().getJSON_DATA();
                    recycler.setAdapter(new AllAchievementsAdapter(ViewAllAchievementsActivity.this, arrayList));
                }

                @Override public void onFailure(Call<GetAchievements> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
}