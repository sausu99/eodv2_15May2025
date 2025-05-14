/**
 * The ViewAllTicketsActivity class is an Android activity that displays a list of tickets and
 * retrieves data from an API using Retrofit.
 */
package com.wowcodes.supreme.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.wowcodes.supreme.Adapter.TicketAdapter;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.UserBid;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewAllTicketsActivity extends AppCompatActivity {

    public BindingService videoService;
    RecyclerView recyclerYourBid;
    SavePref savePref;
    String oId;
    ArrayList<UserBid> ticketlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewalltickets);
        savePref = new SavePref(ViewAllTicketsActivity.this);
        ImageView imgBackk = findViewById(R.id.imgBackk);
        TextView txtAucname = findViewById(R.id.txtAucname);
        oId = getIntent().getStringExtra("o_id");
        txtAucname.setText(R.string.string44);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerYourBid = findViewById(R.id.recycler_view);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        isNetworkConnected();
    }
    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            getofferapi();
        else{
            Intent intent=new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        if (cm.getActiveNetworkInfo() != null)
            cm.getActiveNetworkInfo().isConnected();
    }

    public void getofferapi() {
        try {
            callofferApi().enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    ticketlist = response.body().getJSON_DATA().get(0).getUser_bid();
                    recyclerYourBid.setAdapter(new TicketAdapter(ViewAllTicketsActivity.this, ticketlist,"2","6"));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ViewAllTicketsActivity.this,LinearLayoutManager.VERTICAL,true);
                    recyclerYourBid.setLayoutManager(layoutManager);
                }

                @Override public void onFailure(Call<AllBidder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<AllBidder> callofferApi() {
        return videoService.get_offers_id(oId, savePref.getUserId());
    }
}