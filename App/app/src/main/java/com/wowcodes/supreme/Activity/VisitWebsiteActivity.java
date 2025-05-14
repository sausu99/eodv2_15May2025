// The line `package com.wowcodes.prizex.Activity;` is declaring the package name for the Java file. In
// Java, packages are used to organize classes and provide a namespace for the classes within the
// package. In this case, the package name is `com.wowcodes.prizex.Activity`, indicating that the file
// belongs to the `Activity` package within the `prizex` package within the `wowcodes` package.
package com.wowcodes.supreme.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.R;

import java.util.Objects;

public class VisitWebsiteActivity extends AppCompatActivity {
    TextView txtToolbarTittle;
    LinearLayout lvlVisitweb;
    WebView webView;
    private String getUrl;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String stringCameraPhoto;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private final Uri mCapturedImageURI = null;
    String stringName;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_visitwebsite);
        pDialog = new ProgressDialog(VisitWebsiteActivity.this);
        pDialog.setTitle(R.string.string147);
        pDialog.setMessage(getText(R.string.string147));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        getUrl = getIntent().getStringExtra("url");
        stringName = getIntent().getStringExtra("name");
        if (Objects.equals(stringName, "About Us"))
            stringName = String.valueOf((getText(R.string.string334)));
        ImageView backimage = findViewById(R.id.imgBackk);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtToolbarTittle = findViewById(R.id.txtAucname);
        txtToolbarTittle.setText(stringName);
        webView = findViewById(R.id.webview);
        lvlVisitweb = findViewById(R.id.linearlay);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.requestFocus();
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setSoundEffectsEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override public void onPageFinished(WebView view, final String url) {pDialog.dismiss();}
        });
        webView.loadUrl(getUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (stringCameraPhoto != null)
                    results = new Uri[]{Uri.parse(stringCameraPhoto)};
            } else {
                String dataString = data.getDataString();
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}