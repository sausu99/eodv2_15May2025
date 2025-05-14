package com.wowcodes.supreme.Adapter;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.AlarmReceiver;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionItemAdapter extends RecyclerView.Adapter<AuctionItemAdapter.ItemViewHolder>  {

    String start_date;
    private Context context;
    public String from;
    SharedPreferences.Editor editor;
    String notified;
    ArrayList<GetCategories.JSONDATum> items;
    public boolean fromCategorySelected;


    public AuctionItemAdapter(Context context, ArrayList<GetCategories.JSONDATum> items, String from, boolean fromCategorySelected) {
        this.context = context;
        this.items = items;
        this.from=from;
        this.fromCategorySelected=fromCategorySelected;
    }


    @NonNull
    @Override
    public AuctionItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        view= LayoutInflater.from(context).inflate(R.layout.auction_item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AuctionItemAdapter.ItemViewHolder holder, int position) {
        createchannel();

        GetCategories.JSONDATum item = items.get(position);
        String oType = item.getoType();

        Glide.with(context)
                .load(item.getoImage())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.imageView2);
        holder.productnametxt.setText(item.getoName());

        try{
            String wishliststatus= item.getWishlist_status();
            if ((wishliststatus.equals("1")||Integer.parseInt(wishliststatus)>=1) && wishliststatus!=null) {
                holder.wishlistblack.setVisibility(GONE);
                holder.wishlistred.setVisibility(VISIBLE);
            }else if(wishliststatus=="0"||Integer.parseInt(wishliststatus)<=1){
                holder.wishlistblack.setVisibility(VISIBLE);
                holder.wishlistred.setVisibility(GONE);
            }else{
                holder.wishlistblack.setVisibility(GONE);
                holder.wishlistred.setVisibility(GONE);
            }
        }catch (Exception ignore){
        }

        int total = 0;
        try {
            String oQtyStr = item.getoQty();
            String totalBidsStr = item.getTotalbids();
            if (oQtyStr != null && !oQtyStr.isEmpty() && totalBidsStr != null && !totalBidsStr.isEmpty()) {
                int oQty = Integer.parseInt(oQtyStr);
                int totalBids = Integer.parseInt(totalBidsStr);
                total = oQty - totalBids;
                if (total < 0) {
                    total=0;
                }
            } else {
                Log.e("AuctionItemAdapter", "Quantity or total bids is null or empty");
            }
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            e.printStackTrace();
            // Optionally, set a default value or handle the error
        }
        Log.e("otype", oType);


        switch (oType) {
            case "1": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                String oStatus = item.getoStatus();

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string440) + " " + MainActivity.currency + item.getoMin());
                break;
            }
            case "2": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                Log.d("TAGY", item.getoId());

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string441) + " " + MainActivity.currency + item.getoMax());
                break;
            }
            case "3": {
                holder.placeimg.setImageResource(R.drawable.gifticon);
                holder.objAmounttxt.setText(item.getoAmount());
                holder.txtTixketNo.setText(total + " " + holder.itemView.getContext().getString(R.string.string452));
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string448));
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string442) + " " + MainActivity.currency + item.getoPrice());
                break;
            }
            case "4": {
                break;
            }
            case "5": {
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string451));
                holder.objAmounttxt.setText(item.getoAmount());
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string446));
                holder.txtTixketNo.setText(total + " " + holder.itemView.getContext().getString(R.string.string459));
                break;
            }
            case "7": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string447));
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                Log.d("TAGY", item.getoId());

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string443) + " " + MainActivity.currency + item.getoMin());
                break;
            }
            case "8": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                Log.d("TAGY", item.getoId());

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText(context.getString(R.string.string443) + " " + MainActivity.currency + item.getoMin());
                break;
            }
            case "9": {
                holder.placeimg.setImageResource(R.drawable.ic_cart);
                holder.objAmounttxt.setText(MainActivity.currency + " " + item.getoAmount());
                holder.coinimg.setVisibility(View.GONE);
                if (total == 0) {
                    holder.txtTixketNo.setText( holder.itemView.getContext().getString(R.string.nostock));
                }else {
                    holder.txtTixketNo.setText(total + " " + holder.itemView.getContext().getString(R.string.string452));
                }
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string449));
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string442) + " " + MainActivity.currency + item.getoPrice());
                break;
            }
            case "10": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                Log.d("TAGY", item.getoId());

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText(context.getString(R.string.string443) + " " + MainActivity.currency + item.getoMin());
                break;
            }
            case "11": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                String startDateTime = item.getoDate() + " " + item.getoStime();
                Log.d("TAGY", item.getoId());

                if (holder.txtTixketNo != null && endDateTime != null && !endDateTime.trim().isEmpty()) {
                    holder.startCountdown(startDateTime, endDateTime, position, items, this);
                } else {
                    Log.e("AuctionItemAdapter", "TextView or endDateTime is null or empty");
                }

                holder.bidstarttxt.setText( context.getString(R.string.string445)+" " +item.getoQty() + " " + context.getString(R.string.string444));
                break;
            }
            default: {
                break;
            }
        }

        holder.productnametxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;

                if (Objects.equals(new SavePref(context).getUserId(), "0")) {
                    i = new Intent(context, LoginActivity.class);
                    i.putExtra("Decider", "Category");
                    context.startActivity(i);
                }
                else if (item.getoStatus()!=null && item.getoStatus().equalsIgnoreCase("0")) {
                    if (oType.equalsIgnoreCase("4") || oType.equalsIgnoreCase("5"))
                        Toast.makeText(context, context.getResources().getString(R.string.pleasewaitlottery), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, context.getResources().getString(R.string.pleasewaitauction), Toast.LENGTH_SHORT).show();
                }
                else {
                    switch (oType) {
                        case "1":
                        case "10":
                        case "11" :
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("check", "live");
                            i.putExtra("O_id", item.getoId());
                            break;
                        case "2":
                        case "7":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            break;
                            /*case "4":
                                i = new Intent(context, BeforeRaffleActivity.class);
                                i.putExtra("check", "draw");
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("total_bids", item.getTotalbids());
                                i.putExtra("qty", item.getoQty());
                                i.putExtra("type", item.getoType());
                                i.putExtra("name", item.getoName());
                                i.putExtra("etime", item.getoEtime());
                                i.putExtra("edate", item.getoEdate());
                                i.putExtra("image", item.getoImage());
                                i.putExtra("desc", item.getoDesc());
                                i.putExtra("coins", item.getoAmount());
                                i.putExtra("oamt", item.getoAmount());
                                i.putExtra("colorcode", item.getcColor());
                                i.putExtra("umax", item.getoUmax());
                                i.putExtra("cdesc", item.getcDesc());
                                i.putExtra("link", item.getoLink());
                                if (TextUtils.isEmpty(item.getoUlimit())) {
                                    i.putExtra("limit", "1");
                                } else {
                                    i.putExtra("limit", item.getoUlimit());
                                }
                                i.putExtra("id", item.getId());
                                break;*/
                        case "5":
                            i = new Intent(context, BeforeRaffleActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "raffle");
                            i.putExtra("total_bids", item.getTotalbids());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("type", item.getoType());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("edate", item.getoEdate());
                            break;
                        case "8":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("total_bids", item.getTotalbids());
                            i.putExtra("qty", Integer.parseInt(item.getoQty()));
                            i.putExtra("type", item.getoType());
                            break;
                        case "3":
                        case "4":
                        case "9":
                            i = new Intent(context, ShopItemsActivity.class);
                            i.putExtra("image", item.getoImage());
                            i.putExtra("image1", item.getoImage1());
                            i.putExtra("image2", item.getoImage2());
                            i.putExtra("image3", item.getoImage3());
                            i.putExtra("image4", item.getoImage4());
                            i.putExtra("name", item.getoName());
                            i.putExtra("type", item.getoType());
                            i.putExtra("desc", item.getoDesc());
                            i.putExtra("edate", item.getoEdate());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("coins", item.getoPrice());
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("wishliststatus", item.getWishlist_status());
                            i.putExtra("id", item.getId());
                            i.putExtra("itemId", item.getItem_id());

                            break;
                        default:
                            i = new Intent(context, CategoryDetailsActivity.class);
                            i.putExtra("image", item.getoImage());
                            i.putExtra("image1", item.getoImage1());
                            i.putExtra("image2", item.getoImage2());
                            i.putExtra("image3", item.getoImage3());
                            i.putExtra("image4", item.getoImage4());
                            i.putExtra("name", item.getoName());
                            i.putExtra("type", item.getoType());
                            i.putExtra("desc", item.getoDesc());
                            i.putExtra("edate", item.getoEdate());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("coins", item.getoAmount());
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("id", item.getId());
                    }

                    context.startActivity(i);
                }
            }
        });

        holder.wishlistblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wishlistred.setVisibility(View.VISIBLE);
                holder.wishlistblack.setVisibility(View.GONE);
                RetrofitVideoApiBaseUrl.getClient().create(BindingService.class).updateWishlist(new SavePref(context).getUserId(), item.getItem_id(),  "add").enqueue(new Callback<WishlistResponse>() {
                    @Override
                    public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                        if (response.isSuccessful()&& response.body()!= null) {
                            WishlistResponse wishlistResponse = response.body();
                            if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                                WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                                Toast.makeText(context, "Added to Wishlist: " + message.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Failed to add to Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WishlistResponse> call, Throwable t) {
                        Toast.makeText(context, "Failed to add to Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.wishlistred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wishlistblack.setVisibility(View.VISIBLE);
                holder.wishlistred.setVisibility(View.GONE);
                RetrofitVideoApiBaseUrl.getClient().create(BindingService.class).updateWishlist(new SavePref(context).getUserId(), item.getItem_id(),  "delete").enqueue(new Callback<WishlistResponse>() {
                    @Override
                    public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                        if (response.isSuccessful()&& response.body()!= null) {
                            WishlistResponse wishlistResponse = response.body();
                            if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                                WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                                Toast.makeText(context, "Deleted from Wishlist: " + message.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Failed to delete from Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WishlistResponse> call, Throwable t) {
                        Toast.makeText(context, "Failed to delete from Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        try {
            SharedPreferences notiprefs = context.getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
            editor = notiprefs.edit();
            notified = notiprefs.getString(item.getoId(), "");

            // Combine start date and time into one string
            String startDateTimeStr = item.getoDate() + " " + item.getoStime();


            // Parse start date-time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // Adjust the format as needed
            Date startDateTime = sdf.parse(startDateTimeStr);
            Date currentDateTime = new Date();

            if (startDateTime != null && currentDateTime.compareTo(startDateTime) >= 0) {
                // Current date-time is after or equal to the start date-time
                holder.blocker.setVisibility(View.GONE);
                holder.lockImgBlocker.setVisibility(View.GONE);
            } else if (item.getoStatus() != null && item.getoStatus().equalsIgnoreCase("0")) {
                // Update UI based on the current notification state
                updateNotificationUI(holder, notified);

                holder.blocker.setVisibility(View.VISIBLE);
                holder.lockImgBlocker.setVisibility(View.VISIBLE);

                holder.placebidtxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7);
                            } else {
                                if (notified.isEmpty()) {
                                    holder.placebidtxt.setText(context.getResources().getString(R.string.notified));
                                    holder.placeimg.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
                                    makenotification(item.getoId(), item.getoDate() + " " + item.getoStime(), holder.getAbsoluteAdapterPosition());
                                    Toast.makeText(context, context.getResources().getString(R.string.notiset), Toast.LENGTH_SHORT).show();
                                } else {
                                    holder.placebidtxt.setText(context.getResources().getString(R.string.notify));
                                    holder.placeimg.setImageDrawable(context.getDrawable(R.drawable.ic_notification));

                                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                    am.cancel(PendingIntent.getBroadcast(context, Integer.parseInt(notified), new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_MUTABLE));

                                    Toast.makeText(context, context.getResources().getString(R.string.noticancel), Toast.LENGTH_SHORT).show();

                                    editor.remove(item.getoId());
                                }

                                editor.apply();
                                notified = notiprefs.getString(item.getoId(), "");
                            }
                        } else {
                            if (notified.isEmpty()) {
                                holder.placebidtxt.setText(context.getResources().getString(R.string.notified));
                                holder.placeimg.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
                                makenotification(item.getoId(), item.getoDate() + " " + item.getoStime(), holder.getAbsoluteAdapterPosition());
                                Toast.makeText(context, context.getResources().getString(R.string.notiset), Toast.LENGTH_SHORT).show();
                            } else {
                                holder.placebidtxt.setText(context.getResources().getString(R.string.notify));
                                holder.placeimg.setImageDrawable(context.getDrawable(R.drawable.ic_notification));
                                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                am.cancel(PendingIntent.getBroadcast(context, Integer.parseInt(notified), new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_MUTABLE));

                                Toast.makeText(context, context.getResources().getString(R.string.noticancel), Toast.LENGTH_SHORT).show();

                                editor.remove(item.getoId());
                            }

                            editor.apply();
                            notified = notiprefs.getString(item.getoId(), "");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace(); // Consider logging or handling this exception properly.
        }

// Method to send a notification


        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i;

                    if (Objects.equals(new SavePref(context).getUserId(), "0")) {
                        i = new Intent(context, LoginActivity.class);
                        i.putExtra("Decider", "Category");
                        context.startActivity(i);
                    }
                    else if (item.getoStatus()!=null && item.getoStatus().equalsIgnoreCase("0")) {
                        if (oType.equalsIgnoreCase("4") || oType.equalsIgnoreCase("5"))
                            Toast.makeText(context, context.getResources().getString(R.string.pleasewaitlottery), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getResources().getString(R.string.pleasewaitauction), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        switch (oType) {
                            case "1":
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("check", "live");
                                i.putExtra("O_id", item.getoId());
                                break;

                            case "2":
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("check", "live");
                                break;

                            case "4":
                                i = new Intent(context, BeforeRaffleActivity.class);
                                i.putExtra("check", "draw");
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("total_bids", item.getTotalbids());
                                i.putExtra("qty", item.getoQty());
                                i.putExtra("type", item.getoType());
                                i.putExtra("name", item.getoName());
                                i.putExtra("etime", item.getoEtime());
                                i.putExtra("edate", item.getoEdate());
                                i.putExtra("image", item.getoImage());
                                i.putExtra("desc", item.getoDesc());
                                i.putExtra("coins", item.getoAmount());
                                i.putExtra("oamt", item.getoAmount());
                                i.putExtra("colorcode", item.getcColor());
                                i.putExtra("umax", item.getoUmax());
                                i.putExtra("cdesc", item.getcDesc());
                                i.putExtra("link", item.getoLink());
                                if (TextUtils.isEmpty(item.getoUlimit())) {
                                    i.putExtra("limit", "1");
                                } else {
                                    i.putExtra("limit", item.getoUlimit());
                                }
                                i.putExtra("id", item.getId());
                                break;

                            case "5":
                                i = new Intent(context, BeforeRaffleActivity.class);
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("check", "raffle");
                                i.putExtra("total_bids", item.getTotalbids());
                                i.putExtra("qty", item.getoQty());
                                i.putExtra("type", item.getoType());
                                i.putExtra("etime", item.getoEtime());
                                i.putExtra("edate", item.getoEdate());
                                break;

                            case "7":
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("check", "live");
                                break;

                            case "8":
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("check", "live");
                                i.putExtra("total_bids", item.getTotalbids());
                                i.putExtra("qty", Integer.parseInt(item.getoQty()));
                                i.putExtra("type", item.getoType());
                                break;

                            case "3":
                            case "9":
                                i = new Intent(context, ShopItemsActivity.class);
                                i.putExtra("image", item.getoImage());
                                i.putExtra("image1", item.getoImage1());
                                i.putExtra("image2", item.getoImage2());
                                i.putExtra("image3", item.getoImage3());
                                i.putExtra("image4", item.getoImage4());
                                i.putExtra("name", item.getoName());
                                i.putExtra("type", item.getoType());
                                i.putExtra("desc", item.getoDesc());
                                i.putExtra("edate", item.getoEdate());
                                i.putExtra("etime", item.getoEtime());
                                i.putExtra("coins", item.getoPrice());
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("qty", item.getoQty());
                                i.putExtra("oamt", item.getoAmount());
                                i.putExtra("link", item.getoLink());
                                i.putExtra("colorcode", item.getoColor());
                                i.putExtra("cdesc", item.getcDesc());
                                i.putExtra("umax", item.getoUmax());
                                i.putExtra("limit", item.getoUlimit());
                                i.putExtra("totalbids", item.getTotalbids());
                                i.putExtra("id", item.getId());

                                break;
                            case "10":{
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("check", "live");
                                i.putExtra("O_id", item.getoId());
                                break;

                            }
                            case "11" :{
                                i = new Intent(context, AuctionActivity.class);
                                i.putExtra("check", "live");
                                i.putExtra("O_id", item.getoId());
                                break;
                            }



                            default:
                                i = new Intent(context, CategoryDetailsActivity.class);
                                i.putExtra("image", item.getoImage());
                                i.putExtra("image1", item.getoImage1());
                                i.putExtra("image2", item.getoImage2());
                                i.putExtra("image3", item.getoImage3());
                                i.putExtra("image4", item.getoImage4());
                                i.putExtra("name", item.getoName());
                                i.putExtra("type", item.getoType());
                                i.putExtra("desc", item.getoDesc());
                                i.putExtra("edate", item.getoEdate());
                                i.putExtra("etime", item.getoEtime());
                                i.putExtra("coins", item.getoAmount());
                                i.putExtra("O_id", item.getoId());
                                i.putExtra("qty", item.getoQty());
                                i.putExtra("oamt", item.getoAmount());
                                i.putExtra("link", item.getoLink());
                                i.putExtra("colorcode", item.getoColor());
                                i.putExtra("cdesc", item.getcDesc());
                                i.putExtra("umax", item.getoUmax());
                                i.putExtra("limit", item.getoUlimit());
                                i.putExtra("totalbids", item.getTotalbids());
                                i.putExtra("id", item.getId());
                        }

                        context.startActivity(i);
                    }
                }
            });
        }catch (Exception ignore){}



    }

    @Override
    public int getItemCount() {
        if(from.equalsIgnoreCase("shop") && !fromCategorySelected)
            return Math.min(items.size(), 4);
        else
            return items.size();
    }
    private void updateNotificationUI(ItemViewHolder holder, String notified) {
        if (notified.isEmpty()) {
            holder.placeimg.setImageResource(R.drawable.ic_notifications);
            holder.placebidtxt.setText(R.string.string450); // "Notify me"
        } else {
            holder.placeimg.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
            holder.placebidtxt.setText(context.getResources().getString(R.string.notified)); // "Notified"
        }
    }
    public void createchannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("CHANNEL_ID","Prizex", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Prizex Notification Channel");

            NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    public void makenotification(String name,String time,int pos){
        GetCategories.JSONDATum item = items.get(pos);

        int id=new Random().nextInt();
        editor.putString(name,String.valueOf(id));
        editor.putString("image", item.getoImage());
        editor.putString("image1", item.getoImage1());
        editor.putString("image2", item.getoImage2());
        editor.putString("image3", item.getoImage3());
        editor.putString("image4", item.getoImage4());
        editor.putString("name", item.getoName());
        editor.putString("type", item.getoType());
        editor.putString("desc", item.getoDesc());
        editor.putString("edate", item.getoEdate());
        editor.putString("etime", item.getoEtime());
        editor.putString("coins", item.getoPrice());
        editor.putString("oid", item.getoId());
        editor.putString("qty", item.getoQty());
        editor.putString("oamt", item.getoAmount());
        editor.putString("link", item.getoLink());
        editor.putString("colorcode", item.getoColor());
        editor.putString("cdesc", item.getcDesc());
        editor.putString("umax", item.getoUmax());
        editor.putString("limit", item.getoUlimit());
        editor.putString("totalbids", item.getTotalbids());
        editor.putString("id", item.getId());
        editor.apply();


        Intent i=new Intent(context, AlarmReceiver.class);
        PendingIntent pending=PendingIntent.getBroadcast(context,id,i,PendingIntent.FLAG_MUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d2 ;
            try {
                d2 = sdf.parse(time);
                Instant instant=d2.toInstant();
                long duration = Duration.between(Instant.now(),instant).toMillis();
                AlarmManager am= (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+duration,pending);
            } catch (Exception ignore) {}
        }
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtTixketNo, productnametxt, bidstarttxt, placebidtxt, objAmounttxt;
        ImageView imageView2, coinimg, wishlistred, wishlistblack, placeimg, lockImgBlocker;
        View blocker;
        private Handler handler;
        private Runnable updateTimeRunnable;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean isExpired = false;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            blocker = itemView.findViewById(R.id.blocker);
            lockImgBlocker = itemView.findViewById(R.id.lockImgBlocker);
            txtTixketNo = itemView.findViewById(R.id.txtTixketNo);
            imageView2 = itemView.findViewById(R.id.image2);
            objAmounttxt = itemView.findViewById(R.id.objAmounttxt);
            productnametxt = itemView.findViewById(R.id.productnametxt);
            bidstarttxt = itemView.findViewById(R.id.bidstarttxt);
            placebidtxt = itemView.findViewById(R.id.placebidtxt);
            coinimg = itemView.findViewById(R.id.coinimg);
            wishlistred = itemView.findViewById(R.id.wishlistred);
            wishlistblack = itemView.findViewById(R.id.wishlistblack);
            placeimg = itemView.findViewById(R.id.placeimg);
            handler = new Handler(Looper.getMainLooper());
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startCountdown(String startDateTime, String endDateTime, final int position, final List<GetCategories.JSONDATum> itemList, final RecyclerView.Adapter<?> adapter) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startTime = LocalDateTime.parse(startDateTime, formatter);
            endTime = LocalDateTime.parse(endDateTime, formatter);

            updateTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    updateCountdown(position, itemList, adapter);
                    handler.postDelayed(this, 1000); // Update every second
                }
            };
            handler.post(updateTimeRunnable);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void updateCountdown(int position, List<GetCategories.JSONDATum> itemList, RecyclerView.Adapter<?> adapter) {
            LocalDateTime currentTime = LocalDateTime.now();

            // Check if the position is within bounds
            if (position < 0 || position >= itemList.size()) {
                Log.e("AuctionItemAdapter", "Invalid position: " + position);
                return; // Exit the method if the position is invalid
            }

            GetCategories.JSONDATum item = itemList.get(position);

            if (currentTime.isBefore(startTime)) {
                // Auction hasn't started yet
                Duration duration = Duration.between(currentTime, startTime);
                long seconds = duration.getSeconds();
                long days = seconds / (24 * 3600);
                seconds %= (24 * 3600);
                long hours = seconds / 3600;
                seconds %= 3600;
                long minutes = seconds / 60;
                seconds %= 60;

                StringBuilder timeUntilStart = new StringBuilder(context.getString(R.string.string539));

                if (days > 0) {
                    timeUntilStart.append(days).append("d ");
                }
                if (hours > 0 || days > 0) { // Include hours if days are shown
                    timeUntilStart.append(hours).append("h ");
                }
                if (minutes > 0 || hours > 0 || days > 0) { // Include minutes if hours or days are shown
                    timeUntilStart.append(minutes).append("m ");
                }
                timeUntilStart.append(seconds).append("s"); // Always show seconds

                txtTixketNo.setText(timeUntilStart.toString().trim());

            } else if (currentTime.isAfter(endTime) || currentTime.isEqual(endTime)) {
                // Auction has ended
                txtTixketNo.setText(R.string.string540);

                if (!isExpired) {
                    // Mark as expired and remove item
                    isExpired = true;
                    handler.removeCallbacks(updateTimeRunnable); // Stop updates when expired

                    // Notify the adapter that item should be removed
                    if (itemList != null && !itemList.isEmpty() && position >= 0 && position < itemList.size()) {
                        itemList.remove(position); // Remove the item from the list
                        adapter.notifyItemRemoved(position); // Notify adapter of item removal
                    }
                }

            } else {
                blocker.setVisibility(View.GONE);
                lockImgBlocker.setVisibility(View.GONE);

                // Auction is ongoing
                Duration duration = Duration.between(currentTime, endTime);
                long seconds = duration.getSeconds();
                long days = seconds / (24 * 3600);
                seconds %= (24 * 3600);
                long hours = seconds / 3600;
                seconds %= 3600;
                long minutes = seconds / 60;
                seconds %= 60;

                StringBuilder timeRemaining = new StringBuilder("Ends in: ");

                if (days > 0) {
                    timeRemaining.append(days).append("d ");
                }
                if (hours > 0 || days > 0) { // Include hours if days are shown
                    timeRemaining.append(hours).append("h ");
                }
                if (minutes > 0 || hours > 0 || days > 0) { // Include minutes if hours or days are shown
                    timeRemaining.append(minutes).append("m ");
                }
                timeRemaining.append(seconds).append("s"); // Always show seconds

                txtTixketNo.setText(timeRemaining.toString().trim());

                // Update images and text based on the item type
                switch (item.getoType()) {
                    case "1":
                    case "2":
                    case "7":
                    case "8":
                    case "10":
                    case "11":
                        placeimg.setImageResource(R.drawable.pnghammer2);
                        placebidtxt.setText(R.string.string447);
                        break;
                    case "3":
                        placeimg.setImageResource(R.drawable.gifticon);
                        placebidtxt.setText(R.string.string448);
                        break;
                    case "9":
                        placeimg.setImageResource(R.drawable.ic_cart);
                        placebidtxt.setText(R.string.string449);
                        break;
                    default:
                        placeimg.setImageResource(0); // Default or no image
                        placebidtxt.setText(""); // Default or no text
                        break;
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleItemClick(v, position, itemList);
                    }
                });
                productnametxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleItemClick(view, position, itemList);
                    }
                });
            }
        }
    }

    private void handleItemClick(View view, int position, List<GetCategories.JSONDATum> itemList) {
        Intent i;
        if (Objects.equals(new SavePref(context).getUserId(), "0")) {
            i = new Intent(context, LoginActivity.class);
            i.putExtra("Decider", "Category");
            context.startActivity(i);
        } else {
            switch (itemList.get(position).getoType()) {
                case "1":
                case "10":
                case "11":
                    i = new Intent(context, AuctionActivity.class);
                    i.putExtra("check", "live");
                    i.putExtra("O_id", itemList.get(position).getoId());
                    break;
                case "2":
                case "7":
                    i = new Intent(context, AuctionActivity.class);
                    i.putExtra("O_id", itemList.get(position).getoId());
                    i.putExtra("check", "live");
                    break;
                case "5":
                    i = new Intent(context, BeforeRaffleActivity.class);
                    i.putExtra("O_id", itemList.get(position).getoId());
                    i.putExtra("check", "raffle");
                    i.putExtra("total_bids", itemList.get(position).getTotalbids());
                    i.putExtra("qty", itemList.get(position).getoQty());
                    i.putExtra("type", itemList.get(position).getoType());
                    i.putExtra("etime", itemList.get(position).getoEtime());
                    i.putExtra("edate", itemList.get(position).getoEdate());
                    break;
                case "8":
                    i = new Intent(context, AuctionActivity.class);
                    i.putExtra("O_id", itemList.get(position).getoId());
                    i.putExtra("check", "live");
                    i.putExtra("total_bids", itemList.get(position).getTotalbids());
                    i.putExtra("qty", Integer.parseInt(itemList.get(position).getoQty()));
                    i.putExtra("type", itemList.get(position).getoType());
                    break;
                case "3":
                case "4":
                case "9":
                    i = new Intent(context, ShopItemsActivity.class);
                    i.putExtra("image", itemList.get(position).getoImage());
                    i.putExtra("image1", itemList.get(position).getoImage1());
                    i.putExtra("image2", itemList.get(position).getoImage2());
                    i.putExtra("image3", itemList.get(position).getoImage3());
                    i.putExtra("image4", itemList.get(position).getoImage4());
                    i.putExtra("name", itemList.get(position).getoName());
                    i.putExtra("type", itemList.get(position).getoType());
                    i.putExtra("desc", itemList.get(position).getoDesc());
                    i.putExtra("edate", itemList.get(position).getoEdate());
                    i.putExtra("etime", itemList.get(position).getoEtime());
                    i.putExtra("coins", itemList.get(position).getoPrice());
                    i.putExtra("O_id", itemList.get(position).getoId());
                    i.putExtra("qty", itemList.get(position).getoQty());
                    i.putExtra("oamt", itemList.get(position).getoAmount());
                    i.putExtra("link", itemList.get(position).getoLink());
                    i.putExtra("cdesc", itemList.get(position).getcDesc());
                    i.putExtra("umax", itemList.get(position).getoUmax());
                    i.putExtra("limit", itemList.get(position).getoUlimit());
                    i.putExtra("totalbids", itemList.get(position).getTotalbids());
                    i.putExtra("wishliststatus", itemList.get(position).getWishlist_status());
                    i.putExtra("id", itemList.get(position).getId());
                    i.putExtra("itemId", itemList.get(position).getItem_id());
                    break;
                default:
                    i = new Intent(context, CategoryDetailsActivity.class);
                    i.putExtra("image", itemList.get(position).getoImage());
                    i.putExtra("image1", itemList.get(position).getoImage1());
                    i.putExtra("image2", itemList.get(position).getoImage2());
                    i.putExtra("image3", itemList.get(position).getoImage3());
                    i.putExtra("image4", itemList.get(position).getoImage4());
                    i.putExtra("name", itemList.get(position).getoName());
                    i.putExtra("type", itemList.get(position).getoType());
                    i.putExtra("desc", itemList.get(position).getoDesc());
                    i.putExtra("edate", itemList.get(position).getoEdate());
                    i.putExtra("etime", itemList.get(position).getoEtime());
                    i.putExtra("coins", itemList.get(position).getoAmount());
                    i.putExtra("O_id", itemList.get(position).getoId());
                    i.putExtra("qty", itemList.get(position).getoQty());
                    i.putExtra("oamt", itemList.get(position).getoAmount());
                    i.putExtra("link", itemList.get(position).getoLink());
                    i.putExtra("cdesc", itemList.get(position).getcDesc());
                    i.putExtra("umax", itemList.get(position).getoUmax());
                    i.putExtra("limit", itemList.get(position).getoUlimit());
                    i.putExtra("totalbids", itemList.get(position).getTotalbids());
                    i.putExtra("id", itemList.get(position).getId());
            }
            context.startActivity(i);
        }
    }




    public void filterList(ArrayList<GetCategories.JSONDATum> filtered_list){
        this.items=filtered_list;
        notifyDataSetChanged();
    }


}
