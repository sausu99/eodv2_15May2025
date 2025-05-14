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

import com.wowcodes.supreme.Adapter.TansactionDetailAdapter;
import com.wowcodes.supreme.Modelclas.GetTransaction;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class GetCoinTraActivity extends AppCompatActivity {
    RecyclerView recyclerGetCoinTra;
    LinearLayout lvlGetCoinTra,nodatalayout;
    ImageView nodataimage;
    TextView nodatatext;
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
        setContentView(R.layout.activity_get__bid);
        recyclerGetCoinTra =findViewById(R.id.recycler);
        lvlGetCoinTra =findViewById(R.id.linearlay);
        nodataimage= findViewById(R.id.nodataimage);
        nodatatext=findViewById(R.id.nodatatext);
        nodatalayout=findViewById(R.id.nodatalayout);
        savePref=new SavePref(GetCoinTraActivity.this);

        ImageView imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(R.string.string10);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerGetCoinTra.setLayoutManager(new LinearLayoutManager(GetCoinTraActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        gettraapi();
    }


    public void gettraapi() {
        lvlGetCoinTra.setVisibility(View.VISIBLE);
        try {
            callgettraApi().enqueue(new Callback<GetTransaction>() {
                @Override
                public void onResponse(Call<GetTransaction> call, retrofit2.Response<GetTransaction> response) {
                    try {
                        lvlGetCoinTra.setVisibility(View.GONE);
                        ArrayList<GetTransaction.Get_transaction_Inner> arrayList = response.body().getJSON_DATA();
                        if(arrayList.size()==0){
                            nodatalayout.setVisibility(View.VISIBLE);
                            nodataimage.setImageResource(R.drawable.nocoins);
                            nodatatext.setText(R.string.notran);
                        }
                        recyclerGetCoinTra.setAdapter(new TansactionDetailAdapter(GetCoinTraActivity.this, arrayList));
                    }catch (Exception e){
                        lvlGetCoinTra.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetTransaction> call, Throwable t) {
                    lvlGetCoinTra.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<GetTransaction> callgettraApi() {
        return videoService.get_transaction(savePref.getUserId());
    }
}