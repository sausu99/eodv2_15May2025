package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.wowcodes.supreme.Adapter.BannerAdapter;
import com.wowcodes.supreme.Adapter.DiffCategoryRedeemAdapter;
import com.wowcodes.supreme.Adapter.MyRedeemAdapter;
import com.wowcodes.supreme.Modelclas.CategoryHorizontal;
import com.wowcodes.supreme.Modelclas.GetRedeem;
import com.wowcodes.supreme.Modelclas.ItemRedeem;
import com.wowcodes.supreme.Modelclas.getcity;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends AppCompatActivity implements MyRedeemAdapter.categorySel, DiffCategoryRedeemAdapter.CategorySel{

    SavePref savePref;
    String city , category;
    Boolean responseReceived = false;
    Spinner spinner1;
    MyRedeemAdapter.categorySel catSelected;
    DiffCategoryRedeemAdapter.CategorySel CatSelected;
    Handler slideHandler = new Handler();
    MyRedeemAdapter myAdapter;
    public BindingService videoService;
    RecyclerView recyclerViewCategory  , recyclerViewHorizontal;
    ViewPager2 recyclerView;
    List<GetRedeem.JSONDATum> dataList = new ArrayList<>();
    BannerAdapter bannerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout mShimmerViewContainer;
    ImageView locimage,imgBackk;
    LinearLayout nodatalayout;
    TextView txtAucname;
    public static boolean city_shop_changed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_redeem);

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        recyclerView = findViewById(R.id.recyclerViewGame);
        recyclerViewCategory = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        recyclerViewHorizontal = findViewById(R.id.horizontalRecyclerView);
          swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        nodatalayout = (LinearLayout) findViewById(R.id.noitems);
        category = "Featured Items";
        savePref=new SavePref(this);
        txtAucname=findViewById(R.id.txtAucname);
        imgBackk=findViewById(R.id.imgBackk);
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(RedeemActivity.this, RecyclerView.HORIZONTAL, false));
        horizontalCategory(city_shop_changed);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtAucname.setText(getString(R.string.withdraw_coins));
        catSelected = this;
        CatSelected = this;
        new CountDownTimer(10000, 20000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                if (!responseReceived)
                    openalertdialog();
            }
        }.start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                horizontalCategory(true);
                Toast.makeText(RedeemActivity.this, R.string.string169, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        isNetworkConnected();

    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,3500);
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) RedeemActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            getcity();
            if(city_shop_changed) {
                city_shop_changed=false;
                setUp(true);
            }
            else
                setUp(false);
        } else {
            Intent intent = new Intent(RedeemActivity.this, NoInternetActivity.class);
            startActivity(intent);
        }
        if (cm.getActiveNetworkInfo() != null) {
            cm.getActiveNetworkInfo().isConnected();
        }
    }

    private void horizontalCategory(boolean refresh){
        if(!StaticData.redeemlist.isEmpty() && !refresh){
            responseReceived=true;
            dataList=StaticData.redeemlist;
            sethorizontaldata(false);
        }
        else {
            getCategoriesCall().enqueue(new Callback<GetRedeem>() {
                @Override
                public void onResponse(Call<GetRedeem> call, Response<GetRedeem> response) {
                    responseReceived = true;
                    dataList = response.body().getJsonData();
                    StaticData.redeemlist=dataList;
                    sethorizontaldata(true);
                }

                @Override
                public void onFailure(Call<GetRedeem> call, Throwable t) {
                    nodatalayout.setVisibility(VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(GONE);
                }
            });
        }
    }

    public void sethorizontaldata(boolean refresh){
        List<CategoryHorizontal> categoryList = new ArrayList<>();
        List<String> list = new ArrayList<>();

        try {
            Collections.sort(dataList, (o1, o2) -> {
                int catId1 = Integer.parseInt(o1.getcId());
                int catId2 = Integer.parseInt(o2.getcId());
                return Integer.compare(catId1, catId2);
            });
        }catch (Exception ignore){}

        for (GetRedeem.JSONDATum item : dataList) {
            CategoryHorizontal cz = new CategoryHorizontal();
            if(!list.contains(item.getcName())  && Objects.equals(item.getCity_id(), savePref.getCityId()) && !Objects.equals(item.getcName(), "Home banner")){
                list.add(item.getcName());
                cz.setName(item.getcName());
                cz.setImage(item.getcImage());
                cz.setId(item.getcId());
                categoryList.add(cz);
            }
        }

        Collections.sort(categoryList, (o1, o2) -> {
            int catId1 = Integer.parseInt(o1.getId());
            int catId2 = Integer.parseInt(o2.getId());
            return Integer.compare(catId1, catId2);
        });
        myAdapter = new MyRedeemAdapter(categoryList , dataList ,catSelected);
        recyclerViewHorizontal.setAdapter(myAdapter);

        if(refresh)
            setUp(true);
    }

    private void setUp(boolean refresh) {
        if(!StaticData.redeemlist.isEmpty() && !refresh){
            responseReceived=true;
            dataList=StaticData.redeemlist;
            setcategorydata();
        }
        else {
            getCategoriesCall().enqueue(new Callback<GetRedeem>() {
                @Override
                public void onResponse(Call<GetRedeem> call, Response<GetRedeem> response) {
                    responseReceived = true;
                    dataList = response.body().getJsonData();
                    StaticData.redeemlist=dataList;
                    setcategorydata();
                }

                @Override
                public void onFailure(Call<GetRedeem> call, Throwable t) {
                    nodatalayout.setVisibility(VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(GONE);
                }
            });
        }
    }

    public void setcategorydata(){
        try {
            if (dataList.size() == 1) {
                nodatalayout.setVisibility(VISIBLE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(GONE);
            } else {
                nodatalayout.setVisibility(GONE);
                List<GetRedeem.JSONDATum> accToCountry = new ArrayList<>();
                for (GetRedeem.JSONDATum item : dataList) {
                    if (Objects.equals(item.getCity_id(), savePref.getCityId()))
                        accToCountry.add(item);
                }
                setBanners(accToCountry);

                List<GetRedeem.JSONDATum> removedBanners = new ArrayList<>();
                for (GetRedeem.JSONDATum item : accToCountry) {
                    if (!"1".equals(item.getcId()))
                        removedBanners.add(item);
                }

                List<ItemRedeem> itemCategory = new ArrayList<>();
                for (GetRedeem.JSONDATum item : removedBanners) {
                    String catId = item.getcId();
                    String title = item.getcName();
                    String color = item.getcColor();
                    String description = item.getcDesc();
                    String imageUrl = item.getcImage();
                    ItemRedeem existingCategory = null;
                    for (ItemRedeem category : itemCategory) {
                        if (category.getTitle().equals(title)) {
                            existingCategory = category;
                            break;
                        }
                    }

                    List<GetRedeem.JSONDATum> items = new ArrayList<>();
                    if (existingCategory == null) {
                        items.add(item);
                        ItemRedeem newItemCategory = new ItemRedeem(catId, title, color, description, imageUrl, items);
                        itemCategory.add(newItemCategory);
                    } else {
                        existingCategory.getItems().add(item);
                    }
                }

                Collections.sort(itemCategory, (o1, o2) -> {
                    int catId1 = Integer.parseInt(o1.getCatId());
                    int catId2 = Integer.parseInt(o2.getCatId());
                    return Integer.compare(catId1, catId2);
                });

                recyclerViewCategory.setAdapter(new DiffCategoryRedeemAdapter(RedeemActivity.this, itemCategory, CatSelected,  "shop"));
                recyclerViewCategory.setLayoutManager(new LinearLayoutManager(RedeemActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(GONE);
        } catch (Exception e) {
            nodatalayout.setVisibility(VISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(GONE);
        }
    }

    private void setBanners(List<GetRedeem.JSONDATum> dataList) {
        List<String> banners = new ArrayList<>();
        List<String> banner_links = new ArrayList<>();
        List<String> banner_names = new ArrayList<>();
        for (GetRedeem.JSONDATum item : dataList) {
            if ("1".equals(item.getcId())) {
                banners.add(item.getoImage());
                banner_links.add(item.getoLink());
                banner_names.add(item.getoName());
            }
        }

        bannerAdapter = new BannerAdapter(RedeemActivity.this, banners,banner_links, banner_names,recyclerView);
        recyclerView.setAdapter(bannerAdapter);
        recyclerView.setClipToPadding(false);
        //recyclerView.setVisibility(GONE);
        recyclerView.setClipChildren(false);
        recyclerView.setOffscreenPageLimit(3);
        recyclerView.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        recyclerView.setPageTransformer(compositePageTransformer);
        recyclerView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,3500);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            recyclerView.setCurrentItem(recyclerView.getCurrentItem() +1);
        }
    };



    private Call<GetRedeem> getCategoriesCall() {
        return videoService.get_redeem(savePref.getCityId(),savePref.getUserId());
    }

    public void getcity() {
        try {
            callgetcity().enqueue(new Callback<getcity>() {
                @Override
                public void onResponse(Call<getcity> call, Response<getcity> response) {

                    try {
                        ArrayList<getcity.get_city_Inner> arrayList = response.body().getJSON_DATA();
                        Log.e("city", arrayList.toString());
                        String[] citys = new String[arrayList.size()];
                        Glide.with(RedeemActivity.this).load(arrayList.get(0).getCity_image()).placeholder(R.drawable.location).into(locimage);
                        for (int l = 0; l < arrayList.size(); l++) {
                            citys[l] = arrayList.get(l).getCity_name();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RedeemActivity.this, R.layout.spinner_item, citys);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner1.setAdapter(adapter);
                        if (!Objects.equals(savePref.getCityId(), "0")) {
                            for (int l = 0; l < arrayList.size(); l++) {
                                if (Objects.equals(savePref.getCityId(), arrayList.get(l).getCity_id())) {
                                    spinner1.setSelection(l);
                                    Glide.with(RedeemActivity.this).load(arrayList.get(l).getCity_image()).placeholder(R.drawable.location).into(locimage);
                                    break;
                                }
                            }
                        }
                        city = String.valueOf(spinner1.getSelectedItem());
                        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                Log.e("city chcek", "fsdf");
                                // your code here
                                Glide.with(RedeemActivity.this).load(arrayList.get(position).getCity_image()).placeholder(R.drawable.location).into(locimage);
                                if (!Objects.equals(city, String.valueOf(spinner1.getSelectedItem()))) {
                                    city = String.valueOf(spinner1.getSelectedItem());
                                    savePref.setCity(city);
                                    savePref.setCityId(arrayList.get(position).getCity_id());
                                    Intent intent = new Intent(RedeemActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<getcity> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Call<getcity> callgetcity() {
        return videoService.get_city();
    }

    private void openalertdialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(RedeemActivity.this)
                .setTitle("Slow Internet Connection").setMessage(
                        "Please check your internet connection!");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void sendData(String cateName,String c_id){
        category = cateName;

        Intent intent = new Intent(RedeemActivity.this, CategorySelected.class);
        intent.putExtra("category" , cateName);
        intent.putExtra("c_id" , c_id);
        intent.putExtra("type" , "shop");
        startActivity(intent);

        setUp(false);
//        Toast.makeText(getContext() , cateName , Toast.LENGTH_SHORT).show();
    }


}