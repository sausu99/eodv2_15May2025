package com.wowcodes.supreme.Activity;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.wowcodes.supreme.Adapter.ReviewAdapter;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.AllReviewer;
import com.wowcodes.supreme.Modelclas.GetSellerDetails;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.Modelclas.ReviewModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopItemsActivity extends AppCompatActivity {
    SavePref savePref;
    String itemId;
    private static final String DEEP_LINK_URL = "https://bidkingapp.page.link/7Yoh";
    BindingService videoService;
    ImageView imgBackBtn, imgShareBtn,p0,p1,p2,p3,p4,wishlistbtn,redheartbtn,leftimgbtn,rightimgbtn,sellerrating1,sellerrating2,sellerrating3,sellerrating4,sellerrating5,sellerimg;
    TextView txtQtyStock,txtItemName, txtItemDesc,buynow,price,walletbal,noreviewtext,reviewfailure,noratingstar,sellername,sellerabouttxt,otherlisting,discount;
    LinearLayout points,layoutbottom,ratingstars;
    RecyclerView recycler_view,seller_items,moreitems;
    ViewPager imgpager;
    String name, type, etime, edate, image, image1, image2, image3, image4, desc, coins, SellerName, SellerAbout, SellerLink, SellerImage,start_date, qty ,oid="", totalWallet, oAmt,  colorcode, umax, cdesc, olink, olimit, seller,curr_dt_time = "",currentDate="",currentTime="",totalbids = "0", availableqty = "0",SellerJoinDate,claimable="",wishliststatus,Sellerrating;
    int qtyO;
    CardView soldby_card;
    RelativeLayout txt_morelikethis;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(ShopItemsActivity.this);
        if(savePref.getLang() == null)
            savePref.setLang("en");

        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        setContentView(R.layout.activity_shop_items);

        imgBackBtn = findViewById(R.id.imgBackk);
        imgShareBtn = findViewById(R.id.ShareBtn);
        walletbal = findViewById(R.id.walletbal);
        imgpager=findViewById(R.id.image_pager);

        p0=findViewById(R.id.p0);
        p1=findViewById(R.id.p1);
        p2=findViewById(R.id.p2);
        p3=findViewById(R.id.p3);
        p4=findViewById(R.id.p4);
        discount=findViewById(R.id.discount);
        points=findViewById(R.id.pointslay);
        txtItemName =findViewById(R.id.o_name);
        wishlistbtn =findViewById(R.id.wishlistbtn);
        redheartbtn =findViewById(R.id.redheartbtn);
        txtItemDesc =findViewById(R.id.itemDescr);
        txtQtyStock =findViewById(R.id.txtQtyStock);
        recycler_view =findViewById(R.id.recycler_view);
        buynow=findViewById(R.id.txtContinue);
        leftimgbtn=findViewById(R.id.leftimgbtn);
        rightimgbtn=findViewById(R.id.rightimgbtn);
        noreviewtext=findViewById(R.id.noreviewtxt);
        reviewfailure=findViewById(R.id.fetchingfailure);
        price=findViewById(R.id.amount);
        sellerrating1=findViewById(R.id.sellerrating1);
        sellerrating2=findViewById(R.id.sellerrating2);
        sellerrating3=findViewById(R.id.sellerrating3);
        sellerrating4=findViewById(R.id.sellerrating4);
        sellerrating5=findViewById(R.id.sellerrating5);
        sellerimg=findViewById(R.id.sellerimg);
        sellername=findViewById(R.id.sellername);
        sellerabouttxt=findViewById(R.id.sellerabouttxt);
        ratingstars=findViewById(R.id.rating_stars);
        noratingstar=findViewById(R.id.noratingstar);
        otherlisting=findViewById(R.id.otherlisting);
        soldby_card=findViewById(R.id.soldby_card);
        moreitems=findViewById(R.id.moreitems);
        txt_morelikethis=findViewById(R.id.txt_morelikethis);
        moreitems.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        isNetworkConnected();
        getprofile();
        getReview();
        getSeller();
        handledynamiclinks();

        //mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        imgShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(ShopItemsActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }
                try {
                    shareDeepLink();
                } catch (Exception ignore) {}
            }
        });

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

        wishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtowishlist();
                wishlistbtn.setVisibility(GONE);
                redheartbtn.setVisibility(VISIBLE);

            }
        });

        redheartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletefromwishlist();
                wishlistbtn.setVisibility(VISIBLE);
                redheartbtn.setVisibility(GONE);
            }
        });

        otherlisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SellerName != null && !SellerName.isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            openWindialog();
                    } else
                        Toast.makeText(getApplicationContext(), "Seller name is not specified.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
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
                oid = getIntent().getStringExtra("O_id");
                qty = getIntent().getStringExtra("qty");
                oAmt = getIntent().getStringExtra("oamt");
                colorcode = getIntent().getStringExtra("colorcode");
                umax = getIntent().getStringExtra("umax");
                cdesc = getIntent().getStringExtra("cdesc");
                olink = getIntent().getStringExtra("link");
                olimit = getIntent().getStringExtra("limit");
                totalbids = getIntent().getStringExtra("totalbids");
                wishliststatus = getIntent().getStringExtra("wishliststatus");
                seller = getIntent().getStringExtra("id");
                itemId=getIntent().getStringExtra("itemId");
                if(getIntent().hasExtra("claimable"))
                    claimable=getIntent().getStringExtra("claimable");
                if (TextUtils.isEmpty(olimit))
                    olimit = "1";
                else
                    olimit = getIntent().getStringExtra("limit");
                if (wishliststatus==null)
                    wishliststatus="0";

                qtyO = Integer.parseInt(qty);
            }
            availableqty = String.valueOf(Integer.parseInt(qty) - Integer.parseInt(totalbids));
        } catch (Exception ignore) {}

        if (colorcode != null && !colorcode.isEmpty()) {
            //imgCategory.getBackground().setColorFilter(Color.parseColor("#" + colorcode), PorterDuff.Mode.SRC_ATOP);
            //layoutbottom.getBackground().setColorFilter(Color.parseColor("#" + colorcode), PorterDuff.Mode.SRC_ATOP);
            //getWindow().setStatusBarColor(Color.parseColor("#" + colorcode));
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        txtItemName.setText(name);
        Log.d("oid", oid);
        if(Integer.parseInt(availableqty) <= 0)
            txtQtyStock.setText(getString(R.string.string319));
        else
            txtQtyStock.setText(getString(R.string.hurry_up).replace("44",availableqty));
        if(type.equalsIgnoreCase("4")) {
            price.setText(oAmt + " " + getString(R.string.coins));
            txtQtyStock.setVisibility(GONE);
            soldby_card.setVisibility(GONE);
        }
        else {
            if(type.equalsIgnoreCase("3"))
                price.setText(oAmt + " " + getString(R.string.coins));
            else{
                // Parse the coins and oAmt values to integers
                int coinsInt = Integer.parseInt(coins);
                int oAmtInt = Integer.parseInt(oAmt);

                double offerprice = ((double) (coinsInt - oAmtInt) / coinsInt) * 100;

                String off = String.valueOf((int) offerprice);
                discount.setText(off+"% "+getString(R.string.off));
                price.setText(MainActivity.currency+" "+oAmt);}
            txtQtyStock.setVisibility(VISIBLE);
            soldby_card.setVisibility(VISIBLE);
        }

        if (Objects.equals(wishliststatus, "1") ||Integer.parseInt(wishliststatus)>=1) {
            wishlistbtn.setVisibility(GONE);
            redheartbtn.setVisibility(VISIBLE);
        }else if(Objects.equals(wishliststatus, "0")){
            wishlistbtn.setVisibility(VISIBLE);
            redheartbtn.setVisibility(GONE);
        }

        if (desc== null||desc.isEmpty())
            txtItemDesc.setText("No Description");
        else {
            CharSequence sequence;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                sequence = Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY);
            else
                sequence = Html.fromHtml(desc);

            Linkify.addLinks(txtItemDesc, Linkify.WEB_URLS);
            txtItemDesc.setText(sequence);
            txtItemDesc.setMovementMethod(LinkMovementMethod.getInstance());
        }


        List<String> images=new ArrayList<>();
        if(!image.endsWith("/"))
            images.add(image);
        if (image1 != null && !image1.endsWith("/")) {
            images.add(image1);
        }
        if (image2 != null && !image2.endsWith("/")) {
            images.add(image2);
        }
        if (image3 != null && !image3.endsWith("/")) {
            images.add(image3);
        }
        if (image4 != null && !image4.endsWith("/")) {
            images.add(image4);
        }

        if(images.size()==1) {
            points.setVisibility(View.GONE);
            leftimgbtn.setVisibility(GONE);
            rightimgbtn.setVisibility(GONE);
        }
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

        imgpager.setAdapter(new ImagesAdapter(ShopItemsActivity.this,images));
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

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(savePref.getUserId(), "0")) {
                    Intent intent = new Intent(ShopItemsActivity.this, LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }else {
                    try {
                        if (Integer.parseInt(availableqty) > 0){
                            if (type.equals("9") || type.equals("3") || type.equals("4")) {
                                // Take user to the address selection page
                                if (olink != null && isValidUrl(olink))  {
                                    // Open the WebViewActivity with the URL
                                    Intent webIntent = new Intent(ShopItemsActivity.this, WebViewActivity.class);
                                    webIntent.putExtra("url", olink);
                                    webIntent.putExtra("title",name);
                                    webIntent.putExtra("from", "categories");

                                    Log.d("olink",olink );
                                    startActivity(webIntent);
                                } else {
                                    // Take user to the address selection page
                                    Intent intent = new Intent(ShopItemsActivity.this, AddressSelectionActivity.class);
                                    intent.putExtra("oid", oid);
                                    intent.putExtra("oamt", oAmt);
                                    intent.putExtra("otype", type);
                                    intent.putExtra("name", name);
                                    intent.putExtra("link", image);
                                    intent.putExtra("claimable", claimable);
                                    startActivity(intent);
                                }
                            }
                        } else
                            Toast.makeText(getApplicationContext(), R.string.string151, Toast.LENGTH_SHORT).show();
                    }catch(Exception ignore) {}
                }
            }
        });
    }

    private void shareDeepLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String deepLink= Constants.baseurl.substring(0,Constants.baseurl.indexOf("seller/")) + "share.php?share_id="+oid;
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text).replace("watch", name).replace("wowcodes", getString(R.string.app_name))+" "+deepLink);
        startActivity(intent);
    }

    private void handledynamiclinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {@Override public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {}})
                .addOnFailureListener(this, new OnFailureListener() {@Override public void onFailure(@NonNull Exception e) {}});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openWindialog() {
        Dialog dialog=new Dialog(ShopItemsActivity.this);
        dialog.setContentView(R.layout.activity_user_profile);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.getWindow().setLayout(950, dialog.getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().height()-350);
        }
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
        seller_items.setLayoutManager(new LinearLayoutManager(ShopItemsActivity.this));

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
            Glide.with(ShopItemsActivity.this)
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
                    Intent intent = new Intent(ShopItemsActivity.this, WebViewActivity.class);
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
                        seller_items.setAdapter(new ProductListAdapter(ShopItemsActivity.this, arrayList,seller));
                    } catch (Exception ignore) {}
                }
                @Override public void onFailure(Call<GetSellerItems> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void getSeller() {
        try {
            videoService.get_seller().enqueue(new Callback<GetSellerDetails>() {
                @Override
                public void onResponse(Call<GetSellerDetails> call, retrofit2.Response<GetSellerDetails> response) {
                    try {
                        List<GetSellerDetails.JSONDATum> arrayList = new ArrayList<>(response.body().getJsonData());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            GetSellerDetails.JSONDATum reqsellers = arrayList.stream().filter( reqseller -> seller.equals(reqseller.getId())).findAny().orElse(null);
                            SellerName = reqsellers.getName();
                            SellerAbout = reqsellers.getAbout();
                            SellerLink = reqsellers.getLink();
                            SellerJoinDate=reqsellers.getJoin_date();
                            SellerImage = reqsellers.getImage();
                            Sellerrating=reqsellers.getRating();
                            sellername.setText(SellerName);
                            try {
                                Glide.with(ShopItemsActivity.this)
                                        .load(SellerImage)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerInside()
                                        .into(sellerimg);
                            } catch (Exception ignore) {}
                        }

                        switch (Sellerrating){
                            case "1":
                                sellerrating1.setVisibility(VISIBLE);
                                sellerrating2.setVisibility(View.INVISIBLE);
                                sellerrating3.setVisibility(View.INVISIBLE);
                                sellerrating4.setVisibility(View.INVISIBLE);
                                sellerrating5.setVisibility(View.INVISIBLE);
                                break;
                            case "2":
                                sellerrating1.setVisibility(VISIBLE);
                                sellerrating2.setVisibility(VISIBLE);
                                sellerrating3.setVisibility(View.INVISIBLE);
                                sellerrating4.setVisibility(View.INVISIBLE);
                                sellerrating5.setVisibility(View.INVISIBLE);
                                break;
                            case "3":
                                sellerrating1.setVisibility(VISIBLE);
                                sellerrating2.setVisibility(VISIBLE);
                                sellerrating3.setVisibility(VISIBLE);
                                sellerrating4.setVisibility(View.INVISIBLE);
                                sellerrating5.setVisibility(View.INVISIBLE);
                                break;
                            case "4":
                                sellerrating1.setVisibility(VISIBLE);
                                sellerrating2.setVisibility(VISIBLE);
                                sellerrating3.setVisibility(VISIBLE);
                                sellerrating4.setVisibility(VISIBLE);
                                sellerrating5.setVisibility(View.INVISIBLE);
                                break;
                            case "5":
                                sellerrating1.setVisibility(VISIBLE);
                                sellerrating2.setVisibility(View.VISIBLE);
                                sellerrating3.setVisibility(View.VISIBLE);
                                sellerrating4.setVisibility(View.VISIBLE);
                                sellerrating5.setVisibility(VISIBLE);
                                break;
                            default:
                                sellerrating1.setVisibility(GONE);
                                sellerrating2.setVisibility(GONE);
                                sellerrating3.setVisibility(GONE);
                                sellerrating4.setVisibility(GONE);
                                sellerrating5.setVisibility(GONE);
                                noratingstar.setVisibility(VISIBLE);
                                break;
                        }
                        sellerabouttxt.setText(SellerAbout);
                        //moreaboutseller.setClickable(SellerName != null);
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<GetSellerDetails> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void getprofile() {
        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        totalWallet = arrayList.get(0).getWallet();
                        walletbal.setText(totalWallet);
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments are in the back stack, finish the current activity
            finish();
        }
    }

    public void updatewallet(String bd_value) {
        try {
            videoService.add_bid(savePref.getUserId(), oid, bd_value, oAmt).enqueue(new Callback<SuccessModel>() {
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

    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(ShopItemsActivity.this);
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
                Intent i = new Intent(ShopItemsActivity.this, GetOrderActivity.class);
                startActivity(i);
                finish();
            }
        });

        ratinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private boolean isValidUrl(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            getprofile();
        else {
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gettime() {
        currentDate= String.valueOf(LocalDate.now());
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

    public void addtowishlist(){
        try{
            videoService.updateWishlist(savePref.getUserId(), oid,  "add").enqueue(new Callback<WishlistResponse>() {
                @Override
                public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                    if (response.isSuccessful()&& response.body()!= null) {
                        WishlistResponse wishlistResponse = response.body();
                        if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                            WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                            Toast.makeText(ShopItemsActivity.this, "Added to Wishlist: " + message.getMsg(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ShopItemsActivity.this, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ShopItemsActivity.this, "Failed to add to Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                @Override public void onFailure(Call<WishlistResponse> call, Throwable t) {
                    Toast.makeText(ShopItemsActivity.this, "Failed to add to Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ignore){}

    }
    public void deletefromwishlist(){
        try{
            videoService.updateWishlist(savePref.getUserId(), oid,  "delete").enqueue(new Callback<WishlistResponse>() {
                @Override
                public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                    if (response.isSuccessful()&& response.body()!= null) {
                        WishlistResponse wishlistResponse = response.body();
                        if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                            WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                            Toast.makeText(ShopItemsActivity.this, "Deleted from Wishlist: " + message.getMsg() , Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ShopItemsActivity.this, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ShopItemsActivity.this, "Failed to delete from Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                @Override public void onFailure(Call<WishlistResponse> call, Throwable t) {
                    Toast.makeText(ShopItemsActivity.this, "Failed to  delete from  Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ignore){}
    }

    public void getReview(){
        try{
            videoService.getReviews(oid).enqueue(new Callback<ReviewModel>() {
                @Override
                public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<AllReviewer> reviewList = response.body().getJSON_DATA();
                        if (reviewList != null && !reviewList.isEmpty()) {
                            ReviewAdapter adapter = new ReviewAdapter(ShopItemsActivity.this, reviewList);
                            recycler_view.setAdapter(adapter);
                        } else
                            noreviewtext.setVisibility(View.VISIBLE);
                    } else
                        reviewfailure.setVisibility(View.VISIBLE);
                }
                @Override public void onFailure(Call<ReviewModel> call, Throwable t) {}
            });
        }catch (Exception ignore){}
    }

}