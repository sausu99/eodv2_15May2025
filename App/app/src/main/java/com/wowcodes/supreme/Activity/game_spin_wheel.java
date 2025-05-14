package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game_spin_wheel extends AppCompatActivity {

    ImageView redcoin,bluecoin,imgBackk,wheel;
    TextView winner_tv,txtGetCoinTop,underline,submit,spin_min,spin_max,spin_win,spin_chance;
    EditText noofcoins;
    BindingService videoService;
    SavePref savePref;
    int set=0;
    int coinValue=0;
    int coins=0;

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
        setContentView(R.layout.game_spin_wheel);

        imgBackk=findViewById(R.id.imgBackk);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText(getResources().getString(R.string.string370));
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        spin_min=findViewById(R.id.spin_min);
        spin_max=findViewById(R.id.spin_max);
        spin_win=findViewById(R.id.spin_win);
        spin_chance=findViewById(R.id.spin_chance);
        redcoin=findViewById(R.id.redcoin);
        bluecoin=findViewById(R.id.bluecoin);
        winner_tv=findViewById(R.id.winner_tv);
        submit=findViewById(R.id.submit);
        wheel=findViewById(R.id.wheel);
        noofcoins=findViewById(R.id.noofcoins);
        underline=findViewById(R.id.underline);
        txtGetCoinTop=findViewById(R.id.txtGetCoinTop);
        savePref=new SavePref(game_spin_wheel.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getrules();
        getprofile();


        underline.setPaintFlags(underline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(spin_min.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.minbidallowed)+" "+spin_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(spin_max.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.maxbidallowed)+" "+spin_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else if (set==0)
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.selectmovefirst), Toast.LENGTH_SHORT).show();
                else {

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    submit.setEnabled(false);
                    coins=Integer.parseInt(noofcoins.getText().toString());
                    double bonus=(Double.parseDouble(spin_win.getText().toString())*0.01);
                    coinValue = coins + (int) Math.ceil(coins * bonus);

                    updatemoney(-coins);
                    int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                    txtGetCoinTop.setText(String.valueOf(curcoins-coins));


                    int finrot = new Random().nextInt(7)+16;
                    finrot*=36;

                    CountDownTimer timer = new CountDownTimer(finrot*3,1) {
                        @Override
                        public void onTick(long l) {
                            wheel.setRotation(wheel.getRotation()+10);
                        }
                        @Override
                        public void onFinish() {
                            submit.setEnabled(true);
                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }



                            int finrot= (int) wheel.getRotation();
                            while(finrot>360)
                                finrot-=360;

                            if(set==1 && Integer.parseInt(spin_chance.getText().toString()) == 0)
                                finrot=100;
                            else if(set==2 && Integer.parseInt(spin_chance.getText().toString()) == 0)
                                finrot=225;
                            if(finrot==18 || finrot==54 || finrot==90 || finrot==126 || finrot==162 || finrot==198 || finrot==234 || finrot==270 || finrot==306 || finrot==342) {
                                finrot++;
                                wheel.setRotation(wheel.getRotation()+2);
                            }
                            else if(finrot==360) {
                                finrot = 1;
                                wheel.setRotation(wheel.getRotation()+2);
                            }

                            //Toast.makeText(game_spin.this, String.valueOf(finrot), Toast.LENGTH_SHORT).show();

                            if (set==1){
                                if ((finrot>0 && finrot<36) || (finrot>144 && finrot<180) || (finrot>216 && finrot<252) || (finrot>288 && finrot<324)) {
                                    winner_tv.setText("CONGRATULATIONS !!\nYOU HAVE WON !!\n\n" + coinValue + " COINS HAVE BEEN ADDED TO YOUR ACCOUNT.");
                                    updatemoney(coinValue);
                                    int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                                    txtGetCoinTop.setText(String.valueOf(curcoins+coinValue));
                                }
                                else{
                                    winner_tv.setText("OOPS!.\nYOU LOST!..\n\n" + noofcoins.getText().toString() + " COINS HAVE BEEN DEDUCTED FROM YOUR ACCOUNT..");
                                }
                            }

                            else if(set==2) {
                                if ((finrot>0 && finrot<36) || (finrot>72 && finrot<108) || (finrot>144 && finrot<180) || (finrot>216 && finrot<324)) {
                                    winner_tv.setText("OOPS!.\nYOU LOST!..\n\n" + noofcoins.getText().toString() + " COINS HAVE BEEN DEDUCTED FROM YOUR ACCOUNT..");
                                }
                                else{
                                    winner_tv.setText("CONGRATULATIONS !!\nYOU HAVE WON !!\n\n" + coinValue + " COINS HAVE BEEN ADDED TO YOUR ACCOUNT.");
                                    updatemoney(coinValue);
                                    int curcoins=Integer.parseInt(txtGetCoinTop.getText().toString());
                                    txtGetCoinTop.setText(String.valueOf(curcoins+coinValue));
                                }
                            }

                            Dialog dialog=new Dialog(game_spin_wheel.this);
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
                                    redcoin.setImageDrawable(getResources().getDrawable(R.drawable.redcoin));
                                    bluecoin.setImageDrawable(getResources().getDrawable(R.drawable.bluecoin));
                                }
                            });
                            dialog.show();
                        }
                    }.start();
                }
            }
        });

        redcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(spin_min.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.minbidallowed)+" "+spin_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(spin_max.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.maxbidallowed)+" "+spin_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    set = 1;
                    redcoin.setImageDrawable(getResources().getDrawable(R.drawable.redcoinsel));
                    bluecoin.setImageDrawable(getResources().getDrawable(R.drawable.bluecoin));
                }
            }
        });

        bluecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noofcoins.getText().toString().isEmpty())
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.entervaluefirst), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(txtGetCoinTop.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())<Integer.parseInt(spin_min.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.minbidallowed)+" "+spin_min.getText().toString(), Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(noofcoins.getText().toString())>Integer.parseInt(spin_max.getText().toString()))
                    Toast.makeText(game_spin_wheel.this, getResources().getString(R.string.maxbidallowed)+" "+spin_max.getText().toString(), Toast.LENGTH_SHORT).show();
                else {
                    set = 2;
                    redcoin.setImageDrawable(getResources().getDrawable(R.drawable.redcoin));
                    bluecoin.setImageDrawable(getResources().getDrawable(R.drawable.bluecoinsel));
                }
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2=new Dialog(game_spin_wheel.this);
                dialog2.setContentView(R.layout.dialogbox2);
                dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(true);
                dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView okay2 = dialog2.findViewById(R.id.okay);
                TextView textview2 = dialog2.findViewById(R.id.textview);

                textview2.setText(getText(R.string.gamerules)+"\n\n"+getText(R.string.minbet)+spin_min.getText().toString()+"\n"+getText(R.string.maxbet)+spin_max.getText().toString()+"\n"+getText(R.string.winbonus)+spin_win.getText().toString()+"\n"+getText(R.string.winchance)+spin_chance.getText().toString()+"\n\n"+getText(R.string.steps)+"\n\n"+getText(R.string.step1)+"\n\n"+getText(R.string.spin_step2)+"\n\n"+getText(R.string.spin_step3)+"\n\n"+getText(R.string.spin_step4)+"\n\n"+getText(R.string.bestofluck));
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
                    //Toast.makeText(game_spin.this, "ERROR GETTING COINS", Toast.LENGTH_SHORT).show();
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

                        spin_min.setText(arrayList.get(0).getSpin_min());
                        spin_max.setText(arrayList.get(0).getSpin_max());
                        spin_win.setText(arrayList.get(0).getSpin_win());
                        spin_chance.setText(arrayList.get(0).getSpin_chance());

                        /*spin_min.setText("1");
                        spin_max.setText("100");
                        spin_win.setText("20");
                        spin_chance.setText("20");*/

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetGames> call, Throwable t) {
                    //Toast.makeText(game_spin.this, "ERROR GETTING RULES", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}