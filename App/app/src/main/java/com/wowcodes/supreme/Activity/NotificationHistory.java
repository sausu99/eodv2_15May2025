package com.wowcodes.supreme.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Adapter.NotificationsAdapter;
import com.wowcodes.supreme.Modelclas.GetNotification;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class NotificationHistory extends AppCompatActivity {

    public static String delete_notifications = "";
    RecyclerView notifications_rv;
    BindingService videoService;
    ImageView imgBackk;
    ArrayList<GetNotification.Get_notification_Inner> notifications_list = new ArrayList<>();
    NotificationsAdapter adapter;
    ImageView no_items;
    TextView no_itemstext, txtAucname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_notification_history);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        no_items = findViewById(R.id.noitems);
        no_itemstext = findViewById(R.id.noitemstext);
        txtAucname = findViewById(R.id.txtAucname);
        imgBackk = findViewById(R.id.imgBackk);
        notifications_rv = findViewById(R.id.notifications_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        notifications_rv.setLayoutManager(layoutManager);
        txtAucname.setText(R.string.title_notifications);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fetchNotifications();
    }

    private void fetchNotifications() {
        try {
            videoService.get_notification_history(new SavePref(NotificationHistory.this).getUserId()).enqueue(new Callback<GetNotification>() {
                @Override
                public void onResponse(Call<GetNotification> call, retrofit2.Response<GetNotification> response) {
                    notifications_list = response.body().getJSON_DATA();
                    adapter = new NotificationsAdapter(NotificationHistory.this, notifications_list);
                    notifications_rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (!notifications_list.isEmpty()) {
                        notifications_rv.scrollToPosition(notifications_list.size());
                        sendNotificationToPanel(notifications_list);
                    }

                    if (notifications_list.isEmpty()) {
                        no_items.setVisibility(View.VISIBLE);
                        no_itemstext.setVisibility(View.VISIBLE);
                        notifications_rv.setVisibility(View.GONE);
                    } else {
                        notifications_rv.setVisibility(View.VISIBLE);
                        no_items.setVisibility(View.GONE);
                        no_itemstext.setVisibility(View.GONE);
                        markAllNotificationsAsRead();
                    }
                }

                @Override
                public void onFailure(Call<GetNotification> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private void markAllNotificationsAsRead() {
        try {
            videoService.read_all_notifications(new SavePref(NotificationHistory.this).getUserId()).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private void sendNotificationToPanel(ArrayList<GetNotification.Get_notification_Inner> notifications) {
        for (GetNotification.Get_notification_Inner notification : notifications) {
            String message = notification.getBody(); // Assuming 'getMessage' is a method in 'Get_notification_Inner' class
            String type = notification.getTittle(); // Assuming 'getType' is a method in 'Get_notification_Inner' class
            showNotification(message, type);
        }
    }

    private void showNotification(String message, String type) {
        Intent intent = new Intent(this, StartSplashActivity.class);
        intent.putExtra("type", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "MY_CHANNEL_ID";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    protected void onStop() {
        handleDeleteNotifications();
        super.onStop();
    }

    private void handleDeleteNotifications() {
        try {
            if (!delete_notifications.isEmpty()) {
                if (notifications_list.isEmpty()) {
                    videoService.delete_all_notifications(new SavePref(NotificationHistory.this).getUserId()).enqueue(new Callback<SuccessModel>() {
                        @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                        @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                    });
                } else {
                    videoService.delete_notifications(new SavePref(NotificationHistory.this).getUserId(), delete_notifications.substring(0, delete_notifications.length() - 1)).enqueue(new Callback<SuccessModel>() {
                        @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                        @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
                    });
                }
            }
        } catch (Exception ignore) {}
        delete_notifications = "";
    }
}
