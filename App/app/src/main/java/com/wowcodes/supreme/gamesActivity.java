package com.wowcodes.supreme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowcodes.supreme.Activity.game_cric;
import com.wowcodes.supreme.Activity.game_gn;
import com.wowcodes.supreme.Activity.game_ouc;
import com.wowcodes.supreme.Activity.game_rps;
import com.wowcodes.supreme.Activity.game_spin_wheel;
import com.wowcodes.supreme.Activity.game_tc;
import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class gamesActivity extends AppCompatActivity {
    ImageView imgBackk,rps,gn,spin,coin,ouc,cric;
    LinearLayout rps_ll,gn_ll,spin_ll,coin_ll,ouc_ll,cric_ll;
    TextView aucName;
    BindingService videoService;
    public static boolean game=false;

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

        setContentView(R.layout.activity_games);

        imgBackk=findViewById(R.id.imgBackk);
        aucName=findViewById(R.id.txtAucname);
        aucName.setText("PLAY & EARN");

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        rps=findViewById(R.id.rps);
        gn=findViewById(R.id.gn);
        spin=findViewById(R.id.spin);
        coin=findViewById(R.id.coin);
        ouc=findViewById(R.id.ouc);
        cric=findViewById(R.id.cric);
        rps_ll=findViewById(R.id.rps_ll);
        gn_ll=findViewById(R.id.gn_ll);
        spin_ll=findViewById(R.id.spin_ll);
        coin_ll=findViewById(R.id.coin_ll);
        ouc_ll=findViewById(R.id.ouc_ll);
        cric_ll=findViewById(R.id.cric_ll);


        getrules();

        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText("GAMES");
        imgBackk=findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        rps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_rps.class));
            }
        });

        gn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_gn.class));
            }
        });

        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_tc.class));
            }
        });

        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_spin_wheel.class));
            }
        });

        ouc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_ouc.class));
            }
        });

        cric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(gamesActivity.this, game_cric.class));
            }
        });


    }


    public void getrules() {
        try {
            videoService.get_games().enqueue(new Callback<GetGames>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetGames> call, retrofit2.Response<GetGames> response) {
                    try {
                        ArrayList<GetGames.Get_games_Inner> arrayList = response.body().getJSON_DATA();

                        if(!arrayList.get(0).getRps_status().equals("1")) {
                            rps.setVisibility(View.GONE);
                            rps_ll.setVisibility(View.GONE);
                        }

                        if(!arrayList.get(0).getGn_status().equals("1")) {
                            gn.setVisibility(View.GONE);
                            gn_ll.setVisibility(View.GONE);
                        }

                        if(!arrayList.get(0).getCt_status().equals("1")) {
                            coin.setVisibility(View.GONE);
                            coin_ll.setVisibility(View.GONE);
                        }

                        if(!arrayList.get(0).getSpin_status().equals("1")) {
                            spin.setVisibility(View.GONE);
                            spin_ll.setVisibility(View.GONE);
                        }

                        if(!arrayList.get(0).getOuc_status().equals("1")) {
                            ouc.setVisibility(View.GONE);
                            ouc_ll.setVisibility(View.GONE);
                        }

                        if(!arrayList.get(0).getCric_status().equals("1")) {
                            cric.setVisibility(View.GONE);
                            cric_ll.setVisibility(View.GONE);
                        }


                        /*rps_min.setText("Minimum Bid : "+arrayList.get(0).getRps_min());
                        rps_bonus.setText("Win Bonus : "+arrayList.get(0).getRps_win());
                        Glide.with(gamesActivity.this).load(arrayList.get(0).getRps_image()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(rps_img);
                        gn_min.setText("Minimum Bid : "+arrayList.get(0).getGn_min());
                        gn_bonus.setText("Win Bonus : "+arrayList.get(0).getGn_win());
                        Glide.with(gamesActivity.this).load(arrayList.get(0).getGn_image()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(gn_img);
                        spin_min.setText("Minimum Bid : "+arrayList.get(0).getSpin_min());
                        spin_bonus.setText("Win Bonus : "+arrayList.get(0).getSpin_win());*/
                        //Glide.with(gamesActivity.this).load(arrayList.get(0).getSpin_image()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(spin_img);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetGames> call, Throwable t) {
                    //Toast.makeText(game_rps.this, "ERROR GETTING RULES", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}