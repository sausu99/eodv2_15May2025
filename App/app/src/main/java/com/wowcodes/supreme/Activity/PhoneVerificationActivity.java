package com.wowcodes.supreme.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.Modelclas.RegisterModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PhoneVerificationActivity extends AppCompatActivity {
    BindingService videoService;
    TextView buttonVerifyNow,resendotp;
    SavePref savePref;
    String getOtp;
    ImageView imgBackBtn;
    EditText edEt4, edEt3, edEt2, edEt1;
    LinearLayout resend,otps;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonevarification);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(PhoneVerificationActivity.this);

        imgBackBtn = findViewById(R.id.imgBackBtn);
        edEt1 = findViewById(R.id.edEt1);
        edEt2 = findViewById(R.id.edEt2);
        edEt3 = findViewById(R.id.edEt3);
        edEt4 = findViewById(R.id.edEt4);
        otps=findViewById(R.id.otps);
        resend=findViewById(R.id.resend);
        resendotp=findViewById(R.id.resendotp);
        buttonVerifyNow = findViewById(R.id.buttonVerifyNow);
        buttonVerifyNow.setEnabled(false);

        edEt1.addTextChangedListener(new GenericTextWatcher(edEt1));
        edEt2.addTextChangedListener(new GenericTextWatcher(edEt2));
        edEt3.addTextChangedListener(new GenericTextWatcher(edEt3));
        edEt4.addTextChangedListener(new GenericTextWatcher(edEt4));

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonVerifyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtp = (edEt1.getText().toString() + edEt2.getText().toString() + edEt3.getText().toString() + edEt4.getText().toString()).trim();
                getregapi();
            }
        });

    }


    public void getregapi() {
        try {
            callregisterApi().enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, retrofit2.Response<RegisterModel> response) {
                    try {
                        ArrayList<RegisterModel.Register_model_Inner> arrayList = response.body().getJSON_DATA();
                        String msg;
                        msg = arrayList.get(0).getMsg();
                        Toast.makeText(PhoneVerificationActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                            // Set/Update fcm_notification_token for this user
                            try {
                                videoService.set_fcm_token(arrayList.get(0).getId(), getSharedPreferences("FCM_REG_TOKEN", MODE_PRIVATE).getString("token", "")).enqueue(new Callback<SuccessModel>() {
                                    @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                                    @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                                });
                            } catch (Exception ignore) {}


                            savePref.setUserId(arrayList.get(0).getId());
                            savePref.setUserPhone(arrayList.get(0).getPhone());
                            savePref.setemail(arrayList.get(0).getEmail());

                            Intent i = new Intent(PhoneVerificationActivity.this, CityDetailActivity.class);
                            startActivity(i);
                        }
                    } catch (Exception ignore) {
                    }
                }

                @Override public void onFailure(Call<RegisterModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    private Call<RegisterModel> callregisterApi() {
        if(savePref.getUserPhone().isEmpty())
            return videoService.mobilenumberverify_setting(getIntent().hasExtra("verify")?getIntent().getStringExtra("verify"):"", getOtp);
        else
            return videoService.mobilenumberverify_setting(savePref.getUserPhone(), getOtp);
    }

    public class GenericTextWatcher implements TextWatcher {
        private final View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edEt1:
                    if (text.length() == 1)
                        edEt2.requestFocus();
                    break;
                case R.id.edEt2:
                    if (text.length() == 1)
                        edEt3.requestFocus();
                    else if (text.length() == 0)
                        edEt1.requestFocus();
                    break;
                case R.id.edEt3:
                    if (text.length() == 1)
                        edEt4.requestFocus();
                    else if (text.length() == 0)
                        edEt2.requestFocus();
                    break;
                case R.id.edEt4:
                    if (text.length() == 0) {
                        buttonVerifyNow.setEnabled(false);
                        buttonVerifyNow.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                        edEt3.requestFocus();
                    }
                    else if(text.length() == 1) {
                        buttonVerifyNow.setEnabled(true);
                        buttonVerifyNow.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                    }

                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
