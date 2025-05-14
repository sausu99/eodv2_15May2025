package com.wowcodes.supreme.Activity;

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
import android.widget.TextView;
import android.widget.Toast;
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
public class EnterPasswordActivity extends AppCompatActivity {
    ImageView imgBackk;
    EditText password;
    TextView txtcontinue,forgot;
    BindingService videoService;
    String mobno="";
    SavePref savePref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.offwhiteblack));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtcontinue=findViewById(R.id.txtcontinue);
        imgBackk=findViewById(R.id.imgBackk);
        password=findViewById(R.id.edPass);
        forgot=findViewById(R.id.forgot_pass);
        savePref=new SavePref(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().isEmpty()){
                    txtcontinue.setEnabled(false);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    txtcontinue.setEnabled(true);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnterPasswordActivity.this, ForgotPasswordActivity.class));
            }
        });
        
        if(getIntent().hasExtra("mobno"))
            mobno=getIntent().getStringExtra("mobno");
        
        txtcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    videoService.postUserLogin(mobno,password.getText().toString()).enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
                            try {
                                ArrayList<LoginModel.Login_model_Inner> arrayList = response.body().getJSON_DATA();
                                String msg = arrayList.get(0).getMsg();
                                if (Objects.equals(msg, "Welcome back to the Auction App"))
                                    Toast.makeText(EnterPasswordActivity.this, R.string.string208, Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(EnterPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                                if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                                    savePref.setUserId(arrayList.get(0).getId());
                                    savePref.setUserPhone(arrayList.get(0).getPhone());
                                    savePref.setemail(arrayList.get(0).getEmail());
                                    try {
                                        videoService.set_fcm_token(arrayList.get(0).getId(),getSharedPreferences("FCM_REG_TOKEN",MODE_PRIVATE).getString("token","")).enqueue(new Callback<SuccessModel>() {
                                            @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                                            @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                                        });
                                    } catch (Exception ignore) {}

                                    Intent i = new Intent(EnterPasswordActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            } catch (Exception ignore) {}
                        }
                        @Override public void onFailure(Call<LoginModel> call, Throwable t) {}
                    });
                } catch (Exception ignore) {}
            }
        });
        
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}