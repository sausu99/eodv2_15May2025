/**
 * The `YourBidsActivity` class is an Android activity that displays a list of bid users using a
 * RecyclerView and retrieves the data from an API using Retrofit.
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

import com.wowcodes.supreme.Adapter.BidUserAdapter;
import com.wowcodes.supreme.Modelclas.GetBidUser;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class YourBidsActivity extends AppCompatActivity {

    RecyclerView recyclerYourBidsacti;
    LinearLayout lvlYourBidsacti;
    ImageView nodata, imgBackk;
    TextView txtAucname,nodataText;
    SavePref savePref;
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
        setContentView(R.layout.activity_bid);
        nodata=findViewById(R.id.empty_list);
        nodataText=findViewById(R.id.empty_text);
        txtAucname = findViewById(R.id.txtAucname);
        imgBackk = findViewById(R.id.imgBackk);
        recyclerYourBidsacti =findViewById(R.id.recycler);
        lvlYourBidsacti =findViewById(R.id.linearlay);
        recyclerYourBidsacti.setLayoutManager(new LinearLayoutManager(YourBidsActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtAucname.setText(R.string.string373);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        savePref=new SavePref(YourBidsActivity.this);
        gettraapi();
    }

    public void gettraapi() {
        lvlYourBidsacti.setVisibility(View.VISIBLE);
        try {
            callgettraApi().enqueue(new Callback<GetBidUser>() {
                @Override
                public void onResponse(Call<GetBidUser> call, retrofit2.Response<GetBidUser> response) {
                    try {
                        lvlYourBidsacti.setVisibility(View.GONE);
                        ArrayList<GetBidUser.Get_biduser_Inner> arrayList = response.body().getJSON_DATA();
                        if (arrayList.isEmpty()) {
                            nodata.setVisibility(View.VISIBLE);
                            nodataText.setVisibility(View.VISIBLE);
                        } else {
                            nodata.setVisibility(View.GONE);
                            nodataText.setVisibility(View.GONE);
                            recyclerYourBidsacti.setAdapter(new BidUserAdapter(YourBidsActivity.this, arrayList));
                        }
                    }catch (Exception e){
                        lvlYourBidsacti.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetBidUser> call, Throwable t) {
                    lvlYourBidsacti.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<GetBidUser> callgettraApi() {
        return videoService.get_bid_user(savePref.getUserId());
    }
}