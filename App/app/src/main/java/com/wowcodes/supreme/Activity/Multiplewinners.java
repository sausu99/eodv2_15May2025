package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Adapter.MultipleWinnerRankAdapter;
import com.wowcodes.supreme.Modelclas.MultipleWinnersId;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Multiplewinners extends AppCompatActivity {

    SavePref savePref;
    String oid, imgpath,otype;
    Button view_orderbtn1,claim_prizebtn1,view_orderbtn2,claim_prizebtn2,view_orderbtn3,claim_prizebtn3;
    RecyclerView recycler_viewwinners;
    ImageView imgBackBtn, firstimg, secondimg, thirdimg;
    public BindingService apiinfoservice;
    LinearLayout loading;
    ImageView p0, p1, p2, p3, p4;

    LinearLayout mainlayout;
    ViewPager imgpager;
    LinearLayout  pointslay;


    String itemid,itemworth1;
    TextView winnercount,closedt,prizepool;
    ImageView  imgShareBtn, leftimgbtn, rightimgbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(Multiplewinners.this);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        if (Objects.equals(savePref.getLang(), "en")) {
            setLocale("");
        } else {
            setLocale(savePref.getLang());
        }
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_multiplewinners);

        // Initialize views
        oid = getIntent().getStringExtra("O_id");
        itemid=getIntent().getStringExtra("itemid");
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        imgBackBtn = findViewById(R.id.imgBackk);
        recycler_viewwinners = findViewById(R.id.recycler_viewwinners);
        loading = findViewById(R.id.linearlay);
        mainlayout=findViewById(R.id.allwinnersll);
        leftimgbtn = findViewById(R.id.leftimgbtn);
        rightimgbtn = findViewById(R.id.rightimgbtn);
        imgpager = findViewById(R.id.image_pager);
        p0 = findViewById(R.id.p0);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        pointslay = findViewById(R.id.pointslay);
        winnercount=findViewById(R.id.winnercount);
        closedt=findViewById(R.id.closedt);
        prizepool=findViewById(R.id.prizepool);
        // Show loading view and hide main layout initially
        loading.setVisibility(View.VISIBLE);
        mainlayout.setVisibility(View.VISIBLE);
        getwinnersid();

        leftimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = imgpager.getCurrentItem();
                if (currentItem > 0) {
                    imgpager.setCurrentItem(currentItem - 1);
                }
            }
        });

        rightimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = imgpager.getCurrentItem();
                if (currentItem < imgpager.getAdapter().getCount() - 1) {
                    imgpager.setCurrentItem(currentItem + 1);
                }
            }
        });


        // Set back button listener
        imgBackBtn.setOnClickListener(v -> onBackPressed());


    }

    @Override
    protected void onResume() {
        super.onResume();
        getwinnersid();
    }

    public void getwinnersid() {
        try {
            callmultiplewinnersid().enqueue(new Callback<MultipleWinnersId>() {
                @Override
                public void onResponse(Call<MultipleWinnersId> call, Response<MultipleWinnersId> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MultipleWinnersId.Item> arrayList = response.body().getJSON_DATA();
                        List<MultipleWinnersId.Winner> arrayList1 = response.body().getJSON_DATA().get(0).getWinners();
                        winnercount.setText(getString(R.string.totalwinner)+arrayList1.size());
                        otype=arrayList.get(0).getO_type();
                        prizepool.setText(getString(R.string.prizepool)+" : "+arrayList.get(0).getPrize_pool()+" " +MainActivity.currency);
                        recycler_viewwinners.setLayoutManager(new LinearLayoutManager(Multiplewinners.this, LinearLayoutManager.VERTICAL, false));
                        recycler_viewwinners.setAdapter(new MultipleWinnerRankAdapter((Context) Multiplewinners.this,oid,otype, (ArrayList<MultipleWinnersId.Winner>) arrayList1));
                        closedt.setText(getString(R.string.closeddate)+arrayList.get(0).getO_edate());
                        List<String> images = new ArrayList<>();
                        processImages(images,  arrayList);

                        updateViewPager(images);


                        loading.setVisibility(View.GONE);
                        mainlayout.setVisibility(View.VISIBLE);
                    } else {
                        // Handle the scenario where response is not successful
                        Log.d("Tagy",response.message());
                        loading.setVisibility(View.GONE);
                        mainlayout.setVisibility(View.VISIBLE);
                        Toast.makeText(Multiplewinners.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MultipleWinnersId> call, Throwable t) {
                    // Handle API call failure
                    loading.setVisibility(View.GONE);
                    mainlayout.setVisibility(View.VISIBLE);
                    Toast.makeText(Multiplewinners.this, "onFailure data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ignore) {
            // Handle any other exceptions
            loading.setVisibility(View.GONE);
            mainlayout.setVisibility(View.VISIBLE);
            Toast.makeText(Multiplewinners.this, "Failed to load data", Toast.LENGTH_SHORT).show();
        }
    }

    private Call<MultipleWinnersId> callmultiplewinnersid() {
        return apiinfoservice.get_multiplewinners_id(savePref.getUserId(), oid);
    }





    private void processImages(List<String> images,  List<MultipleWinnersId.Item> arrayList) {
        if (arrayList.size() > 0) {
            addValidImage(images, arrayList.get(0).getO_image());
            addValidImage(images, arrayList.get(0).getO_image1());
            addValidImage(images, arrayList.get(0).getO_image2());
            addValidImage(images, arrayList.get(0).getO_image3());
            addValidImage(images, arrayList.get(0).getO_image4());
        }
        else {
            pointslay.setVisibility(GONE);
        }
    }

    private void addValidImage(List<String> images, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.endsWith("/")) {
            images.add(imageUrl);
        }else{
            pointslay.setVisibility(GONE);
            leftimgbtn.setVisibility(GONE);
            rightimgbtn.setVisibility(GONE);
        }
    }

    private void updateViewPager(List<String> images) {
        imgpager.setAdapter(new ImagesAdapter(Multiplewinners.this, images));

        // Set page change listener for ViewPager
        imgpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIndicatorImages(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateIndicatorImages(int position) {
        ImageView[] indicators = {p0, p1, p2, p3, p4};
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setImageDrawable(getResources().getDrawable(i == position ?
                    R.drawable.img_selected_b : R.drawable.img_notselected_b));
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
}
