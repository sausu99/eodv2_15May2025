package com.wowcodes.supreme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoManualListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAdsShowOptions;
import com.vungle.warren.InitCallback;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.NoInternetActivity;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;


import com.vungle.warren.Vungle;
import com.vungle.warren.LoadAdCallback;        // Load ad callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.error.VungleException;  // onError message

import java.util.ArrayList;
import java.util.List;

import com.unity3d.ads.UnityAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ADSHOW extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    RewardedVideoAd rewardedVideoAd;
    private RewardedAd mRewardedAd;
    public BindingService videoService;
    TextView txtMpesa;
    private boolean vunglerewarded = false;
    SavePref savePref;
    //adcolony
    private final String APP_ID = MainActivity.adcolony_app;
    String message;
    private final String REWARD_ZONE_ID = MainActivity.adcolony_id;
    private Button adcolonyBtn;
    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;
    private static boolean isRewardLoaded;

    //adcoloy end
    //unity
    private String rewardedPlacement = MainActivity.unity_id;
    private String unityGameID = MainActivity.unity_game;
    private boolean unityInitialized = false;
    // "4147749";
    // "5076731";
    private Boolean testMode = true, setLoading = false;
    private Button rewarded;
    private Button applovinBtn, ironsourceBtn;
    //unity//
    String getWallet, packageId, oId;
    ArrayList<GetCoin.Get_Coin_Inner> arrayList;
    private ProgressDialog mProgressDialog;

    //apvloin
    private MaxRewardedAd maxRewardedAd;
    private int retryAttempt;
    TextView AucName;
    ImageView imgBackk;


    //applovin end
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
        setContentView(R.layout.activity_a_d_s_h_o_w);
        savePref = new SavePref(ADSHOW.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        //  listmethode();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mProgressDialog = new ProgressDialog(this);
        getWallet = "50";
        packageId = "second";
        oId = "adsbenifite";
        Button admob = findViewById(R.id.adreward_bttn);
        AucName=findViewById(R.id.txtAucname);
        imgBackk=findViewById(R.id.imgBackk);

        try {
            videoService.settings().enqueue(new Callback<SettingModel>() {
                @Override public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                    try {
                        getWallet = arrayList.get(0).getAds_reward();
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<SettingModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}

        AucName.setText(getText(R.string.string109));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button facebook = findViewById(R.id.fbrewardbtn);
        applovinBtn = findViewById(R.id.applovinBtn);
        admob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                showadmobreward();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                showfacebook();
            }
        });
//start unity
        //unityadsInitialize();
        rewarded = findViewById(R.id.unity_bttn);
        rewarded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                if (!unityInitialized) {
                    UnityAds.initialize(ADSHOW.this, unityGameID, new IUnityAdsInitializationListener() {
                        @Override
                        public void onInitializationComplete() {

                        }

                        @Override
                        public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                        }
                    });
                    setLoading = true;
                }
               /*     mProgressDialog.setMessage("Loading");
                    mProgressDialog.setTitle("Please Wait...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                } */

                Runnable runnable = new Runnable() { //New Thread
                    @Override
                    public void run() {
                        UnityAds.load(rewardedPlacement, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                mProgressDialog.dismiss();
                                UnityAds.show(ADSHOW.this, rewardedPlacement, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                                    @Override
                                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                    }

                                    @Override
                                    public void onUnityAdsShowStart(String placementId) {
                                    }

                                    @Override
                                    public void onUnityAdsShowClick(String placementId) {
                                    }

                                    @Override
                                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                        postUserwalletUpdate();
                                    }
                                });
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                            }
                        });
                }};
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(runnable, 0);
            }
        });
        //unity end

