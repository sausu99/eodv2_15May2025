/**
 * The `BannerAdapter` class is a RecyclerView adapter used to display a list of banner images in a
 * ViewPager2.
 */
package com.wowcodes.supreme.Adapter;
import static android.view.View.GONE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.WebViewActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetSellerDetails;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private Context context;
    private List<String> bannerUrls;
    private List<String> bannerLinks;
    private List<String> bannerNames;
    private ViewPager2 viewPager2;
    BindingService videoService;
    RecyclerView seller_items;
    String SellerName,SellerAbout,SellerLink,SellerImage,SellerJoinDate;
    public GetCategories.JSONDATum item;
    public BannerAdapter(Context context, List<String> bannerUrls, List<String> bannerLinks , List<String> bannerNames , ViewPager2 viewPager2) {
        this.context = context;
        this.bannerUrls = bannerUrls;
        this.bannerLinks = bannerLinks;
        this.bannerNames=bannerNames;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        String imageUrl = bannerUrls.get(position);
        String o_link = bannerLinks.get(position);
        String o_name = bannerNames.get(position);
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.bannerImageView);

        if(o_link!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Objects.equals(new SavePref(context).getUserId(), "0")){
                        Toast.makeText(context, context.getString(R.string.loginfirst), Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                    else {
                        if (!o_link.isEmpty()) {
                            if (o_link.contains("o_id="))
                                open_product_activity(o_link.replace("o_id=", ""));
                            else if (o_link.contains("s_id=")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    open_seller(o_link.replace("s_id=", ""));
                            } else {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", o_link);
                                intent.putExtra("from", "banner");
                                intent.putExtra("title", o_name);
                                context.startActivity(intent);
                            }
                        }
                    }
                }
            });
        }

        if(position == bannerUrls.size() -2)
            viewPager2.post(runnable);
    }

    @Override public int getItemCount() {
        return bannerUrls.size();
    }

    public void addBanners(List<String> newBanners,List<String> newBannerLinks) {
        int insertPosition = bannerUrls.size();
        bannerUrls.addAll(newBanners);
        bannerLinks.addAll(newBannerLinks);
        bannerNames.addAll(newBannerLinks);
        notifyItemRangeInserted(insertPosition, newBanners.size());
        notifyItemRangeInserted(insertPosition,newBannerLinks.size());
    }

    public void clearBanners() {
        bannerUrls.clear();
        bannerLinks.clear();
        bannerNames.clear();
        notifyDataSetChanged();
    }

    public void open_product_activity(String oId){
        try {
        videoService.get_product(oId).enqueue(new Callback<GetCategories>() {
            @Override
            public void onResponse(Call<GetCategories> call, retrofit2.Response<GetCategories> response) {
                try {
                    GetCategories.JSONDATum item = response.body().getJsonData().get(0);
                    Intent i;
                    switch (item.getoType()) {
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
                            if (TextUtils.isEmpty(item.getoUlimit()))
                                i.putExtra("limit", "1");
                            else
                                i.putExtra("limit", item.getoUlimit());
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
                            i.putExtra("oid", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("colorcode", item.getoColor());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
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
                            i.putExtra("oid", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("colorcode", item.getcColor());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("id", item.getId());
                    }

                    context.startActivity(i);
                }catch (Exception ignore){}
            }
            @Override public void onFailure(Call<GetCategories> call, Throwable t) {}
        });
        } catch (Exception ignore) {}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void open_seller(String seller){
        try {
            callgetApiSeller().enqueue(new Callback<GetSellerDetails>() {
                @Override
                public void onResponse(Call<GetSellerDetails> call, retrofit2.Response<GetSellerDetails> response) {
                    List<GetSellerDetails.JSONDATum> arrayList = new ArrayList<>(response.body().getJsonData());
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        GetSellerDetails.JSONDATum reqsellers = arrayList.stream()
                                .filter( reqseller -> seller.equals(reqseller.getId()))
                                .findAny()
                                .orElse(null);
                        SellerName = reqsellers.getName();
                        SellerAbout = reqsellers.getAbout();
                        SellerLink = reqsellers.getLink();
                        SellerJoinDate=reqsellers.getJoin_date();
                        SellerImage = reqsellers.getImage();

                        Dialog dialog=new Dialog(context);
                        dialog.setContentView(R.layout.activity_user_profile);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                            dialog.getWindow().setLayout(dialog.getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().width()-110, dialog.getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().height()-350);
                        dialog.setCancelable(true);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                        TextView seller_name=dialog.findViewById(R.id.user_name);
                        TextView txtuser_profile=dialog.findViewById(R.id.txtuser_profile);
                        TextView join_date=dialog.findViewById(R.id.join_date);
                        TextView txtuser_int=dialog.findViewById(R.id.txtuser_int);
                        TextView sellerWebsite=dialog.findViewById(R.id.seller_website);
                        ImageView seller_img=dialog.findViewById(R.id.profile_img);
                        ImageView close=dialog.findViewById(R.id.close);
                        seller_items=dialog.findViewById(R.id.user_int);
                        seller_items.setLayoutManager(new LinearLayoutManager(context));

                        try {
                            if (SellerLink.isEmpty())
                                sellerWebsite.setVisibility(GONE);

                            LocalDate currentDate = LocalDate.parse(SellerJoinDate);
                            Month mon = currentDate.getMonth();
                            int year = currentDate.getYear();
                            String month=String.valueOf(mon.toString().charAt(0)).toUpperCase() + mon.toString().substring(1).toLowerCase();

                            txtuser_profile.setText(context.getResources().getString(R.string.seller_profile));
                            seller_name.setText(SellerName);
                            join_date.setText(context.getResources().getString(R.string.member_since)+month+" "+year);
                            txtuser_int.setText("- "+SellerName+ context.getResources().getString(R.string.other_products));

                            Glide.with(context)
                                    .load(SellerImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerInside()
                                    .into(seller_img);
                        } catch (Exception ignore) {}

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });

                        sellerWebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!SellerLink.isEmpty()){
                                    Intent intent = new Intent(context, WebViewActivity.class);
                                    intent.putExtra("url", SellerLink);
                                    intent.putExtra("from", "winner_list");
                                    intent.putExtra("title", "2");
                                    context.startActivity(intent);
                                }
                            }
                        });

                        videoService.get_seller_items(seller).enqueue(new Callback<GetSellerItems>() {
                            @Override
                            public void onResponse(Call<GetSellerItems> call, retrofit2.Response<GetSellerItems> response) {
                                    seller_items.setAdapter(new ProductListAdapter(context,response.body().getJSON_DATA(), seller));
                            }
                            @Override public void onFailure(Call<GetSellerItems> call, Throwable t) {}
                        });

                        dialog.show();
                    }
                }
                @Override public void onFailure(Call<GetSellerDetails> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    private Call<GetSellerDetails> callgetApiSeller() {
        return videoService.get_seller();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.bannerImage);
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bannerUrls.addAll(bannerUrls);
            bannerLinks.addAll(bannerLinks);
            bannerNames.addAll(bannerNames);
            notifyDataSetChanged();
        }
    };
}