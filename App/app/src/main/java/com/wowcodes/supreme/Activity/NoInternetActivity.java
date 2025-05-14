package com.wowcodes.supreme.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

public class NoInternetActivity extends AppCompatActivity {
    BindingService videoService;
    SavePref savePref;
    private static final String MyPREFERENCES = "DoctorPrefrance";
    private SharedPreferences sharedPreferences;
    LinearLayout layoutTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        layoutTryAgain=(LinearLayout)findViewById(R.id.layouttryagain);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        savePref = new SavePref(NoInternetActivity.this);
        layoutTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
isNetworkConnected();
            }
        });
    }

    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            Intent intent=new Intent(NoInternetActivity.this,MainActivity.class);
            intent.putExtra("page","1");
            startActivity(intent);
        } else{
            Intent intent=new Intent(NoInternetActivity.this,NoInternetActivity.class);
            startActivity(intent);
            finish();
        }
        if (cm.getActiveNetworkInfo() != null)
            cm.getActiveNetworkInfo().isConnected();
    }
}