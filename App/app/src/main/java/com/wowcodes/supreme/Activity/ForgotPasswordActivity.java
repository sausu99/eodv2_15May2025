package com.wowcodes.supreme.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rilixtech.CountryCodePicker;
import com.wowcodes.supreme.Modelclas.RegisterModel;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ForgotPasswordActivity extends AppCompatActivity {

    EditText mobno;
    SavePref savePref;
    ImageView imgBackBtn,google,fb;
    TextView back,txtSubmit,signup,txtnoacc,verifymob,acnm;
    RelativeLayout txtor;
    LinearLayout login_options;
    CountryCodePicker ccp;
    BindingService videoService;
    public String otp_system="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        savePref=new SavePref(ForgotPasswordActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        mobno=findViewById(R.id.mobno);
        txtSubmit=findViewById(R.id.txtSubmit);
        imgBackBtn=findViewById(R.id.imgBackBtn);
        google=findViewById(R.id.google_login);
        fb=findViewById(R.id.fb_login);
        back=findViewById(R.id.back);
        signup=findViewById(R.id.signup);
        ccp=findViewById(R.id.ccp);
        acnm=findViewById(R.id.acnm);
        txtnoacc=findViewById(R.id.txtnoacc);
        txtor=findViewById(R.id.txtor);
        verifymob=findViewById(R.id.verifymob);
        login_options=findViewById(R.id.login_options);

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("verify"))
                    registerUser();
                else
                    forpass();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
            }
        });

        mobno.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mobno.getText().toString().isEmpty()){
                    txtSubmit.setEnabled(false);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    txtSubmit.setEnabled(true);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        if(getIntent().hasExtra("verify")){
            acnm.setVisibility(View.GONE);
            verifymob.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            txtor.setVisibility(View.GONE);
            login_options.setVisibility(View.GONE);
            txtnoacc.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);
        }
        else{
            acnm.setVisibility(View.VISIBLE);
            verifymob.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            //txtor.setVisibility(View.VISIBLE);
            //login_options.setVisibility(View.VISIBLE);
            txtnoacc.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
        }

        try {
            videoService.settings().enqueue(new Callback<SettingModel>() {
                @Override public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    otp_system = response.body().getJSON_DATA().get(0).getOtp_system();
                }
                @Override public void onFailure(Call<SettingModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    public void forpass(){
        try {
            callloginApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList= response.body().getJSON_DATA();
                        String msg;
                        msg=arrayList.get(0).getMsg();
                        Toast.makeText(ForgotPasswordActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            savePref.setUserPhone(mobno.getText().toString());
                            Intent i=new Intent(ForgotPasswordActivity.this, PhoneVerificationnActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        }catch (Exception ignore){}
    }


    private Call<SuccessModel> callloginApi() {
        return videoService.forgotpassword(mobno.getText().toString());
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void registerUser(){
        try {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ForgotPasswordActivity.this);
            if (acct != null) {
                String username = acct.getDisplayName();
                String email = acct.getEmail();
                String code = ccp.getSelectedCountryCode();
                String num = mobno.getText().toString();
                String androidDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                if(isPhoneNumberValid(mobno.getText().toString().trim(),ccp.getSelectedCountryNameCode().trim())) {
                    videoService.postUserRegister(username, email, code, num, "", "", androidDeviceId).enqueue(new Callback<SuccessModel>() {
                        @Override
                        public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();

                                if (arrayList != null && !arrayList.isEmpty()) {
                                    String msg = arrayList.get(0).getMsg();
                                    Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                    if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                                        if (otp_system.equalsIgnoreCase("1")) {
                                            Intent i = new Intent(ForgotPasswordActivity.this, PhoneVerificationActivity.class);
                                            i.putExtra("verify", mobno.getText().toString());
                                            startActivity(i);
                                        } else
                                            direct_login();
                                    } else {
                                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }

                                } else
                                    Toast.makeText(ForgotPasswordActivity.this, "Arraylist empty", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ForgotPasswordActivity.this, "response unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SuccessModel> call, Throwable t) {
                            Toast.makeText(ForgotPasswordActivity.this, "f: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(this, getString(R.string.entervalidmobileno), Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Google Account is null", Toast.LENGTH_SHORT).show();
        } catch(Exception gnore){
            Toast.makeText(this, "e: "+gnore.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void direct_login(){
        try {
            videoService.mobilenumberverify_setting(mobno.getText().toString().trim(), "1234").enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, retrofit2.Response<RegisterModel> response) {
                    try {
                        ArrayList<RegisterModel.Register_model_Inner> arrayList = response.body().getJSON_DATA();
                        String msg;
                        msg = arrayList.get(0).getMsg();
                        Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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

                            Intent i = new Intent(ForgotPasswordActivity.this, CityDetailActivity.class);
                            startActivity(i);
                        }
                    } catch (Exception ignore) {
                    }
                }

                @Override public void onFailure(Call<RegisterModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public boolean isPhoneNumberValid(String phoneNumber, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCode);
            return phoneNumberUtil.isValidNumber(parsedNumber) && phoneNumberUtil.isPossibleNumber(parsedNumber);
        } catch (Exception e) {
            return false;
        }
    }
}
