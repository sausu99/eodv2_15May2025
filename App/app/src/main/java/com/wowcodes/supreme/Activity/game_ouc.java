package com.wowcodes.supreme.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Adapter.oucAdapter;
import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game_ouc extends AppCompatActivity {

    ImageView imgBackk;
    TextView txtGetCoinTop,underline,ouc_min,ouc_max,ouc_win_min,ouc_win_max,ouc_bonus1,ouc_bonus2,ouc_bonus3,ouc_amount,spin,txtAmount;
    BindingService videoService;
    RecyclerView rvnumbers;
    int min,max,amount;
    SwitchCompat bonus1,bonus2,bonus3;
    SavePref savePref;
    int set=0;
    int coinValue=0;
    int coins=0;
    boolean rolling=false;
    int[] oucDataList =new int[20];

    LinearLayoutManager manager;
    oucAdapter oucadapter;
    LinearLayout loading;
    int target = -1;
    int index = -1;

    @SuppressLint({"UseCompatLoadingForDrawables", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_ouc);

        imgBackk=findViewById(R.id.imgBackk);
        loading=findViewById(R.id.linearlay);
        TextView txtAucname=findViewById(R.id.txtAucname);
        txtAucname.setText("SPIN & WIN");
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ouc_min=findViewById(R.id.ouc_min);
        ouc_win_min=findViewById(R.id.ouc_win_min);
        ouc_max=findViewById(R.id.ouc_max);
        ouc_win_max=findViewById(R.id.ouc_win_max);
        ouc_bonus1=findViewById(R.id.ouc_bonus1);
        ouc_bonus2=findViewById(R.id.ouc_bonus2);
        ouc_bonus3=findViewById(R.id.ouc_bonus3);
        ouc_amount=findViewById(R.id.ouc_amount);
        rvnumbers=findViewById(R.id.rvnumbers);
        spin=findViewById(R.id.spin);
        bonus1=findViewById(R.id.bonus1);
        bonus2=findViewById(R.id.bonus2);
        bonus3=findViewById(R.id.bonus3);

        ouc_min.setText("1");
        ouc_max.setText("100");
        ouc_win_min.setText("5");
        ouc_win_max.setText("30");
        ouc_amount.setText("5");
        ouc_bonus1.setText("1");
        ouc_bonus2.setText("2");
        ouc_bonus3.setText("3");

        txtAmount=findViewById(R.id.txtAmount);
        underline=findViewById(R.id.underline);
        txtGetCoinTop=findViewById(R.id.txtGetCoinTop);
        savePref=new SavePref(game_ouc.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);


        getprofile();
        getrules();

        underline.setPaintFlags(underline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        bonus1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bonus2.setChecked(false);
                bonus3.setChecked(false);

                if(b){
                    bonus1.setChecked(true);
                    txtAmount.setText(String.valueOf(Integer.parseInt(ouc_amount.getText().toString()) + Integer.parseInt(ouc_bonus1.getText().toString())));
                }
                else {
                    bonus1.setChecked(false);
                    txtAmount.setText(ouc_amount.getText().toString());
                }
            }
        });

        bonus2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bonus1.setChecked(false);
                bonus3.setChecked(false);

                if(b) {
                    bonus2.setChecked(true);
                    txtAmount.setText(String.valueOf(Integer.parseInt(ouc_amount.getText().toString()) + Integer.parseInt(ouc_bonus2.getText().toString())));
                }
                else {
                    bonus2.setChecked(false);
                    txtAmount.setText(ouc_amount.getText().toString());
                }
            }
        });

        bonus3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bonus1.setChecked(false);
                bonus2.setChecked(false);

                if(b) {
                    bonus3.setChecked(true);
                    txtAmount.setText(String.valueOf(Integer.parseInt(ouc_amount.getText().toString()) + Integer.parseInt(ouc_bonus3.getText().toString())));
                }
                else {
                    bonus3.setChecked(false);
                    txtAmount.setText(ouc_amount.getText().toString());
                }
            }
        });


        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int curcoins = Integer.parseInt(txtGetCoinTop.getText().toString());

                if (amount > curcoins) {
                    Toast.makeText(game_ouc.this, getResources().getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                } else {
                    updatemoney(-amount);
                    txtGetCoinTop.setText(String.valueOf(curcoins - amount));

                    // Ensure the target is chosen from visible items in the RecyclerView
                    int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = manager.findLastVisibleItemPosition();

                    if (firstVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != RecyclerView.NO_POSITION) {
                        // Get visible items from oucDataList
                        int[] visibleItems = Arrays.copyOfRange(oucDataList, firstVisibleItemPosition, lastVisibleItemPosition + 1);

                        target = visibleItems[new Random().nextInt(visibleItems.length)];

                        if (target < min || target > max) {
                            Toast.makeText(game_ouc.this, "Invalid target", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Find the index of the target in the original list
                        for (int i = 0; i < oucDataList.length; i++) {
                            if (target == oucDataList[i]) {
                                index = i;
                                break;
                            }
                        }

                        if (index >= 0 && index < oucDataList.length) {
                            // Swap target with the 18th element in the list
                            int temp = oucDataList[18];
                            oucDataList[18] = oucDataList[index];
                            oucDataList[index] = temp;

                            oucadapter = new oucAdapter(oucDataList, getApplicationContext());
                            rvnumbers.setAdapter(oucadapter);

                            rvnumbers.smoothScrollToPosition(13);
                            rolling = true;

                            rvnumbers.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    if(newState == RecyclerView.SCROLL_STATE_IDLE && rolling){
                                        rolling=false;
                                        int curcoins2 = Integer.parseInt(txtGetCoinTop.getText().toString());
                                        updatemoney(target);
                                        Log.d("Target", String.valueOf(target));
                                        txtGetCoinTop.setText(String.valueOf(curcoins2 + target));
                                        Dialog dialog=new Dialog(game_ouc.this);
                                        dialog.setContentView(R.layout.dialogbox);
                                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback);
                                        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        dialog.setCancelable(false);
                                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                        TextView okay = dialog.findViewById(R.id.okay);
                                        TextView textview = dialog.findViewById(R.id.textview);

                                        textview.setText("CONGRATULATIONS !!\nYOU WON "+target+" COINS!");
                                        okay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                set=0;
                                                reload();
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            });

                        } else {
                            Log.e("ArrayAccess", "Target not found in list: " + target);
                        }
                    } else {
                        Toast.makeText(game_ouc.this, "No visible items to select target", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2=new Dialog(game_ouc.this);
                dialog2.setContentView(R.layout.dialogbox2);
                dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                dialog2.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(true);
                dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView okay2 = dialog2.findViewById(R.id.okay);
                TextView textview2 = dialog2.findViewById(R.id.textview);

                textview2.setText(getText(R.string.gamerules)+"\n\n"+getText(R.string.ouc_baseamt)+ouc_amount.getText().toString()+"\n"+getText(R.string.ouc_min_winamt)+ouc_min.getText().toString()+"\n"+getText(R.string.ouc_max_winamt)+ouc_max.getText().toString()+"\n"+getText(R.string.ouc_extra10)+ouc_bonus1.getText().toString()+"\n"+getText(R.string.ouc_extra20)+ouc_bonus2.getText().toString()+"\n"+getText(R.string.ouc_extra30)+ouc_bonus3.getText().toString()+"\n\n"+getText(R.string.steps)+"\n\n"+getText(R.string.oucstep1)+"\n\n"+getText(R.string.oucstep2)+"\n\n"+getText(R.string.oucstep3)+"\n\n"+getText(R.string.bestofluck));
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
        loading.setVisibility(View.VISIBLE);
        try {
            videoService.postUserwalletUpdate(savePref.getUserId(),String.valueOf(coins),"gm_ouc","gm_ouc",String.valueOf(coins)).enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    loading.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.setVisibility(View.GONE);

        }
    }


    public void getprofile() {
        loading.setVisibility(View.VISIBLE);
        try {
            videoService.getUserProfile(savePref.getUserId()).enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtGetCoinTop.setText(arrayList.get(0).getWallet());
                        loading.setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.setVisibility(View.GONE);

        }
    }

    public void getrules() {
        loading.setVisibility(View.VISIBLE);
        try {
            videoService.get_games().enqueue(new Callback<GetGames>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetGames> call, Response<GetGames> response) {
                    try {
                        ArrayList<GetGames.Get_games_Inner> arrayList = response.body().getJSON_DATA();

                        ouc_min.setText(arrayList.get(0).getOuc_min());
                        min=Integer.parseInt(arrayList.get(0).getOuc_min());
                        ouc_max.setText(arrayList.get(0).getOuc_max());
                        max=Integer.parseInt(arrayList.get(0).getOuc_max());
                        ouc_win_min.setText(arrayList.get(0).getOuc_win_min());
                        ouc_win_max.setText(arrayList.get(0).getOuc_win_max());
                        ouc_bonus1.setText(arrayList.get(0).getOuc_bonus1());
                        ouc_bonus2.setText(arrayList.get(0).getOuc_bonus2());
                        ouc_bonus3.setText(arrayList.get(0).getOuc_bonus3());
                        ouc_amount.setText(arrayList.get(0).getOuc_amount());
                        amount=Integer.parseInt(arrayList.get(0).getOuc_amount());

                        //Toast.makeText(game_ouc.this, "done", Toast.LENGTH_SHORT).show();

                        txtAmount.setText(ouc_amount.getText().toString());



                        manager=new OucLayoutManager(game_ouc.this,LinearLayoutManager.HORIZONTAL,false);
                        rvnumbers.setLayoutManager(manager);
                        preparedata();
                        oucadapter = new oucAdapter(oucDataList,getApplicationContext());
                        rvnumbers.setAdapter(oucadapter);
                        loading.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<GetGames> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.setVisibility(View.GONE);
        }
    }


    public void preparedata() {
        loading.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            oucDataList= IntStream.generate(() -> new Random().nextInt(Integer.parseInt(ouc_max.getText().toString()) - Integer.parseInt(ouc_min.getText().toString()))+Integer.parseInt(ouc_min.getText().toString())).limit(20).toArray();
            loading.setVisibility(View.GONE);
        }
        else{
            onBackPressed();
            Toast.makeText(this, getResources().getString(R.string.ouc_olderversion_error), Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
        }
    }


    public boolean contains(int val){
        loading.setVisibility(View.VISIBLE);
        for (int j : oucDataList) {
            if (j == val)
                loading.setVisibility(View.GONE);
            return true;
        }
        loading.setVisibility(View.GONE);
        return false;
    }

    public void reload(){
        loading.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, game_ouc.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loading.setVisibility(View.GONE);
        startActivity(i);
    }
}