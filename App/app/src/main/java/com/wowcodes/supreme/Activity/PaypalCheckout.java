package com.wowcodes.supreme.Activity;
import static com.wowcodes.supreme.Constants.PAYPAL_BASE_URL;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

public class PaypalCheckout extends AppCompatActivity {
    TextView orderID_label,amt;
    Button confirm_btn;
    String orderID,pgId;
    BindingService videoService;
    String[] strArray = Paypalpay.senduserdata();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_checkout);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        Uri redirectUri = getIntent().getData();
        orderID = redirectUri.getQueryParameter("token");
        orderID_label = (TextView) findViewById(R.id.orderID);
        amt = (TextView) findViewById(R.id.amt);
        pgId=getIntent().getStringExtra("pgId");
        orderID_label.setText(getText(R.string.string19) + orderID);
        amt.setText(getText(R.string.string20) +MainActivity.currency +strArray[4]);
        captureOrder(orderID);
        if(Objects.equals(strArray[2], "0"))
            strArray[1] = String.valueOf(Integer.parseInt(String.valueOf(MainActivity.coinvalue)) * Integer.parseInt(String.valueOf(amt)));
        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PaypalCheckout.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    void captureOrder(String orderID){
        String accessToken = Paypalpay.getMyAccessToken();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);
        client.post(PAYPAL_BASE_URL+"/v2/checkout/orders/"+orderID+"/capture", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Toast.makeText(PaypalCheckout.this, R.string.string140, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    if(Objects.equals(strArray[2], "CategoryDetailsAct"))
                        addorder();
                    else
                        postUserwalletUpdate();
                } catch (Exception ignore) {}
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
                        Toast.makeText(PaypalCheckout.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1"))
                            Toast.makeText(PaypalCheckout.this, R.string.string139, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(PaypalCheckout.this, R.string.string140, Toast.LENGTH_SHORT).show();
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<SuccessModel> calladdbidApi() {
        return videoService.postUserwalletUpdate(strArray[0], strArray[1], strArray[2], orderID, String.valueOf(amt));
    }

    public void addorder() {
        try {
            calladdorderApi().enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {
                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(PaypalCheckout.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1"))
                            openconfirmorderdialog();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<AddOrder> calladdorderApi() {
        return videoService.add_order(strArray[0], strArray[5], strArray[4], "", strArray[4], strArray[6],"",pgId);
    }
    private void openconfirmorderdialog() {
        final Dialog dialog = new Dialog(PaypalCheckout.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_orderconfirmed);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout layout =  dialog.findViewById(R.id.layoutmypurchases);
        LinearLayout ratinglayout = (LinearLayout) dialog.findViewById(R.id.layoutrateapp);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(PaypalCheckout.this, GetOrderActivity.class);
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