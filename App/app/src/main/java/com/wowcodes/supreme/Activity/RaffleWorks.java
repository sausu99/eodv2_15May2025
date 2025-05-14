/**
 * The RaffleWorks class is an activity in an Android app that displays information about how a raffle
 * works and includes a back button to navigate back to the previous screen.
 */
package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wowcodes.supreme.R;

public class RaffleWorks extends AppCompatActivity {

    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raffle_works);

        imgBack = findViewById(R.id.imgBackBtn);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}