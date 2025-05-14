package com.wowcodes.supreme.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.getBotName;
import com.wowcodes.supreme.PulseCountdown;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class game_cric extends AppCompatActivity {

    ImageView imgBackk,compmove,playermove;
    TextView txtGetCoinTop,underline,submit,cric_min,cric_max,cric_win,cric_chance,txtAucname,result,youare,one,two,three,four,five,six,pover1,pover2,pover3,pover4,pover5,pover6,cover1,cover2,cover3,cover4,cover5,cover6,youscore,compscore,names;
    TextView timeleft;
    EditText noofcoins;
    BindingService videoService;
    LinearLayout gamezone,enternoofcoins,lnlcoin,activity;
    SavePref savePref;
    boolean set=false;
    int coinValue=0;
    int coins=0;
    int compRun=0,currentRun=0;
    int youtotal=0,comptotal=0;
    boolean batsman=true;
    boolean manual=false;
    boolean engaged=false;
    int balls=1;
    String bot_name;
    int interval=6;
    int cricChance=0;
    int previousUserRun=0;


    @SuppressLint({"UseCompatLoadingForDrawables", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.game_cric);

        imgBackk=findViewById(R.id.imgBackk);
        txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(getText(R.string.cricket));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pover1=findViewById(R.id.pover1);
        pover2=findViewById(R.id.pover2);
        pover3=findViewById(R.id.pover3);
        pover4=findViewById(R.id.pover4);
        pover5=findViewById(R.id.pover5);
        pover6=findViewById(R.id.pover6);
        cover1=findViewById(R.id.cover1);
        cover2=findViewById(R.id.cover2);
        cover3=findViewById(R.id.cover3);
        cover4=findViewById(R.id.cover4);
        cover5=findViewById(R.id.cover5);
        cover6=findViewById(R.id.cover6);
        timeleft=findViewById(R.id.timeleft);
        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        four=findViewById(R.id.four);
        five=findViewById(R.id.five);
        six=findViewById(R.id.six);
        youare=findViewById(R.id.youare);
        youscore=findViewById(R.id.youscore);
        compscore=findViewById(R.id.computerscore);
        compmove=findViewById(R.id.compmove);
        playermove=findViewById(R.id.playermove);
        names=findViewById(R.id.compname);
        cric_min=findViewById(R.id.cric_min);
        cric_max=findViewById(R.id.cric_max);
        cric_win=findViewById(R.id.cric_win);
        cric_chance=findViewById(R.id.cric_chance);
        result=findViewById(R.id.result);
        submit=findViewById(R.id.submit);
        noofcoins=findViewById(R.id.noofcoins);
        underline=findViewById(R.id.underline);
        txtGetCoinTop=findViewById(R.id.txtGetCoinTop);
        lnlcoin=findViewById(R.id.lnlCoin);
        enternoofcoins=findViewById(R.id.enternoofcoins);
        gamezone=findViewById(R.id.gamezone);
        activity=findViewById(R.id.activity);
        savePref=new SavePref(game_cric.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getrules();
        getprofile();
        getname();

        ViewGroup.LayoutParams params=gamezone.getLayoutParams();
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        params.height=metrics.heightPixels-200;
        gamezone.setLayoutParams(params);

        underline.setPaintFlags(underline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 1;

                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    one.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    two.setTextColor(getResources().getColor(R.color.white));
                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    three.setTextColor(getResources().getColor(R.color.white));
                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    four.setTextColor(getResources().getColor(R.color.white));
                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    five.setTextColor(getResources().getColor(R.color.white));
                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    six.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 2;

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    two.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    one.setTextColor(getResources().getColor(R.color.white));
                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    three.setTextColor(getResources().getColor(R.color.white));
                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    four.setTextColor(getResources().getColor(R.color.white));
                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    five.setTextColor(getResources().getColor(R.color.white));
                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    six.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 3;

                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    three.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    two.setTextColor(getResources().getColor(R.color.white));
                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    one.setTextColor(getResources().getColor(R.color.white));
                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    four.setTextColor(getResources().getColor(R.color.white));
                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    five.setTextColor(getResources().getColor(R.color.white));
                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    six.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 4;

                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    four.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    two.setTextColor(getResources().getColor(R.color.white));
                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    three.setTextColor(getResources().getColor(R.color.white));
                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    one.setTextColor(getResources().getColor(R.color.white));
                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    five.setTextColor(getResources().getColor(R.color.white));
                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    six.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 5;

                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    five.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    two.setTextColor(getResources().getColor(R.color.white));
                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    three.setTextColor(getResources().getColor(R.color.white));
                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    four.setTextColor(getResources().getColor(R.color.white));
                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    one.setTextColor(getResources().getColor(R.color.white));
                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    six.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!engaged) {
                    engaged = true;
                    currentRun = 6;

                    six.setBackground(getResources().getDrawable(R.drawable.et_dotted_white));
                    six.setTextColor(getResources().getColor(R.color.green_forest_secondary));

                    two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    two.setTextColor(getResources().getColor(R.color.white));
                    three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    three.setTextColor(getResources().getColor(R.color.white));
                    four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    four.setTextColor(getResources().getColor(R.color.white));
                    five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    five.setTextColor(getResources().getColor(R.color.white));
                    one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
                    one.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_cric.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString()) > Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_cric.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString()) < Integer.parseInt(cric_min.getText().toString()))
                    Toast.makeText(game_cric.this, getResources().getString(R.string.minbidallowed) + " " + cric_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString()) > Integer.parseInt(cric_max.getText().toString()))
                    Toast.makeText(game_cric.this, getResources().getString(R.string.maxbidallowed) + " " + cric_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    coins = Integer.parseInt(noofcoins.getText().toString());
                    double bonus = (Double.parseDouble(cric_win.getText().toString()) * 0.01);
                    coinValue = coins + (int) Math.ceil(coins * bonus);
                    set = true;
                    manual = true;
                    lnlcoin.setVisibility(View.GONE);
                    enternoofcoins.setVisibility(View.GONE);
                    gamezone.setVisibility(View.VISIBLE);
                    activity.setBackgroundColor(getResources().getColor(R.color.green_forest_secondary));
                    closeKeyboard();

                    // Initialize and start the PulseCountdown
                    TextView countdownTextView = findViewById(R.id.timeleft); // Assuming you have this TextView in your layout
                    PulseCountdown pulseCountdown = new PulseCountdown(countdownTextView);
                    pulseCountdown.startPulseCountdown(5000); // 5 seconds countdown

                    // Handle countdown completion (You can pass a custom listener in the PulseCountdown class)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            engaged = true;
                            computeScore();
                        }
                    }, 5000); // Delay for the duration of the countdown (5 seconds)
                }
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2=new Dialog(game_cric.this);
                dialog2.setContentView(R.layout.dialogbox2);
                dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(true);
                dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView okay2 = dialog2.findViewById(R.id.okay);
                TextView textview2 = dialog2.findViewById(R.id.textview);

                textview2.setText(getText(R.string.gamerules)+"\n\n"+getText(R.string.minbet)+" "+cric_min.getText().toString()+" "+getText(R.string.string118)+"\n"+getText(R.string.maxbet)+" "+cric_max.getText().toString()+" "+getText(R.string.string118)+"\n"+getText(R.string.winbonus)+" "+cric_win.getText().toString()+"%"+"\n\n"+getText(R.string.steps)+"\n\n"+getText(R.string.step1)+"\n\n"+getText(R.string.step2)+"\n\n"+getText(R.string.cricstep3)+"\n\n"+getText(R.string.cricstep4)+"\n\n"+getText(R.string.cricstep5)+"\n\n"+getText(R.string.cricstep6)+"\n\n"+getText(R.string.cricstep7)+"\n\n"+getText(R.string.bestofluck));
                okay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
            }
        });
    }

    public void updatemoney(int coins){
        try {
            videoService.post_addUserBal(savePref.getUserId(),String.valueOf(coins),"7").enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {}
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    @Override
    public void onBackPressed() {
        if(manual){
            Dialog dialog2=new Dialog(game_cric.this);
            dialog2.setContentView(R.layout.dialogbox2);
            dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
            dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog2.setCancelable(true);
            dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
            TextView okay2 = dialog2.findViewById(R.id.okay);
            TextView textview2 = dialog2.findViewById(R.id.textview);

            textview2.setText(getResources().getString(R.string.quit));
            okay2.setText(getResources().getString(R.string.quitgame));
            okay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    updatemoney(-coins);
                    int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                    txtGetCoinTop.setText(String.valueOf(curcoins-coins));
                    manual=false;
                    onBackPressed();
                }
            });

            dialog2.show();
        }
        else
            finish();
    }

    public void getprofile() {
        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtGetCoinTop.setText(arrayList.get(0).getWallet());
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void getrules() {
        try {
            videoService.get_games().enqueue(new Callback<GetGames>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetGames> call, Response<GetGames> response) {
                    try {
                        ArrayList<GetGames.Get_games_Inner> arrayList = response.body().getJSON_DATA();

                        cric_min.setText(arrayList.get(0).getCric_min());
                        cric_max.setText(arrayList.get(0).getCric_max());
                        cric_win.setText(arrayList.get(0).getCric_win());
                        cric_chance.setText(arrayList.get(0).getCric_chance());
                        cricChance=Integer.parseInt(arrayList.get(0).getCric_chance());

                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<GetGames> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    public void getname() {
        try {
            videoService.get_bot_name().enqueue(new Callback<getBotName>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<getBotName> call, Response<getBotName> response) {
                    try {
                        ArrayList<getBotName.Get_names_Inner> arrayList = response.body().getJSON_DATA();
                        bot_name = arrayList.get(0).getBot_name();
                        compscore.setText(getResources().getString(R.string.computertotal).replace("Opponent",bot_name));
                        names.setText(bot_name);
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<getBotName> call, Throwable t) {}
            });
        } catch (Exception e) {}
    }



    public void computeScore(){
        Random random = new Random();

        int maxRun = 6; // Maximum run per ball
        int minRun = 1; // Minimum run per ball

        if (cricChance == 100) {
            // User should always win
            compRun = random.nextInt(Math.max(currentRun, maxRun)) + 1; // Ensure computer’s run is less than the user’s run

        } else if (cricChance == 0) {
            // Computer should always win or ensure user does not win
            if (comptotal >= youtotal) {
                compRun = random.nextInt(maxRun) + 1; // Random run within valid range
            } else {
                // Adjust computer's run to ensure victory
                if (balls < 6) {
                    compRun = Math.min(maxRun, currentRun + random.nextInt(2) + 1); // Slightly higher to ensure victory
                } else {
                    // On the last ball, ensure compRun is enough to win if possible
                    compRun = Math.max(minRun, youtotal - comptotal + currentRun);
                    compRun = Math.min(maxRun, compRun); // Cap at maxRun
                }
            }

            // Adjust to avoid out situation if needed
            if (compRun == currentRun) {
                if (comptotal > youtotal || (balls == 6 && comptotal == youtotal)) {
                    compRun = currentRun; // Same run is acceptable if computer is already winning
                } else {
                    compRun = currentRun + 1; // Adjust run to avoid equal runs
                    if (compRun > maxRun) {
                        compRun = maxRun;
                    }
                }
            }

        } else {
            // Random chance scenario based on cricChance
            int randomFactor = random.nextInt(100);

            if (randomFactor < cricChance) {
                // Computer has a higher chance to win
                compRun = random.nextInt(maxRun) + 1;
                if (compRun <= currentRun) {
                    compRun = Math.min(maxRun, currentRun + random.nextInt(2) + 1); // Favor computer
                }
            } else {
                // User has a higher chance to win
                compRun = random.nextInt(maxRun) + 1;
                if (compRun >= currentRun) {
                    compRun = Math.max(minRun, currentRun - random.nextInt(2) - 1); // Favor user
                }
            }

            compRun = Math.max(minRun, Math.min(compRun, maxRun)); // Ensure compRun is within valid bounds
        }

        // Update the total scores
        if (currentRun != compRun) {
            if (batsman) {
                youtotal += currentRun;
            } else {
                comptotal += compRun;
            }
        }

        // Store the user's previous run
        previousUserRun = currentRun;
        if(batsman){
            if(balls==1) {
                pover1.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover1.setText("w");
                else
                    pover1.setText(String.valueOf(currentRun));
            }
            if(balls==2) {
                pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover2.setText("w");
                else
                    pover2.setText(String.valueOf(currentRun));
            }
            if(balls==3) {
                pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover3.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover3.setText("w");
                else
                    pover3.setText(String.valueOf(currentRun));
            }
            if(balls==4) {
                pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover4.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover4.setText("w");
                else
                    pover4.setText(String.valueOf(currentRun));
            }
            if(balls==5) {
                pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover5.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover5.setText("w");
                else
                    pover5.setText(String.valueOf(currentRun));
            }
            if(balls==6) {
                pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover5.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover6.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    pover6.setText("w");
                else
                    pover6.setText(String.valueOf(currentRun));
            }
        }







        else {
            if(balls==1) {
                cover1.setBackground(getResources().getDrawable(R.drawable.desm1));

                /*pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover5.setBackground(getResources().getDrawable(R.drawable.hitball));
                pover6.setBackground(getResources().getDrawable(R.drawable.hitball));*/


                if(currentRun==compRun)
                    cover1.setText("w");
                else
                    cover1.setText(String.valueOf(compRun));
            }

            if(balls==2) {
                cover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover2.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    cover2.setText("w");
                else
                    cover2.setText(String.valueOf(compRun));
            }
            if(balls==3) {
                cover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover3.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    cover3.setText("w");
                else
                    cover3.setText(String.valueOf(compRun));
            }
            if(balls==4) {
                cover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover4.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    cover4.setText("w");
                else
                    cover4.setText(String.valueOf(compRun));
            }
            if(balls==5) {
                cover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover5.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    cover5.setText("w");
                else
                    cover5.setText(String.valueOf(compRun));
            }
            if(balls==6) {
                cover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover5.setBackground(getResources().getDrawable(R.drawable.hitball));
                cover6.setBackground(getResources().getDrawable(R.drawable.desm1));


                if(currentRun==compRun)
                    cover6.setText("w");
                else
                    cover6.setText(String.valueOf(compRun));
            }
        }




        if(currentRun==0)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
        if(currentRun==1)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p1));
        if(currentRun==2)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p2));
        if(currentRun==3)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p3));
        if(currentRun==4)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p4));
        if(currentRun==5)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p5));
        if(currentRun==6)
            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p6));



        if(compRun==1)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c1));
        if(compRun==2)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c2));
        if(compRun==3)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c3));
        if(compRun==4)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c4));
        if(compRun==5)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c5));
        if(compRun==6)
            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c6));



        if(compRun==currentRun  &&  batsman) {
            Dialog dialog = new Dialog(game_cric.this);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
            dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            TextView okay = dialog.findViewById(R.id.okay);
            TextView textview = dialog.findViewById(R.id.textview);

            textview.setText(getResources().getString(R.string.youareout).replace("6",String.valueOf(youtotal)).replace("7",String.valueOf(++youtotal)));
// Assuming timeWasteTextView and timeLeftTextView are your TextView elements
            TextView timeWasteTextView = findViewById(R.id.timewaste);
            TextView timeLeftTextView = findViewById(R.id.timeleft);

// Initialize the PulseCountdown
            PulseCountdown timeWasteCountdown = new PulseCountdown(timeWasteTextView);
            PulseCountdown timeLeftCountdown = new PulseCountdown(timeLeftTextView);

            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    engaged = false;
                    timeLeftTextView.setText("5");

                    playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
                    compmove.setImageDrawable(getResources().getDrawable(R.drawable.c0));
                    youare.setText(getResources().getString(R.string.youareballer));
                    youscore.setText(getResources().getString(R.string.yourtotal).replace("0", String.valueOf(youtotal - 1)));

                    // Update the ball visuals as needed
                    if (balls == 1) {
                        pover1.setText("w");
                        pover1.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }
                    if (balls == 2) {
                        pover2.setText("w");
                        pover2.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }
                    if (balls == 3) {
                        pover3.setText("w");
                        pover3.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }
                    if (balls == 4) {
                        pover4.setText("w");
                        pover4.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }
                    if (balls == 5) {
                        pover5.setText("w");
                        pover5.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }
                    if (balls == 6) {
                        pover6.setText("w");
                        pover6.setBackground(getResources().getDrawable(R.drawable.hitball));
                    }

                    batsman = false;
                    compRun = 0;
                    currentRun = 0;
                    balls = 1;

                    // Start the timeWaste countdown
                    timeWasteCountdown.startPulseCountdown(3000); // 3 seconds for "wasting time"

                    // Set a listener for timeWaste countdown completion
                    timeWasteCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                        @Override
                        public void onCompleted() {
                            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
                            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c0));

                            // Start the timeLeft countdown after timeWaste is done
                            timeLeftCountdown.startPulseCountdown(5000); // 5 seconds countdown

                            // Set listener for timeLeft countdown completion
                            timeLeftCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                                @Override
                                public void onCompleted() {
                                    engaged = true;
                                    computeScore();
                                }
                            });
                        }
                    });
                }
            });
            dialog.show();
        }


