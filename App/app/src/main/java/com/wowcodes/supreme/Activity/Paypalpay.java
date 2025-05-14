package com.wowcodes.supreme.Activity;
import static com.wowcodes.supreme.Constants.PAYPAL_BASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
public class Paypalpay extends Activity {
    BindingService videoService;
    private SavePref savePref;
    private static String amount, wallet, packageid, old, userId, o_id, email;
    private static String accessToken;

    public static String getMyAccessToken() {
        return accessToken;
    }

    public static String[] senduserdata() {
        String[] strArray = new String[7];
        strArray[0] = userId;
        strArray[1] = wallet;
        strArray[2] = packageid;
        strArray[3] = old;
        strArray[4] = amount;
        strArray[5] = o_id;
        strArray[6] = email;
        return strArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new SavePref(Paypalpay.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypalpay);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(Paypalpay.this);
        userId = savePref.getUserId();
        email = getIntent().getStringExtra("email");
        o_id = getIntent().getStringExtra("o_id");
        EditText amte = findViewById(R.id.amountedit);
        amount = getIntent().getStringExtra("amt");
        wallet = getIntent().getStringExtra("wallet");
        packageid = getIntent().getStringExtra("packageid");
        old = getIntent().getStringExtra("packageid");

        amte.setText(getText(R.string.string21) +": "+ MainActivity.currency + amount);
        amte.setClickable(false);
        amte.setEnabled(false);
        Button payPalButton;
        payPalButton = (Button) findViewById(R.id.buyItBtn);
        getAccessToken();
        payPalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
    }

    String encodeStringToBase64() {
        String input = MainActivity.paypal_id + ":" + MainActivity.paypal_secret;
        String encode = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            encode = Base64.getEncoder().encodeToString(input.getBytes());
        return encode;
    }

    void getAccessToken() {
        String AUTH = encodeStringToBase64();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/x-www-form-urlencoded");
        client.addHeader("Authorization", "Basic " + AUTH);
        String jsonString = "grant_type=client_credentials";
        HttpEntity entity = new StringEntity(jsonString, "utf-8");
        client.post(this, PAYPAL_BASE_URL + "/v1/oauth2/token", entity, "application/x-www-form-urlencoded", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    accessToken = jobj.getString("access_token");
                } catch (JSONException ignore) {
                }
            }
        });
    }

    void createOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);
        String order = "{" + "\"intent\": \"CAPTURE\"," + "\"purchase_units\": [\n" + "      {\n" + "        \"amount\": {\n" + "          \"currency_code\": \"" + MainActivity.paypal_currency + "\",\n" + "          \"value\": \"" + amount + "\"\n" + "        }\n" + "      }\n" + "    ],\"application_context\": {\n" + "        \"brand_name\": \"PRIZEX\",\n" + "        \"return_url\": \"https://wowcodes.shop/raffle/paypal/checkout.php\",\n" + "        \"cancel_url\": \"https://wowcodes.shop/raffle/paypal/checkout.php\"\n" + "    }}";
        HttpEntity entity = new StringEntity(order, "utf-8");
        client.post(this, PAYPAL_BASE_URL + "/v2/checkout/orders", entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONArray links = new JSONObject(response).getJSONArray("links");
                    for (int i = 0; i < links.length(); ++i) {
                        String rel = links.getJSONObject(i).getString("rel");
                        if (rel.equals("approve")) {
                            String link = links.getJSONObject(i).getString("href");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            startActivity(browserIntent);
                        }
                    }
                } catch (JSONException ignore) {
                }
            }
        });
    }
}