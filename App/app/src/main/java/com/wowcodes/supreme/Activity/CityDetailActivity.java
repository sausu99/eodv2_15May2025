/**
 * The CityDetailActivity class is an activity in an Android app that displays a list of cities and
 * their corresponding flag images in a RecyclerView, and allows the user to select a city and return
 * the selected position to the calling activity.
 */
package com.wowcodes.supreme.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Modelclas.getcity;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityDetailActivity extends AppCompatActivity {
    RecyclerView recyclerViewCities;
    ImageView imgBackk;
    TextView txtAucName,submit;
    CityAdapter cityAdapter;
    String city="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        BindingService videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        imgBackk = findViewById(R.id.imgBackk);
        txtAucName = findViewById(R.id.txtAucname);
        submit = findViewById(R.id.submit);
        recyclerViewCities = findViewById(R.id.recyclerViewCities);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));

        txtAucName.setText(getString(R.string.choose_country));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            videoService.get_city().enqueue(new Callback<getcity>() {
                @Override
                public void onResponse(Call<getcity> call, Response<getcity> response) {
                    ArrayList<getcity.get_city_Inner> arrayList = response.body().getJSON_DATA();
                    cityAdapter=new CityAdapter(CityDetailActivity.this,arrayList);
                    recyclerViewCities.setAdapter(cityAdapter);
                }
                @Override public void onFailure(Call<getcity> call, Throwable t) {}
            });
        } catch (Exception ignore) {}

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getcity.get_city_Inner selectedOption = cityAdapter.getSelectedItem();

                if (selectedOption != null) {
                    String selectedCity = selectedOption.getCity_name();

                    if (!Objects.equals(city, selectedCity)) {
                        city = selectedCity;
                        new SavePref(getApplicationContext()).setCity(city);
                        new SavePref(getApplicationContext()).setCityId(selectedOption.getCity_id());

                        Intent i = new Intent(CityDetailActivity.this, changeCityAnimationActivity.class);
                        i.putExtra("name",city);
                        startActivity(i);
                    }
                } else
                    Toast.makeText(CityDetailActivity.this, getString(R.string.nooptionsel), Toast.LENGTH_SHORT).show();
            }
        });
    }
}