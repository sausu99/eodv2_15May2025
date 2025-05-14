package com.wowcodes.supreme.Activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.wowcodes.supreme.R;

public class IMPFeedbackActivity extends AppCompatActivity {

    TextView txtTittle;
    ImageView imgBackk;
    TextView txtTxt;
    String stringImpFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        stringImpFeedback = getIntent().getStringExtra("check");
        txtTittle = findViewById(R.id.txtAucname);
        imgBackk = findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTxt = findViewById(R.id.txtTxt);
        if (stringImpFeedback.equalsIgnoreCase("1")) {
            txtTittle.setText(R.string.string32);
            txtTxt.setText(Html.fromHtml(MainActivity.stringPrivacy));
        } else if (stringImpFeedback.equalsIgnoreCase("2")) {
            txtTittle.setText(R.string.string113);
            txtTxt.setText(Html.fromHtml(MainActivity.stringHowTo));
        } else if (stringImpFeedback.equalsIgnoreCase("3")) {
            txtTittle.setText(R.string.string116);
            txtTxt.setText(Html.fromHtml(MainActivity.stringContact));
        } else if (stringImpFeedback.equalsIgnoreCase("4")) {
            txtTittle.setText(R.string.string334);
            txtTxt.setText(Html.fromHtml(MainActivity.stringAbout));
        }
    }
}