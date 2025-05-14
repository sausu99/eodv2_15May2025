/**
 * The `MyFirebaseMessagingService` class is responsible for handling incoming Firebase Cloud Messaging
 * (FCM) messages and displaying notifications based on the message content.
 */
package com.wowcodes.supreme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wowcodes.supreme.Activity.StartSplashActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

            if (remoteMessage.getNotification() != null)
                sendMyNotification(remoteMessage.getNotification().getBody(), "");

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();

                String title = data.get("title");
                String body = data.get("body");
                String type = data.get("type");
                sendMyNotification(body, type);
            }
        } catch (Exception ignore) {}
    }

    @Override
    public void onNewToken(@NonNull String token) {
        getApplicationContext().getSharedPreferences("FCM_REG_TOKEN", MODE_PRIVATE).edit().putString("token", token).apply();
    }

    private void sendMyNotification(String message, String type) {
        Intent intent = new Intent(this, StartSplashActivity.class);
        intent.putExtra("type", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Adding the FLAG_IMMUTABLE flag for security
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CH_ID")
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Changing Default mode of notification
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            // Creating Channel
            NotificationChannel notificationChannel = new NotificationChannel("CH_ID", "Testing_Audio", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(0, notificationBuilder.build());
    }
}
