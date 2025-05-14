package com.wowcodes.supreme.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.wowcodes.supreme.Modelclas.LoginModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivityEmail extends AppCompatActivity {

    public BindingService videoService;
    TextView txtcontinue;
    ImageView imgBackk;
    LinearLayout google,phone,fb;
    EditText email;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtcontinue=findViewById(R.id.txtcontinue);
        imgBackk=findViewById(R.id.imgBackk);
        google=findViewById(R.id.google_login);
        phone=findViewById(R.id.mob_login);
        fb=findViewById(R.id.fb_login);
        email=findViewById(R.id.email);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        txtcontinue.setEnabled(false);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(gsc.getSignInIntent(),52);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmailValid(email.getText().toString().trim())){
                    try {
                        videoService.checkRegistrationEmail(email.getText().toString().trim()).enqueue(new Callback<SuccessModel>() {
                            @Override
                            public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                                boolean not_there = response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1");
                                if(not_there) {
                                    Intent i = new Intent(LoginActivityEmail.this, RegisterActivity.class);
                                    i.putExtra("email", email.getText().toString().trim());
                                    startActivity(i);
                                    Toast.makeText(LoginActivityEmail.this, getString(R.string.emaildoesnotexist), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                else {
                                    Intent i = new Intent(LoginActivityEmail.this, EnterPasswordActivity.class);
                                    i.putExtra("mobno",email.getText().toString().trim());
                                    startActivity(i);
                                }
                            }

                            @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                        });
                    } catch (Exception ignore) {}
                }
                else
                    Toast.makeText(LoginActivityEmail.this, getString(R.string.entervalidemail), Toast.LENGTH_SHORT).show();
            }
        });

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(email.getText().toString().isEmpty()){
                    txtcontinue.setEnabled(false);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    txtcontinue.setEnabled(true);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==52){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivityEmail.this);
                if (acct != null) {
                    try {
                        videoService.checkRegistrationEmail(acct.getEmail()).enqueue(new Callback<SuccessModel>() {
                            @Override
                            public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                                boolean not_there = response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1");

                                if (not_there) {
                                    Intent i = new Intent(LoginActivityEmail.this, ForgotPasswordActivity.class);
                                    i.putExtra("verify", true);
                                    startActivity(i);
                                } else {
                                    try {
                                        videoService.postUserLogin(acct.getEmail(), "").enqueue(new Callback<LoginModel>() {
                                            @Override
                                            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
                                                try {
                                                    ArrayList<LoginModel.Login_model_Inner> arrayList = response.body().getJSON_DATA();
                                                    String msg = arrayList.get(0).getMsg();
                                                    if (Objects.equals(msg, "Welcome back to the Auction App"))
                                                        Toast.makeText(LoginActivityEmail.this, R.string.string208, Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(LoginActivityEmail.this, "" + msg, Toast.LENGTH_SHORT).show();

                                                    if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                                                        SavePref savePref = new SavePref(LoginActivityEmail.this);
                                                        savePref.setUserId(arrayList.get(0).getId());
                                                        savePref.setUserPhone(arrayList.get(0).getPhone());
                                                        savePref.setemail(arrayList.get(0).getEmail());

                                                        // Set/Update fcm_notification_token for this user
                                                        try {
                                                            videoService.set_fcm_token(arrayList.get(0).getId(), getSharedPreferences("FCM_REG_TOKEN", MODE_PRIVATE).getString("token", "")).enqueue(new Callback<SuccessModel>() {
                                                                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                                                                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                                                            });
                                                        } catch (Exception ignore) {}

                                                        Intent i = new Intent(LoginActivityEmail.this, MainActivity.class);
                                                        startActivity(i);
                                                    }
                                                } catch (Exception ignore) {}
                                            }

                                            @Override public void onFailure(Call<LoginModel> call, Throwable t) {}
                                        });
                                    } catch (Exception ignore) {}
                                }
                            }

                            @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                        });
                    } catch (Exception ignore) {}
                }
                else
                    Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            } catch(ApiException e){
                Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
}