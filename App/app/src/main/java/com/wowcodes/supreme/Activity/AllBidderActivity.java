/**
 * The `AllBidderActivity` class is an Android activity that retrieves a list of bidders from an API
 * and displays them in a RecyclerView.
 */
package com.wowcodes.supreme.Activity;
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
import com.wowcodes.supreme.Adapter.AllBiddersAdapter;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.AllBid;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
public class AllBidderActivity extends AppCompatActivity {
    public BindingService videoService;
    RecyclerView recyclerAllBidder;
    LinearLayout lvlAllBidder;
    SavePref savePref;
    String oId;
    TextView txtNoBid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_get_passbook_all);
        savePref = new SavePref(AllBidderActivity.this);
        ImageView imgBackk = findViewById(R.id.imgBackk);
        TextView txtAucname = findViewById(R.id.txtAucname);
        oId = getIntent().getStringExtra("o_id");
        txtAucname.setText(R.string.string2);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // These lines of code are performing the following tasks:
        recyclerAllBidder = findViewById(R.id.recycler);
        lvlAllBidder = findViewById(R.id.linearlay);
        txtNoBid = findViewById(R.id.txtNoBid);
        txtNoBid.setVisibility(View.GONE);
        recyclerAllBidder.setLayoutManager(new LinearLayoutManager(AllBidderActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        getofferapi();
    }

    public void getofferapi() {
        lvlAllBidder.setVisibility(View.VISIBLE);
        try {
            videoService.get_offers_id(oId, savePref.getUserId()).enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    lvlAllBidder.setVisibility(View.GONE);
                    ArrayList<AllBid> arrayList = response.body().getJSON_DATA().get(0).getAll_bid();
                    recyclerAllBidder.setAdapter(new AllBiddersAdapter(AllBidderActivity.this, arrayList));
                    if (arrayList.size() == 0)
                        txtNoBid.setVisibility(View.VISIBLE);
                    else
                        txtNoBid.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    lvlAllBidder.setVisibility(View.GONE);
                    txtNoBid.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            lvlAllBidder.setVisibility(View.GONE);
            txtNoBid.setVisibility(View.VISIBLE);
        }
    }
}