package com.wowcodes.supreme.Activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Adapter.WishlistItemAdapter;
import com.wowcodes.supreme.Modelclas.WishlistItem;
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

public class MyWishlist extends AppCompatActivity {

    private TextView txtAucname, noItemTxt;
    private ImageView imgBackBtn;
    private SavePref savePref;
    private LinearLayout loadingLayout;
    private BindingService apiinfoservice;
    private WishlistItemAdapter adapter;
    private List<WishlistItem.Item> wishlistItems = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);

        // Initialize views
        imgBackBtn = findViewById(R.id.imgBackk);
        txtAucname = findViewById(R.id.txtAucname);
        recyclerView = findViewById(R.id.wishlistrecycler);
        loadingLayout = findViewById(R.id.linearlay);
        noItemTxt = findViewById(R.id.noItemTxt);

        // Initialize services and preferences
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(this);

        // Setup UI
        setupWindowAppearance();
        setupLocale();

        // Fetch wishlist
        getWishlist();

        // Handle back button click
        imgBackBtn.setOnClickListener(v -> onBackPressed());
    }

    private void setupWindowAppearance() {
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
    }

    private void setupLocale() {
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        setLocale(savePref.getLang().equals("en") ? "" : savePref.getLang());
        txtAucname.setText(getString(R.string.string521));
    }

    private void getWishlist() {
        // Show the loading layout
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        try {
            callGetWishlist().enqueue(new Callback<WishlistItem>() {
                @Override
                public void onResponse(Call<WishlistItem> call, Response<WishlistItem> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        WishlistItem wishlistItem = response.body();
                        // Clear the existing list
                        wishlistItems.clear();
                        // Get the list of items from the response
                        List<WishlistItem.Item> items = wishlistItem.getJSON_DATA();
                        if (items != null && !items.isEmpty()) {
                            wishlistItems.addAll(items); // Add the items to the list

                            // Initialize or update the adapter
                            if (adapter == null) {
                                adapter = new WishlistItemAdapter(MyWishlist.this, wishlistItems);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MyWishlist.this));
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            // Hide the loading layout and show the RecyclerView
                            loadingLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            // Log the size of the list for debugging
                            Log.d("WishlistSize", "Number of items: " + wishlistItems.size());

                            // Iterate through the items and log their names
                            for (WishlistItem.Item item : wishlistItems) {
                                Log.d("WishlistItemName", "Item Name: " + item.getO_name());
                            }
                        } else {
                            // Handle empty wishlist
                            noItemTxt.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            Log.d("Wishlist", "No items found in the wishlist.");
                        }
                    } else {
                        // Handle failure
                        Toast.makeText(MyWishlist.this, "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
                        loadingLayout.setVisibility(View.GONE); // Hide loading layout in case of failure
                    }
                }

                @Override
                public void onFailure(Call<WishlistItem> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(MyWishlist.this, "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
                    loadingLayout.setVisibility(View.GONE); // Hide loading layout in case of failure
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Exception caught: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingLayout.setVisibility(View.GONE); // Hide loading layout in case of exception
        }
    }

    private Call<WishlistItem> callGetWishlist() {
        return apiinfoservice.getWishlist(savePref.getUserId());
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
