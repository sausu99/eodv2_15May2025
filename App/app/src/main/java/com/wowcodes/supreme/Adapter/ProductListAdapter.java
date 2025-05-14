package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.LotoDetailActivity;
import com.wowcodes.supreme.Activity.RaffleDetailActivity;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    ViewHolder viewHolder;
    Context mContext;
    String seller_id;
    ArrayList<GetSellerItems.Get_items_Inner> chaptersModelArrayList;

    public ProductListAdapter(Context mContext, ArrayList<GetSellerItems.Get_items_Inner> chaptersModelArrayList,String seller_id) {
        this.mContext = mContext;
        this.chaptersModelArrayList = chaptersModelArrayList;
        this.seller_id=seller_id;
    }

    @Override public long getItemId(int position) {return position;}
    @Override public int getItemViewType(int position) {return position;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_card, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(position == 0){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,10,15,0);
        }
        
        switch (chaptersModelArrayList.get(position).getO_type()) {
            case "1":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.lub));
                break;
            case "2":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.hub));
                break;
            case "3":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.redeem));
                break;
            case "4":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.raffle));
                break;
            case "5":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.lotto));
                break;
            case "7":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.engauc));
                break;
            case "8":
                holder.pro_type.setText(mContext.getResources().getString(R.string.type)+mContext.getResources().getString(R.string.penny));
                break;
            default:
                holder.pro_type.setText("");
                break;
        }
        
        holder.pro_name.setText(chaptersModelArrayList.get(position).getO_name());
        holder.pro_price.setText(mContext.getResources().getString(R.string.string18)+" "+chaptersModelArrayList.get(position).getO_amount());

        try {
            Glide.with(mContext)
                    .load(chaptersModelArrayList.get(position).getO_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.imageview);
        } catch (Exception ignore) {}

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (Objects.equals(new SavePref(mContext).getUserId(), "0")){
                    i=new Intent(mContext, LoginActivity.class);
                    i.putExtra("Decider", "Category");
                }
                else {
                    switch (chaptersModelArrayList.get(position).getO_type()){
                        case "1":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("check", "live");
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            break;
                        case "2":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("check", "live");
                            break;
                        case "4":
                            i = new Intent(mContext, RaffleDetailActivity.class);
                            i.putExtra("check", "draw");
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("total_bids" , chaptersModelArrayList.get(position).getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(chaptersModelArrayList.get(position).getO_qty()));
                            i.putExtra("type" , chaptersModelArrayList.get(position).getO_type());
                            i.putExtra("name" , chaptersModelArrayList.get(position).getO_name());
                            i.putExtra("etime" , chaptersModelArrayList.get(position).getO_etime());
                            i.putExtra("edate" , chaptersModelArrayList.get(position).getO_edate());
                            i.putExtra("image" , chaptersModelArrayList.get(position).getO_image());
                            i.putExtra("desc" , chaptersModelArrayList.get(position).getO_desc());
                            i.putExtra("coins" , chaptersModelArrayList.get(position).getO_amount());
                            i.putExtra("oamt" , chaptersModelArrayList.get(position).getO_amount());
                            i.putExtra("colorcode" , chaptersModelArrayList.get(position).getC_color());
                            i.putExtra("cdesc" ,chaptersModelArrayList.get(position).getC_desc());
                            i.putExtra("link" , chaptersModelArrayList.get(position).getO_link());
                            i.putExtra("id",seller_id);
                            break;
                        case "5":
                            i = new Intent(mContext, LotoDetailActivity.class);
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("check", "raffle");
                            i.putExtra("total_bids" , chaptersModelArrayList.get(position).getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(chaptersModelArrayList.get(position).getO_qty()));
                            i.putExtra("type" , chaptersModelArrayList.get(position).getO_type());
                            break;
                        case "7":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("check", "live");
                            break;
                        case "8":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("check", "live");
                            i.putExtra("total_bids" , chaptersModelArrayList.get(position).getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(chaptersModelArrayList.get(position).getO_qty()));
                            i.putExtra("type" , chaptersModelArrayList.get(position).getO_type());
                            break;
                        case "3":
                        case "9":
                            i= new Intent(mContext, ShopItemsActivity.class);

                            i.putExtra("image", chaptersModelArrayList.get(position).getO_image());
                            i.putExtra("image1", chaptersModelArrayList.get(position).getO_image1());
                            i.putExtra("image2", chaptersModelArrayList.get(position).getO_image2());
                            i.putExtra("image3", chaptersModelArrayList.get(position).getO_image3());
                            i.putExtra("image4", chaptersModelArrayList.get(position).getO_image4());
                            i.putExtra("name", chaptersModelArrayList.get(position).getO_name());
                            i.putExtra("type", chaptersModelArrayList.get(position).getO_type());
                            i.putExtra("desc", chaptersModelArrayList.get(position).getO_desc());
                            i.putExtra("edate", chaptersModelArrayList.get(position).getO_edate());
                            i.putExtra("etime", chaptersModelArrayList.get(position).getO_etime());
                            i.putExtra("coins", chaptersModelArrayList.get(position).getO_price());
                            i.putExtra("oid", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("qty", chaptersModelArrayList.get(position).getO_qty());
                            i.putExtra("oamt", chaptersModelArrayList.get(position).getO_amount());
                            i.putExtra("link", chaptersModelArrayList.get(position).getO_link());
                            i.putExtra("colorcode", chaptersModelArrayList.get(position).getO_color());
                            i.putExtra("cdesc", chaptersModelArrayList.get(position).getC_desc());
                            i.putExtra("totalbids", chaptersModelArrayList.get(position).getTotalbids());
                            i.putExtra("id", seller_id);
                            break;
                        default:
                            i= new Intent(mContext, CategoryDetailsActivity.class);
                            i.putExtra("image", chaptersModelArrayList.get(position).getO_image());
                            i.putExtra("image1", chaptersModelArrayList.get(position).getO_image1());
                            i.putExtra("image2", chaptersModelArrayList.get(position).getO_image2());
                            i.putExtra("image3", chaptersModelArrayList.get(position).getO_image3());
                            i.putExtra("image4", chaptersModelArrayList.get(position).getO_image4());
                            i.putExtra("name", chaptersModelArrayList.get(position).getO_name());
                            i.putExtra("type", chaptersModelArrayList.get(position).getO_type());
                            i.putExtra("desc", chaptersModelArrayList.get(position).getO_desc());
                            i.putExtra("edate", chaptersModelArrayList.get(position).getO_edate());
                            i.putExtra("etime", chaptersModelArrayList.get(position).getO_etime());
                            i.putExtra("coins", chaptersModelArrayList.get(position).getO_amount());
                            i.putExtra("oid", chaptersModelArrayList.get(position).getO_id());
                            i.putExtra("qty", chaptersModelArrayList.get(position).getO_qty());
                            i.putExtra("oamt", chaptersModelArrayList.get(position).getO_amount());
                            i.putExtra("link", chaptersModelArrayList.get(position).getO_link());
                            i.putExtra("colorcode", chaptersModelArrayList.get(position).getC_color());
                            i.putExtra("cdesc", chaptersModelArrayList.get(position).getC_desc());
                            i.putExtra("totalbids", chaptersModelArrayList.get(position).getTotalbids());
                            i.putExtra("id", seller_id);
                    }
                }
                mContext.startActivity(i);
            }
        });
    }

    @Override public int getItemCount() {return chaptersModelArrayList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView pro_name,pro_type,pro_price;

        ViewHolder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageview);
            pro_name=itemView.findViewById(R.id.pro_name);
            pro_type=itemView.findViewById(R.id.pro_type);
            pro_price=itemView.findViewById(R.id.pro_price);
        }
    }
}  