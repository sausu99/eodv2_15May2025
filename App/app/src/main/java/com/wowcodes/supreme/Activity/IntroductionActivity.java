/**
 * The IntroductionActivity class is an Android activity that displays a series of onboarding slides to
 * introduce the user to the app and allows them to skip or continue to the next slide.
 */
package com.wowcodes.supreme.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.Locale;
import java.util.Objects;

public class IntroductionActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private static final String MyPREFERENCES = "DoctorPrefrance";
    LinearLayout layoutCraeteAcc,layoutSignIn;
    SavePref savePref;
    TextView skipbtn;
    ImageView p1,p2,p3,p4,p5,front,back,language;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startsplash);
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_intoduction);
            initializeIntroductionComponents();
        }, 4000);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        savePref = new SavePref(IntroductionActivity.this);


        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());





    }


    private void initializeIntroductionComponents(){

        mViewPager = findViewById(R.id.viewpager);
        layoutCraeteAcc=findViewById(R.id.layoutCreateAcc);
        skipbtn=findViewById(R.id.skipbtn);
        layoutSignIn=findViewById(R.id.layoutSignIn);
        language=findViewById(R.id.language);
        lazyadapter la = new lazyadapter();
        mViewPager.setAdapter(la);

        p1=findViewById(R.id.p1);
        p2=findViewById(R.id.p2);
        p3=findViewById(R.id.p3);
        p4=findViewById(R.id.p4);
        p5=findViewById(R.id.p5);
        front=findViewById(R.id.front);
        back=findViewById(R.id.back);


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(position==0){
                    p1.setImageResource(R.drawable.img_selected);
                    p2.setImageResource(R.drawable.img_notselected);
                    p3.setImageResource(R.drawable.img_notselected);
                    p4.setImageResource(R.drawable.img_notselected);
                    p5.setImageResource(R.drawable.img_notselected);
                    back.setVisibility(View.INVISIBLE);
                    front.setVisibility(View.VISIBLE);
                }
                else if(position==1){
                    p1.setImageResource(R.drawable.img_notselected);
                    p2.setImageResource(R.drawable.img_selected);
                    p3.setImageResource(R.drawable.img_notselected);
                    p4.setImageResource(R.drawable.img_notselected);
                    p5.setImageResource(R.drawable.img_notselected);
                    back.setVisibility(View.VISIBLE);
                    front.setVisibility(View.VISIBLE);
                }
                else if(position==2){
                    p1.setImageResource(R.drawable.img_notselected);
                    p2.setImageResource(R.drawable.img_notselected);
                    p3.setImageResource(R.drawable.img_selected);
                    p4.setImageResource(R.drawable.img_notselected);
                    p5.setImageResource(R.drawable.img_notselected);
                    back.setVisibility(View.VISIBLE);
                    front.setVisibility(View.VISIBLE);
                }
                else if(position==3){
                    p1.setImageResource(R.drawable.img_notselected);
                    p2.setImageResource(R.drawable.img_notselected);
                    p3.setImageResource(R.drawable.img_notselected);
                    p4.setImageResource(R.drawable.img_selected);
                    p5.setImageResource(R.drawable.img_notselected);
                    back.setVisibility(View.VISIBLE);
                    front.setVisibility(View.VISIBLE);
                }
                else{
                    p1.setImageResource(R.drawable.img_notselected);
                    p2.setImageResource(R.drawable.img_notselected);
                    p3.setImageResource(R.drawable.img_notselected);
                    p4.setImageResource(R.drawable.img_notselected);
                    p5.setImageResource(R.drawable.img_selected);
                    back.setVisibility(View.VISIBLE);
                    front.setVisibility(View.INVISIBLE);
                }
            }
        });


        boolean user = true;
        SharedPreferences.Editor sp = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
        sp.putBoolean("userfirsttime", user);
        sp.apply();

        if (ContextCompat.checkSelfPermission(IntroductionActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(IntroductionActivity.this, new String[] {Manifest.permission.POST_NOTIFICATIONS,NOTIFICATION_SERVICE}, 52);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(IntroductionActivity.this, chooseLanguageActivity.class);
                i.putExtra("from","introduction");
                startActivity(i);
            }
        });

        layoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IntroductionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        layoutCraeteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IntroductionActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IntroductionActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(savePref.getLang() == null)
            savePref.setLang("en");

        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(savePref.getLang() == null){
            savePref.setLang("en");
        }
        if(Objects.equals(savePref.getLang(), "en")){
            setLocale("");
        }
        else {
            setLocale(savePref.getLang());
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(savePref.getLang() == null){
            savePref.setLang("en");
        }
        if(Objects.equals(savePref.getLang(), "en")){
            setLocale("");
        }
        else {
            setLocale(savePref.getLang());
        }
    }
    class lazyadapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            TextView title1;
            TextView title2;
            ImageView sliderimage;

            LayoutInflater inflater = LayoutInflater.from(IntroductionActivity.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.activity_onboarding, container, false);

            title1 = layout.findViewById(R.id.txt_tittle1);
            title2 = layout.findViewById(R.id.txt_tittle2);
            sliderimage = layout.findViewById(R.id.img);

            switch (position) {
                case 0:
                    title1.setText(R.string.slidetitle1);
                    title2.setText(R.string.slidetitle3);
                    sliderimage.setImageResource(R.drawable.home_a);
                    break;
                case 1:
                    title1.setText(R.string.secondtittle1);
                    title2.setText(R.string.secondtittle3);
                    sliderimage.setImageResource(R.drawable.home_b);
                    break;
                case 2:
                    title1.setText(R.string.lotto);
                    title2.setText(R.string.thirdtittle3);
                    sliderimage.setImageResource(R.drawable.home_c);
                    break;
                case 3:
                    title1.setText(R.string.marketplace);
                    title2.setText(R.string.fourthtittle3);
                    sliderimage.setImageResource(R.drawable.home_d);
                    break;
                case 4:
                    title1.setText(R.string.fifthtittle1);
                    title2.setText(R.string.fifthtittle3);
                    sliderimage.setImageResource(R.drawable.home_e);
                    break;
            }

            container.addView(layout);
            return layout;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }
}