//applovin start
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading
            }
        });
        MaxRewardedAdListener maxrewardedListner = new MaxRewardedAdListener() {

            @Override
            public void onAdLoaded(MaxAd ad) {

            }


            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {

            }


            @Override
            public void onRewardedVideoStarted(MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {

            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {

                vunglerewarded = true;
                postUserwalletUpdate();
            }
        };
        maxRewardedAd = MaxRewardedAd.getInstance(""+MainActivity.applovin_rewarded, this);
        maxRewardedAd.setListener(maxrewardedListner);
        maxRewardedAd.loadAd();
        applovinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                if (maxRewardedAd.isReady()) {
                    maxRewardedAd.showAd();
                }
            }
        });

        // applovin end
        //adcolony start
        adcolonyBtn = findViewById(R.id.adcolonyBtn);
        initColonySdk();
        adcolonyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                initRewardedAd();
            }
        });


        //adcolony end
        //iron source

        ironsourceBtn = findViewById(R.id.ironsourceBtn);


        ironsourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                RewardedVideoManualListener rewardedVideoListenerM = new RewardedVideoManualListener() {
                    @Override
                    public void onRewardedVideoAdReady() {
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo("DefaultRewardedVideo");
                        }
                    }

                    @Override
                    public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onRewardedVideoAdOpened() {

                    }

                    @Override
                    public void onRewardedVideoAdClosed() {
                        mProgressDialog.dismiss();
                        if(vunglerewarded) {
                            postUserwalletUpdate();
                            vunglerewarded = false;
                        }
                        else {
                            message = "Ad Skipped";
                            opendialog();
                        }
                    }
                    @Override
                    public void onRewardedVideoAvailabilityChanged(boolean b) {
                    }

                    @Override
                    public void onRewardedVideoAdStarted() {
                    }

                    @Override
                    public void onRewardedVideoAdEnded() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onRewardedVideoAdRewarded(Placement placement) {
vunglerewarded = true;                    }

                    @Override
                    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onRewardedVideoAdClicked(Placement placement) {
                        mProgressDialog.dismiss();
                    }
                };
                IronSource.setManualLoadRewardedVideo(rewardedVideoListenerM);
                IronSource.setRewardedVideoListener(rewardedVideoListenerM);
                IronSource.loadRewardedVideo();
            }
        });
        //iron source end
        //vungle


        Button vungleBtn = findViewById(R.id.vungleBtn);
        vungleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                mProgressDialog.setMessage(getText(R.string.string147));
                mProgressDialog.setTitle(R.string.string148);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
                    @Override
                    public void onAdStart(String id) {
                        // Ad experience started
                    }

                    @Override
                    public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                        if (vunglerewarded) {
                            vunglerewarded = false;
                            postUserwalletUpdate();
                        }
                        else {
                            message = "Ad Skipped";
                            opendialog();
                        }
                    }

                    @Override
                    public void onAdEnd(String placementId) {

                    }

                    @Override
                    public void onAdViewed(String id) {
                        // Ad has rendered
                    }


                    @Override
                    public void onAdClick(String id) {
                        // User clicked on ad
                    }

                    @Override
                    public void onAdRewarded(String id) {

                        vunglerewarded = true;
                        //postUserwalletUpdate();
                        // User earned reward for watching an rewarded ad
                    }

                    @Override
                    public void onAdLeftApplication(String id) {
                        // User has left app during an ad experience
                    }

                    @Override
                    public void creativeId(String creativeId) {
                        // Vungle creative ID to be displayed
                    }

                    @Override
                    public void onError(String id, VungleException exception) {
                        // Ad failed to play
                    }
                };
                Vungle.init(""+MainActivity.vungle_app, getApplicationContext(), new InitCallback() {
                    @Override
                    public void onSuccess() {
                        // SDK has successfully initialized
                    }

                    @Override
                    public void onError(VungleException exception) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ADSHOW.this, "Failed. Please try again.", Toast.LENGTH_SHORT).show();

                        // SDK has failed to initialize
                    }

                    @Override
                    public void onAutoCacheAdAvailable(String placementId) {
                        // Ad has become available to play for a cache optimized placement
                    }
                });

                Runnable runnable = new Runnable() { //New Thread
                    @Override
                    public void run() {
                        if (Vungle.isInitialized()) {

                            Vungle.loadAd(""+MainActivity.vungle_id, new LoadAdCallback() {
                                @Override
                                public void onAdLoad(String placementReferenceId) {
                                    if (Vungle.canPlayAd(""+MainActivity.vungle_id)) {
                                        mProgressDialog.dismiss();
                                        Vungle.playAd(""+MainActivity.vungle_id, null, vunglePlayAdCallback);
                                    }
                                }

                                @Override
                                public void onError(String placementReferenceId, VungleException exception) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(ADSHOW.this, "Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                };
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(runnable, 3000);

            }
        });

        //vungle end
    }

   /* private void unityadsInitialize() {
        UnityAds.initialize(ADSHOW.this, unityGameID, testMode, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {

                unityInitialized = true;
                IUnityAdsListener rewardedListner = new IUnityAdsListener() {
                    @Override
                    public void onUnityAdsReady(String s) {
                }

                    @Override
                    public void onUnityAdsStart(String s) {

                    }

                    @Override
                    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
                        // Implement conditional logic for each ad completion status:
                        if (finishState.equals(UnityAds.FinishState.COMPLETED)) {
                            postUserwalletUpdate();
                            //Toast.makeText(ADSHOW.this, "Completed", Toast.LENGTH_SHORT).show();

                        } else if (finishState == UnityAds.FinishState.SKIPPED) {
                            // Do not reward the user for skipping the ad.
                            message = "Ad Skipped";
                            opendialog();
                        } else if (finishState == UnityAds.FinishState.ERROR) {
                            // Log an error.
                            Toast.makeText(ADSHOW.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
                        Toast.makeText(ADSHOW.this, "Error " + unityAdsError, Toast.LENGTH_SHORT).show();
                    }
                };
                UnityAds.setListener(rewardedListner);
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

            }
        });
    } */


    private Call<GetCoin> callcoinApi() {
        return videoService.get_coin_list();
    }

    private void listmethode() {
        try {
            callcoinApi().enqueue(new Callback<GetCoin>() {
                @Override
                public void onResponse(Call<GetCoin> call, retrofit2.Response<GetCoin> response) {

                    try {
                        // lvlYoufrag.setVisibility(View.GONE);
                        arrayList = response.body().getJSON_DATA();
                        //   recyclerYoufrag.setAdapter(new CoinAdapter(getContext(), arrayList));
                    } catch (Exception e) {
                        e.printStackTrace();
                        // lvlYoufrag.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetCoin> call, Throwable t) {
                    // lvlYoufrag.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showfacebook() {
        AudienceNetworkAds.initialize(this);
        //  AdSettings.addTestDevice("866c86a2-e40a-422d-9eba-b701060570e4");
        List<String> testDevices = new ArrayList<>();
        testDevices.add("866c86a2-e40a-422d-9eba-b701060570e4");
        testDevices.add("666cd059-0056-4e35-81b6-ace471f1b81b");
        testDevices.add("be257cf2-23f0-4099-9f60-8c0898887793");
        AdSettings.addTestDevices(testDevices);
        //banner       1372463509617191_1372463572950518
        // rewardedd    1372463509617191_1644534835743389
        rewardedVideoAd = new RewardedVideoAd(this, ""+MainActivity.facebook_rewarded);
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                mProgressDialog.dismiss();
                Toast.makeText(ADSHOW.this, "" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                mProgressDialog.dismiss();
                // Rewarded video ad is loaded and ready to be displayed
                Toast.makeText(ADSHOW.this, "loaded", Toast.LENGTH_SHORT).show();
                rewardedVideoAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                // Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                //  Log.d(TAG, "Rewarded video completed!");

                // Call method to give reward
                // giveReward();

                postUserwalletUpdate();
            }

            @Override
            public void onRewardedVideoClosed() {

                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                //   Log.d(TAG, "Rewarded video ad closed!");
            }
        };
        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());


    }

    private void showadmobreward() {
        AdRequest vadRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, ""+MainActivity.admob_rewarded,
                vadRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mProgressDialog.dismiss();
                        Toast.makeText(ADSHOW.this, "" + loadAdError, Toast.LENGTH_SHORT).show();
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mProgressDialog.dismiss();
                        mRewardedAd = rewardedAd;
                        //Toast.makeText(ADSHOW.this, "adloaded", Toast.LENGTH_SHORT).show();
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                // Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                // Called when ad fails to show.
                                //Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                // Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                        Activity activityContext = ADSHOW.this;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                //  Log.d(TAG, "The user earned the reward.");
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                                postUserwalletUpdate();
                            }
                        });
                    }
                });

        if (mRewardedAd != null) {
            Activity activityContext = ADSHOW.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    //  Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    postUserwalletUpdate();
                }
            });
        }
      /*  else {
            Toast.makeText(this, "not loaded", Toast.LENGTH_SHORT).show();
        } */

    }

    public void postUserwalletUpdate() {

        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {

                    try {

                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(ADSHOW.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                            message = "Reward Claimed !!";
                            opendialog();
                          //  Intent i = new Intent(ADSHOW.this, MainActivity.class);
                            //startActivity(i);
                        } else {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();


                    }


                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Call<SuccessModel> calladdbidApi() {
        return videoService.post_addUserBal(savePref.getUserId(), getWallet, "6");
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initColonySdk() {

        // Construct optional app options object to be sent with configure
        // setKeepScreenOn: set a flag on our Activity's window to keep the display from going to sleep.
        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(this, appOptions, APP_ID, REWARD_ZONE_ID);

    }

    private void initRewardedAd() {
        //  rewardAdOptions = new AdColonyAdOptions()
        //          .enableConfirmationDialog(true)
        //    .enableResultsDialog(true);

        //setRewardListener:set the AdColonyRewardListener for global reward callbacks for the app.
        AdColony.setRewardListener(new AdColonyRewardListener() {

            @Override
            public void onReward(AdColonyReward reward) {

                if (reward.success()) {
                    postUserwalletUpdate();
                    Toast.makeText(getApplicationContext(), "Reward Earned", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reward Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about.
        rewardListener = new AdColonyInterstitialListener() {

            // Code to be executed when an ad request is filled
            // get AdColonyInterstitial Reward object from adcolony Ad Server
            @Override
            public void onRequestFilled(AdColonyInterstitial adReward) {
                mProgressDialog.dismiss();
                // Ad passed back in request filled callback, ad can now be shown
                rewardAdColony = adReward;
                isRewardLoaded = true;
                rewardAdColony.show();
            }

            // Code to be executed when an ad request is not filled
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
            }

            //Code to be executed when an ad opens
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }

            //Code to be executed when user closed an ad
            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();

                //request new reward on close
                //     AdColony.requestInterstitial(REWARD_ZONE_ID, rewardListener, rewardAdOptions);
            }

            // Code to be executed when the user clicks on an ad.
            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }

            // called after onAdOpened(), when a user click opens another app
            // (such as the Google Play), backgrounding the current app
            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }

            // Code to be executed when an ad expires.
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };
        // Ad specific options to be sent with request
        rewardAdOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(false)
                .enableResultsDialog(false);
        AdColony.requestInterstitial(REWARD_ZONE_ID, rewardListener, rewardAdOptions);
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    private void opendialog() {
        final Dialog dialog = new Dialog(ADSHOW.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.mpesa_success);
        Window window = dialog.getWindow();
        txtMpesa = (TextView) dialog.findViewById(R.id.txtMpesa);
        txtMpesa.setText(message);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.okaybtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
        {
            //getItems();
            //nointernetlayout.setVisibility(View.GONE);

        }
        else
        {
            // nointernetlayout.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
            Intent intent=new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}




