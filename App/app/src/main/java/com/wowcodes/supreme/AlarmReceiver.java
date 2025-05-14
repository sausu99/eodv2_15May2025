package com.wowcodes.supreme;

import static android.content.Context.MODE_PRIVATE;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;

import java.util.concurrent.ExecutionException;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences notiprefs = context.getSharedPreferences("Notification_Preferences", MODE_PRIVATE);

        Intent i;
        switch (notiprefs.getString("type","")){
            case "1":
                i = new Intent(context, AuctionActivity.class);
                i.putExtra("check", "live");
                i.putExtra("O_id", notiprefs.getString("oid",""));
                break;

            case "2":
                i = new Intent(context, AuctionActivity.class);
                i.putExtra("O_id", notiprefs.getString("oid",""));
                i.putExtra("check", "live");
                break;

            case "4":
                i = new Intent(context, BeforeRaffleActivity.class);
                i.putExtra("check", "draw");
                i.putExtra("O_id", notiprefs.getString("oid",""));
                i.putExtra("edate", notiprefs.getString("edate",""));
                i.putExtra("etime", notiprefs.getString("etime",""));
                i.putExtra("total_bids" , notiprefs.getString("totalbids",""));
                i.putExtra("qty" , notiprefs.getString("qty",""));
                i.putExtra("type" , notiprefs.getString("type",""));
                i.putExtra("title" , notiprefs.getString("name",""));
                break;

            case "5":
                i = new Intent(context, BeforeRaffleActivity.class);
                i.putExtra("O_id", notiprefs.getString("oid",""));
                i.putExtra("check", "raffle");
                i.putExtra("edate", notiprefs.getString("edate",""));
                i.putExtra("etime", notiprefs.getString("etime",""));
                i.putExtra("total_bids" , notiprefs.getString("totalbids",""));
                i.putExtra("qty" , notiprefs.getString("qty",""));
                i.putExtra("type" , notiprefs.getString("type",""));
                break;

            case "7":
                i = new Intent(context, AuctionActivity.class);
                i.putExtra("O_id", notiprefs.getString("oid",""));
                i.putExtra("check", "live");
                break;

            case "8":
                i = new Intent(context, AuctionActivity.class);
                i.putExtra("O_id", notiprefs.getString("oid",""));
                break;


            case "3":
            case "9":
                i= new Intent(context, ShopItemsActivity.class);
                i.putExtra("image", notiprefs.getString("image",""));
                i.putExtra("image1", notiprefs.getString("image1",""));
                i.putExtra("image2", notiprefs.getString("image2",""));
                i.putExtra("image3", notiprefs.getString("image3",""));
                i.putExtra("image4", notiprefs.getString("image4",""));
                i.putExtra("name", notiprefs.getString("name",""));
                i.putExtra("type", notiprefs.getString("type",""));
                i.putExtra("desc", notiprefs.getString("desc",""));
                i.putExtra("edate", notiprefs.getString("edate",""));
                i.putExtra("etime", notiprefs.getString("etime",""));
                i.putExtra("coins", notiprefs.getString("coins",""));
                i.putExtra("oid", notiprefs.getString("oid",""));
                i.putExtra("qty", notiprefs.getString("qty",""));
                i.putExtra("oamt", notiprefs.getString("coins",""));
                i.putExtra("link", notiprefs.getString("link",""));
                i.putExtra("colorcode", notiprefs.getString("colorcode",""));
                i.putExtra("cdesc", notiprefs.getString("cdesc",""));
                i.putExtra("umax", notiprefs.getString("umax",""));
                i.putExtra("limit", notiprefs.getString("limit",""));
                i.putExtra("totalbids", notiprefs.getString("totalbids",""));
                i.putExtra("id", notiprefs.getString("id",""));

            default:
                i= new Intent(context, CategoryDetailsActivity.class);
                i.putExtra("image", notiprefs.getString("image",""));
                i.putExtra("image1", notiprefs.getString("image1",""));
                i.putExtra("image2", notiprefs.getString("image2",""));
                i.putExtra("image3", notiprefs.getString("image3",""));
                i.putExtra("image4", notiprefs.getString("image4",""));
                i.putExtra("name", notiprefs.getString("name",""));
                i.putExtra("type", notiprefs.getString("type",""));
                i.putExtra("desc", notiprefs.getString("desc",""));
                i.putExtra("edate", notiprefs.getString("edate",""));
                i.putExtra("etime", notiprefs.getString("etime",""));
                i.putExtra("coins", notiprefs.getString("coins",""));
                i.putExtra("oid", notiprefs.getString("oid",""));
                i.putExtra("qty", notiprefs.getString("qty",""));
                i.putExtra("oamt", notiprefs.getString("coins",""));
                i.putExtra("link", notiprefs.getString("link",""));
                i.putExtra("colorcode", notiprefs.getString("colorcode",""));
                i.putExtra("cdesc", notiprefs.getString("cdesc",""));
                i.putExtra("umax", notiprefs.getString("umax",""));
                i.putExtra("limit", notiprefs.getString("limit",""));
                i.putExtra("totalbids", notiprefs.getString("totalbids",""));
                i.putExtra("id", notiprefs.getString("id",""));
        }
        PendingIntent pending=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"CHANNEL_ID");

        builder.setSmallIcon(R.drawable.ic_alarm_on)
                .setContentTitle("It's Live")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if(notiprefs.getString("oid","").equalsIgnoreCase("4") || notiprefs.getString("oid","").equalsIgnoreCase("5"))
            builder.setContentText("Lottery of "+notiprefs.getString("name","")+" is available now !!\nGrab on before it's gone !!");
        else
            builder.setContentText("Auction for "+notiprefs.getString("name","")+" is available now !!\nGrab on before it's gone !!");

        NotificationManagerCompat manager= NotificationManagerCompat.from(context);
        manager.notify(0,builder.build());

        SharedPreferences.Editor editor=notiprefs.edit();
        editor.remove(notiprefs.getString("oid",""));
        editor.remove("image");
        editor.remove("image1");
        editor.remove("image2");
        editor.remove("image3");
        editor.remove("image4");
        editor.remove("oid");
        editor.remove("type");
        editor.remove("name");
        editor.remove("colorcode");
        editor.remove("desc");
        editor.remove("edate");
        editor.remove("etime");
        editor.remove("coins");
        editor.remove("link");
        editor.remove("cdesc");
        editor.remove("qty");
        editor.remove("oamt");
        editor.remove("limit");
        editor.remove("umax");
        editor.remove("toalbids");
        editor.remove("id");
        editor.apply();

    }

    public Bitmap loadImage(String src,Context c){
        FutureTarget<Bitmap> futureTarget= Glide.with(c)
                .asBitmap()
                .load(src)
                .submit();
        try {
            return futureTarget.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
