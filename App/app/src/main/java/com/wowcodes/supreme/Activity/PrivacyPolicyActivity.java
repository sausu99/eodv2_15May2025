/**
 * The PrivacyPolicyActivity class is an Android activity that displays a privacy policy text.
 */
package com.wowcodes.supreme.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.R;
public class PrivacyPolicyActivity extends AppCompatActivity {
    TextView txtTittle,txtTxt;
    ImageView imgBackk;
    String getPrivcyPolicy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_feedback);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getPrivcyPolicy =getIntent().getStringExtra("check");
        txtTittle = findViewById(R.id.txtAucname);
        imgBackk = findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTxt = findViewById(R.id.txtTxt);
        txtTittle.setText(R.string.string32);
        txtTxt.setText(Html.fromHtml(getPrivcyPolicy));
    }
}