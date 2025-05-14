package com.wowcodes.supreme.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.WishlistItem;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistItemAdapter extends RecyclerView.Adapter<WishlistItemAdapter.WishlistViewHolder> {

    private Context context;
    private List<WishlistItem.Item> wishlistItems;
    BindingService videoService;


    // Constructor
    public WishlistItemAdapter(Context context, List<WishlistItem.Item> wishlistItems) {
        this.context = context;
        this.wishlistItems = wishlistItems;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist_layout, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
      try{


        // Check if the position is valid
        if (position < 0 || position >= wishlistItems.size()) {
            return; // Or handle this case as needed
        }

        // Get the item at the current position
        WishlistItem.Item item = wishlistItems.get(position);

        // Check if the item is null
        if (item == null) {
            return; // Or handle this case as needed
        }

        // Bind the item name to the TextView
        holder.itemName.setText(item.getO_name());

        // Load the image using Glide with error handling
        Glide.with(context)
                .load(item.getO_image())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.itemImg);

        // Check if available_items is not null and not empty
        if (item.getAvailable_items() == null || item.getAvailable_items().isEmpty()) {
            holder.addmorebtn.setVisibility(View.GONE); // Hide the button if no items available
            return;
        }

        // Get the first available item
        String oid = item.getAvailable_items().get(0).getO_id();
        String itemType = item.getAvailable_items().get(0).getO_type();
        setAddMoreBtnText(holder.addmorebtn, itemType);

        // Create the videoService instance
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        // Fetch the product details
        videoService.get_product(item.getAvailable_items().get(0).getO_id()).enqueue(new Callback<GetCategories>() {
            @Override
            public void onResponse(Call<GetCategories> call, Response<GetCategories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetCategories.JSONDATum> jsonData = response.body().getJsonData();
                    if (jsonData != null && !jsonData.isEmpty()) {
                        GetCategories.JSONDATum productitem = jsonData.get(0);


                        holder.addmorebtn.setOnClickListener(v -> {
                            Intent intent;
                            switch (itemType) {
                                case "1":{
                                    intent = new Intent(context, AuctionActivity.class);
                                    intent.putExtra("check", "live");
                                    intent.putExtra("O_id", oid);
                                    context.startActivity(intent);
                                    break;
                                }
                                case "2":{
                                    intent = new Intent(context, AuctionActivity.class);
                                    intent.putExtra("check", "live");
                                    intent.putExtra("O_id", oid);
                                    context.startActivity(intent);
                                    break;

                                }
                                case "7":{
                                    intent = new Intent(context, AuctionActivity.class);
                                    intent.putExtra("check", "live");
                                    intent.putExtra("O_id", oid);
                                    context.startActivity(intent);
                                    break;
                                }
                                case "10":{
                                    intent = new Intent(context, AuctionActivity.class);
                                    intent.putExtra("check", "live");
                                    intent.putExtra("O_id", oid);
                                    context.startActivity(intent);
                                    break;
                                }


                                case "8":{
                                    intent = new Intent(context, AuctionActivity.class);
                                    intent.putExtra("check", "live");
                                    intent.putExtra("O_id", oid);
                                    intent.putExtra("total_bids", productitem.getTotalbids());
                                    intent.putExtra("qty", Integer.parseInt(productitem.getoQty()));
                                    intent.putExtra("type", productitem.getoType());
                                    context.startActivity(intent);
                                    break;
                                }


                                case "3":{
                                    Intent shopIntent = new Intent(context, ShopItemsActivity.class);
                                    shopIntent.putExtra("image", productitem.getoImage());
                                    shopIntent.putExtra("image1", productitem.getoImage1());
                                    shopIntent.putExtra("image2", productitem.getoImage2());
                                    shopIntent.putExtra("image3", productitem.getoImage3());
                                    shopIntent.putExtra("image4", productitem.getoImage4());
                                    shopIntent.putExtra("name", productitem.getoName());
                                    shopIntent.putExtra("type", productitem.getoType());
                                    shopIntent.putExtra("desc", productitem.getoDesc());
                                    shopIntent.putExtra("edate", productitem.getoEdate());
                                    shopIntent.putExtra("etime", productitem.getoEtime());
                                    shopIntent.putExtra("coins", productitem.getoPrice());
                                    shopIntent.putExtra("oid", productitem.getoId());
                                    shopIntent.putExtra("qty", productitem.getoQty());
                                    shopIntent.putExtra("oamt", productitem.getoAmount());
                                    shopIntent.putExtra("link", productitem.getoLink());
                                    shopIntent.putExtra("colorcode", productitem.getoColor());
                                    shopIntent.putExtra("cdesc", productitem.getcDesc());
                                    shopIntent.putExtra("umax", productitem.getoUmax());
                                    shopIntent.putExtra("limit", productitem.getoUlimit());
                                    shopIntent.putExtra("totalbids", productitem.getTotalbids());
                                    shopIntent.putExtra("id", productitem.getId());
                                    shopIntent.putExtra("itemId", item.getItem_id());
                                    context.startActivity(shopIntent);
                                    break;}

                                case "4":{
                                    Intent raffleIntent = new Intent(context, BeforeRaffleActivity.class);
                                    raffleIntent.putExtra("check", "draw");
                                    raffleIntent.putExtra("O_id", productitem.getoId());
                                    raffleIntent.putExtra("total_bids", productitem.getTotalbids());
                                    raffleIntent.putExtra("qty", productitem.getoQty());
                                    raffleIntent.putExtra("type", productitem.getoType());
                                    raffleIntent.putExtra("name", productitem.getoName());
                                    raffleIntent.putExtra("etime", productitem.getoEtime());
                                    raffleIntent.putExtra("edate", productitem.getoEdate());
                                    raffleIntent.putExtra("image", productitem.getoImage());
                                    raffleIntent.putExtra("desc", productitem.getoDesc());
                                    raffleIntent.putExtra("coins", productitem.getoAmount());
                                    raffleIntent.putExtra("oamt", productitem.getoAmount());
                                    raffleIntent.putExtra("colorcode", productitem.getcColor());
                                    raffleIntent.putExtra("umax", productitem.getoUmax());
                                    raffleIntent.putExtra("cdesc", productitem.getcDesc());
                                    raffleIntent.putExtra("link", productitem.getoLink());
                                    raffleIntent.putExtra("limit", TextUtils.isEmpty(productitem.getoUlimit()) ? "1" : productitem.getoUlimit());
                                    raffleIntent.putExtra("id", productitem.getId());
                                    context.startActivity(raffleIntent);
                                    break;}

                                case "5":{
                                    Intent raffleIntent5 = new Intent(context, BeforeRaffleActivity.class);
                                    raffleIntent5.putExtra("O_id", productitem.getoId());
                                    raffleIntent5.putExtra("check", "raffle");
                                    raffleIntent5.putExtra("total_bids", productitem.getTotalbids());
                                    raffleIntent5.putExtra("qty", productitem.getoQty());
                                    raffleIntent5.putExtra("type", productitem.getoType());
                                    raffleIntent5.putExtra("etime", productitem.getoEtime());
                                    raffleIntent5.putExtra("edate", productitem.getoEdate());
                                    context.startActivity(raffleIntent5);
                                    break;}

                                case "9":{

                                    Intent shopIntent9 = new Intent(context, ShopItemsActivity.class);
                                    shopIntent9.putExtra("image", productitem.getoImage());
                                    shopIntent9.putExtra("image1", productitem.getoImage1());
                                    shopIntent9.putExtra("image2", productitem.getoImage2());
                                    shopIntent9.putExtra("image3", productitem.getoImage3());
                                    shopIntent9.putExtra("image4", productitem.getoImage4());
                                    shopIntent9.putExtra("name", productitem.getoName());
                                    shopIntent9.putExtra("type", productitem.getoType());
                                    shopIntent9.putExtra("desc", productitem.getoDesc());
                                    shopIntent9.putExtra("edate", productitem.getoEdate());
                                    shopIntent9.putExtra("etime", productitem.getoEtime());
                                    shopIntent9.putExtra("coins", productitem.getoPrice());
                                    shopIntent9.putExtra("oid", productitem.getoId());
                                    shopIntent9.putExtra("qty", productitem.getoQty());
                                    shopIntent9.putExtra("oamt", productitem.getoAmount());
                                    shopIntent9.putExtra("link", productitem.getoLink());
                                    shopIntent9.putExtra("colorcode", productitem.getoColor());
                                    shopIntent9.putExtra("cdesc", productitem.getcDesc());
                                    shopIntent9.putExtra("umax", productitem.getoUmax());
                                    shopIntent9.putExtra("limit", productitem.getoUlimit());
                                    shopIntent9.putExtra("totalbids", productitem.getTotalbids());
                                    shopIntent9.putExtra("id", productitem.getId());
                                    shopIntent9.putExtra("itemId", item.getItem_id());
                                    context.startActivity(shopIntent9);
                                    break;

                                }
                                case "11":{
                                    Intent auctionIntent11 = new Intent(context, AuctionActivity.class);
                                    auctionIntent11.putExtra("check", "live");
                                    auctionIntent11.putExtra("O_id", productitem.getoId());
                                    context.startActivity(auctionIntent11);
                                    break;}

                                case "6":
                                default:{
                                    Intent i = new Intent(context, CategoryDetailsActivity.class);
                                    i.putExtra("image", productitem.getoImage());
                                    i.putExtra("image1", productitem.getoImage1());
                                    i.putExtra("image2", productitem.getoImage2());
                                    i.putExtra("image3", productitem.getoImage3());
                                    i.putExtra("image4", productitem.getoImage4());
                                    i.putExtra("name", productitem.getoName());
                                    i.putExtra("type", productitem.getoType());
                                    i.putExtra("desc", productitem.getoDesc());
                                    i.putExtra("edate", productitem.getoEdate());
                                    i.putExtra("etime", productitem.getoEtime());
                                    i.putExtra("coins", productitem.getoAmount());
                                    i.putExtra("O_id", productitem.getoId());
                                    i.putExtra("qty", productitem.getoQty());
                                    i.putExtra("oamt", productitem.getoAmount());
                                    i.putExtra("link", productitem.getoLink());
                                    i.putExtra("cdesc", productitem.getcDesc());
                                    i.putExtra("umax", productitem.getoUmax());
                                    i.putExtra("limit", productitem.getoUlimit());
                                    i.putExtra("totalbids", productitem.getTotalbids());
                                    i.putExtra("id", productitem.getId());
                                    context.startActivity(i);
                                    break;
                                }
                            }
                        });
                    } else {
                        holder.addmorebtn.setVisibility(View.GONE); // Hide the button if no product item available
                    }
                } else {
                    holder.addmorebtn.setVisibility(View.GONE); // Hide the button if response is not successful
                }
            }

            @Override
            public void onFailure(Call<GetCategories> call, Throwable t) {
                holder.addmorebtn.setVisibility(View.GONE);
                Toast.makeText(context, "Failed to load available items: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Handle remove button click
        holder.removeBtn.setOnClickListener(v -> {
            RetrofitVideoApiBaseUrl.getClient().create(BindingService.class)
                    .updateWishlist(new SavePref(context).getUserId(), item.getItem_id(), "delete")
                    .enqueue(new Callback<WishlistResponse>() {
                        @Override
                        public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                WishlistResponse wishlistResponse = response.body();
                                if (wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                                    WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                                 try{
                                     wishlistItems.remove(holder.getAdapterPosition());
                                     notifyItemRemoved(holder.getAdapterPosition());

                                 }catch (Exception ignore){

                                 }
                                            Toast.makeText(context, "Removed from Wishlist: " + message.getMsg(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Failed to delete from Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<WishlistResponse> call, Throwable t) {
                            Toast.makeText(context, "Failed to delete from Wishlist. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });}catch (Exception ignore){

      }
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }
    private void navigateToMainActivity(int selectedTabIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("SELECTED_TAB_INDEX", selectedTabIndex);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    private void setAddMoreBtnText(TextView addmorebtn, String o_type) {
        switch (o_type) {
            case "1":
            case "2":
            case "7":
            case "8":
            case "10":
                addmorebtn.setText(context.getString(R.string.string523));
                break;
            case "3":
                addmorebtn.setText(context.getString(R.string.string527));
                break;
            case "4":
                addmorebtn.setText(context.getString(R.string.string526));
                break;
            case "5":
                addmorebtn.setText(context.getString(R.string.string525));
                break;
            case "9":
                addmorebtn.setText(context.getString(R.string.string528));
                break;
            case "11":
                addmorebtn.setText(context.getString(R.string.string524));
                break;
            case "6":
            default:
                // No action required or handle unexpected cases if needed
                break;
        }
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        // Define views
        TextView itemName,removeBtn,addmorebtn;
        ImageView itemImg;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            itemName = itemView.findViewById(R.id.itemName);
            itemImg = itemView.findViewById(R.id.itemImg);
            addmorebtn = itemView.findViewById(R.id.addmorebtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);

        }
    }
}

