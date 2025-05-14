package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.wowcodes.supreme.R;

public class changeLangAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_lang_animation);

        new CountDownTimer(4000,3000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                Toast.makeText(changeLangAnimationActivity.this, getText(R.string.languageset)+" "+getIntent().getStringExtra("selected"), Toast.LENGTH_SHORT).show();
                if(getIntent().getStringExtra("from").equalsIgnoreCase("introduction"))
                    startActivity(new Intent(changeLangAnimationActivity.this, IntroductionActivity.class));
                else
                    startActivity(new Intent(changeLangAnimationActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}