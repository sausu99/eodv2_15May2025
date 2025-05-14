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

import com.wowcodes.supreme.Adapter.GetOrderAdapter;
import com.wowcodes.supreme.Modelclas.GetOrderUser;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class GetOrderActivity extends AppCompatActivity {

    RecyclerView recyclerGetOrder;
    LinearLayout lvlGetOrder,nodatalayout;
    ImageView nodataimage;
    TextView nodatatext;;
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
        setContentView(R.layout.activity_alluser_lotto);
        savePref=new SavePref(GetOrderActivity.this);
        ImageView imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        nodataimage= findViewById(R.id.nodataimage);
        nodatatext=findViewById(R.id.nodatatext);
        nodatalayout=findViewById(R.id.nodatalayout);
        txtAucname.setText(R.string.string12);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerGetOrder =findViewById(R.id.recycler);
        lvlGetOrder =findViewById(R.id.linearlay);
        recyclerGetOrder.setLayoutManager(new LinearLayoutManager(GetOrderActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        getcoinapi();
    }


    public void getcoinapi() {
        lvlGetOrder.setVisibility(View.VISIBLE);
        try {
            callcoinApi().enqueue(new Callback<GetOrderUser>() {
                @Override
                public void onResponse(Call<GetOrderUser> call, retrofit2.Response<GetOrderUser> response) {
                    try {
                        lvlGetOrder.setVisibility(View.GONE);
                        ArrayList<GetOrderUser.Get_order_user_Inner> arrayList = response.body().getJSON_DATA();
                        if(arrayList.size()==0){
                            nodatalayout.setVisibility(View.VISIBLE);
                            nodataimage.setImageResource(R.drawable.noorder);
                            nodatatext.setText(R.string.noorder);
                        }
                        recyclerGetOrder.setAdapter(new GetOrderAdapter(GetOrderActivity.this, arrayList));
                    }catch (Exception e){
                        lvlGetOrder.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetOrderUser> call, Throwable t) {
                    lvlGetOrder.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<GetOrderUser> callcoinApi() {
        return videoService.get_order_user(savePref.getUserId());
    }
}