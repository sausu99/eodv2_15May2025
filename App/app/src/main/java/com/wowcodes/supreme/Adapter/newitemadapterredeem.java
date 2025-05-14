package com.wowcodes.supreme.Adapter;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.wowcodes.supreme.Modelclas.GetRedeem;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newitemadapterredeem extends RecyclerView.Adapter<newitemadapterredeem.ItemViewHolder>  {

    String start_date;
    private Context context;
    public String from;
    SharedPreferences.Editor editor;
    String notified;
    ArrayList<GetRedeem.JSONDATum> items;
    public boolean fromCategorySelected;


    public newitemadapterredeem(Context context, ArrayList<GetRedeem.JSONDATum> items, String from, boolean fromCategorySelected) {
        this.context = context;
        this.items = items;
        this.from=from;
        this.fromCategorySelected=fromCategorySelected;
    }


    @NonNull
    @Override
    public newitemadapterredeem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        view= LayoutInflater.from(context).inflate(R.layout.auction_item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        GetRedeem.JSONDATum item = items.get(position);
        String oType = item.getoType();
        createchannel();
        Glide.with(context)
                .load(item.getoImage())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.imageView2);
        holder.productnametxt.setText(item.getoName());

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


        switch (oType) {
            case "1": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string440) +" " +MainActivity.currency+item.getoMin());
                break;
            }
            case "2": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string441) +" "+MainActivity.currency+item.getoMax());

                break;
            }
            case "3": {
                holder.placeimg.setImageResource(R.drawable.gifticon);
                holder.objAmounttxt.setText(item.getoAmount());
                if (total == 0) {
                    holder.txtTixketNo.setText( holder.itemView.getContext().getString(R.string.nostock));
                }else {
                    holder.txtTixketNo.setText(total + " " + holder.itemView.getContext().getString(R.string.string452));
                }                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string448));
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string442)+" " +MainActivity.currency+item.getoPrice());

                break;
            }
            case "4": {
                break;
            }
            case "5": {
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string451));
                holder.objAmounttxt.setText(item.getoAmount());
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string446) );
                holder.txtTixketNo.setText(item.getoQty()+" "+holder.itemView.getContext().getString(R.string.string459));

                break;
            }
            case "7": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string443) +" "+MainActivity.currency+item.getoMin());
                break;
            }
            case "8": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string443) +" "+MainActivity.currency+item.getoMin());
                break;
            }
            case "9": {
                holder.placeimg.setImageResource(R.drawable.ic_cart);
                holder.objAmounttxt.setText(MainActivity.currency+" " +item.getoAmount());
                holder.coinimg.setVisibility(View.GONE);
                holder.txtTixketNo.setText(item.getoQty()+" "+holder.itemView.getContext().getString(R.string.string452));
                holder.placebidtxt.setText(holder.itemView.getContext().getString(R.string.string449));
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string442)+" "+MainActivity.currency+item.getoPrice());
                break;
            }
            case "10": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string443) +" "+MainActivity.currency+item.getoMin());



                break;
            }
            case "11": {
                holder.placeimg.setImageResource(R.drawable.pnghammer2);
                holder.placebidtxt.setText(R.string.string447);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.bidstarttxt.setText(holder.itemView.getContext().getString(R.string.string443)+" " +item.getoQty()+R.string.string444);

                break;
            }


            default: {


                break;
            }
        }
        try{
            if (item.getoStatus() == "0") {
                holder.placeimg.setImageResource(R.drawable.ic_notifications);
                holder.placebidtxt.setText(R.string.string450);
                holder.objAmounttxt.setText(item.getoAmount());
                new Thread(new newitemadapterredeem.CountDownRunner(holder.txtTixketNo, item.getoEdate() + " " + item.getoEtime())).start();
                holder.txtTixketNo.setTextColor(ContextCompat.getColor(context, R.color.bluelayout));
                holder.txtTixketNo.setBackgroundResource(R.drawable.btn_bglightbluebgcard);
                holder.bidstarttxt.setText(R.string.string442+" "+MainActivity.currency+item.getoPrice());

            }
        }catch (Exception ignore){

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
                            i.putExtra("check", "shop");
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
        }catch (Exception ignore){}



    }

    @Override
    public int getItemCount() {
        if(from.equalsIgnoreCase("shop") && !fromCategorySelected)
            return Math.min(items.size(), 4);
        else
            return items.size();
    }

    public void makenotification(String name,String time,int pos){
        GetRedeem.JSONDATum item = items.get(pos);

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
        editor.putString("cdesc", item.getcDesc());
        editor.putString("umax", item.getoUmax());
        editor.putString("limit", item.getoUlimit());
        editor.putString("totalbids", item.getTotalbids());
        editor.putString("id", item.getId());
        editor.apply();


        Intent i=new Intent(context, AlarmReceiver.class);
        PendingIntent pending=PendingIntent.getBroadcast(context,id,i,PendingIntent.FLAG_MUTABLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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

    public void createchannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("CHANNEL_ID","Prizex", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Prizex Notification Channel");

            NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtTixketNo,productnametxt,bidstarttxt,placebidtxt,objAmounttxt;
        ImageView imageView2,coinimg,wishlistred,wishlistblack,placeimg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTixketNo=itemView.findViewById(R.id.txtTixketNo);
            imageView2=itemView.findViewById(R.id.image2);
            objAmounttxt=itemView.findViewById(R.id.objAmounttxt);
            productnametxt=itemView.findViewById(R.id.productnametxt);
            bidstarttxt=itemView.findViewById(R.id.bidstarttxt);
            placebidtxt=itemView.findViewById(R.id.placebidtxt);
            coinimg=itemView.findViewById(R.id.coinimg);
            wishlistred=itemView.findViewById(R.id.wishlistred);
            wishlistblack=itemView.findViewById(R.id.wishlistblack);
            placeimg=itemView.findViewById(R.id.placeimg);



        }
    }

    public void doWork(final TextView textView, final String o_etime)
    {
        ((Activity)context).runOnUiThread(new Runnable()
        {
            public void run()
            {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                start_date = currentDate + " " + currentTime;
                findDifference(start_date, textView, o_etime);
            }
        });
    }

    class CountDownRunner implements Runnable
    {
        TextView textView;
        String o_etime;
        public CountDownRunner(TextView tx_time, String o_etime) {
            this.textView=tx_time;
            this.o_etime=o_etime;
        }

        public void run()
        {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(textView,o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                }
                catch (InterruptedException e) { Thread.currentThread().interrupt();}
                catch(Exception ignore) {}
            }
        }
    }

    void findDifference(String start_date, TextView textView, String end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            String diff="";
            if(difference_In_Days!=0)
                diff = difference_In_Days + "d " + difference_In_Hours + "h " + (difference_In_Minutes<10?"0"+difference_In_Minutes:difference_In_Minutes) + "m " + (difference_In_Seconds<10?"0"+difference_In_Seconds:difference_In_Seconds) + "s";
            else
                diff =  difference_In_Hours + "h " + (difference_In_Minutes<10?"0"+difference_In_Minutes:difference_In_Minutes) + "m " + (difference_In_Seconds<10?"0"+difference_In_Seconds:difference_In_Seconds) + "s";


            if (diff.equalsIgnoreCase("0d 0h 00m 00s") || diff.equalsIgnoreCase("0h 00m 00s"))
                textView.setText(" Ended");
            else {
                textView.setText(" " + diff);
            }
        }
        catch (Exception ignore) {}
    }

    public void filterList(ArrayList<GetRedeem.JSONDATum> filtered_list){
        this.items=filtered_list;
        notifyDataSetChanged();
    }


}
