/**
 * The AuctionWorks class is an activity in an Android app that displays information about how an
 * auction works and includes a back button to return to the previous screen.
 */
package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wowcodes.supreme.R;

public class AuctionWorks extends AppCompatActivity {
    ImageView imgback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_works);

        imgback = findViewById(R.id.imgBackBtn);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}