/**
 * The WebViewActivity class is an Android activity that displays a web page in a WebView and handles
 * navigation and internet connectivity.
 */
package com.wowcodes.supreme.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.R;
public class WebViewActivity extends AppCompatActivity {
    ImageView imgBackk;
    TextView txtAucname;
    WebView mywebview;
    String url,from,title;
    private ValueCallback<Uri[]> filePathCallback;
    private final int FILE_CHOOSER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_webview);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        imgBackk=(ImageView)findViewById(R.id.imgBackk);
        txtAucname=(TextView)findViewById(R.id.txtAucname);
        mywebview = (WebView) findViewById(R.id.webView);
        if (getIntent().hasExtra("address")) {
            String address = getIntent().getStringExtra("address");
            if (address != null) {
                Log.d("addressinweb", address);
            } else {
                Log.d("addressinweb", "Address is null");
            }
        } else {
            Log.d("addressinweb", "No address extra found");
        }

        try {
            if(getIntent()!=null){
                url=getIntent().getStringExtra("url");
                from=getIntent().getStringExtra("from");
                title=getIntent().getStringExtra("title");
            }
        } catch (Exception ignore) {}

        switch (title) {
            case "1":
                txtAucname.setText(R.string.fourthtittle2);
                break;
            case "2":
                txtAucname.setText(R.string.string223);
                break;
            case "3":
                txtAucname.setText(R.string.string222);
                break;
            default:
                txtAucname.setText(title);
                break;
        }
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();
            }
        });
        isNetworkConnected();
        setupWebView();
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    private void setupWebView() {
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (WebViewActivity.this.filePathCallback != null) {
                    WebViewActivity.this.filePathCallback.onReceiveValue(null);
                }
                WebViewActivity.this.filePathCallback = filePathCallback;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILE_CHOOSER_REQUEST_CODE);

                return true;
            }
        });

        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        mywebview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Ensure 'from' is not null before checking its value
        if (from != null && from.equalsIgnoreCase("main")) {
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.putExtra("page", "2");
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && filePathCallback != null) {
            Uri[] results = null;
            if (resultCode == RESULT_OK && data != null) {
                results = new Uri[]{data.getData()};
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(null);
            filePathCallback = null;
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            if(TextUtils.isEmpty(url)){
                Toast.makeText(getApplicationContext(),"Invalid URL",Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                mywebview.setWebViewClient(new MyWebViewClient());
                mywebview.loadUrl(url);
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(url); // load the url on the web view
            }
        } else {
            Intent intent=new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}