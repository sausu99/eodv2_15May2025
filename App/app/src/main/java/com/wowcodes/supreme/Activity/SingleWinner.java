package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Modelclas.SingleWinnersId;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class SingleWinner extends AppCompatActivity {
    SavePref savePref;
    String oid,oamt,otype, oname;
    ImageView imgBackBtn, leftimgbtn, rightimgbtn, winnerimg;
    TextView productname, winnername, allBids, yourBids, joindate, winningvalue, savingpercent;
    public BindingService apiinfoservice;
    LinearLayout pointslay, linearlay;
    ScrollView mainlayout;
    ImageView p0, p1, p2, p3, p4;
    String iswinner,orderplaced;
    ViewPager imgpager;
    Button claim_prizebtn,view_orderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(SingleWinner.this);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        if (Objects.equals(savePref.getLang(), "en")) {
            setLocale("");
        } else {
            setLocale(savePref.getLang());
        }

        // Configure window and layout
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_singlewinner);
        isNetworkConnected();

        oid = getIntent().getStringExtra("O_id");
        oname = getIntent().getStringExtra("productname");
        Log.d("objectid",oid);
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        imgBackBtn = findViewById(R.id.imgBackk);
        productname = findViewById(R.id.txtAucname);
        winnerimg = findViewById(R.id.winnerimg);
        winnername = findViewById(R.id.winnername);
        joindate = findViewById(R.id.joindate);
        winningvalue = findViewById(R.id.winningvalue);
        savingpercent = findViewById(R.id.savingpercent);
        leftimgbtn = findViewById(R.id.leftimgbtn);
        rightimgbtn = findViewById(R.id.rightimgbtn);
        imgpager = findViewById(R.id.image_pager);
        allBids = findViewById(R.id.allBids);
        yourBids = findViewById(R.id.yourBids);
        p0 = findViewById(R.id.p0);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        pointslay = findViewById(R.id.pointslay);
        linearlay = findViewById(R.id.linearlay);
        mainlayout = findViewById(R.id.mainlayout);
        linearlay.setVisibility(View.VISIBLE);
        mainlayout.setVisibility(View.INVISIBLE);
        claim_prizebtn=findViewById(R.id.claim_prizebtn);
        view_orderbtn=findViewById(R.id.view_orderbtn);


        productname.setText(oname);
        getwinnersid();



        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        allBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SingleWinner.this, AllBidderActivity.class);
                i.putExtra("o_id", oid);
                startActivity(i);
            }
        });
        yourBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SingleWinner.this, AllUserBidderActivity.class);
                i.putExtra("o_id", oid);
                startActivity(i);
            }
        });
        claim_prizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")) {
                    Intent intent = new Intent(SingleWinner.this, LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SingleWinner.this, SelectAddress.class);
                    intent.putExtra("oid",oid );
                    intent.putExtra("oamt",oamt );
                    intent.putExtra("otype", otype);
                    startActivity(intent);
                }

            }
        });
        view_orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SingleWinner.this, GetOrderActivity.class);
                startActivity(i);
            }
        });
    }

     @Override
     protected void onResume() {
         super.onResume();
         getwinnersid();
     }

     @Override
     protected void onRestart() {
         super.onRestart();
         getwinnersid();
     }

     public void getwinnersid() {
         linearlay.setVisibility(View.VISIBLE);
         mainlayout.setVisibility(View.INVISIBLE);
         try {
             callsinglewinnersid().enqueue(new Callback<SingleWinnersId>() {
                 @Override
                 public void onResponse(Call<SingleWinnersId> call, Response<SingleWinnersId> response) {
                     if (response.isSuccessful() && response.body() != null) {
                         List<SingleWinnersId.Item> arrayList = response.body().getJSON_DATA();

                         if (arrayList != null && !arrayList.isEmpty()) {
                             SingleWinnersId.Item firstItem = arrayList.get(0);

                             winnername.setText(firstItem.getWinner_name());
                             List<String> images = new ArrayList<>();
                             otype = firstItem.getO_type();
                             if ("5".equals(otype)) {
                                 oamt = "0";
                             } else if (Arrays.asList("1", "2", "7", "8", "10", "11").contains(otype)) {
                                 oamt = firstItem.getWinning_value();
                             } else if (Arrays.asList("3", "9").contains(otype)) {
                                 oamt = firstItem.getWinning_value(); // Assuming getO_amount() exists in your model
                             }
                             processImages(images, (ArrayList<SingleWinnersId.Item>) arrayList);
                             updateViewPager(images);
                             allBids.setText(firstItem.getTotal_users() + " Users");

                             Glide.with(SingleWinner.this)
                                     .load(firstItem.getUser_image())
                                     .error(R.drawable.img_background)
                                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                                     .fitCenter()
                                     .into(winnerimg);

                             joindate.setText("Joined on " + firstItem.getJoining_date());
                             winningvalue.setText(MainActivity.currency + firstItem.getWinning_value());
                             savingpercent.setText(firstItem.getWinner_discount() + "%");

                             iswinner = firstItem.getIs_winner();
                             orderplaced = firstItem.getOrder_placed();
                             Log.e("orderplaced", orderplaced );
                             if (Integer.parseInt(iswinner) == 1 && Integer.parseInt(orderplaced) == 0) {
                                 claim_prizebtn.setVisibility(View.VISIBLE);
                             } else if (Integer.parseInt(iswinner) == 1 && Integer.parseInt(orderplaced) == 1) {
                                 claim_prizebtn.setVisibility(GONE);
                                 view_orderbtn.setVisibility(View.VISIBLE);
                             } else {
                                 claim_prizebtn.setVisibility(GONE);
                                 view_orderbtn.setVisibility(GONE);
                             }

                             linearlay.setVisibility(View.INVISIBLE);
                             mainlayout.setVisibility(View.VISIBLE);
                         } else {
                             showError(); // Handle empty list case
                         }
                     } else {
                         showError(); // Handle response error
                     }
                 }

                 @Override
                 public void onFailure(Call<SingleWinnersId> call, Throwable t) {
                     showError(); // Handle request failure
                 }
             });
         } catch (Exception e) {
             e.printStackTrace(); // Log the exception for debugging
             showError(); // Handle unexpected exceptions
         }
     }

    private Call<SingleWinnersId> callsinglewinnersid() {
        return apiinfoservice.get_singlewinners_id(savePref.getUserId(), oid);
    }

    private void processImages(List<String> images, ArrayList<SingleWinnersId.Item> arrayList) {
        if (arrayList.size() > 0) {
            addValidImage(images, arrayList.get(0).getO_image());
            addValidImage(images, arrayList.get(0).getO_image1());
            addValidImage(images, arrayList.get(0).getO_image2());
            addValidImage(images, arrayList.get(0).getO_image3());
            addValidImage(images, arrayList.get(0).getO_image4());
        } else {
            pointslay.setVisibility(GONE);
        }
    }

    private void addValidImage(List<String> images, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.endsWith("/")) {
            images.add(imageUrl);
        } else {
            pointslay.setVisibility(GONE);
            leftimgbtn.setVisibility(GONE);
            rightimgbtn.setVisibility(GONE);
        }
    }

    private void updateViewPager(List<String> images) {
        imgpager.setAdapter(new ImagesAdapter(SingleWinner.this, images));

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

    private void showError() {
        linearlay.setVisibility(View.INVISIBLE);
        mainlayout.setVisibility(View.VISIBLE);
        Toast.makeText(SingleWinner.this, "Failed to load data", Toast.LENGTH_SHORT).show();
    }






     public void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}