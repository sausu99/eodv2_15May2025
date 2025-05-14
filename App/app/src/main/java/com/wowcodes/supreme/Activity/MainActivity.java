package com.wowcodes.supreme.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wowcodes.supreme.Fragments.CoinHistoryFragment;
import com.wowcodes.supreme.Fragments.InvestFragment;
import com.wowcodes.supreme.Fragments.ReferralFragment;
import com.wowcodes.supreme.Fragments.AuctionFragment;
import com.wowcodes.supreme.Fragments.UpcomingFragment;
import com.wowcodes.supreme.Fragments.WinnerList;
import com.wowcodes.supreme.Fragments.YourProfileFragment;
import com.wowcodes.supreme.Modelclas.GetNotification;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    public static String stringAbout, stringCondi, stringPrivacy, stringContact, stringHowTo, stringEmail;
    public static String showad, fb_interstitial, admob_interstitial, fb_banner;
    public static String name, logo, paypal_currency, vungle_app, vungle_id, adcolony_app, adcolony_id, unity_game, unity_id, currency = "₹", admob_rewarded, facebook_rewarded, applovin_rewarded, startio_rewarded, ironsource_rewarded, mpesa_key, mpesa_code, paypal_id, paypal_secret, flutterwave_public, flutterwave_encryption, razorpay, stripe, coinvalue;
    public BindingService videoService;
    CoinHistoryFragment shopFragment;
    AuctionFragment auctionFragment;
    InvestFragment investFragment;
    private ScheduledExecutorService scheduler;

    public static Fragment active;
    String title, type, totalBids, oId, checkStatus, activityType;
    int qty_O;
    Boolean backR;
    WinnerList winnerFragment;
    public static BottomNavigationView bottomNavigationView;
    SavePref savePref;
    String page = "1";
    LinearLayout nointernetlayout,walletTxtBtn  ;
    private AdView mAdView;
    TextView coinstxt;
    private com.facebook.ads.AdView fbadView;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd finterstitialAd;
    ImageView notifications;
    YourProfileFragment hamburgerFrag;
    ReferralFragment referralFragment;
    Boolean commited = true;
    private Boolean Admobshow = true;
    private MenuItem previousMenuItem;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(MainActivity.this);

        if (savePref.getLang() == null)
            savePref.setLang("en");
        if (Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blackdeep));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        nointernetlayout = (LinearLayout) findViewById(R.id.nointernetlayout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        notifications=findViewById(R.id.notifications);
        walletTxtBtn=findViewById(R.id.walletTxtBtn);
        coinstxt=findViewById(R.id.coinstxt);
        hamburgerFrag = new YourProfileFragment();

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        auctionFragment = new AuctionFragment();
        shopFragment = new CoinHistoryFragment();
        referralFragment = new ReferralFragment();
        winnerFragment = new WinnerList();
        investFragment=new InvestFragment();


        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            oId = getIntent().getStringExtra("O_id");
            qty_O = getIntent().getIntExtra("qty", 0);
            totalBids = getIntent().getStringExtra("total_bids");
            type = getIntent().getStringExtra("type");
            activityType = getIntent().getStringExtra("activityType");
            backR = getIntent().getBooleanExtra("back", false);
            checkStatus = getIntent().getStringExtra("check");
        }

        load_all_fragments_at_first();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::getprofile, 0, 5, TimeUnit.SECONDS);

        try {
            if(StaticData.userProfileList.isEmpty()) {
                videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                        try {
                            ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                            StaticData.userProfileList.add(0, arrayList.get(0).getImage() == null ? "" : arrayList.get(0).getImage());
                            StaticData.userProfileList.add(1, arrayList.get(0).getName());
                        }catch (Exception ignore){}
                    }

                    @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
                });
            }
        } catch (Exception ignore) {}


        try {
            videoService.get_notification_history(new SavePref(this).getUserId()).enqueue(new Callback<GetNotification>() {
                @Override
                public void onResponse(@NonNull Call<GetNotification> call, @NonNull retrofit2.Response<GetNotification> response) {
                    ArrayList<GetNotification.Get_notification_Inner> arrayList= null;
                    if (response.body() != null) {
                        arrayList = response.body().getJSON_DATA();
                        boolean newer=false;
                        for(GetNotification.Get_notification_Inner notification : arrayList){
                            if (notification.getStatus().equalsIgnoreCase("1")) {
                                newer = true;
                                break;
                            }
                        }

                        if(newer)
                            notifications.setImageDrawable(getResources().getDrawable(R.drawable.ic_bell_filled));
                        else
                            notifications.setImageDrawable(getResources().getDrawable(R.drawable.ic_bell));
                    }
                }

                @Override public void onFailure(Call<GetNotification> call, Throwable t) {}
            });
        } catch (Exception ignore) {}

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NotificationHistory.class));
                notifications.setImageDrawable(getResources().getDrawable(R.drawable.ic_bell));
            }
        });



        stringAbout = "https://wowcodes.in/About-Us.html";

        if (backR) {
            if (Objects.equals(activityType, "Lotto"))
                showAlertDialogLoto();
            else
                showAlertDialogRaffle();
        }
        getsetting();
        mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        try {
            if (getIntent() != null)
                page = getIntent().getStringExtra("page");
        } catch (Exception ignore) {}

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        if (Objects.equals(showad, "1"))
            loadAdmobIntertial();



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile_section:
                        loadFragment(hamburgerFrag);
                        return true;

                    case R.id.auctions_section:
                        loadFragment(auctionFragment);
                        return true;

                    case R.id.wallet_section:
                        loadFragment(shopFragment);
                        return true;

                    case R.id.winners_section:

                        loadFragment(winnerFragment);
                        return true;
                    default:
                        return false;


                }

            }
        });

        walletTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.wallet_section);
            }
        });
        // Set default selection

        bottomNavigationView.setSelectedItemId(R.id.auctions_section);

        isNetworkConnected();
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(active).show(fragment);
        transaction.commit();
        active=fragment;
    }

    private void load_all_fragments_at_first() {
        active=auctionFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,new UpcomingFragment(),"lottery").hide(new UpcomingFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,winnerFragment,"winner").hide(winnerFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,hamburgerFrag,"You").hide(hamburgerFrag).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,referralFragment,"referrals").hide(referralFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,investFragment,"investments").hide(investFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,shopFragment,"wallet").hide(shopFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout,auctionFragment,"home").commit();
        bottomNavigationView.setSelectedItemId(R.id.auctions_section);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("navigateTo")) {
            int navigateTo = intent.getIntExtra("navigateTo", R.id.wallet_section); // Set your default section if none is passed
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(navigateTo);
        }
    }

    public void showAlertDialogRaffle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Continue Bidding");
        builder.setMessage(getString(R.string.string337) + "  " + title);
        builder.setPositiveButton(R.string.string338, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, RaffleDetailActivity.class);
                intent.putExtra("O_id", oId);
                intent.putExtra("check", "draw");
                intent.putExtra("total_bids", totalBids);
                intent.putExtra("qty", qty_O);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.string339, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlertDialogLoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Continue Bidding");
        builder.setMessage(getString(R.string.string341) + "  " + title);
        builder.setPositiveButton(R.string.string338, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LotoDetailActivity.class);
                intent.putExtra("O_id", oId);
                intent.putExtra("check", "draw");
                intent.putExtra("total_bids", totalBids);
                intent.putExtra("qty", qty_O);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.string339, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loadAdmobIntertial() {
        if (Admobshow) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(MainActivity.this, admob_interstitial, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                public void onAdClicked() {
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    mInterstitialAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    mInterstitialAd = null;
                                }
                                @Override public void onAdImpression() {}
                                @Override public void onAdShowedFullScreenContent() {}
                            });
                            if (mInterstitialAd != null) {
                                Admobshow = false;
                                mInterstitialAd.show(MainActivity.this);
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mInterstitialAd = null;
                        }
                    });
        } else {
            finterstitialAd = new com.facebook.ads.InterstitialAd(this, fb_interstitial);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override public void onInterstitialDisplayed(Ad ad) {}
                @Override public void onInterstitialDismissed(Ad ad) {}
                @Override public void onError(Ad ad, com.facebook.ads.AdError adError) {}
                @Override public void onAdClicked(Ad ad) {}
                @Override public void onLoggingImpression(Ad ad) {}

                @Override
                public void onAdLoaded(Ad ad) {
                    Admobshow = true;
                    finterstitialAd.show();
                }
            };

            finterstitialAd.loadAd(finterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the scheduler when the activity is destroyed
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (savePref.getLang() == null)
            savePref.setLang("en");
        if (Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (savePref.getLang() == null)
            savePref.setLang("en");
        if (Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (savePref.getLang() == null)
            savePref.setLang("en");
        if (Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());
    }

    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected()) {
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        if (cm.getActiveNetworkInfo() != null) {
            cm.getActiveNetworkInfo().isConnected();
        }
    }

    @Override
    public void onBackPressed() {


        if (active == auctionFragment) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_close);
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button exit = dialog.findViewById(R.id.exit);
                Button cancel = dialog.findViewById(R.id.cancel);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            else{
                loadFragment(auctionFragment);
                bottomNavigationView.setSelectedItemId(R.id.auctions_section);
            }

        }


    public void getsetting() {
        try {
            callseting().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, retrofit2.Response<SettingModel> response) {
                    try {
                        ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                        stringCondi = arrayList.get(0).getAbout_us();
                        stringPrivacy = arrayList.get(0).getApp_privacy_policy();
                        stringContact = arrayList.get(0).getAbout_us();
                        stringHowTo = arrayList.get(0).getHow_to_play();
                        stringEmail = arrayList.get(0).getApp_email();
                        name = arrayList.get(0).getName();
                        logo = arrayList.get(0).getLogo();
                        vungle_app = arrayList.get(0).getVungle_app();
                        paypal_currency = arrayList.get(0).getPaypal_currency();
                        vungle_id = arrayList.get(0).getVungle_id();
                        adcolony_app = arrayList.get(0).getAdcolony_app();
                        adcolony_id = arrayList.get(0).getAdcolony_id();
                        unity_game = arrayList.get(0).getUnity_game();
                        unity_id = arrayList.get(0).getUnity_id();
                        currency = arrayList.get(0).getCurrency();
                        admob_rewarded = arrayList.get(0).getAdmob_rewarded();
                        facebook_rewarded = arrayList.get(0).getFacebook_rewarded();
                        applovin_rewarded = arrayList.get(0).getApplovin_rewarded();
                        startio_rewarded = arrayList.get(0).getStartio_rewarded();
                        ironsource_rewarded = arrayList.get(0).getIronsource_rewarded();
                        mpesa_key = arrayList.get(0).getMpesa_key();
                        mpesa_code = arrayList.get(0).getMpesa_code();
                        coinvalue = arrayList.get(0).getCoinvalue();
                        paypal_id = arrayList.get(0).getPaypal_id();
                        paypal_secret = arrayList.get(0).getPaypal_secret();
                        flutterwave_public = arrayList.get(0).getFlutterwave_public();
                        flutterwave_encryption = arrayList.get(0).getFlutterwave_encryption();
                        razorpay = arrayList.get(0).getRazorpay();
                        stripe = arrayList.get(0).getStripe();
                        showad = arrayList.get(0).getShowad();
                        fb_interstitial = arrayList.get(0).getFb_interstitial();
                        fb_banner = arrayList.get(0).getFb_banner();
                        admob_interstitial = arrayList.get(0).getAdmob_interstitial();
                        if (Objects.equals(showad, "1")) {
                            mAdView.setVisibility(View.VISIBLE);
                            AdRequest adRequest = new AdRequest.Builder().build();
                            mAdView.loadAd(adRequest);
                        }
                        if (Objects.equals(showad, "1")) {
                            LinearLayout adContainer = (LinearLayout) findViewById(R.id.fbadview);
                            adContainer.setVisibility(View.VISIBLE);
                            fbadView = new com.facebook.ads.AdView(MainActivity.this, fb_banner, AdSize.BANNER_HEIGHT_50);
                            adContainer.addView(fbadView);
                            fbadView.loadAd();
                        }
                    } catch (Exception ignore) {}
                }
                @Override public void onFailure(Call<SettingModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<SettingModel> callseting() {
        return videoService.settings();
    }

    public void getprofile() {
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        String totalwallet = arrayList.get(0).getWallet();
                        if (totalwallet == null) {
                            totalwallet = "0"; // Set totalwallet to "0" if it's null
                        }
                        coinstxt.setText(totalwallet);
                        savePref.setName(arrayList.get(0).getName());
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}