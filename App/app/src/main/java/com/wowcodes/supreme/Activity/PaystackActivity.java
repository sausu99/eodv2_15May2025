package com.wowcodes.supreme.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Objects;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Call;
import retrofit2.Callback;

public class PaystackActivity extends AppCompatActivity {
    private Card card;
    private Charge charge;
    ImageView imgBackk;
    private EditText emailField;
    private EditText cardNumberField;
    private EditText expiryMonthField;
    private EditText expiryYearField;
    private EditText cvvField;
    private String email, cardNumber, cvv,pgId;
    private int expiryMonth, expiryYear;
    String cemail;
    TextView txtMpesa;
    public BindingService videoService;
    SavePref savePref;
    String aamount, wallet, packageid, old,message,referenceID,O_id;
    TextView txtAucname;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new SavePref(PaystackActivity.this);
        super.onCreate(savedInstanceState);
        PaystackSdk.initialize(getApplicationContext());
        setContentView(R.layout.activity_paystack);
        mProgressDialog = new ProgressDialog(this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        EditText amte = findViewById(R.id.amountedit);
        aamount = getIntent().getStringExtra("amt");
        wallet = getIntent().getStringExtra("wallet");
        packageid = getIntent().getStringExtra("packageid");
        old = getIntent().getStringExtra("packageid");
        pgId = getIntent().getStringExtra("pgId");
        O_id = getIntent().getStringExtra("o_id");
        cemail = getIntent().getStringExtra("email");
        amte.setText(getText(R.string.string21) + MainActivity.currency + " " + aamount);
        amte.setClickable(false);
        amte.setEnabled(false);
        Button payBtn = (Button) findViewById(R.id.pay_button);
        if (Objects.equals(packageid, "0")) {
            wallet = String.valueOf((int) (Float.parseFloat(String.valueOf(MainActivity.coinvalue)) * Float.parseFloat(aamount)));
        }
        emailField = (EditText) findViewById(R.id.edit_email_address);
        cardNumberField = (EditText) findViewById(R.id.edit_card_number);
        expiryMonthField = (EditText) findViewById(R.id.edit_expiry_month);
        expiryYearField = (EditText) findViewById(R.id.edit_expiry_year);
        cvvField = (EditText) findViewById(R.id.edit_cvv);
        txtAucname = findViewById(R.id.txtAucname);
        txtAucname.setText(R.string.string215);
        imgBackk = findViewById(R.id.imgBackk);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                try {
                    email = emailField.getText().toString().trim();
                    cardNumber = cardNumberField.getText().toString().trim();
                    expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
                    expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
                    cvv = cvvField.getText().toString().trim();
                    card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
                    if (card.isValid()) {
                        mProgressDialog.setMessage(getText(R.string.string147));
                        mProgressDialog.setTitle(R.string.string148);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.show();
                        performCharge();
                    } else {
                        message = "Invalid card details. Please try again !!";
                        opendialog();
                    }
                } catch (Exception ignore) {
                }
            }
        });
    }

    private void performCharge() {
        charge = new Charge();
        charge.setCard(card);
        charge.setEmail(email); //dummy email address
        charge.setAmount(Integer.parseInt(aamount)*100);
        PaystackSdk.chargeCard(PaystackActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                String paymentReference = transaction.getReference();
                referenceID = paymentReference;
                message = "Transaction Successful !! Reference ID: "+paymentReference;
                mProgressDialog.dismiss();
                if ((Objects.equals(packageid, "CategoryDetailsAct")))
                    addorder();
                else {
                    opendialog();
                    postUserwalletUpdate();
                }
            }
            @Override public void beforeValidate(Transaction transaction) {}

            @Override
            public void onError(Throwable error, Transaction transaction) {
                message = "Transaction Failure !! "+error;
                message = message.replace("co.paystack.android.exceptions.ChargeException: ","");
                mProgressDialog.dismiss();
                opendialog();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailField.setError("Required.");
            valid = false;
        }else
            emailField.setError(null);

        if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            emailField.setError("Invalid Email");
            valid = false;
        }
        String cardNumber = cardNumberField.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberField.setError("Required.");
            valid = false;
        } else
            cardNumberField.setError(null);

        String expiryMonth = expiryMonthField.getText().toString();
        if (TextUtils.isEmpty(expiryMonth)) {
            expiryMonthField.setError("Required.");
            valid = false;
        } else
            expiryMonthField.setError(null);

        String expiryYear = expiryYearField.getText().toString();
        if (TextUtils.isEmpty(expiryYear)) {
            expiryYearField.setError("Required.");
            valid = false;
        } else
            expiryYearField.setError(null);

        String cvv = cvvField.getText().toString();
        if (TextUtils.isEmpty(cvv)) {
            cvvField.setError("Required.");
            valid = false;
        } else
            cvvField.setError(null);

        return valid;
    }
    private void opendialog() {
        final Dialog dialog = new Dialog(PaystackActivity.this);
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

    public void postUserwalletUpdate() {
        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(PaystackActivity.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            Intent i=new Intent(PaystackActivity.this, MainActivity.class);
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
                        Toast.makeText(PaystackActivity.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1"))
                            openconfirmorderdialog();
                    } catch (Exception ignore) {}
                }
                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
    private Call<AddOrder> calladdorderApi() {
        return videoService.add_order(savePref.getUserId(), O_id, aamount, "", aamount, cemail,"",pgId);
    }
    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(PaystackActivity.this);
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
                Intent i = new Intent(PaystackActivity.this, GetOrderActivity.class);
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