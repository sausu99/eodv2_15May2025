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
import com.wowcodes.supreme.Adapter.AddressSelectadapter;
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

public class AddressSelectionActivity extends AppCompatActivity{
    private TextView addaddresstxt;
    private String oid,itemId, oamt, otype, name, image, claimable;
    private SavePref savePref;
    private ImageView imgBackBtn;
    private RecyclerView recyclerViewaddress;
    private AddressSelectadapter addressAdapter;
    private BindingService apiinfoservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set locale before setting content view
        savePref = new SavePref(AddressSelectionActivity.this);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        setLocale(savePref.getLang().equals("en") ? "" : savePref.getLang());

        setContentView(R.layout.activity_address_selection);

        // Setup API service
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        // Initialize views
        addaddresstxt = findViewById(R.id.addaddresstxt);
        imgBackBtn = findViewById(R.id.imgBackk);
        recyclerViewaddress = findViewById(R.id.recycler_viewaddress);

        // Setup status bar and navigation bar colors
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        // Get intents (check for null to avoid crashes)
        oid = getIntent().getStringExtra("oid");
        itemId=getIntent().getStringExtra("itemId");
        oamt = getIntent().getStringExtra("oamt");
        otype = getIntent().getStringExtra("otype");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("link");
        claimable = getIntent().getStringExtra("claimable");

        // Initialize RecyclerView
        recyclerViewaddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressAdapter = new AddressSelectadapter(this, new ArrayList<>(), oid, oamt, otype, name, image, claimable);
        recyclerViewaddress.setAdapter(addressAdapter);

        // Load addresses
        getAddress();

        // Set click listeners
        imgBackBtn.setOnClickListener(v -> onBackPressed());
        addaddresstxt.setOnClickListener(v -> {
            Intent intent = new Intent(AddressSelectionActivity.this, AddAddress.class);
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
                        } else {
                            Toast.makeText(AddressSelectionActivity.this, "No addresses found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddressSelectionActivity.this, "Failed to fetch addresses", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse2> call, Throwable t) {
                    Toast.makeText(AddressSelectionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("SelectAddress", "Exception occurred", e);
            Toast.makeText(AddressSelectionActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private Call<AddressResponse2> callGetAddress() {
        String userId = savePref.getUserId();
        if (userId != null) {
            return apiinfoservice.getAddress(userId);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}


