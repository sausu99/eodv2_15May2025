/**
 * The WalletPassbookActivity class is an Android activity that displays a user's wallet passbook,
 * fetching the data from an API and populating it in a RecyclerView.
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

import com.wowcodes.supreme.Adapter.WalletAdapter;
import com.wowcodes.supreme.Modelclas.GetWallet;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletPassbookActivity extends AppCompatActivity {
    RecyclerView recyclerWalletPass;
    LinearLayout lvlWalletPass,nodatalayout;
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
        setContentView(R.layout.activity_alluser_lotto);
        savePref=new SavePref(WalletPassbookActivity.this);
        ImageView imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        nodataimage= findViewById(R.id.nodataimage);
        nodatatext=findViewById(R.id.nodatatext);
        txtAucname.setText(R.string.string45);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerWalletPass =findViewById(R.id.recycler);
        lvlWalletPass =findViewById(R.id.linearlay);
        nodatalayout=findViewById(R.id.nodatalayout);
        recyclerWalletPass.setLayoutManager(new LinearLayoutManager(WalletPassbookActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        getcoinapi();
    }

    public void getcoinapi() {
        lvlWalletPass.setVisibility(View.VISIBLE);
        try {
            callcoinApi().enqueue(new Callback<GetWallet>() {
                @Override
                public void onResponse(Call<GetWallet> call, retrofit2.Response<GetWallet> response) {
                    try {
                        lvlWalletPass.setVisibility(View.GONE);
                        ArrayList<GetWallet.Get_Wallet_Inner> arrayList = response.body().getJSON_DATA();
                        if (arrayList.size() == 0) {
                            nodatalayout.setVisibility(View.VISIBLE);
                            nodataimage.setImageResource(R.drawable.nocoins);
                            nodatatext.setText(R.string.notran);
                        }
                        recyclerWalletPass.setAdapter(new WalletAdapter(WalletPassbookActivity.this, arrayList));
                    } catch (Exception e) {
                        lvlWalletPass.setVisibility(View.GONE);
                    }
                }
                @Override public void onFailure(Call<GetWallet> call, Throwable t) {
                    lvlWalletPass.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<GetWallet> callcoinApi() {
        return videoService.get_wallet_passbook(savePref.getUserId());
    }
}