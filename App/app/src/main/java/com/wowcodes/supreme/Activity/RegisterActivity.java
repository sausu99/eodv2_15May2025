package com.wowcodes.supreme.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.NumberParseException;
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
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edRegisterName, edRegisterEmail, edForgotNum, edPass;
    TextView txtLoginActi, referralcode, refcode,txtmobno,criteriaTextView;
    SavePref savePref;
    TextView txtPrivacy;
    public BindingService videoService;
    LinearLayout lvlRegister,mobno_ll;
    CheckBox checkBox;
    private String androidDeviceId;
    String getPrivacy,email,name,profileImageUrl;
    ProgressBar strengthProgressBar;
    CountryCodePicker ccp;
    ImageView backk;
    public String otp_system = "",lastValidationMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        androidDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        savePref = new SavePref(RegisterActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        checkBox = findViewById(R.id.checkbox);
        txtPrivacy = findViewById(R.id.txtPrivacy);
        edRegisterName = findViewById(R.id.txtYourName);
        edRegisterEmail = findViewById(R.id.txtEmailId);
        edForgotNum = findViewById(R.id.edForgotNum);
        edRegisterName = findViewById(R.id.txtYourName);
        lvlRegister = findViewById(R.id.linearlay);
        referralcode = findViewById(R.id.referralcode);
        referralcode.setPaintFlags(referralcode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        refcode = findViewById(R.id.refcode);
        edPass = findViewById(R.id.edPass);
        txtLoginActi = findViewById(R.id.txtLoginActi);
        txtmobno=findViewById(R.id.txtmobno);
        mobno_ll=findViewById(R.id.mobno_ll);
        backk = findViewById(R.id.imgBackk);
        strengthProgressBar = findViewById(R.id.strengthProgressBar);
        criteriaTextView = findViewById(R.id.criteriaTextView);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ccp = findViewById(R.id.ccp);

        if (getIntent().hasExtra("username")) {
            name = getIntent().getStringExtra("username").trim();
            Log.d("usernamerecieve", name);// Pass the username
            edRegisterName.setVisibility(View.GONE); // Ensure visibility is set correctly
            edRegisterName.setText(name);
        }

        if(getIntent().hasExtra("mobno")){
            mobno_ll.setVisibility(View.GONE);
            txtmobno.setVisibility(View.GONE);
            edForgotNum.setText(getIntent().getStringExtra("mobno").trim());
            ccp.setCountryForPhoneCode(Integer.parseInt(getIntent().getStringExtra("ccp")));
        }else if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email").trim();
            edRegisterEmail.setVisibility(View.GONE); // Ensure visibility is set correctly
            edRegisterEmail.setText(email);
        }  else {
            edRegisterEmail.setVisibility(View.VISIBLE);
            mobno_ll.setVisibility(View.VISIBLE);
            txtmobno.setVisibility(View.VISIBLE);
        }

        referralcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.dialog_refcode);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback3);
                dialog.getWindow().setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                TextView ok = dialog.findViewById(R.id.ok);
                EditText edcode = dialog.findViewById(R.id.edcode);

                edcode.setText(refcode.getText().toString());
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        refcode.setText(edcode.getText().toString());
                        referralcode.setText("REFERRAL CODE APPLIED..");
                    }
                });
                dialog.show();
            }
        });

        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RegisterActivity.this, PrivacyPolicyActivity.class);
                i.putExtra("check", getPrivacy);
                startActivity(i);

            }
        });
        edPass.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                criteriaTextView.setVisibility(View.VISIBLE);
                if (password.isEmpty())
                    strengthProgressBar.setProgress(0);
                else {
                    PasswordStrength strength = checkPasswordStrength(password);
                    updateUI(strength, password);
                }
            }
        });

        txtLoginActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isEmailValid(edRegisterEmail.getText().toString()))
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.entervalidemail), Toast.LENGTH_SHORT).show();
                    else if (!isPhoneNumberValid(edForgotNum.getText().toString(), ccp.getSelectedCountryNameCode()))
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.entervalidmobileno), Toast.LENGTH_SHORT).show();
                    else if (!isPasswordValid(edPass.getText().toString()))
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.entervalidpass), Toast.LENGTH_SHORT).show();
                    else if (!checkBox.isChecked())
                        Toast.makeText(RegisterActivity.this, R.string.string167, Toast.LENGTH_SHORT).show();
                    else {
                        PasswordStrength strength = checkPasswordStrength(edPass.getText().toString());
                        if (strength == PasswordStrength.STRONG) {
                            savePref.setUserPhone(edForgotNum.getText().toString());
                            getregapi();
                        } else
                            Toast.makeText(RegisterActivity.this, "Password must be strong to proceed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ignore){}
            }
        });

        getsetting();
    }


    private Call<SuccessModel> callregisterApi() {
        String name = edRegisterName.getText().toString();
        String email = edRegisterEmail.getText().toString();
        String countryCode = ccp.getSelectedCountryCode();
        String phone = edForgotNum.getText().toString();
        String referralCode = refcode.getText().toString();
        String password = edPass.getText().toString();
        String anotherParam = androidDeviceId;

        Log.d("Registration Request", "Name: " + name + ", Email: " + email + ", CountryCode: " + countryCode +
                ", Phone: " + phone + ", ReferralCode: " + referralCode + ", Password: " + password + ", AnotherParam: " + anotherParam);

        return videoService.postUserRegister(name, email, countryCode, phone, referralCode, password, anotherParam);
    }

    public void getregapi() {
        lvlRegister.setVisibility(View.VISIBLE);
        try {
            callregisterApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    lvlRegister.setVisibility(View.GONE);  // Hide progress bar on response

                    if (response.isSuccessful() && response.body() != null) {
                        SuccessModel successModel = response.body();
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = successModel.getJSON_DATA();

                        if (arrayList != null && !arrayList.isEmpty()) {
                            SuccessModel.Suc_Model_Inner innerModel = arrayList.get(0);
                            String msg = innerModel.getMsg();

                            if ("1".equalsIgnoreCase(innerModel.getSuccess())) {
                                if ("1".equalsIgnoreCase(otp_system)) {
                                    Intent i = new Intent(RegisterActivity.this, PhoneVerificationActivity.class);
                                    i.putExtra("phone", ccp.getSelectedCountryCode() + edForgotNum.getText().toString());
                                    startActivity(i);
                                    finish();
                                } else {
                                    direct_login();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showRetrySnackbar(getString(R.string.retryTxt));
                        }
                    } else {
                        showRetrySnackbar(getString(R.string.retryTxt));
                    }
                }
                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    lvlRegister.setVisibility(View.GONE);  // Hide progress bar on failure
                    Log.e("RegisterAPI", "API call failed", t);
                    showRetrySnackbar("Network error. Please check your connection and try again.");
                }
            });
        } catch (Exception e) {
            lvlRegister.setVisibility(View.GONE);  // Hide progress bar on exception
            Log.e("RegisterException", "Exception in API call", e);
            showRetrySnackbar(getString(R.string.retryTxt));
        }
    }
    private void showRetrySnackbar(String message) {
        Snackbar snackbar = Snackbar.make(lvlRegister, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getregapi();  // Retry registration on button click
                    }
                });
        snackbar.show();
    }

    public void getsetting() {
        lvlRegister.setVisibility(View.VISIBLE);
        try {
            callseting().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    lvlRegister.setVisibility(View.GONE);
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                    getPrivacy = arrayList.get(0).getApp_privacy_policy();
                    otp_system = arrayList.get(0).getOtp_system();
                }

                @Override
                public void onFailure(Call<SettingModel> call, Throwable t) {
                    lvlRegister.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }


    private Call<SettingModel> callseting() {
        return videoService.settings();
    }

    public boolean isPhoneNumberValid(String phoneNumber, String countryCode) throws NumberParseException {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCode);
        return phoneNumberUtil.isValidNumber(parsedNumber) && phoneNumberUtil.isPossibleNumber(parsedNumber);
    }

    public boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    public boolean isPasswordValid(String pass) {
        return pass.length() >= 6;
    }

    private PasswordStrength checkPasswordStrength(String password) {
        if (password.length() < 6)
            return PasswordStrength.WEAK;
        else if (!Pattern.matches(".*[a-z].*", password) ||
                !Pattern.matches(".*[A-Z].*", password) ||
                !Pattern.matches(".*\\d.*", password) ||
                !Pattern.matches(".*[!@#$%^&*()].*", password)) {
            return PasswordStrength.MEDIUM;
        } else
            return PasswordStrength.STRONG;
    }

    private void updateUI(PasswordStrength strength, String password) {
        int color;
        int progress;
        String validationMessage;

        switch (strength) {
            case WEAK:
                color = Color.parseColor("#FF6347");
                progress = 33;
                validationMessage = getString(R.string.validation1);
                break;
            case MEDIUM:
                color = Color.parseColor("#FFA500");
                progress = 66;
                validationMessage = getString(R.string.validation2);
                break;
            case STRONG:
                color = Color.parseColor("#32CD32");
                progress = 100;
                validationMessage = getString(R.string.validation3);
                break;
            default:
                color = Color.RED;
                progress = 0;
                validationMessage = "";
                break;
        }

        strengthProgressBar.setProgress(progress);
        strengthProgressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

        if (!validationMessage.equals(lastValidationMessage)) {
            Snackbar.make(edPass, validationMessage, Snackbar.LENGTH_SHORT).show();
            lastValidationMessage = validationMessage;
        }
        criteriaTextView.setText(getCriteriaText(password));
    }
    private String getCriteriaText(String password) {
        StringBuilder criteriaText = new StringBuilder();
        criteriaText.append("Criteria:\n");
        criteriaText.append(getString(R.string.criteria1)).append(password.length() >= 6 ? "✔" : "✘").append("\n");
        criteriaText.append(getString(R.string.criteria2)).append(password.matches(".*[a-z].*") ? "✔" : "✘").append("\n");
        criteriaText.append(getString(R.string.criteria3)).append(password.matches(".*[A-Z].*") ? "✔" : "✘").append("\n");
        criteriaText.append(getString(R.string.criteria4)).append(password.matches(".*\\d.*") ? "✔" : "✘").append("\n");
        criteriaText.append(getString(R.string.criteria5)).append(password.matches(".*[!@#$%^&*()].*") ? "✔" : "✘").append("\n");
        return criteriaText.toString();
    }

    private enum PasswordStrength {
        WEAK,
        MEDIUM,
        STRONG
    }

    public void direct_login(){
        try {
            videoService.mobilenumberverify_setting(edForgotNum.getText().toString().trim(), "1234").enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                    try {
                        ArrayList<RegisterModel.Register_model_Inner> arrayList = response.body().getJSON_DATA();
                        String msg;
                        msg = arrayList.get(0).getMsg();
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                            // Set/Update fcm_notification_token for this user
                            try {
                                videoService.set_fcm_token(arrayList.get(0).getId(), getSharedPreferences("FCM_REG_TOKEN", MODE_PRIVATE).getString("token", "")).enqueue(new Callback<SuccessModel>() {
                                    @Override public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {}
                                    @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                                });
                            } catch (Exception ignore) {}

                            savePref.setUserId(arrayList.get(0).getId());
                            savePref.setUserPhone(arrayList.get(0).getPhone());
                            savePref.setemail(arrayList.get(0).getEmail());

                            Intent i = new Intent(RegisterActivity.this, CityDetailActivity.class);
                            startActivity(i);
                        }
                    } catch (Exception ignore) {                }
                }

                @Override public void onFailure(Call<RegisterModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
}