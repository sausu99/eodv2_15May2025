/**
 * The LiveFragment class is a fragment in an Android app that displays banners, categories, and
 * items related to a game zone.
 */
package com.wowcodes.supreme.Fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.wowcodes.supreme.Activity.CategorySelected;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.NoInternetActivity;
import com.wowcodes.supreme.Adapter.BannerAdapter;
import com.wowcodes.supreme.Adapter.DiffCategoryAdapter;
import com.wowcodes.supreme.Adapter.MyAdapter;
import com.wowcodes.supreme.Modelclas.CategoryHorizontal;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.ItemCategory;
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

public class LiveFragment extends Fragment implements MyAdapter.categorySel,DiffCategoryAdapter.CategorySel{
    SavePref savePref;
    String city , category;
    Boolean responseReceived = false;
    Spinner spinner1;
    MyAdapter.categorySel catSelected;
    DiffCategoryAdapter.CategorySel CatSelected;
    Handler slideHandler = new Handler();
    MyAdapter myAdapter;

    public BindingService videoService;
    RecyclerView  recyclerViewCategory  , recyclerViewHorizontal;
    ViewPager2 recyclerView;
    List<GetCategories.JSONDATum> dataList = new ArrayList<>();
    BannerAdapter bannerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout mShimmerViewContainer;
    ImageView locimage;
    LinearLayout nodatalayout,loadinglayout;
    public static boolean city_live_changed=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        savePref = new SavePref(getContext());
        final View view = inflater.inflate(R.layout.fragment_live, container, false);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        recyclerView = view.findViewById(R.id.recyclerViewGame);
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        recyclerViewHorizontal = view.findViewById(R.id.horizontalRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        nodatalayout = (LinearLayout) view.findViewById(R.id.noitems);
        loadinglayout=view.findViewById(R.id.linearlay);
        category = "Featured Items";
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        horizontalCategory(city_live_changed);

        catSelected = this;
        CatSelected = this;


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                horizontalCategory(true);
                Toast.makeText(getActivity(), R.string.string169, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        isNetworkConnected();
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        horizontalCategory(true);
        slideHandler.postDelayed(sliderRunnable,3500);
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            getcity();
            if(city_live_changed) {
                city_live_changed=false;
                setUp(true);
            }
            else
                setUp(false);
        } else {
            Intent intent = new Intent(getContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void horizontalCategory(boolean refresh){
        showLoadingLayout();  // Show loading layout when the request starts

        if(!StaticData.livefragmentList.isEmpty() && !refresh){
            responseReceived=true;
            dataList=StaticData.livefragmentList;
            sethorizontaldata(false);
            hideLoadingLayout();  // Hide loading layout when data is already available
        }
        else {
            getCategoriesCall().enqueue(new Callback<GetCategories>() {
                @Override
                public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                    responseReceived = true;
                    dataList = response.body().getJsonData();
                    StaticData.livefragmentList=dataList;
                    sethorizontaldata(true);
                    hideLoadingLayout();  // Hide loading layout when data is already available

                }
                @Override
                public void onFailure(Call<GetCategories> call, Throwable t) {
                    nodatalayout.setVisibility(VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(GONE);
                    hideLoadingLayout();  // Hide loading layout when data is already available

                }
            });
        }
    }

    public void sethorizontaldata(boolean refresh) {
        // Show loading layout when the request starts
        showLoadingLayout();

        List<CategoryHorizontal> categoryList = new ArrayList<>();
        List<String> list = new ArrayList<>();

        try {
            Collections.sort(dataList, (o1, o2) -> {
                int catId1 = Integer.parseInt(o1.getcId());
                int catId2 = Integer.parseInt(o2.getcId());
                return Integer.compare(catId1, catId2);
            });
        } catch (Exception ignore) {
        }

        // Process dataList to build categoryList
        for (GetCategories.JSONDATum item : dataList) {
            CategoryHorizontal cz = new CategoryHorizontal();
            // Check for unique category and match the city
            if (!list.contains(item.getcName()) && Objects.equals(item.getCity_id(), savePref.getCityId()) && !Objects.equals(item.getcName(), "Home banner")) {
                list.add(item.getcName());
                cz.setName(item.getcName());
                cz.setImage(item.getcImage());
                cz.setId(item.getcId());

                // Use .equals() for string comparison to determine "upcoming" or "live"
                if (item.getoType().equals("4") || item.getoType().equals("5")) {
                    cz.setOtype("upcoming");  // Set Otype to "upcoming"
                } else {
                    cz.setOtype("live");  // Set Otype to "live"
                }
                categoryList.add(cz);
            }
        }

        Collections.sort(categoryList, (o1, o2) -> {
            int catId1 = Integer.parseInt(o1.getId());
            int catId2 = Integer.parseInt(o2.getId());
            return Integer.compare(catId1, catId2);
        });

        // Instead of hardcoding "live", we pass each category's oType to the adapter
        myAdapter = new MyAdapter(categoryList, dataList, catSelected);
        recyclerViewHorizontal.setAdapter(myAdapter);

        hideLoadingLayout();  // Hide the loading layout

        if (refresh) {
            setUp(true);
        }
    }

    private void setUp(boolean refresh) {
        showLoadingLayout();  // Show loading layout when the request starts

        if(!StaticData.livefragmentList.isEmpty() && !refresh){
            responseReceived=true;
            dataList=StaticData.livefragmentList;
            setcategorydata();
            hideLoadingLayout();  // Hide loading layout when data is already available

        }
        else {
            getCategoriesCall().enqueue(new Callback<GetCategories>() {
                @Override
                public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                    responseReceived = true;
                    dataList = response.body().getJsonData();
                    StaticData.livefragmentList=dataList;
                    setcategorydata();
                    hideLoadingLayout();  // Hide loading layout when data is already available
                }
                @Override
                public void onFailure(Call<GetCategories> call, Throwable t) {
                    nodatalayout.setVisibility(VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(GONE);
                    hideLoadingLayout();  // Hide loading layout when data is already available
                }
            });
        }
    }

    public void setcategorydata(){
        showLoadingLayout();  // Show loading layout when the request starts
        try {
            if (dataList.size() == 1) {
                nodatalayout.setVisibility(VISIBLE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(GONE);
            } else {
                nodatalayout.setVisibility(GONE);
                List<GetCategories.JSONDATum> accToCountry = new ArrayList<>();
                for (GetCategories.JSONDATum item : dataList) {
                    if (Objects.equals(item.getCity_id(), savePref.getCityId()))
                        accToCountry.add(item);
                }
                setBanners(accToCountry);

                List<GetCategories.JSONDATum> removedBanners = new ArrayList<>();
                for (GetCategories.JSONDATum item : accToCountry) {
                    if (!"1".equals(item.getcId()))
                        removedBanners.add(item);
                }
                List<ItemCategory> itemCategory = new ArrayList<>();
                for (GetCategories.JSONDATum item : removedBanners) {
                    String catId = item.getcId();
                    String title = item.getcName();
                    String color = item.getcColor();
                    String description = item.getcDesc();
                    String imageUrl = item.getcImage();
                    ItemCategory existingCategory = null;
                    for (ItemCategory category : itemCategory) {
                        if (category.getTitle().equals(title)) {
                            existingCategory = category;
                            break;
                        }
                    }
                    List<GetCategories.JSONDATum> items = new ArrayList<>();
                    if (existingCategory == null) {
                        items.add(item);
                        ItemCategory newItemCategory = new ItemCategory(catId, title, color, description, imageUrl, items);
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

                recyclerViewCategory.setAdapter(new DiffCategoryAdapter(getContext(), itemCategory, CatSelected,"live"));
                recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(GONE);
        } catch (Exception e) {
            nodatalayout.setVisibility(VISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(GONE);
        }
        hideLoadingLayout();  // Hide loading layout when data is already available

    }

    private void setBanners(List<GetCategories.JSONDATum> dataList) {
        showLoadingLayout();  // Show loading layout when the request starts
        List<String> banners = new ArrayList<>();
        List<String> banner_links = new ArrayList<>();
        List<String> banner_names = new ArrayList<>();
        for (GetCategories.JSONDATum item : dataList) {
            if ("1".equals(item.getcId())) {
                banners.add(item.getoImage());
                banner_links.add(item.getoLink());
                banner_names.add(item.getoName());
            }
        }

        bannerAdapter = new BannerAdapter(requireContext(), banners,banner_links, banner_names,recyclerView);
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
                //select_dot(recyclerView.getCurrentItem());
            }
        });
        hideLoadingLayout();  // Hide loading layout when data is already available


    }

    private Runnable sliderRunnable = new Runnable() {
        @Override public void run() {
            recyclerView.setCurrentItem(recyclerView.getCurrentItem() +1);
        }
    };

    private Call<GetCategories> getCategoriesCall() {
        return videoService.get_categories(savePref.getCityId(),savePref.getUserId());
    }

    public void getcity() {
        showLoadingLayout();  // Show loading layout when the request starts
        try {
            callgetcity().enqueue(new Callback<getcity>() {
                @Override
                public void onResponse(Call<getcity> call, Response<getcity> response) {

                    try {
                        ArrayList<getcity.get_city_Inner> arrayList = response.body().getJSON_DATA();
                        Log.e("city", arrayList.toString());
                        String[] citys = new String[arrayList.size()];
                        Glide.with(LiveFragment.this).load(arrayList.get(0).getCity_image()).placeholder(R.drawable.location).into(locimage);
                        for (int l = 0; l < arrayList.size(); l++) {
                            citys[l] = arrayList.get(l).getCity_name();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, citys);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner1.setAdapter(adapter);
                        if (!Objects.equals(savePref.getCityId(), "0")) {
                            for (int l = 0; l < arrayList.size(); l++) {
                                if (Objects.equals(savePref.getCityId(), arrayList.get(l).getCity_id())) {
                                    spinner1.setSelection(l);
                                    Glide.with(LiveFragment.this).load(arrayList.get(l).getCity_image()).placeholder(R.drawable.location).into(locimage);
                                    break;
                                }
                            }
                        }
                        city = String.valueOf(spinner1.getSelectedItem());
                        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                Glide.with(LiveFragment.this).load(arrayList.get(position).getCity_image()).placeholder(R.drawable.location).into(locimage);
                                if (!Objects.equals(city, String.valueOf(spinner1.getSelectedItem()))) {
                                    city = String.valueOf(spinner1.getSelectedItem());
                                    savePref.setCity(city);
                                    savePref.setCityId(arrayList.get(position).getCity_id());
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            @Override public void onNothingSelected(AdapterView<?> parentView) {}
                        });
                    } catch (Exception ignore) {              }

                    hideLoadingLayout();  // Hide loading layout when data is already available
                }

                @Override public void onFailure(Call<getcity> call, Throwable t) {
                    hideLoadingLayout();  // Hide loading layout when data is already available

                }
            });
        } catch (Exception ignore) {}
    }

    private Call<getcity> callgetcity() {
        return videoService.get_city();
    }
    private void showLoadingLayout() {
        loadinglayout.setVisibility(View.VISIBLE);  // Show loading layout
        recyclerView.setVisibility(View.GONE);      // Hide the RecyclerView
        nodatalayout.setVisibility(View.GONE);      // Hide the no data layout
        mShimmerViewContainer.setVisibility(View.GONE);  // Hide shimmer
        mShimmerViewContainer.stopShimmerAnimation();    // Stop shimmer animation if running
    }
    private void hideLoadingLayout() {
        loadinglayout.setVisibility(View.GONE);     // Hide loading layout
        recyclerView.setVisibility(View.VISIBLE);   // Show the RecyclerView
    }


    @Override
    public void sendData(String cateName, String c_id,String from) throws IllegalAccessException, java.lang.InstantiationException {
        category = cateName;
        Intent intent = new Intent(getContext(), CategorySelected.class);
        intent.putExtra("category" , cateName);
        intent.putExtra("c_id" , c_id);
        intent.putExtra("type" , from);
        startActivity(intent);
        setUp(true);
    }


}