package com.wowcodes.supreme.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.AddressResponse;
import com.wowcodes.supreme.Modelclas.UpdateAddressResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {
    SavePref savePref;

    private EditText houseNoEditText;
    private EditText areaEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText postalCodeEditText;
    private EditText countryEditText;
    private Button updateAddressButton;
    BindingService videoService;
    private RelativeLayout homeTxt, workTxt, otherTxt;
    private ImageView homeImg, workImg,imgBackBtn, otherImg;
    private TextView home, work, other;
    private boolean isHomeClicked = false;
    private boolean isWorkClicked = false;
    private boolean isOtherClicked = false;
    private String selectedOption,addressId;
    private String nickname=" ";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        imgBackBtn = findViewById(R.id.imgBackk);
        homeTxt = findViewById(R.id.hometxt);
        workTxt = findViewById(R.id.worktxt);
        otherTxt = findViewById(R.id.othertxt);
        homeImg = homeTxt.findViewById(R.id.homeImg);
        workImg = workTxt.findViewById(R.id.workImg);
        otherImg = otherTxt.findViewById(R.id.otherImg);
        home = homeTxt.findViewById(R.id.home);
        work = workTxt.findViewById(R.id.work);
        other = otherTxt.findViewById(R.id.other);
        houseNoEditText = findViewById(R.id.houseNumberEdittext);
        areaEditText = findViewById(R.id.areaEditText);
        cityEditText = findViewById(R.id.cityEditText);
        stateEditText = findViewById(R.id.stateEditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        countryEditText = findViewById(R.id.countryEditText);
        updateAddressButton = findViewById(R.id.updateAddressButton);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

        isNetworkConnected();
        savePref = new SavePref(AddAddress.this);
        if(savePref.getLang() == null)
            savePref.setLang("en");

        if(Objects.equals(savePref.getLang(), "en"))
            setLocale("");
        else
            setLocale(savePref.getLang());


        if (getIntent().hasExtra("address_id")) {
            // Update address scenario
            addressId = getIntent().getStringExtra("address_id");
            houseNoEditText.setText(getIntent().getStringExtra("address_line1"));
            areaEditText.setText(getIntent().getStringExtra("address_line2"));
            cityEditText.setText(getIntent().getStringExtra("city"));
            stateEditText.setText(getIntent().getStringExtra("state"));
            postalCodeEditText.setText(getIntent().getStringExtra("postal_code"));
            countryEditText.setText(getIntent().getStringExtra("country"));
            selectedOption = getIntent().getStringExtra("address_type");

            updateAddressButton.setText("Update Address");
        }
        if (addressId != null) {
            handleAddressType(selectedOption);

        }


        updateAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    if (addressId != null) {
                        updateAddress();
                    } else {
                        saveaddress();
                    }
                }
            }
        });
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                updateLayout(homeTxt, homeImg, home);
                resetLayout(workTxt, workImg, work);
                resetLayout(otherTxt, otherImg, other);
                selectedOption = "Home";
            }
        });

        workTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                resetLayout(homeTxt, homeImg, home);
                updateLayout(workTxt, workImg, work);
                resetLayout(otherTxt, otherImg, other);
                selectedOption = "Work";
            }
        });

        otherTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                resetLayout(homeTxt, homeImg, home);
                resetLayout(workTxt, workImg, work);
                updateLayout(otherTxt, otherImg, other);
                selectedOption = "Other";
                showNicknameDialog();
            }
        });

    }
    public void saveaddress(){
        callAddAddress().enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {

                    AddressResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getMsg() != null && !apiResponse.getMsg().isEmpty()) {
                        Toast.makeText(AddAddress.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAddress.this, "Address added successfully", Toast.LENGTH_SHORT).show();
                    }

                  onBackPressed();
                }
                else {
                    Toast.makeText(AddAddress.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(AddAddress.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateAddress() {
        callUpdateAddress().enqueue(new Callback<UpdateAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                if (response.isSuccessful()) {
                    UpdateAddressResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getJSON_DATA().get(0).getMsg() != null && !apiResponse.getJSON_DATA().get(0).getMsg().isEmpty()) {
                        Toast.makeText(AddAddress.this, apiResponse.getJSON_DATA().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAddress.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    onBackPressed();
                } else {
                    Toast.makeText(AddAddress.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                Toast.makeText(AddAddress.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<AddressResponse> callAddAddress() {
        String userId = savePref.getUserId();
        String houseNo = houseNoEditText.getText().toString().trim();
        String area = areaEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String postalCode = postalCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();

        return videoService.addAddress(userId, houseNo, area, city, state, postalCode, country,selectedOption,nickname);
    }
    private Call<UpdateAddressResponse> callUpdateAddress() {
        String userId = savePref.getUserId();
        String houseNo = houseNoEditText.getText().toString().trim();
        String area = areaEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String postalCode = postalCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();

        return videoService.updateAddress(addressId,nickname, userId, houseNo, area, city, state, postalCode, country, selectedOption, true);
    }

    private void showNicknameDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_nickname, null);
        final EditText nicknameEditText = dialogView.findViewById(R.id.nickname_edit_text);
        Button submitButton = dialogView.findViewById(R.id.nickname_submit_button);
        TextView nicknamePrompt = dialogView.findViewById(R.id.nickname_prompt);

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Enter Nickname");

        final AlertDialog dialog = builder.create();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname1 = nicknameEditText.getText().toString().trim();
                if (!nickname.isEmpty()) {
                    nickname=nickname1;
                    dialog.dismiss();
                } else {
                    Toast.makeText(AddAddress.this, "Nickname cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private boolean checkFields() {
        if (isEmpty(houseNoEditText)) {
            showToast("House Number");
            return false;
        } else if (isEmpty(areaEditText)) {
            showToast("Area");
            return false;
        } else if (isEmpty(cityEditText)) {
            showToast("City");
            return false;
        } else if (isEmpty(stateEditText)) {
            showToast("State");
            return false;
        } else if (isEmpty(postalCodeEditText)) {
            showToast("Postal Code");
            return false;
        } else if (isEmpty(countryEditText)) {
            showToast("Country");
            return false;
        }
        return true;
    }
    private void updateLayout(RelativeLayout layout, ImageView imageView, TextView textView) {
        layout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
        imageView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));

    }

    private void resetLayout(RelativeLayout layout, ImageView imageView, TextView textView) {
        layout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        imageView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        textView.setTextColor(ContextCompat.getColor(this, R.color.defaultTextColor));
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork == null || !activeNetwork.isConnected()) {
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void handleAddressType(String selectedOption) {
        switch (selectedOption) {
            case "Home":
                updateLayout(homeTxt, homeImg, home);
                resetLayout(workTxt, workImg, work);
                resetLayout(otherTxt, otherImg, other);
                break;
            case "Work":
                resetLayout(homeTxt, homeImg, home);
                updateLayout(workTxt, workImg, work);
                resetLayout(otherTxt, otherImg, other);
                break;
            case "Other":
                resetLayout(homeTxt, homeImg, home);
                resetLayout(workTxt, workImg, work);
                updateLayout(otherTxt, otherImg, other);
                break;
            default:
                // Handle any default case or error
                break;
        }
    }


    private void showToast(String fieldName) {
        Toast.makeText(this, fieldName + " cannot be empty", Toast.LENGTH_SHORT).show();
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }
}