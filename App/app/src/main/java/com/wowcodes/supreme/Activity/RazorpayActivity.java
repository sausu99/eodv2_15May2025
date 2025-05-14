package com.wowcodes.supreme.Activity;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flutterwave.raveandroid.RavePayActivity;

import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.Gopay;
import com.midtrans.sdk.corekit.models.snap.Shopeepay;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import com.wowcodes.supreme.Adapter.PGAdapter;
import com.wowcodes.supreme.CheckoutActivityJava;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.GetPaymentGateway;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {
    LottieAnimationView loader;
    ImageView imgBackk,imageview;
    BindingService videoService;

    SavePref savePref;
    String oId, packageId, getWallet,curr_activity,oAmt,olink,oname,O_id,address,address_Id,pgId;
    TextView txtAucname,txtSetName,txtGetCoin,txtAmount,cointext;
    String email;
    GetCoin.Get_Coin_Inner coin_inner;
    String payumoneykey="uTK7XFZC";
    public static final String MONEY_HASHPAYUMONEY = "https://wowcodes.in/payumoney.php";
    WebView webView;
    Button buttonPayment;
    String demo_access;
    RecyclerView recyclerView;
    PGAdapter pgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_razorpay);

        checkCertificte();
        buttonPayment = findViewById(R.id.buttonPayment);
        loader=findViewById(R.id.loader);
        imgBackk=findViewById(R.id.imgBackk);
        imageview=findViewById(R.id.imageview);
        txtAucname=findViewById(R.id.txtAucname);
        txtSetName=findViewById(R.id.txtSetName);
        txtGetCoin=findViewById(R.id.txtGetCoin);
        cointext=findViewById(R.id.cointext);
        txtAmount=findViewById(R.id.txtAmount);
        recyclerView=findViewById(R.id.pgs_rv);
        address=getIntent().getStringExtra("address");
        address_Id=getIntent().getStringExtra("address_Id");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtAucname.setText(R.string.string138);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        savePref=new SavePref(RazorpayActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        coin_inner= (GetCoin.Get_Coin_Inner) getIntent().getSerializableExtra("list");
        curr_activity = getIntent().getStringExtra("activity");
        oAmt = getIntent().getStringExtra("amount");
        System.out.println("kaja curr"+curr_activity);
        getsetting();

        if (Objects.equals(curr_activity, "CoinAdapter")) {
            txtSetName.setText(coin_inner.getC_name());
            txtGetCoin.setText(coin_inner.getC_coin());
            txtAmount.setText(" " + MainActivity.currency + " " + coin_inner.getC_amt());
            getWallet = coin_inner.getC_coin();
            packageId = coin_inner.getC_id();
            oAmt = coin_inner.getC_amt();
            try {
                Glide.with(RazorpayActivity.this)
                        .load(coin_inner.getC_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(imageview);
            } catch (Exception ignore) {}
        }
        else if(Objects.equals(curr_activity, "PurchaseCoin")){
            txtAmount.setText(" " + MainActivity.currency + " " +oAmt);
            txtSetName.setText(R.string.manual);
            O_id = getIntent().getStringExtra("O_id");
            txtGetCoin.setVisibility(GONE);
            cointext.setVisibility(VISIBLE);
            //int totalcoins = Integer.parseInt(String.valueOf(MainActivity.coinvalue));
            //totalcoins = totalcoins * Integer.parseInt(String.valueOf(oAmt));
            cointext.setText("  "+getIntent().getStringExtra("coins")+" "+getText(R.string.string118));
            packageId = "0";
            getWallet = String.valueOf(Double.parseDouble(String.valueOf(MainActivity.coinvalue)) * Double.parseDouble(String.valueOf(oAmt)));
        }
        else{
            txtAmount.setText(" " + MainActivity.currency + " " +oAmt);
            olink = getIntent().getStringExtra("link");
            oname = getIntent().getStringExtra("name");
            O_id = getIntent().getStringExtra("O_id");
            email = getIntent().getStringExtra("email");
            txtSetName.setText(oname);
            txtGetCoin.setVisibility(GONE);
            cointext.setVisibility(GONE);
            try {
                Glide.with(RazorpayActivity.this)
                        .load(olink)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(imageview);
            } catch (Exception ignore) {}
            packageId = curr_activity;
        }
        loader.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);

        videoService.get_payment_gateway().enqueue(new Callback<GetPaymentGateway>() {
            @Override
            public void onResponse(Call<GetPaymentGateway> call, Response<GetPaymentGateway> response) {
                ArrayList<GetPaymentGateway.Get_PG_Inner> arrayList=response.body().getJSON_DATA();
                loader.setVisibility(GONE);
                pgAdapter=new PGAdapter(RazorpayActivity.this,arrayList);
                recyclerView.setAdapter(pgAdapter);
                recyclerView.setVisibility(VISIBLE);
            }

            @Override public void onFailure(Call<GetPaymentGateway> call, Throwable t) {}
        });

        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTransaction();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        int position=PGAdapter.selectedPosition;
        if (position != -1) {
            PGAdapter.arrayList.get(position).setSelected(false);
            PGAdapter.selectedPosition = -1;
            recyclerView.setAdapter(pgAdapter);
        }
    }

    public void startTransaction(){
        GetPaymentGateway.Get_PG_Inner selectedPG = PGAdapter.getSelectedItem();
        pgId=selectedPG.pg_id;
        if(selectedPG!=null){
            String pgLink=selectedPG.getPg_link();
            String pgName=selectedPG.getPg_name();
            if(pgLink.equalsIgnoreCase("RazorPay")) {
                if (Objects.equals(curr_activity, "CoinAdapter"))
                    startPayment(coin_inner.getC_amt());
                else
                    startPayment(oAmt);
            }
            else if(pgLink.equalsIgnoreCase("PayPal")) {
                Intent intent = new Intent(RazorpayActivity.this, Paypalpay.class);
                if(Objects.equals(curr_activity, "CoinAdapter")) {
                    Toast.makeText(RazorpayActivity.this, "" + coin_inner.getC_amt(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("coins", coin_inner.getC_coin());
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("amt", coin_inner.getC_amt());
                }
                else {
                    intent.putExtra("o_id",O_id);
                    intent.putExtra("email",email);
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("amt", oAmt);
                    intent.putExtra("pgId",pgId);

                }
                startActivity(intent);
            }
            else if(pgLink.equalsIgnoreCase("OpenPix")) {
                Intent intent = new Intent(RazorpayActivity.this, OpenPixActivity.class);
                if (Objects.equals(curr_activity, "CoinAdapter")) {
                    intent.putExtra("coins", coin_inner.getC_coin());
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("amt", coin_inner.getC_amt());
                    startActivity(intent);
                } else {
                    intent.putExtra("o_id", O_id);
                    intent.putExtra("email", email);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("amt", oAmt);
                    startActivity(intent);
                }
            }
            else if(pgLink.equalsIgnoreCase("Stripe")) {
                Intent intent = new Intent(RazorpayActivity.this, CheckoutActivityJava.class);

                if(Objects.equals(curr_activity, "CoinAdapter")) {
                    // Parsing with Float.parseFloat to handle decimal values, then casting to int if necessary
                    intent.putExtra("plan_price", (int) Float.parseFloat(coin_inner.getC_amt()));
                    intent.putExtra("list", coin_inner);
                    intent.putExtra("pgId", pgId);
                }
                else {
                    // Parsing oAmt as a float to handle decimal points
                    intent.putExtra("plan_price", (int) Float.parseFloat(oAmt));
                    intent.putExtra("o_id", O_id);
                    intent.putExtra("email", email);
                    intent.putExtra("pgId", pgId);
                    intent.putExtra("packageid", packageId);
                }

                startActivity(intent);
            }
            else if(pgLink.equalsIgnoreCase("Flutterwave")) {
                if (Objects.equals(curr_activity, "CoinAdapter"))
                    makepaymentflutterwave(coin_inner.getC_amt());
                else
                    makepaymentflutterwave(oAmt);
            }
            else if(pgLink.equalsIgnoreCase("MPesa")) {
                if (Objects.equals(curr_activity, "CoinAdapter")) {
                    Toast.makeText(RazorpayActivity.this, "" + coin_inner.getC_amt(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RazorpayActivity.this, mpesaActivity.class);
                    intent.putExtra("coins", coin_inner.getC_coin());
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("amt", coin_inner.getC_amt());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RazorpayActivity.this, mpesaActivity.class);
                    intent.putExtra("o_id", O_id);
                    intent.putExtra("email", email);
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("amt", oAmt);
                    startActivity(intent);
                }
            }
            else if(pgLink.equalsIgnoreCase("Paystack")) {
                Intent intent = new Intent(RazorpayActivity.this, PaystackActivity.class);
                if (Objects.equals(curr_activity, "CoinAdapter")) {
                    intent.putExtra("coins", coin_inner.getC_coin());
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("amt", coin_inner.getC_amt());
                } else {
                    intent.putExtra("o_id", O_id);
                    intent.putExtra("email", email);
                    intent.putExtra("pgId",pgId);
                    intent.putExtra("wallet", getWallet);
                    intent.putExtra("packageid", packageId);
                    intent.putExtra("amt", oAmt);
                }
                startActivity(intent);
            }
            else if(pgLink.equalsIgnoreCase("Midtrans")) {
                midtransUpdate();
            }
            else if(pgLink.equalsIgnoreCase("EDokanPay")) {
                String am;
                if (Objects.equals(curr_activity, "CoinAdapter"))
                    am = coin_inner.getC_amt();
                else
                    am = oAmt;
                edokanPayment(am);
            }else if(pgLink.equalsIgnoreCase("cod")) {
                addorder();
            }
            else{
                Intent i=new Intent(RazorpayActivity.this,WebViewActivity.class);
                if(Objects.equals(curr_activity,"CoinAdapter"))
                    i.putExtra("url",pgLink.trim()+"&coin_id="+coin_inner.getC_id()+"&user_id="+savePref.getUserId()+"&address_id="+address_Id);
                else if(Objects.equals(curr_activity, "PurchaseCoin")){
                    i.putExtra("url",pgLink.trim()+"&item_id="+O_id+"&user_id="+savePref.getUserId());
                }
                else
                    i.putExtra("url",pgLink.trim()+"&item_id="+O_id+"&user_id="+savePref.getUserId()+"&address_id="+address_Id);
                i.putExtra("from","RazorPay");
                i.putExtra("title",pgName);
                i.putExtra("pgId",pgId);
                i.putExtra("address",address);

                startActivity(i);
            }
        } else
            Toast.makeText(RazorpayActivity.this, getString(R.string.nooptionsel), Toast.LENGTH_SHORT).show();
    }

    private void edokanPayment(String am) {
        webView.setVisibility(VISIBLE);
        String na = "Edokan Pay";
        String em = "abc@gmai.com";
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String loadUrl = "https://pay2.edokanpay.com/woocommerce_checkout.php?api=63cab36ec7ed0&client=63cab36ec8108&secret=1586664048&amount="+am+"&position=https://billing.greenviewsoft.com&success_url=https://pay2.edokanpay.com/android/after_success.php&cancel_url=https://pay2.edokanpay.com/android/cancled.php&cus_name="+na+"&cus_email="+em+"&done";
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals("https://pay2.edokanpay.com/android/success.php")) {
                    if ((Objects.equals(curr_activity, "CategoryDetailsAct")))
                        addorder();
                    else
                        postUserwalletUpdate();
                    Toast.makeText(RazorpayActivity.this, "success", Toast.LENGTH_SHORT).show();
                    webView.setVisibility(View.INVISIBLE);
                    finish();
                } else {
                    if (url.equals("https://pay2.edokanpay.com/android/cancel.php")) {
                        Toast.makeText(RazorpayActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
        webView.loadUrl(loadUrl);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        oId =razorpayPaymentID;
        if ((Objects.equals(curr_activity, "CategoryDetailsAct")))
            addorder();
        else
            postUserwalletUpdate();
    }

    @Override public void onPaymentError(int i, String s) {}

    public void startPayment(String amou) {
        final Context activity=this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("description", "");
            options.put("image", "");
            options.put("currency", "INR");
            int priceint = Integer.parseInt(amou);
            try {
                priceint = priceint * 100;
                options.put("amount", priceint + "");
            } catch (NumberFormatException ignore) {}

            JSONObject preFill = new JSONObject();
            preFill.put("email", savePref.getemail());
            preFill.put("contact",savePref.getUserPhone());
            options.put("prefill", preFill);
            co.open((Activity) activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, getText(R.string.string163)  + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void postUserwalletUpdate() {
        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    try {
                        ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(RazorpayActivity.this, ""+arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();

                        if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                            Intent i=new Intent(RazorpayActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }catch (Exception ignore){}
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<SuccessModel> calladdbidApi() {
        return videoService.postUserwalletUpdate(savePref.getUserId(), getWallet, packageId, oId,oAmt);
    }

    private void makepaymentflutterwave(String c_amount) {
        new RaveUiManager(this)
                .setAmount(Double.parseDouble(c_amount))
                .setEmail("hello@wowcodes.in")
                .setCountry("ZA")
                .setCurrency("ZAR")
                .setfName("Wow Codes")
                .setlName("Wow")
                .setNarration("Coin Purchase")
                .setPublicKey(MainActivity.flutterwave_public)
                .setEncryptionKey(MainActivity.flutterwave_encryption)
                .setTxRef(System.currentTimeMillis() + "Ref")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .acceptBankTransferPayments(true)
                .onStagingEnv(false)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, getText(R.string.string164) + " " + message, Toast.LENGTH_LONG).show();
                if ((Objects.equals(curr_activity, "CategoryDetailsAct")))
                    addorder();
                else
                    postUserwalletUpdate();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR)
                Toast.makeText(this, getText(R.string.string165)+" " + message, Toast.LENGTH_LONG).show();
            else if (resultCode == RavePayActivity.RESULT_CANCELLED)
                Toast.makeText(this, getText(R.string.string165) + " " + message, Toast.LENGTH_LONG).show();
        }
    }

    public void addorder() {
        try {
            calladdorderApi().enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, retrofit2.Response<AddOrder> response) {
                    try {
                        ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                        Toast.makeText(RazorpayActivity.this, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();

                        if (arrayList.get(0).getO_status().equalsIgnoreCase("1"))
                            openConfirmOrderDialog();
                    } catch (Exception ignore) {}
                }

                @Override public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<AddOrder> calladdorderApi() {
        return videoService.add_order(savePref.getUserId(), O_id, oAmt, "", oAmt, address,"",pgId);
    }

    private void openConfirmOrderDialog() {
        final Dialog animationDialog = new Dialog(RazorpayActivity.this);
        animationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        animationDialog.setContentView(R.layout.done_anim_dialog);
        Window animationWindow = animationDialog.getWindow();
        animationWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LottieAnimationView animationView = animationDialog.findViewById(R.id.animationView);
        animationView.setAnimation(R.raw.doneanim);
        animationView.playAnimation();
        final MediaPlayer mediaPlayer = MediaPlayer.create(RazorpayActivity.this, R.raw.ordered);

        animationDialog.show();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // No action needed here
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Dismiss the animation dialog when animation ends
                animationDialog.dismiss();

                // Play completion sound and then show the order dialog
                showOrderDialog();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Handle animation cancellation if needed
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // No action needed here
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }
    private void showOrderDialog() {
        final Dialog orderDialog = new Dialog(RazorpayActivity.this);
        orderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        orderDialog.setContentView(R.layout.dialog_orderconfirmed);
        Window window = orderDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        orderDialog.show();

        LinearLayout layout = orderDialog.findViewById(R.id.layoutmypurchases);
        LinearLayout ratingLayout = orderDialog.findViewById(R.id.layoutrateapp);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
                Intent i = new Intent(RazorpayActivity.this, GetOrderActivity.class);
                i.putExtra("page", "1");
                RazorpayActivity.this.startActivity(i);
                ((Activity) RazorpayActivity.this).finish(); // Finish the activity after starting the new one
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
                ((Activity) RazorpayActivity.this).finish(); // Finish the activity when the dialog is dismissed
            }
        });
        orderDialog.setOnDismissListener(dialog -> RazorpayActivity.this.finish());

    }

    private void midtransUpdate() {
        SdkUIFlowBuilder.init()
                .setClientKey(SdkConfig.MERCHANT_CLIENT_KEY) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult result) {
                        if (result.getResponse() != null) {
                            switch (result.getStatus()) {
                                case TransactionResult.STATUS_SUCCESS:
                                    if (Objects.equals(curr_activity, "CategoryDetailsAct"))
                                        addorder();
                                    else
                                        postUserwalletUpdate();
                                    Toast.makeText(RazorpayActivity.this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_PENDING:
                                    Toast.makeText(RazorpayActivity.this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_FAILED:
                                    Toast.makeText(RazorpayActivity.this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else if (result.isTransactionCanceled())
                            Toast.makeText(RazorpayActivity.this, "Transaction Canceled", Toast.LENGTH_LONG).show();
                        else {
                            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID))
                                Toast.makeText(RazorpayActivity.this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(RazorpayActivity.this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setMerchantBaseUrl(SdkConfig.MERCHANT_BASE_CHECKOUT_URL) // set merchant URL (required)
                .enableLog(true) // enable SDK log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme (optional)
                .setLanguage("en") // `en` for English and `id` for Bahasa
                .buildSDK();

        // Create the transaction request
        TransactionRequest transactionRequestNew;
        int amt;

        if (Objects.equals(curr_activity, "CoinAdapter")) {
            amt = Integer.parseInt(coin_inner.getC_amt());
            transactionRequestNew = new TransactionRequest(System.currentTimeMillis() + "", amt);
        } else {
            amt = Integer.parseInt(oAmt);
            transactionRequestNew = new TransactionRequest(System.currentTimeMillis() + "", amt);
        }

        // Set up payment methods
        transactionRequestNew.setGopay(new Gopay("mysamplesdk:://midtrans"));
        transactionRequestNew.setShopeepay(new Shopeepay("mysamplesdk:://midtrans"));

        // Prepare customer details
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerIdentifier("budi-6789");
        customerDetails.setPhone("08123456789");
        customerDetails.setFirstName("Budi");
        customerDetails.setLastName("Utomo");
        customerDetails.setEmail("budi@utomo.com");
        transactionRequestNew.setCustomerDetails(customerDetails);

        // Set up item details (make sure to populate this based on your items)
        List<ItemDetails> itemDetailsList = new ArrayList<>();

        // Example item detail with the correct types
        String itemId = "item_id_1"; // This should be your actual item ID
        String itemName = "Item Name 1"; // This should be your actual item name
        Double price = 100.0; // Replace with your actual price as a Double
        int quantity = 1; // Replace with your actual quantity

        itemDetailsList.add(new ItemDetails(itemId, price, quantity, itemName));

        // Set item details to transaction request
        transactionRequestNew.setItemDetails((ArrayList<ItemDetails>) itemDetailsList);

        // Set the transaction request
        MidtransSDK.getInstance().setTransactionRequest(transactionRequestNew);

        // Start the payment UI flow
        MidtransSDK.getInstance().startPaymentUiFlow(RazorpayActivity.this);
        finish();
    }

    public void getsetting() {
        try {
            callseting().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                    demo_access=arrayList.get(0).getDemo_access();
                    Log.d("demo_access", demo_access);
                    if (demo_access != null && !demo_access.isEmpty()) {
                        if (demo_access.equals("1")) {
                            buttonPayment.setEnabled(false);
                            buttonPayment.setText(getString(R.string.nopayment));
                        } else {
                            buttonPayment.setEnabled(true);

                        }
                    }
                }

                @Override
                public void onFailure(Call<SettingModel> call, Throwable t) {
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<SettingModel> callseting() {
        return videoService.settings();
    }
    private void checkCertificte() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
    }
}