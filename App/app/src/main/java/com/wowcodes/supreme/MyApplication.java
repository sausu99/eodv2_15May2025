/**
 * The MyApplication class is an Android application class that extends the Application class and
 * implements the Application.ActivityLifecycleCallbacks interface.
 */
package com.wowcodes.supreme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.wowcodes.supreme.Activity.StartSplashActivity;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "MyApplication";
    private static final int CERTIFICATES = 1;
    public static MyApplication instance;
    public static volatile Handler applicationHandler = null;
    public static int started;
    public static int stopped;
    public SharedPreferences LastPositionHolder;
    public SharedPreferences preferences;
    public String prefName = "restro";
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedpreferences;


    public MyApplication() {
        instance = this;
    }

    public static MyApplication getAppInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        applicationHandler = new Handler(getAppInstance().getMainLooper());
        registerActivityLifecycleCallbacks(MyApplication.this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            JSONObject data = result.notification.payload.additionalData;

            String customKey;
            String isExternalLink;
            if (data != null) {
                customKey = data.optString("video_id", null);
                isExternalLink = data.optString("external_link", null);
                if (customKey != null) {
                    if (!customKey.equals("0")) {

                        Intent intent = new Intent(MyApplication.this, StartSplashActivity.class);

                        intent.putExtra("isNotification", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        if (!isExternalLink.equals("false")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(isExternalLink));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MyApplication.this, StartSplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

}
