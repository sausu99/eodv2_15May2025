/**
 * The `AllUserBidderActivity` class is an activity in an Android app that displays a list of bidders
 * for a specific offer.
 */
package com.wowcodes.supreme.Activity;
import android.graphics.drawable.Drawable;
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

import com.wowcodes.supreme.Adapter.UserBiddersAdapter;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.UserBid;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AllUserBidderActivity extends AppCompatActivity {

    public BindingService videoService;
    RecyclerView recyclerYourBid;
    LinearLayout lvlYourBid;
    SavePref savePref;
    String oId;
    TextView txtNoBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_alluser_lotto);
        savePref = new SavePref(AllUserBidderActivity.this);
        ImageView imgBackk = findViewById(R.id.imgBackk);
        TextView txtAucname = findViewById(R.id.txtAucname);
        oId = getIntent().getStringExtra("o_id");
        txtAucname.setText(R.string.string1);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerYourBid = findViewById(R.id.recycler);
        lvlYourBid = findViewById(R.id.linearlay);
        txtNoBid = findViewById(R.id.txtNoBid);
        txtNoBid.setVisibility(View.GONE);
        recyclerYourBid.setLayoutManager(new LinearLayoutManager(AllUserBidderActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        getofferapi();
    }

    public void getofferapi() {
        lvlYourBid.setVisibility(View.VISIBLE);
        try {
            videoService.get_offers_id(oId, savePref.getUserId()).enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    lvlYourBid.setVisibility(View.GONE);
                    ArrayList<UserBid> arrayList = response.body().getJSON_DATA().get(0).getUser_bid();
                    recyclerYourBid.setAdapter(new UserBiddersAdapter(AllUserBidderActivity.this, arrayList));

                    if (arrayList.size() == 0)
                        txtNoBid.setVisibility(View.VISIBLE);
                    else
                        txtNoBid.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    lvlYourBid.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            lvlYourBid.setVisibility(View.GONE);
        }
    }
}
