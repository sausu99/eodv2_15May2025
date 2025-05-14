package com.wowcodes.supreme.Activity;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.facebook.ads.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Adapter.TicketAdapter;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.AllBidderInner;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserBid;
import com.wowcodes.supreme.Modelclas.UserProfile;
//import com.wowcodes.prizex.Modelclas.gettime;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class RaffleDetailActivity extends AppCompatActivity {
    TextView txtTitle,txtItemName,txtItemEnd,txtItemStart,txtItemPrice,txtTotalBids,txtViewAll,txtForCoin,txtItemDesc;
    TextView incbtn,recqnt,decbtn;
    String ticketnoads = "" , titleAuc;
    ImageView imgBack;
    RelativeLayout relraffle;
    ProgressBar progressBar , loadBar;
    int  qty_O;
    private List<GetCategories.JSONDATum> items;
    String title,oId ,type , totalBids,checkStatus,startDate,totalWallet,getBids,getAmount,bdis;
    TextView txtauctiontime,txtnotickets ,  txtProgress;
    LinearLayout lvlLive;
    TextView layoutButton;
    public BindingService videoService;
    SavePref savePref;
    ArrayList<AllBidderInner> arrayList;
    TicketAdapter ticketAdapter;
    ArrayList<UserBid> ticketlist=new ArrayList<>();
    int random =0;
    int fcount = 0;
    ArrayList<String> tickets = new ArrayList<String>();
    RecyclerView recyclerView;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    TextView txt1,txt2,txt3,txt4;
    String randomticket;
    int tcount = 1;
    String curr_dt_time = "0000-00-00 00:00:00";
    private RewardedAd mRewardedAd;
    private ProgressDialog mProgressDialog;
    private RewardedVideoAd rewardedVideoAd;
    boolean unityInitialized = false,ironInitialized = false,adsbtn = false;
    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;
    TextView txtAds;
    TextView buynow,txtTimer,daysleft,txtsaleends;
    ImageView p0,p1,p2,p3,p4;
    LinearLayout points,days;
    ViewPager imgpager;
    String name,email,oBuy,image,claimable="";

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_raffle);
        txtTitle=(TextView)findViewById(R.id.txtAucname);
        imgBack=(ImageView)findViewById(R.id.imgBackk);
        lvlLive = findViewById(R.id.linearlay);
        relraffle=findViewById(R.id.relraffle);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        txtauctiontime=(TextView)findViewById(R.id.txtauctiontime);
        layoutButton = findViewById(R.id.layoutButton);
        progressBar = findViewById(R.id.progressBar);
        txtItemDesc=(TextView)findViewById(R.id.txtItemDesc);
        txtTotalBids=(TextView)findViewById(R.id.txtTotalBids);
        txtViewAll=(TextView)findViewById(R.id.txtViewAll);
        buynow=findViewById(R.id.buynow);
        txtTimer = findViewById(R.id.txtTimer);
        p0=findViewById(R.id.p1);
        p1=findViewById(R.id.p2);
        p2=findViewById(R.id.p3);
        p3=findViewById(R.id.p4);
        p4=findViewById(R.id.p5);
        points=findViewById(R.id.points);
        txtsaleends=findViewById(R.id.txtsaleends);
        imgpager = findViewById(R.id.image_pager);
        days=findViewById(R.id.days);
        daysleft=findViewById(R.id.daysleft);
        txtForCoin=(TextView)findViewById(R.id.txtForCoin);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2=(TextView)findViewById(R.id.txt2);
        txt3=(TextView)findViewById(R.id.txt3);
        txt4=(TextView)findViewById(R.id.txt4);
        txtAds=findViewById(R.id.txtAds);
        txtnotickets=(TextView)findViewById(R.id.txtnotickets);
        txtProgress = findViewById(R.id.txtProgressText);
        decbtn=findViewById(R.id.decbtn);
        incbtn=findViewById(R.id.incbtn);
        recqnt=findViewById(R.id.recqnt);
        savePref = new SavePref(RaffleDetailActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        TextView txtCount =(TextView)findViewById(R.id.recqnt);
        TextView buttonInc= (TextView) findViewById(R.id.incbtn);
        TextView buttonDec= (TextView) findViewById(R.id.decbtn);

        buynow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openemaildialog();
            }
        });

        try {
            if(getIntent()!=null) {
                title = getIntent().getStringExtra("title");
                oId = getIntent().getStringExtra("O_id");
                qty_O = getIntent().getIntExtra("qty", 0);
                totalBids = getIntent().getStringExtra("total_bids");
                type = getIntent().getStringExtra("type");
                checkStatus = getIntent().getStringExtra("check");
                if (getIntent().hasExtra("claimable"))
                    claimable = getIntent().getStringExtra("claimable");
            }
        } catch (Exception ignore) {}

        try {
            int max_bid= qty_O - Integer.parseInt(totalBids);
            if(max_bid <= 0)
                txtProgress.setText(R.string.string336);
            else{
                txtProgress.setText(String.valueOf(max_bid) + " " + getString(R.string.string340));
                layoutButton.isClickable();
            }
            progressBar.setMax(qty_O);
            progressBar.setProgress(qty_O - max_bid);
            buttonInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tcount == 1){
                        txtAds.setVisibility(GONE);
                        buttonDec.setEnabled(false);
                    }
                    if(tcount < max_bid)
                        tcount++;
                    if (tcount != 1){
                        txtAds.setVisibility(GONE);
                        buttonDec.setEnabled(true);
                    }
                    txtCount.setText(String.valueOf(tcount));
                }
            });
            buttonDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tcount--;
                    if (tcount == 1){
                        txtAds.setVisibility(GONE);
                        buttonDec.setEnabled(false);
                    }
                    else
                        txtAds.setVisibility(GONE);
                    txtCount.setText(String.valueOf(tcount));
                }
            });
            txtViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(RaffleDetailActivity.this,ViewAllTicketsActivity.class);
                    intent.putExtra("o_id",oId);
                    startActivity(intent);
                }
            });
            txtTotalBids.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(RaffleDetailActivity.this,AllBidderActivity.class);
                    intent.putExtra("o_id", oId);
                    intent.putExtra("status","2");
                    startActivity(intent);
                }
            });
            progressBar.setScaleY(6f);
            layoutButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onClick(View v) {
                    if((Objects.equals(type, "4") || Objects.equals(type, "5")) && max_bid > 0){
                        fcount = (tcount + 1);
                        randomAlphaNumeric(4,"1");
                    }
                    else{
                        Toast.makeText(RaffleDetailActivity.this ,R.string.string336 , Toast.LENGTH_LONG).show();
                        layoutButton.setClickable(false);
                    }
                }
            });
            txtAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fcount = (tcount + 1);
                    randomAlphaNumeric(4,"1");
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                isNetworkConnected();
        } catch (Exception ignore){}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            getprofile();
            getticketapi();
            getofferapi();
            gettime();
        }
        else{
            Intent intent=new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private  void openarraydialog() {
        String ticketarray[] = new String[tickets.size()];
        for (int j = 0; j < tickets.size(); j++)
            ticketarray[j] = tickets.get(j);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (tcount > 1)
            builder.setTitle(R.string.string33);
        else
            builder.setTitle(R.string.string33);

        builder.setItems(ticketarray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.string37, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(RaffleDetailActivity.this , MainActivity.class);
                        intent.putExtra("O_id", oId);
                        intent.putExtra("title" , titleAuc);
                        intent.putExtra("check", "draw");
                        intent.putExtra("total_bids" , String.valueOf(Integer.parseInt(totalBids) + tcount));
                        intent.putExtra("qty" , qty_O);
                        intent.putExtra("type" , type);
                        intent.putExtra("back" , true);
                        startActivity(intent);                 //       lvlLive.setVisibility(View.GONE);
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView titleView = (TextView) dialog.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null)
            titleView.setGravity(Gravity.CENTER);
    }

    private void openticketdialog(final String s, final String randomono) {
        final Dialog dialog = new Dialog(RaffleDetailActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_ticket);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button ok = dialog.findViewById(R.id.ok);
        TextView d_title = dialog.findViewById(R.id.d_title);
        d_title.setText(R.string.string33);
        Button cancel = dialog.findViewById(R.id.cancel);
        LinearLayout layout=dialog.findViewById(R.id.ticketlayout);
        TextView txtTicketNo=dialog.findViewById(R.id.txtTixketNo);
        TextView bottomtxt=dialog.findViewById(R.id.bottomtxt);
        cancel.setVisibility(GONE);
        if(s.equals("1")) {
            bottomtxt.setVisibility(GONE);
            layout.setVisibility(View.VISIBLE);
            txtTicketNo.setText(String.valueOf(randomono));
        } else {
            bottomtxt.setVisibility(View.VISIBLE);
            layout.setVisibility(GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s.equals("1")) {
                    dialog.dismiss();
                    ProgressDialog pDialog = new ProgressDialog(RaffleDetailActivity.this);
                    pDialog.setMessage(getText(R.string.string147));
                    pDialog.setCancelable(false);
                    pDialog.show();
                    reload();
                } else{
                    reload();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    @SuppressLint("SetTextI18n")
    private void openaddbiddialog(final String ss) {
        if ((tcount == 1) && (adsbtn))
            adsbtn = false;
        else{
            final Dialog dialog = new Dialog(RaffleDetailActivity.this);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_bid);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            View dview = dialog.findViewById(R.id.dview);
            Button ok = dialog.findViewById(R.id.ok);
            Button cancel = dialog.findViewById(R.id.cancel);
            TextView d_title = dialog.findViewById(R.id.d_title);
            TextView bottomtxt = dialog.findViewById(R.id.bottomtxt);
            d_title.setText(getText(R.string.string34) + " " + tcount + " "+ getText(R.string.string35));
            int tamount = (int) (tcount * Double.parseDouble(arrayList.get(0).getO_amount()));
            if (tamount > Integer.valueOf(totalWallet) ) {
                bottomtxt.setText(R.string.string36);
                cancel.setVisibility(GONE);
                dview.setVisibility(GONE);
                ok.setText(R.string.string37);
                d_title.setVisibility(GONE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        lvlLive.setVisibility(View.VISIBLE);
                        reload();
                        lvlLive.setVisibility(GONE);
                    }
                });
                dialog.show();
            } else {
                bottomtxt.setText(tamount + "" + getText(R.string.string23));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (tcount == 1) {
                            tickets.add(ss);
                            fcount--;
                            bdis = ss;
                            addbid(ss);
                        }
                        if (tcount > 1) {
                            for (int i = 0; i < tcount; i++) {
                                tickets.add(randomticket);
                                bdis = randomticket;
                                if (tickets.size() == tcount) {
                                    addbidrange();
                                } else {
                                    fcount--;
                                    randomAlphaNumeric(4, "1");
                                }
                            }
                        }
                    }
                });
                if (fcount == (tcount + 1))
                    dialog.show();
            }
        }
    }

    public void getofferapi() {
        lvlLive.setVisibility(View.VISIBLE);
        try {
            callofferApi().enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    lvlLive.setVisibility(GONE);
                    arrayList = response.body().getJSON_DATA();
                    List<String> images=new ArrayList<>();
                    if(!arrayList.get(0).getO_image().endsWith("/"))
                        images.add(arrayList.get(0).getO_image());
                    if(!arrayList.get(0).getO_image1().endsWith("/"))
                        images.add(arrayList.get(0).getO_image1());
                    if(!arrayList.get(0).getO_image2().endsWith("/"))
                        images.add(arrayList.get(0).getO_image2());
                    if(!arrayList.get(0).getO_image3().endsWith("/"))
                        images.add(arrayList.get(0).getO_image3());
                    if(!arrayList.get(0).getO_image4().endsWith("/"))
                        images.add(arrayList.get(0).getO_image4());

                    if(images.size()==1)
                        points.setVisibility(GONE);
                    else if(images.size()==2){
                        p0.setVisibility(View.VISIBLE);
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(GONE);
                        p3.setVisibility(GONE);
                        p4.setVisibility(GONE);
                    }
                    else if(images.size()==3){
                        p0.setVisibility(View.VISIBLE);
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(View.VISIBLE);
                        p3.setVisibility(GONE);
                        p4.setVisibility(GONE);
                    }
                    else if(images.size()==4){
                        p0.setVisibility(View.VISIBLE);
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(View.VISIBLE);
                        p3.setVisibility(View.VISIBLE);
                        p4.setVisibility(GONE);
                    }
                    else if(images.size()==5){
                        p0.setVisibility(View.VISIBLE);
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(View.VISIBLE);
                        p3.setVisibility(View.VISIBLE);
                        p4.setVisibility(View.VISIBLE);
                    }

                    imgpager.setAdapter(new ImagesAdapter(RaffleDetailActivity.this,images));
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

                    titleAuc = arrayList.get(0).getO_name();
                    //txtItemName.setText(arrayList.get(0).getO_name());
                    txtTitle.setText(arrayList.get(0).getO_name());
                    image=arrayList.get(0).getO_image();
                    oBuy=arrayList.get(0).getO_buy();
                    if(oBuy.equalsIgnoreCase("") || oBuy.equalsIgnoreCase("0"))
                        buynow.setVisibility(GONE);
                    else
                        buynow.setVisibility(VISIBLE);

                    buynow.setText(getString(R.string.buynowfor).replace("$",MainActivity.currency).replace("10",oBuy));
                    name=arrayList.get(0).getO_name();
                    txtItemDesc.setText(arrayList.get(0).getO_desc());
                    txtTotalBids.setText(arrayList.get(0).getTotal_users() + " "+ getText(R.string.string38));

                    txtForCoin.setText(getText(R.string.string40)+" "+arrayList.get(0).getO_amount()+" Coin");
                    getAmount=arrayList.get(0).getO_amount();
                    Thread myThread = new Thread(new CountDownRunner(txtTimer, arrayList.get(0).getO_edate() + " " + arrayList.get(0).getO_etime()));
                    myThread.start();
                }

                @Override public void onFailure(Call<AllBidder> call, Throwable t) {
                    lvlLive.setVisibility(GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<AllBidder> callofferApi() {
        return videoService.get_offers_id(oId, savePref.getUserId());
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
                } catch (Exception ignore) {}
            }
        }
    }

    public void doWork(final TextView textView, final String o_etime) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                gettime();
                startDate=curr_dt_time;
                findDifference(startDate, textView, o_etime);
            }
        });
    }

    void findDifference(String start_date, TextView textView, String end_date) {
        textView.setText(R.string.string41);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            if (difference_In_Seconds < 0 || difference_In_Minutes < 0 || difference_In_Hours < 0 || difference_In_Days<0){
                txtsaleends.setText(getText(R.string.string31));
                days.setVisibility(GONE);
                txtTimer.setText(GONE);
            }
            else {
                days.setVisibility(View.VISIBLE);
                txtTimer.setVisibility(View.VISIBLE);
                txtsaleends.setText(getText(R.string.lotteryendsin));
                if(difference_In_Days<10)
                    daysleft.setText("0"+difference_In_Days);
                else if(difference_In_Days>10)
                    daysleft.setText(difference_In_Days+"");
                else
                    days.setVisibility(GONE);

                String hours=difference_In_Hours<10?"0"+difference_In_Hours:difference_In_Hours+"";
                String mins=difference_In_Minutes<10?"0"+difference_In_Minutes:difference_In_Minutes+"";
                String secs=difference_In_Seconds<10?"0"+difference_In_Seconds:difference_In_Seconds+"";
                textView.setText(hours+":"+mins+":"+secs);
            }
        }
        catch (Exception ignore) {}
    }


    public void addbidrange() {
        lvlLive.setVisibility(View.VISIBLE);
        try {
            calladdbidrangeApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(RaffleDetailActivity.this, R.string.string162, Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1"))
                            openarraydialog();
                        else
                            lvlLive.setVisibility(GONE);
                    }catch (Exception e){
                        lvlLive.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    lvlLive.setVisibility(GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<SuccessModel> calladdbidrangeApi() {
        return videoService.add_bid_multi("" + getJsonObj(tickets));
    }

    public void getprofile() {
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        totalWallet = arrayList.get(0).getWallet();
                    }catch (Exception e){
                        lvlLive.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlLive.setVisibility(GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
    public void addbid(String s) {
        lvlLive.setVisibility(View.VISIBLE);
        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(RaffleDetailActivity.this, R.string.string162, Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                            if (fcount == 1)
                                openarraydialog();
                        }else
                            lvlLive.setVisibility(GONE);
                    }catch (Exception e){
                        lvlLive.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    lvlLive.setVisibility(GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<SuccessModel> calladdbidApi() {
        return videoService.add_bid(savePref.getUserId(), oId, bdis, getAmount);
    }
    public void getticketapi() {
        try {
            callofferApii().enqueue(new Callback<AllBidder>() {
                @SuppressLint("WrongConstant")
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    ticketlist = response.body().getJSON_DATA().get(0).getUser_bid();
                    if(ticketlist.size()!=0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new TicketAdapter(RaffleDetailActivity.this, ticketlist,"1","5"));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(RaffleDetailActivity.this,LinearLayoutManager.HORIZONTAL,true);
                        layoutManager.setStackFromEnd(true);
                        recyclerView.setLayoutManager(layoutManager);
                        txtnotickets.setVisibility(GONE);
                    }else {
                        txtnotickets.setVisibility(View.VISIBLE);
                        txtViewAll.setVisibility(GONE);
                        recyclerView.setVisibility(GONE);
                    }
                }

                @Override public void onFailure(Call<AllBidder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<AllBidder> callofferApii() {
        return videoService.get_offers_id(oId, savePref.getUserId());
    }

    public  String randomAlphaNumeric(int count,String s) {
        StringBuilder builder = new StringBuilder();
        int character = 0;
        while (count-- != 0) {
            character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        if(s.equals("1")) {
            try {
                if ((Double.parseDouble(totalWallet) > 0) || ((tcount == 1) && (adsbtn))) {
                    String ss = builder.toString();
                    randomticket = ss;
                    txt1.setText(String.valueOf(ss.charAt(0)));
                    txt2.setText(String.valueOf(ss.charAt(1)));
                    txt3.setText(String.valueOf(ss.charAt(2)));
                    txt4.setText(String.valueOf(ss.charAt(3)));
                    if (fcount == (tcount + 1))
                        openaddbiddialog(randomticket);
                } else
                    openticketdialog("2", randomticket);
            }catch (Exception ignore){}
        }
        else{
            String ss=builder.toString();
            randomticket=ss;
            txt1.setText( String.valueOf(ss.charAt(0)));
            txt2.setText( String.valueOf(ss.charAt(1)));
            txt3.setText( String.valueOf(ss.charAt(2)));
            txt4.setText( String.valueOf(ss.charAt(3)));
            blink();
        }

        return builder.toString();
    }

    private void blink() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt1.startAnimation(anim);
        txt2.startAnimation(anim);
        txt3.startAnimation(anim);
        txt4.startAnimation(anim);
    }

    private JSONArray getJsonObj(ArrayList<String> arrayList) {
        JSONArray jArray = new JSONArray();
        for (int i = 0; i < arrayList.size(); i++) {
            JSONObject jGroup = new JSONObject();
            try {
                jGroup.put("u_id", savePref.getUserId());
                jGroup.put("o_id", oId);
                jGroup.put("bd_value", arrayList.get(i));
                jGroup.put("bd_amount", getAmount);
                jGroup.put("type_no", "1");
                jArray.put(jGroup);
            } catch (JSONException ignore) {}
        }
        return jArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gettime() {
        String currentDate= String.valueOf(java.time.LocalDate.now());
        String currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
        curr_dt_time= currentDate + " " + currentTime;
    }

    public void openemaildialog() {
        final Dialog dialog = new Dialog(RaffleDetailActivity.this);
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
                        Intent intent = new Intent(RaffleDetailActivity.this, RazorpayActivity.class);
                        intent.putExtra("email", new SavePref(getApplicationContext()).getemail());
                        intent.putExtra("activity", "CategoryDetailsAct");
                        intent.putExtra("amount", oBuy);
                        intent.putExtra("name", name);
                        intent.putExtra("O_id", oId);
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
        return videoService.add_order(new SavePref(getApplicationContext()).getUserId(),arrayList.get(0).getO_id(),arrayList.get(0).getO_amount(),"",arrayList.get(0).getO_amount(),address,"","");
    }

    public void update_api(String status,String sid){
        try {
            videoService.update_consolation(status,sid).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
}