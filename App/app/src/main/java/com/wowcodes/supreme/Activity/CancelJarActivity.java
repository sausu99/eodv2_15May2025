package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Fragments.InvestFragment;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CancelJarActivity extends AppCompatActivity {

    TextView txtAucname,plan_name,invested,earned,charge,total,bal,cancel;
    ImageView imgBackk;
    int coins=0,icharge=0;
    public BindingService videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_jar);

        imgBackk=findViewById(R.id.imgBackk);
        txtAucname=findViewById(R.id.txtAucname);
        plan_name=findViewById(R.id.plan_name);
        invested=findViewById(R.id.amt);
        earned=findViewById(R.id.amt_earned);
        charge=findViewById(R.id.charge);
        total=findViewById(R.id.rec_amt);
        bal=findViewById(R.id.balance);
        cancel=findViewById(R.id.cancel_plan);

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtAucname.setText(getString(R.string.cancel_investment));
        plan_name.setText(getIntent().getStringExtra("plan_name"));
        invested.setText(getIntent().getStringExtra("invested_amount")+" ");
        earned.setText(getIntent().getStringExtra("current_amount")+" ");
        charge.setText(getIntent().getStringExtra("charge")+" ");
        icharge=Integer.parseInt(getIntent().getStringExtra("charge"));
        total.setText(Integer.parseInt(getIntent().getStringExtra("current_amount")) - icharge + " ");


        try {
                videoService.getUserProfile(new SavePref(this).getUserId()).enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        if(arrayList.get(0).getWallet().isEmpty() || arrayList.get(0).getWallet()==null) {
                            bal.setText("N/A");
                            bal.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                        }
                        else {
                            bal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_coin, 0);
                            bal.setText(arrayList.get(0).getWallet() + " ");
                            coins=Integer.parseInt(arrayList.get(0).getWallet());
                        }
                    }

                    @Override public void onFailure(Call<UserProfile> call, Throwable t) {
                        bal.setText("N/A");
                        bal.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    }
                });
        } catch (Exception ignore) {
            bal.setText("N/A");
            bal.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coins>=icharge) {
                    Dialog dialog = new Dialog(CancelJarActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.transprant);
                    dialog.setContentView(R.layout.dialog_cancel_plan);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                    TextView keep = dialog.findViewById(R.id.keep);
                    TextView cancel = dialog.findViewById(R.id.cancel);

                    keep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                videoService.cancel_hyip_plan(new SavePref(CancelJarActivity.this).getUserId(), getIntent().getStringExtra("plan_id")).enqueue(new Callback<SuccessModel>() {
                                    @Override
                                    public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                                        try {
                                            ArrayList<SuccessModel.Suc_Model_Inner> arrayList=response.body().getJSON_DATA();
                                            if(arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                                                Toast.makeText(CancelJarActivity.this, CancelJarActivity.this.getString(R.string.cancel_complete), Toast.LENGTH_SHORT).show();
                                                InvestFragment.reload = true;
                                                Dialog dialog = new Dialog(CancelJarActivity.this);
                                                dialog.getWindow().setBackgroundDrawableResource(R.color.transprant);
                                                dialog.setContentView(R.layout.dialog_success);
                                                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                dialog.show();
                                                dialog.setCancelable(false);
                                                TextView ok = dialog.findViewById(R.id.ok);
                                                ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.cancel();
                                                        InvestFragment.reload=true;
                                                        finish();
                                                    }
                                                });
                                            }
                                            else
                                                Toast.makeText(CancelJarActivity.this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e){
                                            Toast.makeText(CancelJarActivity.this, CancelJarActivity.this.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                                        Toast.makeText(CancelJarActivity.this, CancelJarActivity.this.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                Toast.makeText(CancelJarActivity.this, CancelJarActivity.this.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(CancelJarActivity.this, getString(R.string.cancel_not_possible), Toast.LENGTH_SHORT).show();
            }
        });
    }
}