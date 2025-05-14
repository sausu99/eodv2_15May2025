/**
 * The LoginActivity class is an Android activity that handles user login functionality, including
 * validation, API calls, and language selection.
 */
package com.wowcodes.supreme.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rilixtech.CountryCodePicker;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    BindingService videoService;
    TextView txtcontinue;
    ImageView imgBackk;
    LinearLayout google, email, fb, loadinglayout;
    EditText mobno;
    CountryCodePicker ccp;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initializing UI elements
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        txtcontinue = findViewById(R.id.txtcontinue);
        imgBackk = findViewById(R.id.imgBackk);
        google = findViewById(R.id.google_login);
        email = findViewById(R.id.email_login);
        fb = findViewById(R.id.fb_login);
        mobno = findViewById(R.id.mobno);
        ccp = findViewById(R.id.ccp);
        loadinglayout = findViewById(R.id.linearlay);
        txtcontinue.setEnabled(false);

        // Setup Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(view -> signIn());

        email.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, LoginActivityEmail.class)));

        fb.setOnClickListener(view -> {
            // Handle Facebook login if needed
        });

        txtcontinue.setOnClickListener(view -> {
            if (isPhoneNumberValid(mobno.getText().toString().trim(), ccp.getSelectedCountryNameCode().trim())) {
                try {
                    videoService.checkRegistrationPhone(mobno.getText().toString().trim(), ccp.getSelectedCountryCode().trim()).enqueue(new Callback<SuccessModel>() {
                        @Override
                        public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                            hideLoading();
                            if (response.isSuccessful() && response.body() != null) {
                                boolean not_there = response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1");
                                Intent i;
                                if (not_there) {
                                    i = new Intent(LoginActivity.this, RegisterActivity.class);
                                    i.putExtra("mobno", mobno.getText().toString().trim());
                                    i.putExtra("ccp", ccp.getSelectedCountryCode());
                                } else {
                                    i = new Intent(LoginActivity.this, EnterPasswordActivity.class);
                                    i.putExtra("mobno", mobno.getText().toString().trim());
                                }
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Response is not successful or body is null", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessModel> call, Throwable t) {
                            hideLoading();
                            Log.e("phoneCheckFailure", "Phone check failed", t);
                        }
                    });
                } catch (Exception e) {
                    hideLoading();
                    Log.e("phoneCheckException", "Exception occurred", e);
                }
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.entervalidmobileno), Toast.LENGTH_SHORT).show();
            }
        });

        imgBackk.setOnClickListener(view -> onBackPressed());

        mobno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mobno.getText().toString().isEmpty()) {
                    txtcontinue.setEnabled(false);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    txtcontinue.setEnabled(true);
                    txtcontinue.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    public boolean isPhoneNumberValid(String phoneNumber, String countryCode) {
        showLoading();
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCode);
            return phoneNumberUtil.isValidNumber(parsedNumber) && phoneNumberUtil.isPossibleNumber(parsedNumber);
        } catch (Exception e) {
            Log.e("phoneNumberValidation", "Phone number validation failed", e);
            return false;
        } finally {
            hideLoading();
        }
    }

    private void signIn() {
        showLoading();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            showLoading();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                String username = account.getDisplayName(); // Get the username
                String profileImageUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;

                videoService.checkRegistrationEmail(email).enqueue(new Callback<SuccessModel>() {
                    @Override
                    public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                        hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            boolean not_there = response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1");
                            if (not_there) {
                                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                                i.putExtra("email", email);
                                i.putExtra("username", username);
                                i.putExtra("profileImageUrl", profileImageUrl);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(LoginActivity.this, EnterPasswordActivity.class);
                                i.putExtra("mobno", email);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.loginerror), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                        hideLoading();
                        Log.e("emailCheckFailure", "Email check failed", t);
                    }
                });
            } catch (ApiException e) {
                hideLoading();
                Log.e("GoogleSignInException", "Google Sign-In failed", e);
            }
        }
    }

    private void showLoading() {
        loadinglayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        new Handler().postDelayed(() -> loadinglayout.setVisibility(View.GONE), 1000);
    }
}
