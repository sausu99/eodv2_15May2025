/**
 * The CoinAdapter class is a RecyclerView adapter used to display a list of coins with their names,
 * coin amounts, and prices, and handle the payment process using Razorpay.
 */
package com.wowcodes.supreme.Adapter;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.AuctionWorks;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.DynamicWorks;
import com.wowcodes.supreme.Activity.HighestBidWorks;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.LottoWorks;
import com.wowcodes.supreme.Activity.PennyWorks;
import com.wowcodes.supreme.Activity.RaffleWorks;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Objects;

public class TopDealsAdapter extends RecyclerView.Adapter<TopDealsAdapter.ViewHolder>{
    Context mContext;
    ArrayList<GetCategories.JSONDATum> arrayList;

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }
    public TopDealsAdapter(Context mContext, ArrayList<GetCategories.JSONDATum> arrayList) {
        this.mContext = mContext;
        this.arrayList=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_top_deals, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        GetCategories.JSONDATum item=arrayList.get(position);
        Glide.with(mContext)
                .load(item.getoImage())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.prod_img);

        holder.txtSetName.setText(item.getoName());
        holder.txtSetDesc.setText(item.getoDesc());
        String y=item.getoEdate().substring(0,4);
        int month=Integer.parseInt(item.getoEdate().substring(5,7));
        String d=item.getoEdate().substring(8,10);
        String m;
        switch (month){
            case 1:
                m="Jan";
                break;
            case 2:
                m="Feb";
                break;
            case 3:
                m="March";
                break;
            case 4:
                m="April";
                break;
            case 5:
                m="May";
                break;
            case 6:
                m="June";
                break;
            case 7:
                m="July";
                break;
            case 8:
                m="Aug";
                break;
            case 9:
                m="Sept";
                break;
            case 10:
                m="Oct";
                break;
            case 11:
                m="Nov";
                break;
            default:
                m="Dec";
        }
        int hr=Integer.parseInt(item.getoEtime().substring(0,2));
        String mer=" AM";
        if(hr>12) {
            hr -= 12;
            mer=" PM";
        }
        int mn=Integer.parseInt(item.getoEtime().substring(3,5));
        holder.txtEnds.setText(mContext.getString(R.string.endson)+" "+d+" "+m+" "+y+", "+(hr>9?hr:"0"+hr)+":"+mn+mer);

        int otype=Integer.parseInt(item.getoType());
        if(otype==1 || otype==2 || otype==7 || otype==8)
            holder.icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_auction));
        else if(otype==4 || otype==5)
            holder.icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_ticket));
        else
            holder.icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_bulkcoin));
        holder.txtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (otype) {
                    case 1:
                        mContext.startActivity(new Intent(mContext, AuctionWorks.class));
                        break;
                    case 2:
                        mContext.startActivity(new Intent(mContext, HighestBidWorks.class));
                        break;
                    case 4:
                        mContext.startActivity(new Intent(mContext, RaffleWorks.class));
                        break;
                    case 5:
                        mContext.startActivity(new Intent(mContext, LottoWorks.class));
                        break;
                    case 7:
                        mContext.startActivity(new Intent(mContext, DynamicWorks.class));
                        break;
                    case 8:
                        mContext.startActivity(new Intent(mContext, PennyWorks.class));
                        break;
                }
            }
        });

        switch (otype) {
            case 1:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.lub)+" ");
                break;
            case 2:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.hub)+" ");
                break;
            case 3:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.redeem)+" ");
                break;
            case 4:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.raffle)+" ");
                break;
            case 5:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.lotto)+" ");
                break;
            case 7:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.engauc)+" ");
                break;
            case 8:
                holder.txtIcon.setText(mContext.getResources().getString(R.string.penny)+" ");
                break;
            default:
                holder.txtIcon.setText("");
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float scale = mContext.getResources().getDisplayMetrics().density;
                int pad7 = (int) (7 * scale + 0.5f);
                int pad11 = (int) (11 * scale + 0.5f);

                if(holder.txtIcon.getVisibility()==GONE){
                    holder.aucNameLL.setPadding(pad11,pad7,pad11,pad7);
                    holder.txtIcon.setVisibility(VISIBLE);
                    holder.txtIcon.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.open_tv));
                }
                else{
                    holder.aucNameLL.setPadding(pad7,pad7,pad7,pad7);
                    holder.txtIcon.setVisibility(GONE);
                    holder.txtIcon.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.close_tv));
                }
            }
        });

        

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                String oType=item.getoType();

                if (Objects.equals(new SavePref(mContext).getUserId(), "0")) {
                    i = new Intent(mContext, LoginActivity.class);
                    i.putExtra("Decider", "Category");
                    mContext.startActivity(i);
                } else if (item.getoStatus().equalsIgnoreCase("0")) {
                    if (oType.equalsIgnoreCase("4") || oType.equalsIgnoreCase("5"))
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.pleasewaitlottery), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.pleasewaitauction), Toast.LENGTH_SHORT).show();
                } else {

                    switch (oType) {
                        case "1":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("check", "live");
                            i.putExtra("O_id", item.getoId());
                            break;

                        case "2":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            break;

                        case "4":
                            i = new Intent(mContext, BeforeRaffleActivity.class);
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
                            i = new Intent(mContext, BeforeRaffleActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "raffle");
                            i.putExtra("total_bids", item.getTotalbids());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("type", item.getoType());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("edate", item.getoEdate());
                            break;

                        case "7":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            break;

                        case "8":
                            i = new Intent(mContext, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("total_bids", item.getTotalbids());
                            i.putExtra("qty", Integer.parseInt(item.getoQty()));
                            i.putExtra("type", item.getoType());
                            break;

                        case "3":
                        case "9":
                            i = new Intent(mContext, ShopItemsActivity.class);
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
                            break;

                        default:
                            i = new Intent(mContext, CategoryDetailsActivity.class);
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

                    mContext.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView prod_img,icon;
        TextView txtSetName,txtSetDesc,txtIcon,txtEnds;
        LinearLayout aucNameLL;
        ViewHolder(View itemView) {
            super(itemView);
            prod_img=itemView.findViewById(R.id.prod_img);
            txtSetName=itemView.findViewById(R.id.prod_name);
            txtSetDesc=itemView.findViewById(R.id.prod_desc);
            icon=itemView.findViewById(R.id.infoIcon);
            txtIcon=itemView.findViewById(R.id.aucName);
            txtEnds=itemView.findViewById(R.id.prod_end);
            aucNameLL=itemView.findViewById(R.id.aucNameLL);
        }
    }
}