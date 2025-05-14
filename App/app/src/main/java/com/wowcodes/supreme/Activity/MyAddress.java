package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.MyAddressAdapter;
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

public class MyAddress extends AppCompatActivity {

    private TextView addaddress, txtAucname;
    private String oid, oamt, otype;
    private LinearLayout loadinglay;
    private SavePref savePref;
    private ImageView imgBackBtn;
    private RecyclerView recyclerViewaddress;
    private MyAddressAdapter addressAdapter;
    private BindingService apiinfoservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        // Initialize views
        addaddress = findViewById(R.id.addaddresstxt);
        imgBackBtn = findViewById(R.id.imgBackk);
        recyclerViewaddress = findViewById(R.id.recycler_viewaddress);
        txtAucname = findViewById(R.id.txtAucname);
        loadinglay = findViewById(R.id.linearlay);

        // Setup API service
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(this);

        // Setup status bar and navigation bar colors
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        setLocale(savePref.getLang().equals("en") ? "" : savePref.getLang());
        txtAucname.setText(getString(R.string.string522));

        // Show loading layout and hide RecyclerView
        showLoading(true);

        // Get data from intent
        oid = getIntent().getStringExtra("oid");
        oamt = getIntent().getStringExtra("oamt");
        otype = getIntent().getStringExtra("otype");

        // Initialize RecyclerView
        recyclerViewaddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressAdapter = new MyAddressAdapter(this, new ArrayList<>(), oid, oamt, otype);
        recyclerViewaddress.setAdapter(addressAdapter);

        // Load addresses
        getAddress();

        // Set click listeners
        imgBackBtn.setOnClickListener(v -> onBackPressed());
        addaddress.setOnClickListener(v -> {
            Intent intent = new Intent(MyAddress.this, AddAddress.class);
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
                        if (addresses != null && !addresses.isEmpty()) {
                            addressAdapter.updateAddressList((ArrayList<AddressResponse2.Address>) addresses);
                            showLoading(false); // Hide loading layout and show RecyclerView
                        } else {
                            showEmptyState();
                        }
                    } else {
                        Toast.makeText(MyAddress.this, "Failed to fetch addresses", Toast.LENGTH_SHORT).show();
                        showEmptyState();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse2> call, Throwable t) {
                    Toast.makeText(MyAddress.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            });
        } catch (Exception e) {
            Log.e("MyAddress", "Exception occurred", e);
            Toast.makeText(MyAddress.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
            showEmptyState();
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

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loadinglay.setVisibility(View.VISIBLE);
            recyclerViewaddress.setVisibility(View.GONE);
        } else {
            loadinglay.setVisibility(View.GONE);
            recyclerViewaddress.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyState() {
        loadinglay.setVisibility(View.GONE);
        recyclerViewaddress.setVisibility(View.GONE);
        Toast.makeText(this, "No addresses found", Toast.LENGTH_SHORT).show();
    }
}
