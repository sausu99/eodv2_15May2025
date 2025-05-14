package com.wowcodes.supreme.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.R;

public class changeCityAnimationActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city_animation);
        text=findViewById(R.id.text);

        new CountDownTimer(4000,3000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                Toast.makeText(changeCityAnimationActivity.this, getText(R.string.welcome)+getIntent().getStringExtra("name")+" !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(changeCityAnimationActivity.this,MainActivity.class));
                finish();
            }
        }.start();
    }
}