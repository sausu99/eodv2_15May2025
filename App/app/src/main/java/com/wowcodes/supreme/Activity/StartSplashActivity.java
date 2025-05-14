/**
 * The StartSplashActivity class is an Android activity that displays a splash screen when the app
 * starts and handles the logic for checking if a user is logged in or not, along with a percentage
 * progress bar during loading.
 */
package com.wowcodes.supreme.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.ironsource.mediationsdk.IronSource;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetOffersWinner;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartSplashActivity extends AppCompatActivity {

    BindingService videoService;
    SavePref savePref;
    private static final String MyPREFERENCES = "DoctorPrefrance";
    private SharedPreferences sharedPreferences;
    private static final int DELAY_MILLIS = 10000;
    private Handler handler = new Handler();
    private Runnable slowConnectionRunnable;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        applySavedThemeMode();
        setContentView(R.layout.activity_startsplash);
        savePref = new SavePref(StartSplashActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        isNetworkConnected();
        progressBar = findViewById(R.id.progressBar);

        startSlowConnectionTimer();

        if (savePref.getLang() == null)
            savePref.setLang("en");
        if (Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());

        if (getIntent().hasExtra("open_action")) {
            int action = Integer.parseInt(getIntent().getStringExtra("open_action"));
            String fields = getIntent().getStringExtra("fields");
            handleAction(action, fields);
        }

        if (sharedPreferences.getBoolean("userfirsttime", false))
            checkLoginOrNot();
        else {
            Intent i = new Intent(getBaseContext(), IntroductionActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(slowConnectionRunnable);
    }

    private void startSlowConnectionTimer() {
        slowConnectionRunnable = new Runnable() {
            @Override
            public void run() {
                showSlowConnectionSnackbar();
                handler.postDelayed(this, 20000);
            }
        };

        handler.postDelayed(slowConnectionRunnable, DELAY_MILLIS); // Initial delay
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void showSlowConnectionSnackbar() {
        View rootView = findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make(rootView, getString(R.string.slowinternet), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.WHITE);
        TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextColor(Color.BLACK);

        snackbar.show();
    }

    private void handleAction(int action, String fields) {
        if (action == 1)
            startActivity(new Intent(this, KycUpdateActivity.class));
        else if (action == 2)
            startActivity(new Intent(this, ScratchRewardsActivity.class));
        else if (action == 3) {
            if (!fields.isEmpty()) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", fields);
                intent.putExtra("from", "main");
                intent.putExtra("title", fields.substring(fields.indexOf(".") + 1));
                startActivity(intent);
            }
        } else if (action == 4)
            startActivity(new Intent(this, GetOrderActivity.class));
        else if (action == 5)
            startActivity(new Intent(this, ReferralsActivity.class));
    }

    public void checkLoginOrNot() {
        if (savePref.getUserId().equalsIgnoreCase("0")) {
            Intent intent = new Intent(StartSplashActivity.this, IntroductionActivity.class);
            startActivity(intent);
            finish();
        } else {
            getProfile();
        }
    }

    public void getProfile() {
        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        if (arrayList != null && !arrayList.isEmpty()) {
                            UserProfile.User_profile_Inner userProfile = arrayList.get(0);
                            if (userProfile != null && userProfile.getBan() != null && userProfile.getBan().equalsIgnoreCase("0")) {
                                loadLiveData();
                            } else {
                                savePref.setUserPhone("");
                                savePref.setUserId("0");
                                savePref.setemail("");
                                Intent intent = new Intent(StartSplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        showToast("API not working properly. Please try again.");
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    showToast("Failed to retrieve profile. Please check your internet connection.");
                }
            });
        } catch (Exception e) {
            showToast("An unexpected error occurred.");
        }
    }

    public void loadLiveData() {

        videoService.get_categories(savePref.getCityId(), savePref.getUserId()).enqueue(new Callback<GetCategories>() {
            @Override
            public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StaticData.livefragmentList = response.body().getJsonData();
                    loadShopData();
                } else {
                    showToast("API not working properly. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<GetCategories> call, Throwable t) {
                showToast("Failed to load categories. Please check your internet connection.");
            }
        });
    }

    public void loadShopData() {

        videoService.get_shop(savePref.getCityId(), savePref.getUserId()).enqueue(new Callback<GetCategories>() {
            @Override
            public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StaticData.shopfragmentList = response.body().getJsonData();
                    loadWinnerData();
                } else {
                    showToast("API not working properly. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<GetCategories> call, Throwable t) {
                showToast("Failed to load shop data. Please check your internet connection.");
            }
        });
    }

    public void loadWinnerData() {

        videoService.get_offers_winner(savePref.getUserId()).enqueue(new Callback<GetOffersWinner>() {
            @Override
            public void onResponse(Call<GetOffersWinner> call, retrofit2.Response<GetOffersWinner> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StaticData.winnerfragmentList = response.body().getJSON_DATA();
                    loadUserProfile();
                } else {
                    showToast("API not working properly. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<GetOffersWinner> call, Throwable t) {
                showToast("Failed to load winner data. Please check your internet connection.");
            }
        });
    }

    public void loadUserProfile() {

        videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                    StaticData.userProfileList.add(0, arrayList.get(0).getImage());
                    StaticData.userProfileList.add(1, arrayList.get(0).getName());

                    startMainActivity();
                } else {
                    showToast("API not working properly. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                showToast("Failed to load user profile. Please check your internet connection.");
            }
        });
    }


    private void startMainActivity() {
        Intent intent = new Intent(StartSplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        View rootView = findViewById(android.R.id.content);
        message= getString(R.string.failedretrieve);

        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    private void setLocale(String localeName) {
        if (!localeName.isEmpty()) {
            Locale locale = new Locale(localeName);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
    private void applySavedThemeMode() {
        savePref = new SavePref(StartSplashActivity.this);
        boolean isDarkMode = savePref.getThemeMode();
        boolean isDefaultMode = savePref.getDefaultThemeMode();
        // Apply the theme based on the saved preference
        if (isDefaultMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
