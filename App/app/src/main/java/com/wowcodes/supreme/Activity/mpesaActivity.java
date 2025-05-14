package com.wowcodes.supreme.Activity;
import static com.wowcodes.supreme.Constants.BUSINESS_SHORT_CODE;
import static com.wowcodes.supreme.Constants.CALLBACKURL;
import static com.wowcodes.supreme.Constants.TRANSACTION_TYPE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.Utils;
import com.wowcodes.supreme.model.AccessToken;
import com.wowcodes.supreme.model.STKPush;
import com.wowcodes.supreme.model.STKPushQuery;
import com.wowcodes.supreme.services.DarajaApiClient;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class mpesaActivity extends AppCompatActivity implements View.OnClickListener {
    BindingService videoService;
    SavePref savePref;
    String aamount, wallet, packageid, old, amountrupee, timestampresponse, message,email,O_id,pgId;
    EditText mAmount;
    EditText mPhone;
    TextView txtMpesa;
    String referenceID;
    EditText edtEmail;
    TextView bottomtxt;
    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    Button buyItBtnM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        mAmount = (EditText) findViewById(R.id.amountedit);
        mPhone = (EditText) findViewById(R.id.MpesaMobile);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(mpesaActivity.this);
        EditText amte = findViewById(R.id.amountedit);
        aamount = getIntent().getStringExtra("amt");
        wallet = getIntent().getStringExtra("wallet");
        packageid = getIntent().getStringExtra("packageid");
        old = getIntent().getStringExtra("packageid");
        O_id = getIntent().getStringExtra("o_id");
        pgId = getIntent().getStringExtra("pgId");

        email = getIntent().getStringExtra("email");
        if (Objects.equals(packageid, "0"))
            try {
                // Parse coinvalue and aamount as floats or doubles
                float coinValue = Float.parseFloat(String.valueOf(MainActivity.coinvalue));
                float amount = Float.parseFloat(String.valueOf(aamount));

                // Perform the multiplication
                float result = coinValue * amount;

                // Convert the result to a string
                wallet = String.valueOf(result);
            } catch (NumberFormatException e) {
                // Handle the exception
                e.printStackTrace();
                // Optionally, set a default value or notify the user
                wallet = "0.0";
            }
        amte.setText(getText(R.string.string21) +":  "+MainActivity.currency + aamount);
        amte.setClickable(false);
        amte.setEnabled(false);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true);
        buyItBtnM = (Button) findViewById(R.id.buyItBtnM);//Set True to enable logging, false to disable.
        buyItBtnM.setOnClickListener(this);
        getAccessToken();
    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful())
                    mApiClient.setAuthToken(response.body().accessToken);
                else
                    assert response.errorBody() != null;
            }

            @Override public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {}
        });
    }



    @Override
    public void onClick(View view) {
        if (view == buyItBtnM) {
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            if(phone_number.isEmpty())
                Toast.makeText(mpesaActivity.this, R.string.string168, Toast.LENGTH_SHORT).show();
            else
                performSTKPush(phone_number, amount);
        }
    }

    public void performSTKPush(String phone_number, String amount) {
        mProgressDialog.setMessage(getText(R.string.string149));
        mProgressDialog.setTitle(R.string.string147);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        timestampresponse = timestamp;
        STKPush stkPush = new STKPush(BUSINESS_SHORT_CODE, Utils.getPassword(BUSINESS_SHORT_CODE, MainActivity.mpesa_key, timestamp), timestamp, TRANSACTION_TYPE, String.valueOf(amount), Utils.sanitizePhoneNumber(phone_number), MainActivity.mpesa_code, Utils.sanitizePhoneNumber(phone_number), CALLBACKURL, "CompanyXLTD", "Payment of X");
        mApiClient.setGetAccessToken(false);
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush.Mpesa_Push_Response>() {
            @Override
            public void onResponse(@NonNull Call<STKPush.Mpesa_Push_Response> call, @NonNull retrofit2.Response<STKPush.Mpesa_Push_Response> response) {
                try {
                    if (response.isSuccessful()) {
                        message = (String) getText(R.string.string141);
                        openmpesadialog();
                        referenceID = response.body().getCheckoutRequestID();
                        mProgressDialog.setMessage(getText(R.string.string142));
                        long longTime1 = System.currentTimeMillis();
                        Runnable runnable = new Runnable() { //New Thread
                            @Override
                            public void run() {
                                long longTime2 = System.currentTimeMillis();
                                performSTKPushQuery(referenceID);
                            }
                        };
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(runnable, 30000);
                        Timber.d("post submitted to API. %s", response.body());
                    }
                    else if (response.errorBody().string().contains("Invalid PhoneNumber")) {
                        message = (String) getText(R.string.string143);
                        openmpesadialog();
                        mProgressDialog.dismiss();
                    }
                    else {
                        message = (String) getText(R.string.string140);
                        openmpesadialog();
                        mProgressDialog.dismiss();
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception ignore) {}
            }

            @Override
            public void onFailure(@NonNull Call<STKPush.Mpesa_Push_Response> call, @NonNull Throwable t) {
                message = (String) getText(R.string.string140);
                openmpesadialog();
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    @Override public void onPointerCaptureChanged(boolean hasCapture) {}

    private void openmpesadialog() {
        final Dialog dialog = new Dialog(mpesaActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.mpesa_success);
        Window window = dialog.getWindow();
        txtMpesa = (TextView) dialog.findViewById(R.id.txtMpesa);
        txtMpesa.setText(message);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.okaybtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void performSTKPushQuery(String referenceID) {
        String timestamp = Utils.getTimestamp();
        STKPushQuery stkPushQuery = new STKPushQuery(BUSINESS_SHORT_CODE, Utils.getPassword(BUSINESS_SHORT_CODE, MainActivity.mpesa_key, timestamp), timestamp, referenceID);
        mApiClient.setGetAccessToken(false);
        mApiClient.mpesaQueryService().sendPushQuery(stkPushQuery).enqueue(new Callback<STKPushQuery.Mpesa_Push_Response>() {
            @Override
            public void onResponse(@NonNull Call<STKPushQuery.Mpesa_Push_Response> call, @NonNull Response<STKPushQuery.Mpesa_Push_Response> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getResultCode().equalsIgnoreCase("0")) {
                            Timber.d("post submitted to API. %s", response.body());
                            message = (String) getText(R.string.string139);
                            openmpesadialog();
                            if(Objects.equals(packageid, "CategoryDetailsAct"))
                                addorder();
                            else
                                postUserwalletUpdate();
                        } else {
                            message = (String) getText(R.string.string140);
                            openmpesadialog();
                            mProgressDialog.dismiss();
                            Timber.e("Response %s", response.errorBody().string());
                        }
                    }

                    else
                    if(response.errorBody().string().contains("The transaction is being processed"))
                        performSTKPushQuery(referenceID);
                    else{
                        message = (String) getText(R.string.string140);
                        openmpesadialog();
                        mProgressDialog.dismiss();
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception ignore) {}
            }

            @Override
            public void onFailure(@NonNull Call<STKPushQuery.Mpesa_Push_Response> call, @NonNull Throwable t) {
                message = (String) getText(R.string.string140);
                openmpesadialog();
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    public void postUserwalletUpdate() {
        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(mpesaActivity.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            Intent i=new Intent(mpesaActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    private Call<SuccessModel> calladdbidApi() {
        return videoService.postUserwalletUpdate(savePref.getUserId(), wallet, packageid, referenceID,aamount);
    }

    public void addorder() {
        try {
            calladdorderApi().enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {
                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(mpesaActivity.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1")) {
                            openconfirmorderdialog();
                        }
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<AddOrder> calladdorderApi() {
        return videoService.add_order(savePref.getUserId(), O_id, aamount, "", aamount, email,"",pgId);
    }
    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(mpesaActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_orderconfirmed);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.layoutmypurchases);
        LinearLayout ratinglayout = (LinearLayout) dialog.findViewById(R.id.layoutrateapp);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(mpesaActivity.this, GetOrderActivity.class);
                startActivity(i);
            }
        });

        ratinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}