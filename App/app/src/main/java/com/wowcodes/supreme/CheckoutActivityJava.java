/**
 * The CheckoutActivityJava class is responsible for handling the checkout process and making payments
 * using the Stripe payment gateway in an Android application.
 */
package com.wowcodes.supreme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.wowcodes.supreme.Activity.GetOrderActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class CheckoutActivityJava extends AppCompatActivity {

    // 10.0.2.2 is the Android emulator's alias to localhost
    // TODO: Enter the url where you have stored the stripe folder ;)
    private static final String BACKEND_URL =
            "https://wowcodes.shop/test/Stripe/index.php";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    EditText edtEmail;
    TextView bottomtxt;
    String email,O_id,aamount,pgId;
    Integer amount;
    ImageView imgBack;
    TextView txtTitle;
    public BindingService videoService;
    SavePref savePref;
    String oId="", packageId, getWallet,old, userId, o_id;
    GetCoin.Get_Coin_Inner coin_inner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        imgBack=(ImageView)findViewById(R.id.imgBackk);
        txtTitle=(TextView)findViewById(R.id.txtAucname);
        savePref=new SavePref(CheckoutActivityJava.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        packageId = getIntent().getStringExtra("packageid");
        if (getIntent().hasExtra("pgId") ) {
            pgId=getIntent().getStringExtra("pgId");
        }
        if ((!Objects.equals(packageId, "CategoryDetailsAct")&& !(Objects.equals(packageId, "0")))){
            coin_inner = (GetCoin.Get_Coin_Inner) getIntent().getSerializableExtra("list");
            getWallet = coin_inner.getC_coin();
            packageId = coin_inner.getC_id();
        }
        amount = getIntent().getIntExtra("plan_price",0);
        if (Objects.equals(packageId, "0")){
            getWallet = String.valueOf(Integer.parseInt(String.valueOf(MainActivity.coinvalue)) * Integer.parseInt(String.valueOf(amount)));
        }
        stripe = new Stripe(
                getApplicationContext(), // TODO: Change the Stripe Payment Credentials bellow ;)
                ""+MainActivity.stripe
        );
        startCheckout();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //
        amount = getIntent().getIntExtra("plan_price",0);
        aamount = String.valueOf(amount);
        O_id = getIntent().getStringExtra("o_id");
        email = getIntent().getStringExtra("email");
        txtTitle.setText(R.string.string215);

        Log.e("AMT",amount+"");
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        //   payMap.put("currency","inr"); //dont change currency in testing phase otherwise it won't work
        // itemMap.put("id", "photo_subscription");
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        //   payMap.put("items", itemList);
        payMap.put("amount",  amount);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(BACKEND_URL)
                .post(body)
                .build();

        System.out.println("Stripe req"+request.body());
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        // builder.setPositiveButton("Ok", null);
        // builder.create().show();
        builder .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                //your deleting code

                dialog.dismiss();
                finish();
                startActivity(new Intent(CheckoutActivityJava.this, MainActivity.class));
            }

        }).create().show();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );

        Log.e("Gson",gson.toJson(response.body()));
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }
    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()

            );

            Log.e("Error",e.getLocalizedMessage());

        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );

                Log.e("Error",response.toString());

            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }
    private  final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
               /* activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)
                );*/

                activity.displayAlert(
                        "Payment Status",
                        "Payment Done Successfully!!"
                );
                if ((Objects.equals(packageId, "CategoryDetailsAct")))
                {
                    addorder(email);
                }
                else {
                    postUserwalletUpdate();
                }


            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
               /* activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );*/

                activity.displayAlert(
                        "Payment Status",
                        "Payment Failed!!"
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

    public void postUserwalletUpdate() {
        // lvlRazorpayActi.setVisibility(View.VISIBLE);
        try {
            calladdbidApi().enqueue(new retrofit2.Callback<SuccessModel>() {
                @Override
                public void onResponse(retrofit2.Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {

                    try {

                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(CheckoutActivityJava.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();


                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            Intent i=new Intent(CheckoutActivityJava.this, MainActivity.class);
                            startActivity(i);
                        }else{
                            //lvlRazorpayActi.setVisibility(View.GONE);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        //lvlRazorpayActi.setVisibility(View.GONE);

                    }


                }

                @Override
                public void onFailure(retrofit2.Call<SuccessModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private retrofit2.Call<SuccessModel> calladdbidApi() {


        return videoService.postUserwalletUpdate(savePref.getUserId(), getWallet, packageId, oId,aamount);
    }


    public void addorder(String s) {
        //lvlOrderDetail.setVisibility(View.VISIBLE);
        try {
            calladdorderApi().enqueue(new retrofit2.Callback<AddOrder>() {
                @Override
                public void onResponse(retrofit2.Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {

                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(CheckoutActivityJava.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();


                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1")) {
                            openconfirmorderdialog();
                        } else {
                            //lvlOrderDetail.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // lvlOrderDetail.setVisibility(View.GONE);

                    }


                }

                @Override
                public void onFailure(retrofit2.Call<AddOrder> call, Throwable t) {
                    //lvlOrderDetail.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private retrofit2.Call<AddOrder> calladdorderApi() {


        Log.e("values", O_id + "::" + amount + "::" + email + "::" + amount);
        return videoService.add_order(savePref.getUserId(), O_id, aamount, "", aamount, email,"",pgId);

    }
    private void openconfirmorderdialog() {

        final Dialog dialog = new Dialog(CheckoutActivityJava.this);
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
                Intent i = new Intent(CheckoutActivityJava.this, GetOrderActivity.class);
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