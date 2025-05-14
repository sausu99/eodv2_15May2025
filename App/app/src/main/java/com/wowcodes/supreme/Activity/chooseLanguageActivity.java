package com.wowcodes.supreme.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.Locale;
import java.util.Objects;

public class chooseLanguageActivity extends AppCompatActivity {

    TextView skip, txtAucname,submit,arabic,bengali,english,hindi,french,german,tamil,telugu,urdu,italian,spanish,portuguese;
    ImageView imgBackk;
    SavePref savePref;
    String selectedLang;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_choose_language);

        savePref=new SavePref(chooseLanguageActivity.this);
        String[] Languages = {"(عربي)Arabic", "Bengali (বাংলা)", "English", "French (Français)", "German (Deutsch)", "Hindi (हिंदी)", "Italian (Italiano)","Spanish (Español)", "Portuguese (Português)","Tamil (தமிழ்)","Telugu (తెలుగు)", "Urdu (اردو)"};

        skip=findViewById(R.id.skip);
        txtAucname = findViewById(R.id.txtAucname);
        imgBackk = findViewById(R.id.imgBackk);
        submit=findViewById(R.id.submit);

        arabic=findViewById(R.id.arabic);
        bengali=findViewById(R.id.bengali);
        english=findViewById(R.id.english);
        hindi=findViewById(R.id.hindi);
        french=findViewById(R.id.french);
        german=findViewById(R.id.german);
        tamil=findViewById(R.id.tamil);
        telugu=findViewById(R.id.telugu);
        urdu=findViewById(R.id.urdu);
        italian=findViewById(R.id.italian);
        spanish=findViewById(R.id.spanish);
        portuguese=findViewById(R.id.portugese);

        txtAucname.setText(R.string.choose_app_lang);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (Objects.equals(savePref.getLang(), "ar")) {
            setLocale("");
            savePref.setLang("ar");
            selectedLang=Languages[0];
        }
        else if (Objects.equals(savePref.getLang(), "bn")) {
            setLocale("");
            savePref.setLang("bn");
            selectedLang=Languages[1];
        }
        else if (Objects.equals(savePref.getLang(), "en")) {
            setLocale("");
            savePref.setLang("en");
            selectedLang=Languages[2];
        }
        else if (Objects.equals(savePref.getLang(), "fr")) {
            setLocale("fr");
            savePref.setLang("fr");
            selectedLang=Languages[3];
        }
        else if (Objects.equals(savePref.getLang(), "de")) {
            setLocale("de");
            savePref.setLang("de");
            selectedLang=Languages[4];
        }
        else if (Objects.equals(savePref.getLang(), "hi")) {
            setLocale("hi");
            savePref.setLang("hi");
            selectedLang=Languages[5];
        }
        else if (Objects.equals(savePref.getLang(), "it")) {
            setLocale("it");
            savePref.setLang("it");
            selectedLang=Languages[6];
        }
        else if (Objects.equals(savePref.getLang(), "pt")) {
            setLocale("pt");
            savePref.setLang("pt");
            selectedLang=Languages[7];
        }
        else if (Objects.equals(savePref.getLang(), "ta")) {
            setLocale("ta");
            savePref.setLang("ta");
            selectedLang=Languages[8];
        }
        else if (Objects.equals(savePref.getLang(), "te")) {
            setLocale("te");
            savePref.setLang("te");
            selectedLang=Languages[9];
        }

        else if (Objects.equals(savePref.getLang(), "ur")) {
            setLocale("ur");
            savePref.setLang("ur");
            selectedLang=Languages[10];
        }
        else {
            setLocale("en");
            savePref.setLang("en");
            selectedLang=Languages[2];
        }




        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                arabic.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[0])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(Objects.equals(selectedLang, Languages[0]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[0])?"":Languages[0];
            }
        });

        bengali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                bengali.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[1])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(Objects.equals(selectedLang, Languages[1]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[1])?"":Languages[1];
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                english.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[2])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(Objects.equals(selectedLang, Languages[2]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[2])?"":Languages[2];
            }
        });


        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                french.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[3])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(Objects.equals(selectedLang, Languages[3]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[3])?"":Languages[3];
            }
        });


        german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                german.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[4])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(Objects.equals(selectedLang, Languages[4]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[4])?"":Languages[4];
            }
        });



        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                hindi.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[5])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(Objects.equals(selectedLang, Languages[5]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[5])?"":Languages[5];
            }
        });



        italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                italian.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[6])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(Objects.equals(selectedLang, Languages[6]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[6])?"":Languages[6];
            }
        });

        spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                spanish.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[7])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(Objects.equals(selectedLang, Languages[7]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[7])?"":Languages[7];
            }
        });



        portuguese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                portuguese.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[8])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(Objects.equals(selectedLang, Languages[8]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[8])?"":Languages[8];
            }
        });



        tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                tamil.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[9])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(Objects.equals(selectedLang, Languages[9]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[9])?"":Languages[9];
            }
        });



        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                telugu.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[10])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(Objects.equals(selectedLang, Languages[10]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[10])?"":Languages[10];
            }
        });


        urdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arabic.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                arabic.setTextColor(getResources().getColor(R.color.black));
                bengali.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                bengali.setTextColor(getResources().getColor(R.color.black));
                english.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                english.setTextColor(getResources().getColor(R.color.black));
                hindi.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                hindi.setTextColor(getResources().getColor(R.color.black));
                french.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                french.setTextColor(getResources().getColor(R.color.black));
                tamil.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                tamil.setTextColor(getResources().getColor(R.color.black));
                telugu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                telugu.setTextColor(getResources().getColor(R.color.black));
                urdu.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(getResources().getColor(R.color.black));
                german.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                german.setTextColor(getResources().getColor(R.color.black));
                italian.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                italian.setTextColor(getResources().getColor(R.color.black));
                spanish.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                spanish.setTextColor(getResources().getColor(R.color.black));
                portuguese.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                portuguese.setTextColor(getResources().getColor(R.color.black));

                urdu.getBackground().setColorFilter(Objects.equals(selectedLang,Languages[11])?getResources().getColor(R.color.white):getResources().getColor(R.color.lightcolor), PorterDuff.Mode.SRC_ATOP);
                urdu.setTextColor(Objects.equals(selectedLang, Languages[11]) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                selectedLang=Objects.equals(selectedLang,Languages[11])?"":Languages[11];
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedLang!=null && !selectedLang.isEmpty()) {

                    if (selectedLang.contains("Arabic")) {
                        setLocale("");
                        savePref.setLang("ar");
                    }
                    else if (selectedLang.contains("Bengali")) {
                        setLocale("");
                        savePref.setLang("bn");
                    }
                    else if (selectedLang.contains("English")) {
                        setLocale("");
                        savePref.setLang("en");
                    }
                    else if (selectedLang.contains("French")) {
                        setLocale("fr");
                        savePref.setLang("fr");
                    }
                    else if (selectedLang.contains("German")) {
                        setLocale("de");
                        savePref.setLang("de");
                    }
                    else if (selectedLang.contains("Hindi")) {
                        setLocale("hi");
                        savePref.setLang("hi");
                    }
                    else if (selectedLang.contains("Italian")) {
                        setLocale("it");
                        savePref.setLang("it");
                    }
                    else if (selectedLang.contains("Spanish")) {
                        setLocale("es");
                        savePref.setLang("es");
                    }
                    else if (selectedLang.contains("Portuguese")) {
                        setLocale("pt");
                        savePref.setLang("pt");
                    }
                    else if (selectedLang.contains("Tamil")) {
                        setLocale("ta");
                        savePref.setLang("ta");
                    }
                    else if (selectedLang.contains("Telugu")) {
                        setLocale("te");
                        savePref.setLang("te");
                    }
                    else if (selectedLang.contains("Urdu")) {
                        setLocale("ur");
                        savePref.setLang("ur");
                        selectedLang=Languages[11];
                    }
                    else {
                        setLocale("en");
                        savePref.setLang("en");
                    }


                    Intent i = new Intent(chooseLanguageActivity.this, changeLangAnimationActivity.class);
                    i.putExtra("selected", selectedLang);
                    i.putExtra("from", getIntent().getStringExtra("from"));
                    startActivity(i);
                    finish();
                }
                else
                    Toast.makeText(chooseLanguageActivity.this, getString(R.string.nolangsel), Toast.LENGTH_SHORT).show();
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savePref.getLang()!=null && savePref.getLang().isEmpty()) {
                    setLocale("");
                    savePref.setLang("en");
                    selectedLang = Languages[2];
                }

                if(getIntent().getStringExtra("from").equalsIgnoreCase("introduction"))
                    startActivity(new Intent(chooseLanguageActivity.this, IntroductionActivity.class));
                else
                    startActivity(new Intent(chooseLanguageActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }
}