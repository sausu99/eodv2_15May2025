package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Adapter.ProductListAdapter;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.GetSellerDetails;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
//import com.wowcodes.prizex.Modelclas.gettime;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryDetailsActivity extends AppCompatActivity {
    SavePref savePref;
    public BindingService videoService;
    ImageView imgBackBtn, imgShareBtn;
    TextView txtItemName, txtItemType, txtItemDesc, txtButtonName, txtCoins,redeem;
    LinearLayout otypethreelayout, otypetwolayout, otypeonelayout, otypefivelayout, otypefourlayout,otypesevenlayout,otypeeightlayout;
    LinearLayout layoutbtnRedeem, layoutbtnHUB, layoutbtnLUB, layoutbtnRaffle, layoutbtnDraw,layoutbtnDynamic,layoutbtnPenny;
    TextView txtAuctionEndTimeHUB, txtAuctionEndTimeLUB, txtAuctionEndTimeRaffle, txtAuctionEndTimeDraw, txtReadMore, txtItemSeller,txtAuctionEndTime,txtPAuctionEndTime;
    String name, type, etime, edate, image, image1, image2, image3, image4, desc, coins, SellerName, SellerAbout, SellerLink, SellerImage;
    String start_date, status = "0", qty ;
    int qtyO;
    String totalbids = "0", availableqty = "0";
    public TextView txtDrawEndsIn, txtRaffleEndsIn, txtAuctionEndsLUB, txtAuctionEndsHUB, txtQty, txtQtyStock,txtAuctionEnds,txtPAuctionEnds;
    String oid, totalWallet, oAmt, colorcode, umax, cdesc, olink, olimit, seller,curr_dt_time = "",currentDate="",currentTime="";
    RecyclerView seller_items;
    String SellerJoinDate;
    ImageView p0,p1,p2,p3,p4;
    LinearLayout points;
    ViewPager imgpager;
    String claimable="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(CategoryDetailsActivity.this);
        if(savePref.getLang() == null)
            savePref.setLang("en");

        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());

        setContentView(R.layout.activity_categorydetails);
        //isDarkMode = savePref.getMode();

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        imgBackBtn = (ImageView) findViewById(R.id.imgBackBtn);
        imgShareBtn = (ImageView) findViewById(R.id.imgShareBtn);
        imgpager=findViewById(R.id.image_pager);
        p0=findViewById(R.id.p1);
        p1=findViewById(R.id.p2);
        p2=findViewById(R.id.p3);
        p3=findViewById(R.id.p4);
        p4=findViewById(R.id.p5);
        points=findViewById(R.id.points);
        txtButtonName = (TextView) findViewById(R.id.txtButtonName);
        txtItemName = (TextView) findViewById(R.id.txtItemName);
        txtItemType = (TextView) findViewById(R.id.txtItemType);
        txtItemDesc = (TextView) findViewById(R.id.txtItemDesc);
        txtItemSeller = (TextView) findViewById(R.id.txtItemSeller);
        otypeonelayout = (LinearLayout) findViewById(R.id.otypeonelayout);
        otypethreelayout = (LinearLayout) findViewById(R.id.otypethreelayout);
        otypetwolayout = (LinearLayout) findViewById(R.id.otypetwolayout);
        otypefivelayout = (LinearLayout) findViewById(R.id.otypefivelayout);
        otypefourlayout = (LinearLayout) findViewById(R.id.otypefourlayout);
        otypesevenlayout = (LinearLayout) findViewById(R.id.otypesevenlayout);
        otypeeightlayout = (LinearLayout) findViewById(R.id.otypeeightlayout);
        layoutbtnRedeem = (LinearLayout) findViewById(R.id.layoutbtnRedeem);
        layoutbtnHUB = (LinearLayout) findViewById(R.id.layoutbtnHUB);
        layoutbtnLUB = (LinearLayout) findViewById(R.id.layoutbtnLUB);
        layoutbtnRaffle = (LinearLayout) findViewById(R.id.layoutbtnRaffle);
        layoutbtnDraw = (LinearLayout) findViewById(R.id.layoutbtnDraw);
        layoutbtnDynamic = (LinearLayout) findViewById(R.id.layoutbtnDynamic);
        layoutbtnPenny= (LinearLayout) findViewById(R.id.layoutbtnPenny);
        redeem = (TextView) findViewById(R.id.redeem);
        txtAuctionEndTimeHUB = (TextView) findViewById(R.id.txtAuctionEndTimeHUB);
        txtAuctionEndTimeLUB = (TextView) findViewById(R.id.txtAuctionEndTimeLUB);
        txtAuctionEndTimeRaffle = (TextView) findViewById(R.id.txtAuctionEndTimeRaffle);
        txtAuctionEndTimeDraw = (TextView) findViewById(R.id.txtAuctionEndTimeDraw);
        txtAuctionEndTime = (TextView) findViewById(R.id.txtAuctionEndTime);
        txtPAuctionEndTime =  (TextView) findViewById(R.id.txtPAuctionEndTime);
        txtCoins = (TextView) findViewById(R.id.txtCoins);
        txtReadMore = (TextView) findViewById(R.id.txtReadMore);
        txtDrawEndsIn = (TextView) findViewById(R.id.txtDrawEndsIn);
        txtRaffleEndsIn = (TextView) findViewById(R.id.txtRaffleEndsIn);
        txtAuctionEndsLUB = (TextView) findViewById(R.id.txtAuctionEndsLUB);
        txtAuctionEnds = (TextView) findViewById(R.id.txtAuctionEnds);
        txtAuctionEndsHUB = (TextView) findViewById(R.id.txtAuctionEndsHUB);
        txtPAuctionEnds =  (TextView) findViewById(R.id.txtPAuctionEnds);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtQtyStock = (TextView) findViewById(R.id.txtQtyStock);

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        isNetworkConnected();
        handledynamiclinks();

        imgShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }

                shareDeepLink();
            }
        });

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            if (getIntent() != null) {
                name = getIntent().getStringExtra("name");
                type = getIntent().getStringExtra("type");
                etime = getIntent().getStringExtra("etime");
                edate = getIntent().getStringExtra("edate");
                image = getIntent().getStringExtra("image");
                image1 = getIntent().getStringExtra("image1");
                image2 = getIntent().getStringExtra("image2");
                image3 = getIntent().getStringExtra("image3");
                image4 = getIntent().getStringExtra("image4");
                desc = getIntent().getStringExtra("desc");
                coins = getIntent().getStringExtra("coins");
                oid = getIntent().getStringExtra("oid");
                qty = getIntent().getStringExtra("qty");
                oAmt = getIntent().getStringExtra("oamt");
                colorcode = getIntent().getStringExtra("colorcode");
                umax = getIntent().getStringExtra("umax");
                cdesc = getIntent().getStringExtra("cdesc");
                olink = getIntent().getStringExtra("link");
                olimit = getIntent().getStringExtra("limit");
                totalbids = getIntent().getStringExtra("totalbids");
                seller = getIntent().getStringExtra("id");
                if(getIntent().hasExtra("claimable"))
                    claimable=getIntent().getStringExtra("claimable");
                if (TextUtils.isEmpty(olimit))
                    olimit = "1";
                else
                    olimit = getIntent().getStringExtra("limit");

                qtyO = Integer.parseInt(qty);
            }
            availableqty = String.valueOf(Integer.parseInt(qty) - Integer.parseInt(totalbids));
        } catch (Exception ignore) {}

        getSeller();
        txtItemName.setText(name);
        txtItemType.setText(getText(R.string.string4) + ": " + type);
        txtItemSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWindialog();
            }
        });

        txtItemDesc.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = txtItemDesc.getLineCount();
                if (lineCount > 4) {
                    txtReadMore.setVisibility(View.VISIBLE);
                    txtItemDesc.setMaxLines(4);
                }
            }
        });
        txtItemDesc.setText(desc);
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("0")) {
                    txtItemDesc.setMaxLines(30);
                    txtReadMore.setText(R.string.string5);
                    status = "1";
                } else {
                    txtItemDesc.setMaxLines(10);
                    txtReadMore.setText(R.string.string5);
                    status = "0";
                }
            }
        });

        List<String> images=new ArrayList<>();
        if(!image.endsWith("/"))
            images.add(image);
        if(!image1.endsWith("/"))
            images.add(image1);
        if(!image2.endsWith("/"))
            images.add(image2);
        if(!image3.endsWith("/"))
            images.add(image3);
        if(!image4.endsWith("/"))
            images.add(image4);

        if(images.size()==1)
            points.setVisibility(View.GONE);
        else if(images.size()==2){
            p0.setVisibility(View.VISIBLE);
            p1.setVisibility(View.VISIBLE);
            p2.setVisibility(View.GONE);
            p3.setVisibility(View.GONE);
            p4.setVisibility(View.GONE);
        }
        else if(images.size()==3){
            p0.setVisibility(View.VISIBLE);
            p1.setVisibility(View.VISIBLE);
            p2.setVisibility(View.VISIBLE);
            p3.setVisibility(View.GONE);
            p4.setVisibility(View.GONE);
        }
        else if(images.size()==4){
            p0.setVisibility(View.VISIBLE);
            p1.setVisibility(View.VISIBLE);
            p2.setVisibility(View.VISIBLE);
            p3.setVisibility(View.VISIBLE);
            p4.setVisibility(View.GONE);
        }
        else if(images.size()==5){
            p0.setVisibility(View.VISIBLE);
            p1.setVisibility(View.VISIBLE);
            p2.setVisibility(View.VISIBLE);
            p3.setVisibility(View.VISIBLE);
            p4.setVisibility(View.VISIBLE);
        }

        imgpager.setAdapter(new ImagesAdapter(CategoryDetailsActivity.this,images));
        imgpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0) {
                    p0.setImageDrawable(getResources().getDrawable(R.drawable.img_selected_b));
                    p1.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p2.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p3.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p4.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                }
                else if(position == 1) {
                    p0.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p1.setImageDrawable(getResources().getDrawable(R.drawable.img_selected_b));
                    p2.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p3.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p4.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                }
                else if(position == 2) {
                    p0.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p1.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p2.setImageDrawable(getResources().getDrawable(R.drawable.img_selected_b));
                    p3.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p4.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                }
                else if(position == 3) {
                    p0.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p1.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p2.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p3.setImageDrawable(getResources().getDrawable(R.drawable.img_selected_b));
                    p4.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                }
                else if(position == 4) {
                    p0.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p1.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p2.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p3.setImageDrawable(getResources().getDrawable(R.drawable.img_notselected_b));
                    p4.setImageDrawable(getResources().getDrawable(R.drawable.img_selected_b));
                }
            }

            @Override public void onPageSelected(int position) {}
            @Override public void onPageScrollStateChanged(int state) {}
        });

        layoutbtnHUB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, AuctionActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "live");
                    startActivity(i);
                }
            }
        });
        layoutbtnLUB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, AuctionActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "live");
                    startActivity(i);
                }
            }
        });
        layoutbtnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, RaffleDetailActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "draw");
                    i.putExtra("total_bids" , totalbids);
                    i.putExtra("qty" , qtyO);
                    i.putExtra("type" , type);
                    i.putExtra("name" , name);
                    i.putExtra("etime" , etime);
                    i.putExtra("edate" , edate);
                    i.putExtra("image" , image);
                    i.putExtra("desc" , desc);
                    i.putExtra("coins" , coins);
                    i.putExtra("oamt" , oAmt);
                    i.putExtra("colorcode" , colorcode);
                    i.putExtra("umax" , umax);
                    i.putExtra("cdesc" ,cdesc);
                    i.putExtra("link" , olink);
                    i.putExtra("limit" , olimit);
                    i.putExtra("id",seller);
                    startActivity(i);
                }
            }
        });

        layoutbtnDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, AuctionActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "live");
                    startActivity(i);
                }
            }
        });
        layoutbtnPenny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, AuctionActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "live");
                    i.putExtra("total_bids" , totalbids);
                    i.putExtra("qty" , qtyO);
                    i.putExtra("type" , type);
                    startActivity(i);
                }
            }
        });
        layoutbtnRaffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(CategoryDetailsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(CategoryDetailsActivity.this, LotoDetailActivity.class);
                    i.putExtra("O_id", oid);
                    i.putExtra("check", "raffle");
                    i.putExtra("total_bids" , totalbids);
                    i.putExtra("qty" , qtyO);
                    i.putExtra("type" , type);
                    startActivity(i);
                }
            }
        });

        layoutbtnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")) {
                    Intent intent = new Intent(CategoryDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                else {
                    try {
                        if(type.equals("9")) {
                            if (Integer.parseInt(availableqty) > 0)
                                openemaildialog();
                            else
                                Toast.makeText(getApplicationContext(), R.string.string151, Toast.LENGTH_SHORT).show();
                        }
                        else if(type.equals("3")) {
                            if (Integer.parseInt(availableqty) > 0) {
                                if (Integer.parseInt(totalWallet) >= Integer.parseInt(oAmt)) {
                                    if(claimable.isEmpty())
                                        addbid();
                                    else
                                        addclaimbid(savePref.getemail());
                                }
                                else
                                    opendialog();
                            }
                            else
                                Toast.makeText(getApplicationContext(), R.string.string151, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ignore) {}
                }
            }
        });



        if (type.equals("1")) {
            txtButtonName.setText(R.string.string7);
            otypeonelayout.setVisibility(View.VISIBLE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            gettime();

            Thread myThread = null;
            Runnable myRunnableThread = new CountDownRunner(txtAuctionEndTimeLUB, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        } else if (type.equals("2")) {
            gettime();
            txtButtonName.setText(R.string.string8);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.VISIBLE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);

            Thread myThread = null;
            Runnable myRunnableThread = new CountDownRunner(txtAuctionEndTimeHUB, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        } else if (type.equals("3")) {
            gettime();
            txtButtonName.setText(R.string.string9);
            redeem.setText(R.string.string9);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.VISIBLE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            txtCoins.setText(coins + " "+ getText(R.string.string118));

            if (availableqty.equals("0") || availableqty.startsWith("-")) {
                txtQtyStock.setVisibility(View.VISIBLE);
                txtQty.setVisibility(View.GONE);
            }
            else {
                txtQty.setVisibility(View.VISIBLE);
                txtQty.setText(getText(R.string.string126)+ " " + availableqty + " "+getText(R.string.string127));
                txtQtyStock.setVisibility(View.GONE);
            }
        } else if (type.equals("4")) {
            gettime();
            txtButtonName.setText(R.string.string128);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.VISIBLE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);

            Thread myThread = null;

            Runnable myRunnableThread = new CountDownRunner(txtAuctionEndTimeDraw, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        } else if (type.equals("5")) {
            gettime();
            txtButtonName.setText(R.string.string129);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.VISIBLE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);

            Thread myThread = null;
            Runnable myRunnableThread = new CountDownRunner(txtAuctionEndTimeRaffle, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        } else if (type.equals("6")) {
            gettime();
            String url = olink;
            if (TextUtils.isEmpty(url))
                finish();
            else {
                Intent intent = new Intent(CategoryDetailsActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("from", "main");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        }
        else if (type.equals("7")) {
            gettime();
            txtButtonName.setText(R.string.string130);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.VISIBLE);
            otypeeightlayout.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);

            Thread myThread = null;
            Runnable myRunnableThread = new CountDownRunner(txtAuctionEndTime, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        }
        else if (type.equals("8")) {
            gettime();
            txtButtonName.setText(R.string.string130);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.GONE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.VISIBLE);
            txtQty.setVisibility(View.GONE);

            Thread myThread = null;
            Runnable myRunnableThread = new CountDownRunner(txtPAuctionEnds, edate + " " + etime);
            myThread = new Thread(myRunnableThread);
            myThread.start();
        }
        else if (type.equals("9")) {
            gettime();
            txtButtonName.setText(R.string.string131);
            redeem.setText(R.string.string131);
            otypeonelayout.setVisibility(View.GONE);
            otypetwolayout.setVisibility(View.GONE);
            otypethreelayout.setVisibility(View.VISIBLE);
            otypefourlayout.setVisibility(View.GONE);
            otypefivelayout.setVisibility(View.GONE);
            otypesevenlayout.setVisibility(View.GONE);
            otypeeightlayout.setVisibility(View.GONE);
            txtCoins.setText(MainActivity.currency +coins);
            if (availableqty.equals("0") || availableqty.startsWith("-")) {
                txtQtyStock.setVisibility(View.VISIBLE);
                txtQty.setVisibility(View.GONE);
            }
            else {
                txtQty.setVisibility(View.VISIBLE);
                txtQty.setText(getText(R.string.string126)+" " + availableqty + " "+getText(R.string.string127));
                txtQtyStock.setVisibility(View.GONE);
            }
        }
    }

    private void shareDeepLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String deepLink= Constants.baseurl.substring(0,Constants.baseurl.indexOf("seller/")) + "share.php?share_id="+oid;
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);
        startActivity(intent);
    }

    private void handledynamiclinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null)
                            deepLink = pendingDynamicLinkData.getLink();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {@Override public void onFailure(@NonNull Exception e) {}});
    }


    private void openemaildialog() {
        final Dialog dialog = new Dialog(CategoryDetailsActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_redeem);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        window.setLayout(width-110, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView submit = dialog.findViewById(R.id.submit);
        EditText street = dialog.findViewById(R.id.street);
        EditText adln2 = dialog.findViewById(R.id.addln2);
        EditText city = dialog.findViewById(R.id.city);
        EditText pin = dialog.findViewById(R.id.pin_code);
        EditText comments = dialog.findViewById(R.id.comments);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (street.getText().toString().isEmpty() || city.getText().toString().isEmpty() || pin.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), R.string.string153, Toast.LENGTH_SHORT).show();
                else {
                    if(claimable!=null && !claimable.isEmpty()) {
                        String address = street.getText().toString() + " , " + adln2.getText().toString() + " , " + city.getText().toString() + " , " + pin.getText().toString() + " , " + comments.getText().toString() + " :: ( " + new SavePref(getApplicationContext()).getemail() + " )";
                        addclaimbid(address);
                        dialog.cancel();
                    }
                    else {
                        Intent intent = new Intent(CategoryDetailsActivity.this, RazorpayActivity.class);
                        intent.putExtra("email", new SavePref(getApplicationContext()).getemail());
                        intent.putExtra("activity", "CategoryDetailsAct");
                        intent.putExtra("amount", oAmt);
                        intent.putExtra("name", name);
                        intent.putExtra("O_id", oid);
                        intent.putExtra("link", image);
                        startActivity(intent);
                    }
                }
            }
        });
    }



    public void addclaimbid(String address) {
        try {
            calladdbidApi(address).enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                    Toast.makeText(getApplicationContext(), ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    update_api("3",claimable);
                    claimable="";
                }

                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    private Call<AddOrder> calladdbidApi(String address) {
        return videoService.add_order(new SavePref(getApplicationContext()).getUserId(),oid,oAmt,"",oAmt,address,"","2");
    }

    public void update_api(String status,String sid){
        try {
            videoService.update_consolation(status,sid).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    openconfirmorderdialog();
                }
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private void opendialog() {
        final Dialog dialog = new Dialog(CategoryDetailsActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_insufficientcoins);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button ok = dialog.findViewById(R.id.ok);
        TextView d_title = dialog.findViewById(R.id.d_title);
        d_title.setText(R.string.string134);
        Button cancel = dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryDetailsActivity.this, MainActivity.class);
                intent.putExtra("page", "2");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    public void doWork(final TextView textView, final String o_etime) {
        (CategoryDetailsActivity.this).runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                gettime();
                start_date=curr_dt_time;
                findDifference(start_date, textView, o_etime, type);
            }
        });
    }

    class CountDownRunner implements Runnable {
        TextView textView;
        String o_etime;

        public CountDownRunner(TextView tx_time, String o_etime) {
            this.textView = tx_time;
            this.o_etime = o_etime;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(textView, o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception ignored) {}
            }
        }
    }

    void findDifference(String start_date, TextView textView, String end_date, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            String d3 = simpleDateFormat.format(d2);
            textView.setText(d3);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            String diff = difference_In_Days + "d: " + difference_In_Hours + "h: " + difference_In_Minutes + "m: " + difference_In_Seconds + "s";
            if (diff.equalsIgnoreCase("0d: 0h: 0m: 0s")) {
                textView.setText(R.string.string135);
                textView.setTextColor(Color.parseColor("#00b712"));
                txtDrawEndsIn.setVisibility(View.GONE);
                txtRaffleEndsIn.setVisibility(View.GONE);
                txtAuctionEndsLUB.setVisibility(View.GONE);
                txtAuctionEndsHUB.setVisibility(View.GONE);
                txtAuctionEnds.setVisibility(View.GONE);
                txtPAuctionEnds.setVisibility(View.GONE);
            }
            else if (difference_In_Days < 0 || difference_In_Hours < 0 || difference_In_Minutes < 0 || difference_In_Seconds < 0) {
                textView.setText(R.string.string135);
                textView.setTextColor(Color.parseColor("#ED3124"));
                txtDrawEndsIn.setVisibility(View.GONE);
                txtRaffleEndsIn.setVisibility(View.GONE);
                txtAuctionEndsLUB.setVisibility(View.GONE);
                txtAuctionEndsHUB.setVisibility(View.GONE);
                txtAuctionEnds.setVisibility(View.GONE);
                txtPAuctionEndTime.setVisibility(View.GONE);
            }
            else {
                textView.setTextColor(Color.parseColor("#00b712"));
                if (type.equals("1")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.VISIBLE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                } else if (type.equals("2")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.VISIBLE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                } else if (type.equals("4")) {
                    txtDrawEndsIn.setVisibility(View.VISIBLE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                } else if (type.equals("5")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.VISIBLE);
                    txtRaffleEndsIn.setText(R.string.string136);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                } else if (type.equals("6")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                }
                else if (type.equals("7")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.VISIBLE);
                    txtPAuctionEndTime.setVisibility(View.GONE);
                }
                else if (type.equals("8")) {
                    txtDrawEndsIn.setVisibility(View.GONE);
                    txtRaffleEndsIn.setVisibility(View.GONE);
                    txtAuctionEndsLUB.setVisibility(View.GONE);
                    txtAuctionEndsHUB.setVisibility(View.GONE);
                    txtAuctionEnds.setVisibility(View.GONE);
                    txtPAuctionEndTime.setVisibility(View.VISIBLE);
                }
            }
        } catch (ParseException e) {}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openWindialog() {
        Dialog dialog=new Dialog(CategoryDetailsActivity.this);
        dialog.setContentView(R.layout.activity_user_profile);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            dialog.getWindow().setLayout(950, dialog.getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().height()-350);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView seller_name=dialog.findViewById(R.id.user_name);
        TextView txtuser_profile=dialog.findViewById(R.id.txtuser_profile);
        TextView join_date=dialog.findViewById(R.id.join_date);
        TextView txtuser_int=dialog.findViewById(R.id.txtuser_int);
        TextView seller_website=dialog.findViewById(R.id.seller_website);
        ImageView seller_img=dialog.findViewById(R.id.profile_img);
        ImageView close=dialog.findViewById(R.id.close);
        seller_items=dialog.findViewById(R.id.user_int);
        seller_items.setLayoutManager(new LinearLayoutManager(CategoryDetailsActivity.this));

        try{
            if(SellerLink.isEmpty())
                seller_website.setVisibility(GONE);
        }catch (Exception ignore){}

        LocalDate currentDate = LocalDate.parse(SellerJoinDate);
        Month mon = currentDate.getMonth();
        int year = currentDate.getYear();

        String month=String.valueOf(mon.toString().charAt(0)).toUpperCase() + mon.toString().substring(1).toLowerCase();
        txtuser_profile.setText(getResources().getString(R.string.seller_profile));
        seller_name.setText(SellerName);
        join_date.setText(getResources().getString(R.string.member_since)+month+" "+year);
        txtuser_int.setText("- "+SellerName+getResources().getString(R.string.other_products));

        try {
            Glide.with(CategoryDetailsActivity.this)
                    .load(SellerImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .into(seller_img);
        } catch (Exception ignore) {}

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        seller_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SellerLink.isEmpty()){
                    Intent intent = new Intent(CategoryDetailsActivity.this, WebViewActivity.class);
                    intent.putExtra("url", SellerLink);
                    intent.putExtra("from", "categories");
                    intent.putExtra("title", "2");
                    startActivity(intent);
                }
            }
        });

        load_seller_products();
        dialog.show();
    }

    public void load_seller_products(){
        try {
            videoService.get_seller_items(seller).enqueue(new Callback<GetSellerItems>() {
                @Override
                public void onResponse(Call<GetSellerItems> call, retrofit2.Response<GetSellerItems> response) {
                    try {
                        ArrayList<GetSellerItems.Get_items_Inner> arrayList = response.body().getJSON_DATA();
                        seller_items.setAdapter(new ProductListAdapter(CategoryDetailsActivity.this, arrayList,seller));
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<GetSellerItems> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    public void getSeller() {
        try {
            callgetApiSeller().enqueue(new Callback<GetSellerDetails>() {
                @Override
                public void onResponse(Call<GetSellerDetails> call, retrofit2.Response<GetSellerDetails> response){
                    try {
                        List<GetSellerDetails.JSONDATum> arrayList = new ArrayList<>(response.body().getJsonData());
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            GetSellerDetails.JSONDATum reqsellers = arrayList.stream()
                                    .filter( reqseller -> seller.equals(reqseller.getId()))
                                    .findAny()
                                    .orElse(null);
                            SellerName = reqsellers.getName();
                            SellerAbout = reqsellers.getAbout();
                            SellerLink = reqsellers.getLink();
                            SellerJoinDate=reqsellers.getJoin_date();
                            SellerImage = reqsellers.getImage();
                            txtItemSeller.setText(getText(R.string.string137)+" " + SellerName);
                        }
                        if(SellerName!=null) {
                            txtItemSeller.setClickable(true);
                            txtItemSeller.setEnabled(true);
                        }
                        else {
                            txtItemSeller.setClickable(false);
                            txtItemSeller.setEnabled(false);
                        }
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<GetSellerDetails> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<GetSellerDetails> callgetApiSeller() {
        return videoService.get_seller();
    }

    public void getprofile() {
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        totalWallet = arrayList.get(0).getWallet();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(CategoryDetailsActivity.this,MainActivity.class);
        i.putExtra("open",0);
        startActivity(i);
        finish();
    }

    public void addbid() {
        try {
            calladdbidApi().enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {
                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(CategoryDetailsActivity.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1"))
                            updatewallet();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    private Call<AddOrder> calladdbidApi() {
        return videoService.add_order(savePref.getUserId(), oid, coins, "", coins, savePref.getemail(),"","2");
    }


    public void updatewallet() {
        try {
            calladdbidApii().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1"))
                            openconfirmorderdialog();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    private Call<SuccessModel> calladdbidApii() {
        return videoService.add_bid(savePref.getUserId(), oid, coins, coins);
    }

    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(CategoryDetailsActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_orderconfirmed);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.layoutmypurchases);
        LinearLayout ratinglayout = (LinearLayout) dialog.findViewById(R.id.layoutrateapp);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(CategoryDetailsActivity.this, GetOrderActivity.class);
                startActivity(i);
            }
        });

        ratinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            getprofile();
        else{
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gettime() {
        currentDate= String.valueOf(java.time.LocalDate.now());
        currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
        curr_dt_time=currentDate+" "+currentTime;
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }
}