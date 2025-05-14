package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.SelectAddressAdapter;
import com.wowcodes.supreme.Modelclas.AddressResponse2;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SelectAddress extends AppCompatActivity {

    private TextView addaddress;
    String oid,oamt,otype,itemid;
    private SavePref savePref;
    private ImageView imgBackBtn;
    private RecyclerView recyclerViewaddress;
    private SelectAddressAdapter addressAdapter;
    private BindingService apiinfoservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        // Initialize views
        addaddress = findViewById(R.id.addaddresstxt);
        imgBackBtn = findViewById(R.id.imgBackk);
        recyclerViewaddress = findViewById(R.id.recycler_viewaddress);

        // Setup API service
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(SelectAddress.this);

        // Setup status bar and navigation bar colors
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        // Set locale based on preferences
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        setLocale(savePref.getLang().equals("en") ? "" : savePref.getLang());

        //intentsget
        oid=getIntent().getStringExtra("oid");
        oamt=getIntent().getStringExtra("oamt");
        otype=getIntent().getStringExtra("otype");
        itemid=getIntent().getStringExtra("itemid");
        // Initialize RecyclerView
        recyclerViewaddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressAdapter = new SelectAddressAdapter(this, new ArrayList<>(),oid,oamt,otype,itemid);
        recyclerViewaddress.setAdapter(addressAdapter);

        // Load addresses
        getAddress();

        // Set click listeners
        imgBackBtn.setOnClickListener(v -> onBackPressed());
        addaddress.setOnClickListener(v -> {
            Intent intent = new Intent(SelectAddress.this, AddAddress.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    private void getAddress() {
        try {
            callGetAddress().enqueue(new Callback<AddressResponse2>() {
                @Override
                public void onResponse(Call<AddressResponse2> call, Response<AddressResponse2> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<AddressResponse2.Address> addresses = response.body().getAddresses();
                        addressAdapter.updateAddressList((ArrayList<AddressResponse2.Address>) addresses);
                    } else {
                        Toast.makeText(SelectAddress.this, "Failed to fetch addresses", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse2> call, Throwable t) {
                    Toast.makeText(SelectAddress.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Log unexpected exceptions
            Log.e("SelectAddress", "Exception occurred", e);
            Toast.makeText(SelectAddress.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private Call<AddressResponse2> callGetAddress() {
        return apiinfoservice.getAddress(savePref.getUserId());
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}

