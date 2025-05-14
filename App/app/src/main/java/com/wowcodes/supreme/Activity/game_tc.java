package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.getBotName;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game_tc extends AppCompatActivity {

    ImageView user_move_img,computer_move_img,headsBtn,tailsBtn,imgBackk;
    TextView winner_tv,txtGetCoinTop,underline,submit,tc_min,tc_max,tc_win,tc_chance,compname;
    EditText noofcoins;
    BindingService videoService;
    SavePref savePref;
    int set=0;
    int coinValue=0;
    int coins=0;
    String bot_name;

    @SuppressLint({"UseCompatLoadingForDrawables", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.game_tc);

        imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(getResources().getString(R.string.coin));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tc_min=findViewById(R.id.tc_min);
        tc_max=findViewById(R.id.tc_max);
        tc_win=findViewById(R.id.tc_win);
        tc_chance=findViewById(R.id.tc_chance);
        user_move_img=findViewById(R.id.user_move_img);
        computer_move_img=findViewById(R.id.computer_move_img);
        tailsBtn=findViewById(R.id.tails_btn);
        headsBtn=findViewById(R.id.heads_btn);
        winner_tv=findViewById(R.id.winner_tv);
        submit=findViewById(R.id.submit);
        compname=findViewById(R.id.compname);
        noofcoins=findViewById(R.id.noofcoins);
        underline=findViewById(R.id.underline);
        txtGetCoinTop=findViewById(R.id.txtGetCoinTop);
        savePref=new SavePref(game_tc.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getrules();
        getprofile();
        getname();

        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.heads), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.tails), 400);
        animation.setOneShot(false);
        computer_move_img.setBackgroundDrawable(animation);
        animation.start();


        underline.setPaintFlags(underline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_tc.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(tc_min.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.minbidallowed)+" "+tc_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(tc_max.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.maxbidallowed)+" "+tc_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else if (set==0)
                    Toast.makeText(game_tc.this, getResources().getString(R.string.selectmovefirst), Toast.LENGTH_SHORT).show();
                else {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    coins=Integer.parseInt(noofcoins.getText().toString());
                    double bonus=(Double.parseDouble(tc_win.getText().toString())*0.01);
                    coinValue = coins + (int) Math.ceil(coins * bonus);

                    int computer_move = new Random().nextInt(2) + 1;

                    if (set==1){
                        if(Integer.parseInt(tc_chance.getText().toString()) == 0)
                            computer_move=2;
                        if (computer_move == 1) {
                            computer_move_img.setImageDrawable(getResources().getDrawable(R.drawable.heads));
                            winner_tv.setText("CONGRATULATIONS !!\nYOU HAVE WON !!\n\n" + coinValue + " COINS HAVE BEEN ADDED TO YOUR ACCOUNT.");
                        } else if (computer_move == 2) {
                            computer_move_img.setImageDrawable(getResources().getDrawable(R.drawable.tails));
                            winner_tv.setText("OOPS!.\nYOU LOST!..\n\n" + noofcoins.getText().toString() + " COINS HAVE BEEN DEDUCTED FROM YOUR ACCOUNT..");
                        }

                        if(winner_tv.getText().toString().contains("CONGRATULATIONS !!")) {
                            updatemoney(coinValue);
                            int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins+coinValue));
                        }
                        else if(winner_tv.getText().toString().contains("OOPS!.")) {
                            updatemoney(-coins);
                            int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins-coins));
                        }
                    }

                    else if(set==2) {
                        if(Integer.parseInt(tc_chance.getText().toString()) == 0)
                            computer_move=1;
                        if (computer_move == 1) {
                            computer_move_img.setImageDrawable(getResources().getDrawable(R.drawable.heads));
                            winner_tv.setText("OOPS!.\nYOU LOST!..\n\n" + noofcoins.getText().toString() + " COINS HAVE BEEN DEDUCTED FROM YOUR ACCOUNT..");
                        } else if (computer_move == 2) {
                            computer_move_img.setImageDrawable(getResources().getDrawable(R.drawable.tails));
                            winner_tv.setText("CONGRATULATIONS !!\nYOU HAVE WON !!\n\n" + coinValue + " COINS HAVE BEEN ADDED TO YOUR ACCOUNT.");
                        }

                        if(winner_tv.getText().toString().contains("CONGRATULATIONS !!")) {
                            updatemoney(coinValue);
                            int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins+coinValue));
                        }
                        else if(winner_tv.getText().toString().contains("OOPS!.")) {
                            updatemoney(-coins);
                            int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins-coins));
                        }
                    }

                    Dialog dialog=new Dialog(game_tc.this);
                    dialog.setContentView(R.layout.dialogbox);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
                    dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    TextView okay = dialog.findViewById(R.id.okay);
                    TextView textview = dialog.findViewById(R.id.textview);

                    textview.setText(winner_tv.getText().toString());
                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            set=0;
                            noofcoins.setText("");
                            headsBtn.setImageDrawable(getResources().getDrawable(R.drawable.heads));
                            tailsBtn.setImageDrawable(getResources().getDrawable(R.drawable.tails));
                            reload();

                        }
                    });
                    dialog.show();
                }
            }
        });

        headsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_tc.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(tc_min.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.minbidallowed)+" "+tc_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(tc_max.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.maxbidallowed)+" "+tc_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    set = 1;
                    user_move_img.setImageDrawable(getResources().getDrawable(R.drawable.heads));
                    headsBtn.setImageDrawable(getResources().getDrawable(R.drawable.headssel));
                    tailsBtn.setImageDrawable(getResources().getDrawable(R.drawable.tails));
                }
            }
        });

        tailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_tc.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(tc_min.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.minbidallowed)+" "+tc_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(tc_max.getText().toString()))
                    Toast.makeText(game_tc.this, getResources().getString(R.string.maxbidallowed)+" "+tc_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    set = 2;
                    user_move_img.setImageDrawable(getResources().getDrawable(R.drawable.tails));
                    headsBtn.setImageDrawable(getResources().getDrawable(R.drawable.heads));
                    tailsBtn.setImageDrawable(getResources().getDrawable(R.drawable.tailssel));
                }
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2=new Dialog(game_tc.this);
                dialog2.setContentView(R.layout.dialogbox2);
                dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(true);
                dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView okay2 = dialog2.findViewById(R.id.okay);
                TextView textview2 = dialog2.findViewById(R.id.textview);

                textview2.setText(getText(R.string.gamerules)+"\n\n"+getText(R.string.minbet)+tc_min.getText().toString()+"\n"+getText(R.string.maxbet)+tc_max.getText().toString()+"\n"+getText(R.string.winbonus)+tc_win.getText().toString()+"\n"+getText(R.string.winchance)+tc_chance.getText().toString()+"\n\n"+getText(R.string.steps)+"\n\n"+getText(R.string.step1)+"\n\n"+getText(R.string.tcstep2)+"\n\n"+getText(R.string.step2)+"\n\n"+getText(R.string.tcstep4)+"\n\n"+getText(R.string.bestofluck));
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
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    //nothing to do
                }
                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    //nothing to do
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getprofile() {
        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtGetCoinTop.setText(arrayList.get(0).getWallet());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    //Toast.makeText(game_tc.this, "ERROR GETTING COINS", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getrules() {
        try {
            videoService.get_games().enqueue(new Callback<GetGames>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetGames> call, retrofit2.Response<GetGames> response) {
                    try {
                        ArrayList<GetGames.Get_games_Inner> arrayList = response.body().getJSON_DATA();

                        tc_min.setText(arrayList.get(0).getCt_min());
                        tc_max.setText(arrayList.get(0).getCt_max());
                        tc_win.setText(arrayList.get(0).getCt_win());
                        tc_chance.setText(arrayList.get(0).getCt_chance());


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetGames> call, Throwable t) {
                    //Toast.makeText(game_tc.this, "ERROR GETTING RULES", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void reload(){
        Intent i = new Intent(this, game_tc.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void getname() {
        try {
            videoService.get_bot_name().enqueue(new Callback<getBotName>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<getBotName> call, retrofit2.Response<getBotName> response) {
                    try {
                        ArrayList<getBotName.Get_names_Inner> arrayList = response.body().getJSON_DATA();

                        bot_name = arrayList.get(0).getBot_name();
                        compname.setText(bot_name);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<getBotName> call, Throwable t) {
                    //Toast.makeText(game_cric.this, "ERROR GETTING RULES", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}