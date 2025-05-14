/**
 * The AllUserLottoActivity class is an Android activity that displays a list of user bids for a
 * specific auction.
 */


package com.wowcodes.supreme.Activity;

import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Adapter.Userticketsadapter;
import com.wowcodes.supreme.Modelclas.Ticket;
import com.wowcodes.supreme.R;
        import com.wowcodes.supreme.RetrofitUtils.BindingService;
        import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
        import com.wowcodes.supreme.SavePref;

        import java.util.ArrayList;

        import retrofit2.Call;
        import retrofit2.Callback;

public class AllUserLottoActivity extends AppCompatActivity {

    public BindingService videoService;
    RecyclerView recyclerYourBid;
    LinearLayout lvlYourBid;
    SavePref savePref;
    String oId;
    TextView txtNoBid;


   /**
    * This function is the onCreate method for an activity that sets up the layout and initializes
    * various views and variables.
    * 
    * @param savedInstanceState The savedInstanceState parameter is a Bundle object that contains the
    * activity's previously saved state. It is used to restore the activity's state when it is
    * recreated, such as when the device is rotated.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_alluser_lotto);
        savePref = new SavePref(AllUserLottoActivity.this);
        ImageView imgBackk = findViewById(R.id.imgBackk);
        TextView txtAucname = findViewById(R.id.txtAucname);
        oId = getIntent().getStringExtra("o_id");
        txtAucname.setText(R.string.string3);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerYourBid = findViewById(R.id.recycler);
        lvlYourBid = findViewById(R.id.linearlay);
        txtNoBid = findViewById(R.id.txtNoBid);
        txtNoBid.setVisibility(View.GONE);
        recyclerYourBid.setLayoutManager(new LinearLayoutManager(AllUserLottoActivity.this));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        getofferapi();
    }


   /**
    * The function "getofferapi" makes an API call to retrieve a list of user bids and displays them in
    * a RecyclerView.
    */
   public void getofferapi() {
       lvlYourBid.setVisibility(View.VISIBLE);
       try {
           callofferApi().enqueue(new Callback<Ticket>() {
               @Override
               public void onResponse(Call<Ticket> call, retrofit2.Response<Ticket> response) {
                   lvlYourBid.setVisibility(View.GONE);

                   if (response.isSuccessful() && response.body() != null) {
                       ArrayList<Ticket.Ticket_inner> arrayList = (ArrayList<Ticket.Ticket_inner>) response.body().getJsonData();
                       Log.d("sizearray", String.valueOf(arrayList.size()));
                       recyclerYourBid.setAdapter(new Userticketsadapter(AllUserLottoActivity.this, arrayList));
                       if (arrayList.size() == 0) {
                           txtNoBid.setVisibility(View.VISIBLE);
                       } else {
                           txtNoBid.setVisibility(View.GONE);
                       }
                   } else {
                       // Handle unsuccessful response, e.g., show an error message
                       Toast.makeText(AllUserLottoActivity.this, "Failed to get a successful response", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<Ticket> call, Throwable t) {
                   lvlYourBid.setVisibility(View.GONE);
                   Toast.makeText(AllUserLottoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
               }
           });
       } catch (Exception e) {
           e.printStackTrace();
           lvlYourBid.setVisibility(View.GONE);
       }
   }

    /**
     * The function `callofferApi()` returns a `Call` object that makes an API request to get offers
     * based on the provided offer ID and user ID.
     *
     * @return The method is returning a Call object with a generic type of Ticket.
     */
    private Call<Ticket> callofferApi() {
        return videoService.get_ticketnew(oId, savePref.getUserId());
    }

}

