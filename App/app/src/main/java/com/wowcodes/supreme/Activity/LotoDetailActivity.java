package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shawnlin.numberpicker.NumberPicker;
import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.AllBidderInner;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.AddTktResponse;
import com.wowcodes.supreme.Modelclas.GetLotteryId;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LotoDetailActivity extends AppCompatActivity  {
    BindingService videoService;
    String checkStatus,edate;
    String ball[]= { "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL"};
    int total;
    int tcount = 1,fcount=2;
    View dview;
    TextView txtBid,resetbtn;
    TextView buynow;
    String name,image,oBuy;
    String oId,type,totalBids,titleS,contactEmail;
    LinearLayout lvlLive;
    int  qty_O;
    SavePref savePref;
    String getBids;
    ProgressBar progressBar , circularProgressBar;
    TextView progressText,txtTimer,daysleft,txtsaleends;
    ImageView p0,p1,p2,p3,p4;
    String ticket;
    LinearLayout days,points;
    int premium_balls_start,premium_balls_end,normal_balls_start,normal_balls_end;
    ViewPager imgpager;
    String startDate;
    String totalWallet;
    String oAmount;
    TextView txtPay;
    boolean showDialog = false, randomGen = true;
    TextView txtBids;
    TextView txtYourr;
    ArrayList<AllBidderInner> arrayList;
    String getAmount;
    String userName;
    ConstraintLayout[] balll;
    TextView txtAucname;
    Boolean btn3Bool = false;
    TextView txtAds,btn2,txtitmleft;
    NumberPicker[] numberPickers;
    Button btn3;
    TextView incbtn,recqnt,decbtn,btn1;
    ImageView[] ballbg;
    int premium_balls,normal_balls,total_balls;



    String num1,num2,num3,num4,num5,n6,n7,n8;
    String curr_dt_time = "0000-00-00 00:00:00",claimable="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        setContentView(R.layout.activity_loto_detail);

        oId = getIntent().getStringExtra("O_id");
        checkStatus = getIntent().getStringExtra("check");
        qty_O = getIntent().getIntExtra("qty" , 0);
        totalBids = getIntent().getStringExtra("total_bids");
        type = getIntent().getStringExtra("type");
        edate=getIntent().getStringExtra("edate");
        if(getIntent().hasExtra("claimable"))
            claimable=getIntent().getStringExtra("claimable");
        ImageView imgBackk = findViewById(R.id.imgBackk);
        decbtn=findViewById(R.id.decbtn);
        incbtn=findViewById(R.id.incbtn);
        buynow=findViewById(R.id.buynow);
        recqnt=findViewById(R.id.recqnt);
        resetbtn=findViewById(R.id.resetbtn);
        txtAucname = findViewById(R.id.txtAucname);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        savePref = new SavePref(LotoDetailActivity.this);
        lvlLive = findViewById(R.id.linearlay);
        buynow=findViewById(R.id.buynow);
        txtTimer = findViewById(R.id.txtTimer);
        txtitmleft=findViewById(R.id.tktleft);
        p0=findViewById(R.id.p1);
        p1=findViewById(R.id.p2);
        p2=findViewById(R.id.p3);
        p3=findViewById(R.id.p4);
        p4=findViewById(R.id.p5);


        ballbg = new ImageView[]{
                findViewById(R.id.lottobg1), findViewById(R.id.lottobg2), findViewById(R.id.lottobg3),
                findViewById(R.id.lottobg4), findViewById(R.id.lottobg5), findViewById(R.id.lottobg6),
                findViewById(R.id.lottobg7), findViewById(R.id.lottobg8)
        };

        numberPickers = new NumberPicker[]{
                findViewById(R.id.NumPicker1), findViewById(R.id.NumPicker2), findViewById(R.id.NumPicker3),
                findViewById(R.id.NumPicker4), findViewById(R.id.NumPicker5), findViewById(R.id.NumPicker6),
                findViewById(R.id.NumPicker7), findViewById(R.id.NumPicker8)
        };

        balll = new ConstraintLayout[]{
                findViewById(R.id.ball1), findViewById(R.id.ball2), findViewById(R.id.ball3),
                findViewById(R.id.ball4), findViewById(R.id.ball5), findViewById(R.id.ball6),
                findViewById(R.id.ball7), findViewById(R.id.ball8)
        };

        points = findViewById(R.id.points);
        txtsaleends = findViewById(R.id.txtsaleends);
        imgpager = findViewById(R.id.image_pager);
        days = findViewById(R.id.days);
        daysleft = findViewById(R.id.daysleft);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.txtProgressText);
        circularProgressBar = findViewById(R.id.circleBar);
        txtPay = findViewById(R.id.txtPay);
        txtBids = findViewById(R.id.txtBids);
        txtYourr = findViewById(R.id.txtYourr);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        txtAds = findViewById(R.id.txtAds);
        btn2 = findViewById(R.id.btn2);

        TextView txtCount = findViewById(R.id.recqnt);
        TextView buttonInc = findViewById(R.id.incbtn);
        TextView buttonDec = findViewById(R.id.decbtn);
         total = 0;
        try {
            String oQtyStr = String.valueOf(qty_O);
            String totalBidsStr = getIntent().getStringExtra("total_bids");
            if (oQtyStr != null && !oQtyStr.isEmpty() && totalBidsStr != null && !totalBidsStr.isEmpty()) {
                int oQty = Integer.parseInt(oQtyStr);
                int totalBids = Integer.parseInt(totalBidsStr);
                total = oQty - totalBids;
                if (total < 0) {
                    total=0;
                }
            } else {
                Log.e("AuctionItemAdapter", "Quantity or total bids is null or empty");
            }
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            e.printStackTrace();
            // Optionally, set a default value or handle the error
        }
        txtitmleft.setText(String.valueOf(total)+" "+getString(R.string.tktleft));

        getprofile();

        lottoballs();

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LotoDetailActivity.this, SelectAddress.class);
                startActivity(i);
            }
        });
        if (total <= 0) {total = 0;
            btn2.setText(getString(R.string.notktleft));
            btn2.setClickable(false);
            btn2.setEnabled(false);
        }
        int max_bid = qty_O - Integer.parseInt(totalBids);
        if (max_bid <= 0) {
            progressText.setText(R.string.string336);
        } else {
            progressText.setText(max_bid + " " + getString(R.string.string340));
        }
        progressBar.setScaleY(6f);
        progressBar.setMax(qty_O);
        progressBar.setProgress(max_bid);

        buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tcount == 1) {
                    txtAds.setVisibility(View.GONE);
                    buttonDec.setEnabled(false);
                }
                if (max_bid - tcount >= 1) {
                    tcount++;
                }

                if (tcount != 1) {
                    txtAds.setVisibility(View.GONE);
                    buttonDec.setEnabled(true);
                    btn2.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                    btn3.setVisibility(View.VISIBLE);
                    if (total_balls > 0) {
                        for (int i = 0; i < total_balls; i++) {
                            numberPickers[i].setEnabled(false);
                        }
                    }
                }
                txtCount.setText(String.valueOf(tcount));
            }
        });

        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcount--;
                if (tcount == 1) {
                    txtAds.setVisibility(View.GONE);
                    buttonDec.setEnabled(false);
                    btn2.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn3.setVisibility(View.GONE);
                    if (total_balls > 0) {
                        for (int i = 0; i < total_balls; i++) {
                            numberPickers[i].setEnabled(true);
                        }
                    }
                } else {
                    txtAds.setVisibility(View.GONE);
                }
                txtCount.setText(String.valueOf(tcount));
            }
        });

        initializeNumberPickers();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog = false;
                if (max_bid > 0) {
                    circularProgressBar.setVisibility(View.VISIBLE);
                }
                if (max_bid > 0 && (Objects.equals(type, "4") || Objects.equals(type, "5"))) {
                    generateRandom();
                } else {
                    Toast.makeText(LotoDetailActivity.this, R.string.string336, Toast.LENGTH_SHORT).show();
                    btn1.setClickable(false);
                    btn2.setClickable(false);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog = false;
                circularProgressBar.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.GONE);
                btn3Bool = true;
                fcount=tcount+1;
                generateRandom();
            }
        });