// Assuming timeWasteTextView and timeLeftTextView are your TextView elements
        TextView timeWasteTextView = findViewById(R.id.timewaste);
        TextView timeLeftTextView = findViewById(R.id.timeleft);

// Initialize PulseCountdown for both timers
        PulseCountdown timeWasteCountdown = new PulseCountdown(timeWasteTextView);
        PulseCountdown timeLeftCountdown = new PulseCountdown(timeLeftTextView);

        if(compRun != currentRun && batsman) {
            Dialog dialog = new Dialog(game_cric.this);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
            dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            TextView okay = dialog.findViewById(R.id.okay);
            TextView textview = dialog.findViewById(R.id.textview);

            // Update the respective ball
            if(balls == 1) pover1.setText(String.valueOf(currentRun));
            if(balls == 2) pover2.setText(String.valueOf(currentRun));
            if(balls == 3) pover3.setText(String.valueOf(currentRun));
            if(balls == 4) pover4.setText(String.valueOf(currentRun));
            if(balls == 5) pover5.setText(String.valueOf(currentRun));

            // Update the dialog text based on the current run
            if(currentRun == 0)
                textview.setText(getResources().getString(R.string.dotball).replace("6", String.valueOf(youtotal)).replace("7", String.valueOf(youtotal + 1)));
            else
                textview.setText(getResources().getString(R.string.ballsover).replace("5", String.valueOf(currentRun)).replace("6", String.valueOf(youtotal)).replace("7", String.valueOf(youtotal + 1)));

            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    engaged = false;
                    timeLeftTextView.setText("5");

                    if (balls == 6) {
                        playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
                        compmove.setImageDrawable(getResources().getDrawable(R.drawable.c0));
                        youare.setText(getResources().getString(R.string.youareballer));
                        //pover6.setText(String.valueOf(currentRun));
                        batsman = false;
                        balls = 1;
                        youscore.setText(getResources().getString(R.string.yourtotal).replace("0", String.valueOf(youtotal++)));
                    } else {
                        balls++;
                        youscore.setText(getResources().getString(R.string.yourtotal).replace("0", String.valueOf(youtotal)));
                    }
                    compRun = 0;
                    currentRun = 0;

                    // Start the timeWaste countdown
                    timeWasteCountdown.startPulseCountdown(3000); // 3 seconds for "timewaste"

                    // Set a listener for timeWaste countdown completion
                    timeWasteCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                        @Override
                        public void onCompleted() {
                            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
                            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c0));

                            // Start the timeLeft countdown after timeWaste is done
                            timeLeftCountdown.startPulseCountdown(5000); // 5 seconds countdown

                            // Set listener for timeLeft countdown completion
                            timeLeftCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                                @Override
                                public void onCompleted() {
                                    engaged = true;
                                    computeScore();
                                }
                            });
                        }
                    });
                }
            });

            // Show dialog on the 6th ball
            if(balls == 6) {
                dialog.show();
            } else {
                okay.performClick();
            }
        }


        if(compRun==currentRun  && !batsman) {
            Dialog dialog = new Dialog(game_cric.this);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
            dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            TextView okay = dialog.findViewById(R.id.okay);
            TextView textview = dialog.findViewById(R.id.textview);

            textview.setText(getResources().getString(R.string.cleanbold).replace("9",String.valueOf(coinValue)));
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    engaged=false;

                    if(balls==1)
                        cover1.setText("w");
                    if(balls==2)
                        cover2.setText("w");
                    if(balls==3)
                        cover3.setText("w");
                    if(balls==4)
                        cover4.setText("w");
                    if(balls==5)
                        cover5.setText("w");
                    if(balls==6)
                        cover6.setText("w");

                    updatemoney(coinValue);
                    int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());
                    txtGetCoinTop.setText(String.valueOf(curcoins + coinValue));
                    manual=false;
                    onBackPressed();
                }
            });
            dialog.show();
        }


        if(compRun!=currentRun  &&  !batsman) {
            Dialog dialog = new Dialog(game_cric.this);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
            dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            TextView okay = dialog.findViewById(R.id.okay);
            TextView textview = dialog.findViewById(R.id.textview);


            if(balls<6) {
                if(comptotal>=youtotal)
                    textview.setText(getResources().getString(R.string.computerscoredand).replace("5",String.valueOf(compRun)).replace("6",String.valueOf(comptotal)).replace("Opponent",bot_name)+getResources().getString(R.string.computerwon).replace("9",String.valueOf(coins)));
                else
                    textview.setText(getResources().getString(R.string.computerscored).replace("5",String.valueOf(compRun)).replace("6",String.valueOf(comptotal)).replace("Opponent",bot_name));
            }
            else {
                if(comptotal<youtotal)
                    textview.setText(getResources().getString(R.string.computerscoredbut).replace("6",String.valueOf(youtotal-comptotal)).replace("9",String.valueOf(coinValue)).replace("Opponent",bot_name));
                else if (comptotal == youtotal-1)
                    textview.setText(getResources().getString(R.string.itsadraw));
                else
                    textview.setText(getResources().getString(R.string.computerscoredand).replace("5",String.valueOf(compRun)).replace("6",String.valueOf(comptotal)).replace("Opponent",bot_name)+getResources().getString(R.string.computerwon).replace("9",String.valueOf(coins)));
            }

// Assuming timeWasteTextView and timeLeftTextView are your TextView elements

// Initialize PulseCountdown for both timers

            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    engaged = false;
                    timeLeftTextView.setText("5");

                    if (balls == 6 || comptotal >= youtotal) {
                        updatemoney(-coins);
                        int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());
                        txtGetCoinTop.setText(String.valueOf(curcoins - coins));

                        manual = false;
                        onBackPressed(); // Navigate back
                    } else {
                        balls++;
                    }

                    if (balls == 6 && comptotal < youtotal) {
                        updatemoney(coinValue);
                        int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());
                        txtGetCoinTop.setText(String.valueOf(curcoins + coinValue));
                    }

                    compscore.setText(getResources().getString(R.string.computertotal)
                            .replace("0", String.valueOf(comptotal))
                            .replace("Opponent", bot_name));

                    compRun = 0;
                    currentRun = 0;

                    // Start the timeWaste countdown
                    timeWasteCountdown.startPulseCountdown(3000); // 3 seconds for "timewaste"

                    // Set a listener for timeWaste countdown completion
                    timeWasteCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                        @Override
                        public void onCompleted() {
                            playermove.setImageDrawable(getResources().getDrawable(R.drawable.p0));
                            compmove.setImageDrawable(getResources().getDrawable(R.drawable.c0));

                            // Start the timeLeft countdown after timeWaste is done
                            timeLeftCountdown.startPulseCountdown(5000); // 5 seconds countdown

                            // Set listener for timeLeft countdown completion
                            timeLeftCountdown.setOnCountdownCompletedListener(new PulseCountdown.OnCountdownCompletedListener() {
                                @Override
                                public void onCompleted() {
                                    engaged = true;
                                    computeScore(); // Perform score computation
                                }
                            });
                        }
                    });
                }
            });
            if(balls==6 || comptotal>=youtotal) {
                dialog.show();
            }
            else
                okay.performClick();
        }


        currentRun=0;
        one.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        one.setTextColor(getResources().getColor(R.color.white));
        two.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        two.setTextColor(getResources().getColor(R.color.white));
        three.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        three.setTextColor(getResources().getColor(R.color.white));
        four.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        four.setTextColor(getResources().getColor(R.color.white));
        five.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        five.setTextColor(getResources().getColor(R.color.white));
        six.setBackground(getResources().getDrawable(R.drawable.et_dotted));
        six.setTextColor(getResources().getColor(R.color.white));
    }

    public void closeKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
    }
}