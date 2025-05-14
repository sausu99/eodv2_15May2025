package com.wowcodes.supreme.Adapter;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetConsolation;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScratchRewardsAdapter extends RecyclerView.Adapter<ScratchRewardsAdapter.ViewHolder> {
    ViewHolder viewHolder;
    Context mContext;
    ArrayList<GetConsolation.Get_consolation_Inner> chaptersModelArrayList;
    BindingService videoService;
    Intent i;

    public ScratchRewardsAdapter(Context mContext, ArrayList<GetConsolation.Get_consolation_Inner> chaptersModelArrayList) {
        this.mContext = mContext;
        this.chaptersModelArrayList = chaptersModelArrayList;
        i=new Intent(mContext, MainActivity.class);
    }

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.scratch_card, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        set_colors(position,holder.scratch,holder.prize);
        if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("1")) {
            //not scratched
            holder.card.setVisibility(GONE);
            holder.scratch.setVisibility(VISIBLE);
            holder.locked.setVisibility(GONE);
            holder.redeemed.setVisibility(GONE);
        }
        else if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("2")) {
            //scratched
            holder.card.setVisibility(VISIBLE);
            holder.scratch.setVisibility(GONE);
            holder.locked.setVisibility(GONE);
            holder.redeemed.setVisibility(GONE);
        }
        else if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("3")) {
            //claimed
            holder.card.setVisibility(VISIBLE);
            holder.scratch.setVisibility(GONE);
            holder.locked.setVisibility(GONE);
            holder.prize.setVisibility(GONE);
            holder.redeemed.setVisibility(VISIBLE);
        }
        else{
            //locked
            holder.card.setVisibility(GONE);
            holder.scratch.setVisibility(VISIBLE);
            holder.locked.setVisibility(VISIBLE);
            holder.redeemed.setVisibility(GONE);
        }



        if(chaptersModelArrayList.get(position).getS_expired().equalsIgnoreCase("0")) {
            holder.expired.setVisibility(GONE);
            holder.trophy.setImageDrawable(mContext.getDrawable(R.drawable.trophy));
        }
        else {
            if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("3")){
                holder.card.setVisibility(VISIBLE);
                holder.redeemed.setVisibility(VISIBLE);
                holder.prize.setVisibility(GONE);
                holder.scratch.setVisibility(GONE);
            }
            else {
                holder.locked.setVisibility(GONE);
                holder.prize.setVisibility(GONE);
                holder.expired.setVisibility(VISIBLE);
            }
            holder.trophy.setImageDrawable(mContext.getDrawable(R.drawable.trophy_bw));
        }

        if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("1"))
            holder.prize.setText(chaptersModelArrayList.get(position).getS_code()+" Coins");
        else if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("2"))
            holder.prize.setText(chaptersModelArrayList.get(position).getS_name());
        else
            holder.prize.setText(chaptersModelArrayList.get(position).getS_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chaptersModelArrayList.get(position).getS_expired().equalsIgnoreCase("1"))
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.rewardexpired), Toast.LENGTH_SHORT).show();
                else{
                    if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("0"))
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.rewardlocked), Toast.LENGTH_SHORT).show();
                    else if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("3"))
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.rewardclaimed), Toast.LENGTH_SHORT).show();
                    else{
                        Dialog dialog = new Dialog(mContext);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_scratch);
                        Window window = dialog.getWindow();
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int width = displayMetrics.widthPixels;
                        int height = displayMetrics.heightPixels;
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        ScratchView scratchView=dialog.findViewById(R.id.scratch);
                        TextView prize=dialog.findViewById(R.id.prize);
                        TextView prize2=dialog.findViewById(R.id.prize2);
                        ImageView item_img=dialog.findViewById(R.id.product_img);
                        TextView bonus=dialog.findViewById(R.id.bonus);
                        LinearLayout coupon=dialog.findViewById(R.id.coupon);
                        TextView code=dialog.findViewById(R.id.code);
                        TextView copy=dialog.findViewById(R.id.copy);
                        TextView steps=dialog.findViewById(R.id.steps);
                        TextView desc=dialog.findViewById(R.id.desc);
                        TextView redeem=dialog.findViewById(R.id.redeem);
                        TextView congrats=dialog.findViewById(R.id.congrats);
                        String text = "<font color=#cc0029>C</font><font color=#ffcc00>O</font><font color=#08a9f1>N</font><font color=#536bfb>G</font><font color=#e22062>R</font><font color=#43a148>A</font><font color=#f96a3e>T</font><font color=#ec3732>U</font><font color=#0ff>L</font><font color=#ffcc00>A</font><font color=#536bfb>T</font><font color=#ffcc00>I</font><font color=#f28a00>O</font><font color=#ffcc00>N</font><font color=#d83ff2>S </font><font color=#ff0>!</font><font color=#43a148>!</font>";
                        congrats.setText(Html.fromHtml(text));
                        LinearLayout card=dialog.findViewById(R.id.card);
                        LinearLayout details=dialog.findViewById(R.id.details);

                        scratchView.setStrokeWidth(11);

                        set_dialog_colors(position,prize,prize2);
                        desc.setText(chaptersModelArrayList.get(position).getS_desc());
                        if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("1")) {
                            prize.setText(chaptersModelArrayList.get(position).getS_code() + " Coins");
                            prize2.setText("You won : "+chaptersModelArrayList.get(position).getS_code() + " Coins");
                            bonus.setText("+ "+chaptersModelArrayList.get(position).getS_code() + " Coins");

                            redeem.setText(mContext.getText(R.string.redeemnow));
                            item_img.setVisibility(GONE);
                            coupon.setVisibility(GONE);
                            bonus.setVisibility(VISIBLE);
                            steps.setText(mContext.getText(R.string.coin_steps));
                        }
                        else if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("2")){
                            prize.setText(chaptersModelArrayList.get(position).getS_name());
                            prize2.setText("You won : "+chaptersModelArrayList.get(position).getS_name());

                            redeem.setText(mContext.getText(R.string.redeemnow));
                            get_product_details(chaptersModelArrayList.get(position).getS_code(),item_img,chaptersModelArrayList.get(position).getS_id());
                            item_img.setVisibility(VISIBLE);
                            coupon.setVisibility(GONE);
                            bonus.setVisibility(GONE);
                            steps.setText(mContext.getText(R.string.item_steps));
                        }
                        else {
                            prize.setText(chaptersModelArrayList.get(position).getS_name());
                            prize2.setText("You won : "+chaptersModelArrayList.get(position).getS_name());
                            code.setText(chaptersModelArrayList.get(position).getS_code().toUpperCase());
                            copy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Prizex", chaptersModelArrayList.get(position).getS_code().toUpperCase());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(mContext, "Code copied to Cipboard !", Toast.LENGTH_SHORT).show();
                                }
                            });

                            redeem.setText(mContext.getText(R.string.shopnow));
                            coupon.setVisibility(VISIBLE);
                            item_img.setVisibility(GONE);
                            bonus.setVisibility(GONE);
                            steps.setText(mContext.getText(R.string.coupon_steps));
                        }


                        redeem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("1")) {
                                    if(redeem.getText().toString().equalsIgnoreCase(mContext.getText(R.string.redeemnow).toString())) {
                                        updatemoney(chaptersModelArrayList.get(position).getS_code());
                                        update_api("3", chaptersModelArrayList.get(position).getS_id());
                                        chaptersModelArrayList.get(position).setS_status("3");
                                        holder.scratch.setVisibility(GONE);
                                        holder.card.setVisibility(VISIBLE);
                                        holder.redeemed.setVisibility(VISIBLE);
                                        holder.prize.setVisibility(GONE);
                                        redeem.setText(mContext.getText(R.string.claimed));
                                    }
                                }
                                else if(chaptersModelArrayList.get(position).getS_type().equalsIgnoreCase("2")) {
                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();
                                }
                                else
                                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chaptersModelArrayList.get(position).getS_link().startsWith("https://")?chaptersModelArrayList.get(position).getS_link():"https://"+chaptersModelArrayList.get(position).getS_link())));
                            }
                        });


                        if(chaptersModelArrayList.get(position).getS_status().equalsIgnoreCase("1")){
                            details.setVisibility(GONE);
                            scratchView.setVisibility(VISIBLE);
                        }
                        else{
                            scratchView.setVisibility(GONE);
                            details.setVisibility(VISIBLE);
                            card.setVisibility(VISIBLE);
                        }


                        scratchView.setRevealListener(new ScratchView.IRevealListener() {
                            @Override
                            public void onRevealed(ScratchView scratchView) {
                                scratchView.setVisibility(GONE);
                                Toast.makeText(mContext, "CONGRATULATIONS !!", Toast.LENGTH_SHORT).show();
                                details.setVisibility(VISIBLE);
                                update_api("2",chaptersModelArrayList.get(position).getS_id());
                                chaptersModelArrayList.get(position).setS_status("2");
                                holder.scratch.setVisibility(GONE);
                                holder.card.setVisibility(VISIBLE);
                            }
                            @Override
                            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                            }
                        });
                    }
                }
            }
        });
    }

    public void updatemoney(String coins){
        try {
            videoService.post_addUserBal(new SavePref(mContext).getUserId(),coins,"8").enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if(response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1"))
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.coinsadded), Toast.LENGTH_SHORT).show();
                }
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void set_colors(int position, ImageView scratch , TextView prize){
        if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("1")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.blue_card));
            prize.setTextColor(Color.parseColor("#08a9f1"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("2")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.dark_blue_card));
            prize.setTextColor(Color.parseColor("#536bfb"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("3")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.dark_pink_card));
            prize.setTextColor(Color.parseColor("#e22062"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("4")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.green_card));
            prize.setTextColor(Color.parseColor("#43a148"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("5")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.orange_card));
            prize.setTextColor(Color.parseColor("#f96a3e"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("6")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.pink_card));
            prize.setTextColor(Color.parseColor("#f5407d"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("7")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.red_card));
            prize.setTextColor(Color.parseColor("#ec3732"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("8")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.teal_green_card));
            prize.setTextColor(Color.parseColor("#008e80"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("9")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.violet_card));
            prize.setTextColor(Color.parseColor("#d83ff2"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("10")){
            scratch.setImageDrawable(mContext.getDrawable(R.drawable.yellow_card));
            prize.setTextColor(Color.parseColor("#f28a00"));
        }
    }

    public void set_dialog_colors(int position, TextView prize,TextView prize2){
        if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("1")){
            prize.setTextColor(Color.parseColor("#08a9f1"));
            prize2.setTextColor(Color.parseColor("#08a9f1"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("2")){
            prize.setTextColor(Color.parseColor("#536bfb"));
            prize2.setTextColor(Color.parseColor("#536bfb"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("3")){
            prize.setTextColor(Color.parseColor("#e22062"));
            prize2.setTextColor(Color.parseColor("#e22062"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("4")){
            prize.setTextColor(Color.parseColor("#43a148"));
            prize2.setTextColor(Color.parseColor("#43a148"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("5")){
            prize.setTextColor(Color.parseColor("#f96a3e"));
            prize2.setTextColor(Color.parseColor("#f96a3e"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("6")){
            prize.setTextColor(Color.parseColor("#f5407d"));
            prize2.setTextColor(Color.parseColor("#f5407d"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("7")){
            prize.setTextColor(Color.parseColor("#ec3732"));
            prize2.setTextColor(Color.parseColor("#ec3732"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("8")){
            prize.setTextColor(Color.parseColor("#008e80"));
            prize2.setTextColor(Color.parseColor("#008e80"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("9")){
            prize.setTextColor(Color.parseColor("#d83ff2"));
            prize2.setTextColor(Color.parseColor("#d83ff2"));
        }
        else if(chaptersModelArrayList.get(position).getS_colour().equalsIgnoreCase("10")){
            prize.setTextColor(Color.parseColor("#f28a00"));
            prize2.setTextColor(Color.parseColor("#f28a00"));
        }
    }

    public void get_product_details(String oid,ImageView item_img,String s_id){
        try {
            videoService.get_product(oid).enqueue(new Callback<GetCategories>() {
                @Override
                public void onResponse(Call<GetCategories> call, retrofit2.Response<GetCategories> response) {
                    GetCategories.JSONDATum item=response.body().getJsonData().get(0);
                    try {
                        Glide.with(mContext)
                                .load(item.getoImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .into(item_img);
                    }catch (Exception ignore){}

                    switch (item.getoType()){
                        case "1":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("check", "live");
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("claimable",s_id);
                            break;

                        case "2":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("claimable",s_id);
                            break;

                        case "4":
                            i = new Intent(mContext, BeforeRaffleActivity.class);
                            i.putExtra("check", "draw");
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , item.getoQty());
                            i.putExtra("type" , item.getoType());
                            i.putExtra("name" , item.getoName());
                            i.putExtra("etime" , item.getoEtime());
                            i.putExtra("edate" , item.getoEdate());
                            i.putExtra("image" , item.getoImage());
                            i.putExtra("desc" , item.getoDesc());
                            i.putExtra("coins" , item.getoAmount());
                            i.putExtra("oamt" , item.getoAmount());
                            i.putExtra("colorcode" , item.getcColor());
                            i.putExtra("umax" , item.getoUmax());
                            i.putExtra("cdesc" ,item.getcDesc());
                            i.putExtra("link" , item.getoLink());
                            if (TextUtils.isEmpty(item.getoUlimit())) {
                                i.putExtra("limit" , "1");
                            } else {
                                i.putExtra("limit" , item.getoUlimit());
                            }
                            i.putExtra("id",item.getId());
                            i.putExtra("claimable",s_id);
                            break;

                        case "5":
                            i = new Intent(mContext, BeforeRaffleActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "raffle");
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , item.getoQty());
                            i.putExtra("type" , item.getoType());
                            i.putExtra("claimable",s_id);
                            break;

                        case "7":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("claimable",s_id);
                            break;

                        case "8":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(item.getoQty()));
                            i.putExtra("type" , item.getoType());
                            i.putExtra("claimable",s_id);
                            break;

                        case "3":
                        case "9":
                            i= new Intent(mContext, ShopItemsActivity.class);
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
                            i.putExtra("claimable",s_id);
                            break;

                        default:
                            i= new Intent(mContext, CategoryDetailsActivity.class);
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
                            i.putExtra("colorcode", item.getcColor());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("id", item.getId());
                            i.putExtra("claimable",s_id);
                    }
                }
                @Override public void onFailure(Call<GetCategories> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void update_api(String status,String sid){
        try {
            videoService.update_consolation(status,sid).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }


    @Override
    public int getItemCount() {
        return chaptersModelArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expired,redeemed,prize;
        ImageView scratch,trophy;
        LinearLayout card,locked;

        ViewHolder(View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            scratch=itemView.findViewById(R.id.scratch);
            expired=itemView.findViewById(R.id.expired);
            redeemed=itemView.findViewById(R.id.claimed);
            locked=itemView.findViewById(R.id.locked);
            trophy=itemView.findViewById(R.id.trophy);
            prize=itemView.findViewById(R.id.prize);
        }
    }
}  