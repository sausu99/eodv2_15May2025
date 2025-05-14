/**
 * The CategorySelected class is an activity in an Android app that displays a list of categories and
 * their items, fetched from an API, in a RecyclerView.
 */
package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Adapter.DiffCategoryAdapter;
import com.wowcodes.supreme.Modelclas.CategoryHoriAdapter;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.ItemCategory;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySelected extends AppCompatActivity {

    RecyclerView recyclerViewCategory;
    public BindingService videoService;
    TextView txtView;
    Boolean responseReceived = false;
    ImageView imageView;
    private DiffCategoryAdapter.CategorySel catsel;
    private String categoryTitle;
    private String categoryId;
    private boolean showAllItems;

    SavePref savePref;
    List<GetCategories.JSONDATum> dataList = new ArrayList<>();
    String category,c_id,from_type;
    ArrayList<GetCategories.JSONDATum> currentItems;
    LinearLayout activityloading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_category_selected);

        category = getIntent().getStringExtra("category");
        c_id = getIntent().getStringExtra("c_id");
        from_type=getIntent().getStringExtra("type");
        imageView = findViewById(R.id.imgBackk);
        txtView = findViewById(R.id.txtAucname);
        activityloading = findViewById(R.id.linearlay);
        // Initialize the loading layout
        activityloading.setVisibility(View.VISIBLE); // Set the loading layout visible initially
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setVisibility(View.GONE);
        txtView.setText(category);
        imageView.setVisibility(View.VISIBLE);
        txtView.setVisibility(GONE);
        savePref = new SavePref(this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService .class);

        imageView.setOnClickListener(view ->
                finish()
        );
        setUp();
    }

    private Call<GetCategories> getCategoriesCall() {
        if (from_type != null) {
            if (from_type.equalsIgnoreCase("shop"))
                return videoService.get_category_item(savePref.getCityId(), c_id, "shop");
            else if (from_type.equalsIgnoreCase("upcoming"))
                return videoService.get_category_item(savePref.getCityId(), c_id, "upcoming");
            else
                return videoService.get_category_item(savePref.getCityId(), c_id, "live");
        } else {
            // Handle the case where from_type is null
            // You might want to return an empty Call or handle this scenario in another way
            from_type="upcoming";
            return videoService.get_category_item(savePref.getCityId(), c_id, "upcoming");
        }
    }


    private void setUp() {
        getCategoriesCall().enqueue(new Callback<GetCategories>() {
            @Override
            public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                responseReceived = true;
                try {
                    List<GetCategories.JSONDATum> newDataList = response.body().getJsonData();
                    List<GetCategories.JSONDATum> accToCountry = new ArrayList<>();
                    for (GetCategories.JSONDATum item : newDataList) {
                        if (Objects.equals(item.getCity_id(), savePref.getCityId())) {
                            accToCountry.add(item);
                        }
                    }

                    if (accToCountry.size() > 0) {
                        List<ItemCategory> itemCategory = new ArrayList<>();
                        for (GetCategories.JSONDATum item : accToCountry) {
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

                            ArrayList<GetCategories.JSONDATum> items = new ArrayList<>();
                            if (Objects.equals(item.getcName(), category)) {
                                if (existingCategory == null) {
                                    items.add(item);
                                    ItemCategory newItemCategory = new ItemCategory(catId, title, color, description, imageUrl, items);
                                    itemCategory.add(newItemCategory);
                                } else {
                                    existingCategory.getItems().add(item);
                                }
                            }

                            currentItems = items;
                        }

                        recyclerViewCategory.setVisibility(View.VISIBLE);
                        refreshCategoryList(itemCategory);
                        txtView.setVisibility(View.VISIBLE);
                        activityloading.setVisibility(View.GONE); // Hide the loading layout after the RecyclerView is populated

                    }
                } catch (Exception ignore) {
                }
            }

            @Override
            public void onFailure(Call<GetCategories> call, Throwable t) {
                  activityloading.setVisibility(View.GONE); // Hide the loading layout after the RecyclerView is populated
            }
        });
    }

    private void refreshCategoryList(List<ItemCategory> itemCategory) {
        CategoryHoriAdapter adapter = (CategoryHoriAdapter) recyclerViewCategory.getAdapter();
        if (adapter != null) {
            adapter.updateCategories(itemCategory);
        } else {
            adapter = new CategoryHoriAdapter(CategorySelected.this, itemCategory, from_type);
            recyclerViewCategory.setAdapter(adapter);
            recyclerViewCategory.setLayoutManager(new LinearLayoutManager(CategorySelected.this, LinearLayoutManager.VERTICAL, false));
        }
    }
}