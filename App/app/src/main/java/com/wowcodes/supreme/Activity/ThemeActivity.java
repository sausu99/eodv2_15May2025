/**
 * The ThemeActivity class is responsible for allowing the user to select and save their preferred
 * theme (light, dark, or system default) for the application.
 */
package com.wowcodes.supreme.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wowcodes.supreme.SavePref;

import com.wowcodes.supreme.R;

public class ThemeActivity extends AppCompatActivity {
    SavePref savePref;
    TextView title;
    ImageView backimg;
    RadioGroup radioGroup;
    RadioButton light, dark, system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        title = findViewById(R.id.txtTitle);
        backimg = findViewById(R.id.imgBackBtn);
        radioGroup = findViewById(R.id.radioGroup);
        light = findViewById(R.id.lightBtn);
        dark = findViewById(R.id.darkBtn);
        system = findViewById(R.id.settingBtn);
        savePref = new SavePref(ThemeActivity.this);

        // Initialize the current theme based on saved preferences
        initializeTheme();

        // Set onClick listener for the back button
        backimg.setOnClickListener(view -> finish());

        // Light mode selection
        light.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dark.setChecked(false);
                system.setChecked(false);
                savePref.setThemeMode(false);  // Light mode
                savePref.setDefaultThemeMode(false);  // Set system mode to false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                title.setText("Light Mode");
            }
        });

        // Dark mode selection
        dark.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                light.setChecked(false);
                system.setChecked(false);
                savePref.setThemeMode(true);  // Dark mode
                savePref.setDefaultThemeMode(false);  // Set system mode to false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                title.setText("Dark Mode");
            }
        });

        // System default mode selection
        system.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                light.setChecked(false);
                dark.setChecked(false);
                savePref.setDefaultThemeMode(true);  // System default mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                title.setText("System Default Mode");
            }
        });
    }

    private void initializeTheme() {
        boolean isDarkMode = savePref.getThemeMode();
        boolean isDefaultMode = savePref.getDefaultThemeMode();

        if (isDefaultMode) {
            system.setChecked(true);
            title.setText("System Default Mode");
        } else if (isDarkMode) {
            dark.setChecked(true);
            title.setText("Dark Mode");
        } else {
            light.setChecked(true);
            title.setText("Light Mode");
        }
    }
}
