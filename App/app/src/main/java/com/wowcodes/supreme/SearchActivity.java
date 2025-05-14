package com.wowcodes.supreme;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.TopDealsAdapter;
import com.wowcodes.supreme.Adapter.AuctionItemAdapter;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {
    RelativeLayout txtTopDeals;
    AutoCompleteTextView search;
    ImageView imgBackk,close,mic,noresults;
    RecyclerView top_deals,items;
    BindingService videoService;
    ArrayList<GetCategories.JSONDATum> ItemsArrayList=new ArrayList<>();
    ArrayList<String> items_list=new ArrayList<>();
    ArrayAdapter<String> searchAdapter;
    AuctionItemAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txtTopDeals=findViewById(R.id.txtTopDeals);
        search=findViewById(R.id.searchview);
        imgBackk=findViewById(R.id.imgBackk);
        close=findViewById(R.id.clear);
        mic=findViewById(R.id.mic);
        top_deals=findViewById(R.id.top_deals);
        items=findViewById(R.id.items);
        noresults=findViewById(R.id.noresults);
        top_deals.setLayoutManager(new LinearLayoutManager(this));
        items.setLayoutManager(new GridLayoutManager(this,2));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search.setEnabled(false);
        search.setThreshold(1);

        load_top_deals();
        load_items();

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(search.getText().toString().isEmpty()){
                    close.setVisibility(GONE);
                    mic.setVisibility(VISIBLE);
                }
                else{
                    close.setVisibility(VISIBLE);
                    mic.setVisibility(GONE);
                }
            }
        });

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                search_item(search.getText().toString());

                search.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search_item(search.getText().toString());

                    search.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(i,7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String s = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            if(s.isEmpty())
                Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            else {
                search.setText(s);

                search.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                search_item(s);
            }
        }
    }

    public void load_top_deals(){
        try {
            videoService.get_top_deals(new SavePref(SearchActivity.this).getCityId()).enqueue(new Callback<GetCategories>() {
                @Override
                public void onResponse(Call<GetCategories> call, retrofit2.Response<GetCategories> response) {
                    ArrayList<GetCategories.JSONDATum> arrayList = (ArrayList<GetCategories.JSONDATum>) response.body().getJsonData();
                    top_deals.setAdapter(new TopDealsAdapter(SearchActivity.this,arrayList));
                }

                @Override public void onFailure(Call<GetCategories> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void load_items(){
        try {
            videoService.get_offers(new SavePref(SearchActivity.this).getCityId()).enqueue(new Callback<GetCategories>() {
                @Override
                public void onResponse(Call<GetCategories> call, retrofit2.Response<GetCategories> response) {
                    ItemsArrayList = (ArrayList<GetCategories.JSONDATum>) response.body().getJsonData();
                    adapter=new AuctionItemAdapter(SearchActivity.this, ItemsArrayList,  "shop", true);
                    items.setAdapter(adapter);

                    if(!ItemsArrayList.isEmpty()) {
                        for (GetCategories.JSONDATum item : ItemsArrayList)
                            items_list.add(item.getoName());

                        searchAdapter=new ArrayAdapter<String>(SearchActivity.this, android.R.layout.select_dialog_item,items_list);
                        search.setAdapter(searchAdapter);
                        search.setEnabled(true);
                        searchAdapter.notifyDataSetChanged();
                    }
                }

                @Override public void onFailure(Call<GetCategories> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void search_item(String s){
        ArrayList<GetCategories.JSONDATum> filteredList=new ArrayList<>();
        txtTopDeals.setVisibility(GONE);
        top_deals.setVisibility(GONE);

        try {
            for (GetCategories.JSONDATum item : ItemsArrayList)
                if (item.getoName().trim().toLowerCase().contains(s.trim().toLowerCase()))
                    filteredList.add(item);
        }catch (Exception ignore){}

        if(filteredList.isEmpty()){
            noresults.setVisibility(VISIBLE);
            items.setVisibility(GONE);
        }
        else {
            adapter.filterList(filteredList);

            items.setVisibility(VISIBLE);
            noresults.setVisibility(GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if(items.getVisibility()==VISIBLE || noresults.getVisibility()==VISIBLE){
            search.setText("");
            search.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

            items.setVisibility(GONE);
            noresults.setVisibility(GONE);
            txtTopDeals.setVisibility(VISIBLE);
            top_deals.setVisibility(VISIBLE);
        }
        else
            super.onBackPressed();
    }
}