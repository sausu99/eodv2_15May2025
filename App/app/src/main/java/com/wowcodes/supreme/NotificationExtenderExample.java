/**
 * The `NotificationExtenderExample` class is a Java class that extends the
 * `NotificationExtenderService` class and is responsible for processing and sending notifications in
 * an Android app.
 */
package com.wowcodes.supreme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.wowcodes.supreme.Activity.StartSplashActivity;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class NotificationExtenderExample extends NotificationExtenderService {

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "video";
    String title;
    String message;
    String type;

    private String bigpicture;


    public static Bitmap getBitmapFromURL(String src) {

        try {

            URL url = new URL(src);
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        title = receivedResult.payload.title;

        bigpicture = receivedResult.payload.bigPicture;


        try {
            type = receivedResult.payload.additionalData.getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }


        String url = "";

        try {
            url = receivedResult.payload.launchURL;
        } catch (Exception e) {
            e.printStackTrace();
        }


        sendNotification(title, url, type);
        return true;
    }

    private void sendNotification(String title, String url, String type) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final int NOTIFICATION_COLOR = getResources().getColor(R.color.colorPrimary);


        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Online Channel", importance);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent;
        if (url != null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        } else {
            intent = new Intent(this, StartSplashActivity.class);
            intent.putExtra("type", type);

        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationChannel mChannel;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Online Channel";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setAutoCancel(true)
                .setLights(Color.RED, 800, 800)
                .setChannelId(CHANNEL_ID);

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
        try {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_logo));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "errror large- " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (title.trim().isEmpty()) {
            mBuilder.setContentTitle(getString(R.string.app_name));
            mBuilder.setTicker(getString(R.string.app_name));
        } else {
            mBuilder.setContentTitle(title);
            mBuilder.setTicker(title);
        }


        if (bigpicture != null) {
            if (getBitmapFromURL(bigpicture) == null) {
                Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                        R.drawable.img_logo);

                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon));
            } else {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(bigpicture)));

            }
        }


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
            return R.drawable.img_logo;
        } else {
            return R.drawable.img_logo;
        }
    }

    private int getColour() {
        return 0x8b5630;
    }
}