// Use AtomicBoolean to track if any number picker was manually selected
        AtomicBoolean anyPickerSelected = new AtomicBoolean(false);

        for (int i = 0; i < numberPickers.length; i++) {
            numberPickers[i].setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    anyPickerSelected.set(true); // Set to true if any picker is selected
                }
            });
        }

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog = false;
                if (max_bid > 0)
                    circularProgressBar.setVisibility(View.VISIBLE);

                if (max_bid >= 0) {
                    for (int i = 0; i < numberPickers.length; i++) {
                        NumberPicker picker = numberPickers[i];
                        boolean isPremium = i < premium_balls;
                        ball[i] = getPickerValue(picker, isPremium);
                    }

                    // Check if no picker was selected, then generate random
                    if (randomGen && !anyPickerSelected.get()) {
                        generateRandom();
                    }

                    addticket();

                }
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNumberPickers();
            }
        });

        txtBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LotoDetailActivity.this, AllLottoActivity.class);
                i.putExtra("o_id", oId);
                startActivity(i);
            }
        });

        txtYourr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LotoDetailActivity.this, AllUserLottoActivity.class);
                i.putExtra("o_id", oId);
                startActivity(i);
            }
        });
        getofferapi();
        gettime();
        getsetting();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getofferapi();

        // Refresh the time (or countdown)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gettime();
        }

        // Refresh the user's profile
        getprofile();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("total", total);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    public void getofferapi() {
        lvlLive.setVisibility(View.VISIBLE);
        try {
            callofferApi().enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    lvlLive.setVisibility(View.GONE);
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
                        points.setVisibility(View.GONE);
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

                    imgpager.setAdapter(new ImagesAdapter(LotoDetailActivity.this,images));
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

                    txtPay.setText(getText(R.string.string231) +" @ "+ arrayList.get(0).getO_amount());
                    oAmount = arrayList.get(0).getO_amount();
                    image=arrayList.get(0).getO_image();
                    oBuy=arrayList.get(0).getO_buy();

                    if(oBuy.equalsIgnoreCase("") || oBuy.equalsIgnoreCase("0"))
                        buynow.setVisibility(GONE);
                    else
                        buynow.setVisibility(VISIBLE);

                    buynow.setText(getString(R.string.buynowfor).replace("$",MainActivity.currency).replace("10",oBuy));
                    name=arrayList.get(0).getO_name();
                    txtAucname.setText(arrayList.get(0).getO_name());
                    getAmount = arrayList.get(0).getO_amount();
                    titleS = arrayList.get(0).getO_name();

                    Thread myThread;
                    Runnable myRunnableThread = new CountDownRunner(txtTimer, arrayList.get(0).getO_edate() + " " + arrayList.get(0).getO_etime());
                    myThread = new Thread(myRunnableThread);
                    myThread.start();
                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    lvlLive.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {}
    }

    private Call<AllBidder> callofferApi() {
        return videoService.get_offers_id(oId, savePref.getUserId());
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
                days.setVisibility(View.GONE);
                txtTimer.setVisibility(View.GONE);
                txtBid.setClickable(false);
            }
            else {
                if(difference_In_Days<10)
                    daysleft.setText("0"+difference_In_Days);
                else if(difference_In_Days>10)
                    daysleft.setText(difference_In_Days+"");
                else
                    days.setVisibility(View.GONE);

                String hours=difference_In_Hours<10?"0"+difference_In_Hours:difference_In_Hours+"";
                String mins=difference_In_Minutes<10?"0"+difference_In_Minutes:difference_In_Minutes+"";
                String secs=difference_In_Seconds<10?"0"+difference_In_Seconds:difference_In_Seconds+"";
                textView.setText(hours+":"+mins+":"+secs);
            }
        } catch (Exception ignore) {}
    }

    public void generateRandom() {
        randomGen = false;
        Random random = new Random();
        int totalPickers = total_balls; // Total number of balls to generate random values for
        int[] randomValues = new int[totalPickers]; // Array to hold random values

        // Ensure numberPickers and ball arrays have the correct length
        if (numberPickers.length < totalPickers || ball.length < totalPickers) {
            Log.e("error", "generateRandom method lotto detail activity");
            return;
        }

        for (int i = 0; i < totalPickers; i++) {
            int max;
            if (i < premium_balls) {
                max = premium_balls_end - premium_balls_start + 1; // Adjust for alphabetic range
            } else {
                max = normal_balls_end - normal_balls_start + 1; // Adjust for numeric range
            }

            int ran;
            do {
                ran = random.nextInt(max) + (i < premium_balls ? premium_balls_start : normal_balls_start);
            } while (ran == 0);
            randomValues[i] = ran; // Store the value directly
        }

        // Set the displayed values and scroll to random values
        for (int i = 0; i < totalPickers; i++) {
            if (i < premium_balls) {
                numberPickers[i].setMinValue(premium_balls_start);
                numberPickers[i].setMaxValue(premium_balls_end);
                numberPickers[i].setDisplayedValues(null);
            } else {
                numberPickers[i].setMinValue(normal_balls_start);
                numberPickers[i].setMaxValue(normal_balls_end);
                numberPickers[i].setDisplayedValues(null); // Reset to numeric display
            }

            if (tcount == 1) {
                smoothScrollToValue(numberPickers[i], randomValues[i] - (i < premium_balls ? premium_balls_start : normal_balls_start));
            }
        }

        // Map the random values to your variables
        num1 = (premium_balls > 0 && randomValues.length > 0) ? String.valueOf(randomValues[0] - premium_balls_start) : "";
        num2 = (premium_balls > 1 && randomValues.length > 1) ? String.valueOf(randomValues[1] - premium_balls_start) : "";
        num3 = (premium_balls > 2 && randomValues.length > 2) ? String.valueOf(randomValues[2] - premium_balls_start) : "";
        num4 = (totalPickers > 3 && randomValues.length > 3) ? String.valueOf(randomValues[3]) : "";
        num5 = (totalPickers > 4 && randomValues.length > 4) ? String.valueOf(randomValues[4]) : "";
        n6 = (totalPickers > 5 && randomValues.length > 5) ? String.valueOf(randomValues[5]) : "";
        n7 = (totalPickers > 6 && randomValues.length > 6) ? String.valueOf(randomValues[6]) : "";
        n8 = (totalPickers > 7 && randomValues.length > 7) ? String.valueOf(randomValues[7]) : "";

        // Create final lotto string and handle ticket
        StringBuilder lottoBuilder = new StringBuilder();
        for (int i = 0; i < totalPickers; i++) {
            if (i < premium_balls) {
                lottoBuilder.append(String.valueOf(randomValues[i] - premium_balls_start));
                ball[i] = String.valueOf(randomValues[i] - premium_balls_start);
            } else {
                lottoBuilder.append(randomValues[i]);
                ball[i] = String.valueOf(randomValues[i]);
            }
        }

        circularProgressBar.setVisibility(View.GONE);
    }


    private void smoothScrollToValue(NumberPicker numberPicker, int targetValue) {
        int currentValue = numberPicker.getValue();
        int steps = targetValue - currentValue;
        boolean isIncrementing = steps > 0;
        steps = Math.abs(steps);

        Handler handler = new Handler();
        for (int i = 0; i <= steps; i++) {
            final int step = i;
            handler.postDelayed(() -> {
                if (isIncrementing) {
                    numberPicker.setValue(numberPicker.getValue() + 1);
                } else {
                    numberPicker.setValue(numberPicker.getValue() - 1);
                }
            }, i * 50); // Adjust the delay as needed for smoother or faster scrolling
        }
    }
    public void addticket() {
        try {lvlLive.setVisibility(VISIBLE);
            add_ticket().enqueue(new Callback<AddTktResponse>() {
                @Override
                public void onResponse(Call<AddTktResponse> call, retrofit2.Response<AddTktResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            ArrayList<AddTktResponse.add_tktinner> arrayList = response.body().getJSON_DATA();
                            circularProgressBar.setVisibility(GONE);
                            total -= 1;
                            if (total <= 0) {total = 0;
                                btn2.setText(getString(R.string.notktleft));
                                btn2.setClickable(false);
                                btn2.setEnabled(false);
                            }
                            txtitmleft.setText(String.valueOf(total)+" "+getString(R.string.tktleft));

                            String uniqueTicketId = arrayList.get(0).getTicket_id();
                            progressText.setText(total
                                    + " " + getString(R.string.string340));
                            showTicketDialog(uniqueTicketId);
                            Toast.makeText(LotoDetailActivity.this, arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            lvlLive.setVisibility(View.GONE);

                        }
                    }catch (Exception e){lvlLive.setVisibility(View.GONE);}
                }

                @Override
                public void onFailure(Call<AddTktResponse> call, Throwable t) {
                    lvlLive.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }



    private Call<AddTktResponse> add_ticket() {
        return videoService.add_ticket(oId, savePref.getUserId(),ball[0],ball[1],ball[2],ball[3],ball[4],ball[5],ball[6],ball[7]);
    }
    private void showTicketDialog(String uniqueTicketId) {

        LayoutInflater inflater = LayoutInflater.from(LotoDetailActivity.this);
        Dialog dialogView = new Dialog(LotoDetailActivity.this);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.setContentView(R.layout.dialog_ticket_purchased);
        Window window = dialogView.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView qrImageView = dialogView.findViewById(R.id.qrImg);
        ProgressBar progressBar = dialogView.findViewById(R.id.qr_progress_bar);
        TextView purchaseDt=dialogView.findViewById(R.id.purchaseDt);
        TextView priceTkt=dialogView.findViewById(R.id.priceTkt);
        TextView drawDt=dialogView.findViewById(R.id.drawDt);
        TextView username=dialogView.findViewById(R.id.username);
        TextView ticket0=dialogView.findViewById(R.id.ticket0);
        TextView ticket1=dialogView.findViewById(R.id.ticket1);
        TextView ticket2=dialogView.findViewById(R.id.ticket2);
        TextView ticket3=dialogView.findViewById(R.id.ticket3);
        TextView ticket4=dialogView.findViewById(R.id.ticket4);
        TextView ticket5=dialogView.findViewById(R.id.ticket5);
        TextView ticket6=dialogView.findViewById(R.id.ticket6);
        TextView ticket7=dialogView.findViewById(R.id.ticket7);
        TextView contactEmailTxt=dialogView.findViewById(R.id.contactEmail);
        TextView printBtn=dialogView.findViewById(R.id.printBtn);

        Button closeBtn=dialogView.findViewById(R.id.closeBtn);

        ConstraintLayout[] ballConsll = new ConstraintLayout[]{
                dialogView.findViewById(R.id.ball1bg), dialogView.findViewById(R.id.ball2bg), dialogView.findViewById(R.id.ball3bg),
                dialogView.findViewById(R.id.ball4bg), dialogView.findViewById(R.id.ball5bg), dialogView.findViewById(R.id.ball6bg),
                dialogView.findViewById(R.id.ball7bg), dialogView.findViewById(R.id.ball8bg)
        };
        printBtn.setOnClickListener(v -> {
            printBtn.setVisibility(GONE);
            closeBtn.setVisibility(View.GONE); // Hide the Close button
            printTicket(dialogView.findViewById(R.id.dialog_container));
            printBtn.setVisibility(VISIBLE);
            closeBtn.setVisibility(VISIBLE);// Pass the root view of the dialog
        });
        priceTkt.setText(oAmount +" "+getString(R.string.coinstxt));
        contactEmailTxt.setText(contactEmail);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = formatter.format(currentTime);
            purchaseDt.setText(formattedDate);
        } else{
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(currentTimeMillis);

        purchaseDt.setText(formattedDate);
    }


            for (int i = 0; i <total_balls ; i++) {
                ballConsll[i].setVisibility(View.VISIBLE);
                if (i <premium_balls) {
                    ballConsll[i].setBackgroundResource(R.drawable.premium);
                }
            }
            ticket0.setText(ball[0]);
            ticket1.setText(ball[1]);
            ticket2.setText(ball[2]);
            ticket3.setText(ball[3]);
            ticket4.setText(ball[4]);
            ticket5.setText(ball[5]);
            ticket6.setText(ball[6]);
            ticket7.setText(ball[7]);


        drawDt.setText(edate);
        username.setText(userName);
        String qrCodeUrl = Constants.main_url + "/generate_qr.php?text=" + uniqueTicketId;

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(LotoDetailActivity.this)
                .load(qrCodeUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // Enable disk caching
                .error(R.drawable.ic_error_black_24dp)                // Error image if loading fails
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(qrImageView);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

            }
        });

        dialogView.show();
    }

    public void getprofile() {
        try {
            // Making the API call
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        // Ensure the response body is not null
                        if (response.body() != null) {
                            ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();

                            if (arrayList != null && !arrayList.isEmpty()) {
                                // Extract wallet balance and user name
                                totalWallet = arrayList.get(0).getWallet();
                                userName = arrayList.get(0).getName();

                                // Combined check for null/empty wallet or insufficient balance
                                if (totalWallet == null || totalWallet.isEmpty() || Integer.parseInt(totalWallet) < Integer.parseInt(oAmount)) {
                                    btn2.setEnabled(false);
                                    btn2.setText(R.string.insufficientBal);
                                } else {
                                    btn2.setEnabled(true);
                                }
                            } else {
                                // Handle the case where the arrayList is empty
                                lvlLive.setVisibility(View.GONE);
                            }
                        } else {
                            // Handle the case where response body is null
                            lvlLive.setVisibility(View.GONE);
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid number format
                        e.printStackTrace();
                    } catch (Exception e) {
                        // Handle any unexpected exceptions
                        lvlLive.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    // Handle the failure of the API call
                    lvlLive.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            // Handle any exceptions thrown when attempting to make the API call
            e.printStackTrace();
        }
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gettime() {
        String currentDate= String.valueOf(java.time.LocalDate.now());
        String currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
        curr_dt_time= currentDate + " " + currentTime;
    }








    public void lottoballs() {
        try {
            calllotteryid().enqueue(new Callback<GetLotteryId>() {
                @Override
                public void onResponse(Call<GetLotteryId> call, Response<GetLotteryId> response) {
                    if (response.isSuccessful()) {
                        List<GetLotteryId.BallData> arrayList = response.body().getJSON_DATA();

                        // Debugging log to check API response
                        Log.d("LotoDetailActivity", "API Response: " + arrayList);

                        // Retrieve values from arrayList
                        String premiumBallLimit = arrayList.get(0).getPremium_ball_limit();
                        String normalBallLimit = arrayList.get(0).getNormal_ball_limit();
                        String premiumballstart = arrayList.get(0).getPremium_ball_start();
                        String premiumballend = arrayList.get(0).getPremium_ball_end();
                        String normalballstart = arrayList.get(0).getNormal_ball_start();
                        String normalballend = arrayList.get(0).getNormal_ball_end();

                        btn2.setText("Buy Ticket For " + arrayList.get(0).getTicket_price());

                        // Initialize default values
                        total_balls = 0;
                        premium_balls = 0;
                        normal_balls = 0;

                        // Check and parse premiumBallLimit
                        if (premiumBallLimit != null && !premiumBallLimit.isEmpty()) {
                            try {
                                premium_balls = Integer.parseInt(premiumBallLimit);
                                premium_balls_start = Integer.parseInt(premiumballstart);
                                premium_balls_end = Integer.parseInt(premiumballend);
                                total_balls += premium_balls;
                                Log.d("LotoDetailActivity", "Premium Balls: " + premium_balls);
                            } catch (NumberFormatException e) {
                                e.printStackTrace(); // Log the error for debugging
                            }
                        }

                        // Check and parse normalBallLimit
                        if (normalBallLimit != null && !normalBallLimit.isEmpty()) {
                            try {
                                normal_balls_start = Integer.parseInt(normalballstart);
                                normal_balls_end = Integer.parseInt(normalballend);
                                normal_balls = Integer.parseInt(normalBallLimit);
                                total_balls += normal_balls;
                                Log.d("LotoDetailActivity", "Normal Balls: " + normal_balls);
                            } catch (NumberFormatException e) {
                                e.printStackTrace(); // Log the error for debugging
                            }
                        }

                        initialisevisibility(total_balls);

                        // Set color for premium balls
                        for (int i = 0; i < premium_balls; i++) {
                            final int index = i;
                            runOnUiThread(() -> {
                                ballbg[index].setBackground(ContextCompat.getDrawable(LotoDetailActivity.this, R.drawable.premium));
                                ballbg[index].setVisibility(View.VISIBLE);
                            });
                        }


                        // Initialize the number pickers with the new values
                        initializeNumberPickers();

                    } else {
                        Toast.makeText(LotoDetailActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetLotteryId> call, Throwable t) {
                    Toast.makeText(LotoDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("LotoDetailActivity", "Exception in lottoballs: ", e);
        }
    }

    private void initialisevisibility(int totalBalls) {
        for (int i = 0; i < totalBalls; i++) {
            balll[i].setVisibility(View.VISIBLE);
        }
    }

    private void resetNumberPickers() {
        for (int i = 0; i < numberPickers.length; i++) {
            if (i < premium_balls) {
                numberPickers[i].setMinValue(premium_balls_start);
                numberPickers[i].setMaxValue(premium_balls_end);
                numberPickers[i].setDisplayedValues(null);
                numberPickers[i].setValue(premium_balls_start);
            } else {
                numberPickers[i].setMinValue(normal_balls_start);
                numberPickers[i].setMaxValue(normal_balls_end);
                numberPickers[i].setDisplayedValues(null);
                numberPickers[i].setValue(normal_balls_start);
            }
        }
    }

    private String getPickerValue(NumberPicker picker, boolean isPremium) {
        return String.valueOf(picker.getValue());
    }
    private void initializeNumberPickers() {

        for (int i = 0; i < numberPickers.length; i++) {
            NumberPicker picker = numberPickers[i];
            if (i < premium_balls) {
                picker.setMinValue(premium_balls_start);
                picker.setMaxValue(premium_balls_end);
                picker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        return String.format("%01d", i);
                    }
                });
                picker.setWrapSelectorWheel(true);
                picker.setValue(premium_balls_start);

            } else {
                // For numeric NumberPickers
                picker.setMinValue(normal_balls_start);
                picker.setMaxValue(normal_balls_end);
                picker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        return String.format("%01d", i);
                    }
                });
                picker.setWrapSelectorWheel(true);
                picker.setValue(normal_balls_start); // Set initial value
            }
        }

        // Optionally, if you need specific settings for the first two pickers

    }
    public void getsetting() {
        try {
            callseting().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                  contactEmail=arrayList.get(0).getSupport_email();
                }

                @Override
                public void onFailure(Call<SettingModel> call, Throwable t) {
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<SettingModel> callseting() {
        return videoService.settings();
    }

    private Call<GetLotteryId> calllotteryid( ) {
        return videoService.get_lottery_id(oId);
    }
    private void printTicket(View view) {
        PrintHelper photoPrinter = new PrintHelper(LotoDetailActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        // Create a bitmap of the view to print
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        // Print the bitmap
        photoPrinter.printBitmap(String.valueOf(R.string.ticket_print), bitmap);
    }


}