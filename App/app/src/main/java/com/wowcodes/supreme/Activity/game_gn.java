package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game_gn extends AppCompatActivity {

    ImageView imgBackk,hint;
    TextView txtGetCoinTop,underline,submit,gn_min,gn_max,gn_win,gn_chance,txtAucname,result,tries,guess,subguess;
    EditText noofcoins;
    BindingService videoService;
    LinearLayout gamezone;
    SavePref savePref;
    boolean set=false;
    int coinValue=0;
    int coins=0;
    int keyno;
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
        setContentView(R.layout.game_gn);

        imgBackk=findViewById(R.id.imgBackk);
        txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(getText(R.string.guess));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        gn_min=findViewById(R.id.gn_min);
        gn_max=findViewById(R.id.gn_max);
        gn_win=findViewById(R.id.gn_win);
        gn_chance=findViewById(R.id.gn_chance);
        result=findViewById(R.id.result);
        submit=findViewById(R.id.submit);
        noofcoins=findViewById(R.id.noofcoins);
        underline=findViewById(R.id.underline);
        txtGetCoinTop=findViewById(R.id.txtGetCoinTop);
        gamezone=findViewById(R.id.gamezone);
        tries=findViewById(R.id.tries);
        guess=findViewById(R.id.guess);
        hint=findViewById(R.id.hint);
        subguess=findViewById(R.id.subguess);
        savePref=new SavePref(game_gn.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getrules();
        getprofile();

        underline.setPaintFlags(underline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_gn.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_gn.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(gn_min.getText().toString()))
                    Toast.makeText(game_gn.this, getResources().getString(R.string.minbidallowed)+" "+gn_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(gn_max.getText().toString()))
                    Toast.makeText(game_gn.this, getResources().getString(R.string.maxbidallowed)+" "+gn_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    coins=Integer.parseInt(noofcoins.getText().toString());
                    double bonus=(Double.parseDouble(gn_win.getText().toString())*0.01);
                    coinValue = coins + (int) Math.ceil(coins * bonus);

                    set=true;
                    gamezone.setVisibility(View.VISIBLE);
                }
            }
        });

        keyno = new Random().nextInt(100) + 1;

        subguess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!set)
                    Toast.makeText(game_gn.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else {
                    if (guess.getText().toString().isEmpty())
                        Toast.makeText(game_gn.this, getResources().getString(R.string.enterguessfirst), Toast.LENGTH_SHORT).show();
                    else if (Integer.parseInt(guess.getText().toString()) < 1 || Integer.parseInt(guess.getText().toString()) > 100)
                        Toast.makeText(game_gn.this, getResources().getString(R.string.guessrange), Toast.LENGTH_SHORT).show();
                    else {
                        tries.setText(String.valueOf(Integer.parseInt(tries.getText().toString()) - 1));

                        if (Integer.parseInt(guess.getText().toString()) == keyno && Integer.parseInt(gn_chance.getText().toString())!=0) {
                            result.setText(getText(R.string.string197)+"\n\n" + coinValue + getText(R.string.coinsadded));
                        }
                        else if(Integer.parseInt(guess.getText().toString()) == keyno && Integer.parseInt(gn_chance.getText().toString())==0){
                                hint.setImageDrawable(getResources().getDrawable(R.drawable.higher));

                            if (Integer.parseInt(tries.getText().toString()) == 0)
                                result.setText(getText(R.string.lost)+"\n\n" + noofcoins.getText().toString() +getText(R.string.coinsdeducted)+" "+"\n\n"+getText(R.string.numberwas) + keyno+1);
                            else
                                result.setText(getText(R.string.guesswrong)+"\n"+getText(R.string.hint)+"\n\n"+getText(R.string.remainingtries)+" " + tries.getText().toString() );
                        }
                        else {
                            if (Integer.parseInt(guess.getText().toString()) > keyno)
                                hint.setImageDrawable(getResources().getDrawable(R.drawable.lower));
                            else
                                hint.setImageDrawable(getResources().getDrawable(R.drawable.higher));


                            if (Integer.parseInt(tries.getText().toString()) == 0)
                                result.setText(getText(R.string.lost)+"\n\n" + noofcoins.getText().toString() +" "+getText(R.string.coinsdeducted)+"\n\n"+getText(R.string.numberwas)+" "+ keyno );
                            else
                                result.setText(getText(R.string.guesswrong)+"\n\n"+getText(R.string.remainingtries) +" "+ tries.getText().toString() );
                        }


                        if (result.getText().toString().contains(getText(R.string.string197))) {
                            updatemoney(coinValue);
                            int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins + coinValue));
                        } else if (result.getText().toString().contains(getText(R.string.lost))) {
                            updatemoney(-coins);
                            int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());
                            txtGetCoinTop.setText(String.valueOf(curcoins - coins));
                        }


                        Dialog dialog = new Dialog(game_gn.this);
                        dialog.setContentView(R.layout.dialogbox);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
                        dialog.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                        TextView okay = dialog.findViewById(R.id.okay);
                        TextView textview = dialog.findViewById(R.id.textview);

                        textview.setText(result.getText().toString());
                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (!textview.getText().toString().contains(getText(R.string.remainingtries))) {
                                    hint.setImageDrawable(getResources().getDrawable(R.drawable.guessfirst));
                                    set = false;
                                    tries.setText("3");
                                    guess.setText("");
                                    noofcoins.setText("");
                                    keyno = new Random().nextInt(100) + 1;
                                    gamezone.setVisibility(View.GONE);
                                }
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2=new Dialog(game_gn.this);
                dialog2.setContentView(R.layout.dialogbox2);
                dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(true);
                dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView okay2 = dialog2.findViewById(R.id.okay);
                TextView textview2 = dialog2.findViewById(R.id.textview);

                textview2.setText(getText(R.string.gamerules)+"\n\n"+getText(R.string.minbet)+gn_min.getText().toString()+"\n"+getText(R.string.maxbet)+gn_max.getText().toString()+"\n"+getText(R.string.winbonus)+gn_win.getText().toString()+"\n"+getText(R.string.winchance)+gn_chance.getText().toString()+"\n\n"+getText(R.string.steps)+"\n\n"+getText(R.string.step1)+"\n\n"+getText(R.string.step2)+"\n\n"+getText(R.string.gnstep3)+"\n\n"+getText(R.string.gnstep4)+"\n\n"+getText(R.string.bestofluck));
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
                    //Toast.makeText(game_gn.this, "ERROR GETTING COINS", Toast.LENGTH_SHORT).show();
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

                        gn_min.setText(arrayList.get(0).getGn_min());
                        gn_max.setText(arrayList.get(0).getGn_max());
                        gn_win.setText(arrayList.get(0).getGn_win());
                        gn_chance.setText(arrayList.get(0).getGn_chance());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetGames> call, Throwable t) {
                    //Toast.makeText(game_gn.this, "ERROR GETTING RULES", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}