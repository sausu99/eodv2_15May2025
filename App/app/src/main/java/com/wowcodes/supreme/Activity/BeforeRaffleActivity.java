package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.AuctionItemAdapter;
import com.wowcodes.supreme.Adapter.PrizeListAdapter;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.AllBidderInner;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetPrizes;
import com.wowcodes.supreme.Modelclas.UserTicket;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class BeforeRaffleActivity extends AppCompatActivity {

    RecyclerView prizes_recycler,moreitems;
    BindingService videoService;
    RelativeLayout txt_morelikethis;
    String oid, total_bids;
    SavePref savePref;
    String totalLotto;
    private static final int REQUEST_CODE_LOTO_DETAIL = 1; // Request code for LotoDetailActivity
    ImageView cancel,imgBackk;
    RelativeLayout ticket,myticketrelative;
    LinearLayout loadinglayout;
    String tktnumber;
    ConstraintLayout[] ballbg;

    TextView viewall,day0,day1,hour0,hour1,min0,min1,sec0,sec1,ticket0,ticket1,ticket2,ticket3,ticket4,ticket5,ticket6,ticket7,notickets,addmore,txtAucname,ticketleft;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(BeforeRaffleActivity.this);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        if (Objects.equals(savePref.getLang(), "en")) {
            setLocale("");
        } else {
            setLocale(savePref.getLang());
        }

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_before_raffle);

        cancel=findViewById(R.id.cancel);
        txtAucname=findViewById(R.id.txtAucname);
        addmore=findViewById(R.id.addmore);
        ticketleft=findViewById(R.id.tktleft);
        viewall=findViewById(R.id.viewall);
        notickets=findViewById(R.id.notickets);
        day0=findViewById(R.id.day0);
        day1=findViewById(R.id.day1);
        hour0=findViewById(R.id.hour0);
        hour1=findViewById(R.id.hour1);
        min0=findViewById(R.id.min0);
        min1=findViewById(R.id.min1);
        sec0=findViewById(R.id.sec0);
        sec1=findViewById(R.id.sec1);
        ticket0=findViewById(R.id.ticket0);
        ticket1=findViewById(R.id.ticket1);
        ticket2=findViewById(R.id.ticket2);
        ticket3=findViewById(R.id.ticket3);
        ticket4=findViewById(R.id.ticket4);
        ticket5=findViewById(R.id.ticket5);
        ticket6=findViewById(R.id.ticket6);
        ticket7=findViewById(R.id.ticket7);
        imgBackk = findViewById(R.id.imgBackk);
        myticketrelative=findViewById(R.id.myticketrelative);
        prizes_recycler=findViewById(R.id.recycler_prizes);
        prizes_recycler.setLayoutManager(new LinearLayoutManager(this));
        moreitems=findViewById(R.id.moreitems);
        txt_morelikethis=findViewById(R.id.txt_morelikethis);
        loadinglayout=findViewById(R.id.linearlay);
        moreitems.setLayoutManager(new LinearLayoutManager(BeforeRaffleActivity.this,RecyclerView.HORIZONTAL,false));


        loadinglayout.setVisibility(VISIBLE);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getticketapi();
        load_prizes();
        oid=getIntent().getStringExtra("O_id");
        getmoreitems();


        Log.d("userid", oid);
        new Thread(new BeforeRaffleActivity.CountDownRunner( getIntent().getStringExtra("edate") + " " + getIntent().getStringExtra("etime"))).start();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtAucname.setText(R.string.lotto);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(getIntent().getStringExtra("type").equalsIgnoreCase("4")){
                    i = new Intent(BeforeRaffleActivity.this, RaffleDetailActivity.class);
                    i.putExtra("check", "draw");
                    i.putExtra("O_id", getIntent().getStringExtra("O_id"));
                    i.putExtra("total_bids" , getIntent().getStringExtra("total_bids"));
                    i.putExtra("qty" , Integer.parseInt(getIntent().getStringExtra("qty")));
                    i.putExtra("type" , getIntent().getStringExtra("type"));
                    i.putExtra("title" , getIntent().getStringExtra("name"));
                    i.putExtra("claimable" , getIntent().getStringExtra("claimable"));
                }
                else{
                    i = new Intent(BeforeRaffleActivity.this, LotoDetailActivity.class);
                    i.putExtra("O_id", getIntent().getStringExtra("O_id"));
                    i.putExtra("check", "raffle");
                    i.putExtra("total_bids" , total_bids);
                    String qtyStr = getIntent().getStringExtra("qty");
                    Log.d("qty", qtyStr);

                    if (qtyStr != null && !qtyStr.isEmpty()) {
                        try {
                            int qty = Integer.parseInt(qtyStr);
                            i.putExtra("qty", qty);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            // Handle the error, maybe set a default value or show an error message
                            i.putExtra("qty", 0); // Or any other default value you want to use
                        }
                    } else {
                        // Handle the case where qtyStr is null or empty
                        i.putExtra("qty", 0); // Or any other default value you want to use
                    }
                    i.putExtra("type" , getIntent().getStringExtra("type"));
                    i.putExtra("edate", getIntent().getStringExtra("edate"));
                    i.putExtra("claimable" , getIntent().getStringExtra("claimable"));
                }

                // Use startActivityForResult to receive result from LotoDetailActivity
                startActivityForResult(i, REQUEST_CODE_LOTO_DETAIL);
            }
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BeforeRaffleActivity.this,AllUserLottoActivity.class);
                intent.putExtra("o_id",getIntent().getStringExtra("O_id"));
                startActivity(intent);
            }
        });
        loadinglayout.setVisibility(GONE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getticketapi();
        load_prizes();
        getmoreitems();
    }

    public void getticketapi() {
        try {
            // Show loading layout
            loadinglayout.setVisibility(View.VISIBLE);
            videoService.get_offers_id(getIntent().getStringExtra("O_id"), savePref.getUserId()).enqueue(new Callback<AllBidder>() {
                @SuppressLint("WrongConstant")
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<UserTicket> ticketlist = response.body().getJSON_DATA().get(0).getUser_ticket();
                            total_bids = response.body().getJSON_DATA().get(0).getTotal_bids();
                            int totalQty = Integer.parseInt(response.body().getJSON_DATA().get(0).getO_qty());
                            int ticketRemaining = totalQty - Integer.parseInt(total_bids);
                            if (ticketRemaining <= 0) ticketRemaining=0;
                            ticketleft.setText(String.valueOf(ticketRemaining)+" " +getString(R.string.tktleft));
                            updateButtonState(ticketRemaining);
                            if (ticketRemaining == totalQty) {
                                addmore.setEnabled(true);
                                addmore.setClickable(true);
                            }
                            String oamt=response.body().getJSON_DATA().get(0).getO_amount();
                            addmore.setText(getString(R.string.buy_ticket)+" "+oamt);

                            int totalballs = Integer.parseInt(ticketlist.get(0).getNormal_ball_limit()) + Integer.parseInt(ticketlist.get(0).getPremium_ball_limit());
                            int premiumballs = Integer.parseInt(ticketlist.get(0).getPremium_ball_limit());
                            ballbg = new ConstraintLayout[8];
                            ballbg[0] = findViewById(R.id.ball1bg);
                            ballbg[1] = findViewById(R.id.ball2bg);
                            ballbg[2] = findViewById(R.id.ball3bg);
                            ballbg[3] = findViewById(R.id.ball4bg);
                            ballbg[4] = findViewById(R.id.ball5bg);
                            ballbg[5] = findViewById(R.id.ball6bg);
                            ballbg[6] = findViewById(R.id.ball7bg);
                            ballbg[7] = findViewById(R.id.ball8bg);

                            // Update ball display
                            for (int i = 0; i < totalballs; i++) {
                                ballbg[i].setVisibility(View.VISIBLE);
                                if (i < premiumballs) {
                                    ballbg[i].setBackgroundResource(R.drawable.premium);
                                }
                            }

                            // Set ticket numbers
                            ticket0.setText(ticketlist.get(0).getBall_1());
                            ticket1.setText(ticketlist.get(0).getBall_2());
                            ticket2.setText(ticketlist.get(0).getBall_3());
                            ticket3.setText(ticketlist.get(0).getBall_4());
                            ticket4.setText(ticketlist.get(0).getBall_5());
                            ticket5.setText(ticketlist.get(0).getBall_6());
                            ticket6.setText(ticketlist.get(0).getBall_7());
                            ticket7.setText(ticketlist.get(0).getBall_8());

                            notickets.setVisibility(View.GONE);
                        } else {
                            loadinglayout.setVisibility(View.GONE);
                            addmore.setEnabled(false);
                            addmore.setClickable(false);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        loadinglayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    t.printStackTrace();
                    loadinglayout.setVisibility(View.GONE);
                    addmore.setEnabled(false);
                    addmore.setClickable(false);                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loadinglayout.setVisibility(View.GONE);
            addmore.setEnabled(false);
            addmore.setClickable(false);        }
    }


    public void load_prizes() {
        try {
            loadinglayout.setVisibility(VISIBLE);

            videoService.get_prizes(getIntent().getStringExtra("O_id")).enqueue(new Callback<GetPrizes>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetPrizes> call, retrofit2.Response<GetPrizes> response) {
                    try {
                        ArrayList<GetPrizes.Get_prizes_Inner> arrayList = response.body().getJSON_DATA();

                        if(arrayList.isEmpty())
                            Toast.makeText(BeforeRaffleActivity.this, "No Prizes to show :(", Toast.LENGTH_SHORT).show();

                        prizes_recycler.setAdapter(new PrizeListAdapter(BeforeRaffleActivity.this,arrayList));
                        loadinglayout.setVisibility(GONE);

                    }catch (Exception e){
                        loadinglayout.setVisibility(GONE);
                        e.printStackTrace();
                    }
                }

                @Override public void onFailure(Call<GetPrizes> call, Throwable t) {
                    loadinglayout.setVisibility(GONE);
                }
            });
        } catch (Exception ignore) {
            loadinglayout.setVisibility(GONE);
        }
    }


    class CountDownRunner implements Runnable {
        String o_etime;

        public CountDownRunner(String o_etime) {
            this.o_etime = o_etime;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception ignore) {}
            }
        }
    }

    public void doWork(final String o_etime) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                findDifference(o_etime);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void findDifference(String end_date) {
        String currentDate= String.valueOf(java.time.LocalDate.now());
        String currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
        String start_date= currentDate + " " + currentTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            String days=String.valueOf(difference_In_Days);
            String hours=String.valueOf(difference_In_Hours);
            String minutes=String.valueOf(difference_In_Minutes);
            String seconds=String.valueOf(difference_In_Seconds);

            day0.setText(String.valueOf(days.length()==1?"0":days.charAt(0)));
            day1.setText(String.valueOf(days.length()==2?days.charAt(1):days.charAt(0)));

            hour0.setText(String.valueOf(hours.length()==1?"0":hours.charAt(0)));
            hour1.setText(String.valueOf(hours.length()==2?hours.charAt(1):hours.charAt(0)));

            min0.setText(String.valueOf(minutes.length()==1?"0":minutes.charAt(0)));
            min1.setText(String.valueOf(minutes.length()==2?minutes.charAt(1):minutes.charAt(0)));

            sec0.setText(String.valueOf(seconds.length()==1?"0":seconds.charAt(0)));
            sec1.setText(String.valueOf(seconds.length()==2?seconds.charAt(1):seconds.charAt(0)));
        }
        catch (Exception ignore) {}
    }

    public void getmoreitems(){
        try {
            loadinglayout.setVisibility(VISIBLE);

            videoService.get_offers_id(oid, savePref.getUserId()).enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AllBidder allBidder = response.body();
                        ArrayList<AllBidderInner> arrayList = allBidder.getJSON_DATA();

                        txt_morelikethis.setVisibility(VISIBLE);
                        moreitems.setVisibility(VISIBLE);
                        moreitems.setAdapter(new AuctionItemAdapter(BeforeRaffleActivity.this, (ArrayList<GetCategories.JSONDATum>) arrayList.get(0).getSimiliar_items(),"live",false));
                    }
                    loadinglayout.setVisibility(GONE);

                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    txt_morelikethis.setVisibility(GONE);
                    moreitems.setVisibility(GONE);
                    loadinglayout.setVisibility(GONE);

                }
            });
        } catch (Exception e) {
            txt_morelikethis.setVisibility(GONE);
            moreitems.setVisibility(GONE);
            loadinglayout.setVisibility(GONE);

        }
    }
    private void updateButtonState(int ticketRemaining) {
        if (ticketRemaining > 0) {
            addmore.setEnabled(true);
            addmore.setClickable(true);
        } else {
            addmore.setEnabled(false);
            addmore.setClickable(false);
            addmore.setText(getString(R.string.notktleft));
        }
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }


}