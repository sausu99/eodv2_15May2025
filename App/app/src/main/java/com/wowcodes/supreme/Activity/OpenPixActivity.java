package com.wowcodes.supreme.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.ChargeResponse;
import com.wowcodes.supreme.RetrofitUtils.OpenFixHandler;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.databinding.ActivityOpenPixBinding;

import com.wowcodes.supreme.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenPixActivity extends AppCompatActivity {
    private ActivityOpenPixBinding binding;
    private ChargeResponse chargeResponse;
    private ImageView imageView;
    private OpenFixHandler apiHandler;
    private static String amount, wallet, packageid, old, email;
    BindingService videoService;
    String O_id,pgId;
    SavePref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new SavePref(OpenPixActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityOpenPixBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("OpenPix Payment");
        savePref = new SavePref(OpenPixActivity.this);

        apiHandler = new OpenFixHandler();
        imageView = binding.qrCode;
        email = "openfix@ok";
        if (getIntent().hasExtra("pgId")) {
            pgId=getIntent().getStringExtra("pgId").trim();
        }
        O_id = getIntent().getStringExtra("o_id");
        EditText amte = binding.amountOpenPix;
        amount = getIntent().getStringExtra("amt");
        wallet = getIntent().getStringExtra("wallet");
        packageid = getIntent().getStringExtra("packageid");
        old = getIntent().getStringExtra("packageid");
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        amte.setText(getText(R.string.string21)  + " "+ MainActivity.currency + " " + amount);
        amte.setClickable(false);
        amte.setEnabled(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.UK);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HHmmss", Locale.UK);
        String dateTime = dateFormat.format(new Date());
        String dateTime2 = dateFormat2.format(new Date());
        String coId = dateTime+dateTime2;
        O_id = coId;
        int currInBrl = (int) (Integer.parseInt(amount)*5.8);
        binding.payOpen.setOnClickListener(view ->{
            binding.progressBarOpenFix.setVisibility(View.VISIBLE);
            binding.payOpen.setVisibility(View.GONE);
            apiHandler.createCharge(coId, currInBrl, "my-first-charge", new Callback<ChargeResponse>() {
                @Override
                public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                    if (response.isSuccessful()) {
                        chargeResponse = response.body();
                        binding.brText.setText("BR Code:  "+ chargeResponse.getCharge().getBrCode());
                        binding.copyBr.setOnClickListener(view ->{
                            ClipboardManager clipboardManager = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                Toast.makeText(getApplicationContext(), "Copy", Toast.LENGTH_SHORT).show();
                                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("text", chargeResponse.getCharge().getBrCode());
                                clipboardManager.setPrimaryClip(clipData);
                            }

                        });
                        Glide.with(getApplicationContext())
                                .load(chargeResponse.getCharge().getQrCodeImage())
                                .into(imageView);
                        binding.progressBarOpenFix.setVisibility(View.GONE);
                        binding.lnlDetails.setVisibility(View.VISIBLE);
                    } else
                        binding.payOpen.setVisibility(View.VISIBLE);
                }
                @Override
                public void onFailure(Call<ChargeResponse> call, Throwable t) {
                    binding.payOpen.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Something Went Wrong: RETRY", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.doneButton.setOnClickListener(view -> {
            String chargeId = chargeResponse.getCharge().getCorrelationID();
            // Call getCharge method to get charge details using chargeId
            apiHandler.getCharge(chargeId, new Callback<ChargeResponse>() {
                @Override
                public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                    if (response.isSuccessful()) {
                        ChargeResponse chargeResponse2 = response.body();
                        String status = chargeResponse2.getCharge().getStatus();
                        if ("ACTIVE".equals(status)) {
                            Toast.makeText(getApplicationContext(), "Please Make Payment", Toast.LENGTH_SHORT).show();
                        } else if ("COMPLETED".equals(status)) {
                            Toast.makeText(getApplicationContext(), "Payment successful", Toast.LENGTH_SHORT).show();
                            if ((Objects.equals(packageid, "CategoryDetailsAct")))
                                addorder(email);
                            else
                                postUserwalletUpdate();

                            finish();
                            finish();
                        }else
                            Toast.makeText(getApplicationContext(), "Expired", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(), "RETRY", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ChargeResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "RETRY", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.closeButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Go Back");
            builder.setMessage("Are you sure you want to go back?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Toast.makeText(getApplicationContext(), "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
                    finish();
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
    }
    public void postUserwalletUpdate() {
        try {
            calladdbidApi().enqueue(new retrofit2.Callback<SuccessModel>() {
                @Override
                public void onResponse(retrofit2.Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(OpenPixActivity.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            Intent i=new Intent(OpenPixActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(retrofit2.Call<SuccessModel> call, Throwable t){}
            });
        } catch (Exception ignore) {}
    }

    private retrofit2.Call<SuccessModel> calladdbidApi() {
        return videoService.postUserwalletUpdate(savePref.getUserId(), wallet, packageid, O_id,amount);
    }

    public void addorder(String s) {
        try {
            calladdorderApi().enqueue(new retrofit2.Callback<AddOrder>() {
                @Override
                public void onResponse(retrofit2.Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {
                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(OpenPixActivity.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1"))
                            openconfirmorderdialog();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(retrofit2.Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
    private retrofit2.Call<AddOrder> calladdorderApi() {
        return videoService.add_order(savePref.getUserId(), O_id, amount, "", amount, email,"",pgId);
    }
    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(OpenPixActivity.this);
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
                Intent i = new Intent(OpenPixActivity.this, GetOrderActivity.class);
                i.putExtra("page", "1");
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