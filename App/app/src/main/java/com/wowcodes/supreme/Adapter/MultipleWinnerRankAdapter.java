package com.wowcodes.supreme.Adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.GetOrderActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.SelectAddress;
import com.wowcodes.supreme.Modelclas.MultipleWinnersId;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MultipleWinnerRankAdapter extends RecyclerView.Adapter<MultipleWinnerRankAdapter.ItemViewHolder>{
    private Context context;
    String oid, otype;

    ArrayList<MultipleWinnersId.Winner> items;

    public MultipleWinnerRankAdapter(Context context, String oid, String otype, ArrayList<MultipleWinnersId.Winner> items) {
        this.context = context;
        this.oid = oid;
        this.otype = otype;
        if (items != null ) {
            this.items = new ArrayList<>(items.subList(0, items.size()));
        } else {
            this.items = new ArrayList<>();
        }
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        view= LayoutInflater.from(context).inflate(R.layout.multiple_winner_rank_item, parent, false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MultipleWinnersId.Winner item = items.get(position);

        try {
            holder.ranktxt.setText(context.getString(R.string.rank)+"\n"+item.getRank());
            holder.winnernametxt.setText(item.getWinner_name());
            holder.winningvalue.setText(item.getWinning_value());
            holder.giftcardtxt.setText(item.getO_name());
            Glide.with(context)
                    .load(item.getUser_image())
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.winnerimg);

            if (Integer.parseInt(item.getIs_winner()) == 1 && Integer.parseInt(item.getOrder_placed()) == 0) {
                holder.mainlayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010E1F")));
                holder.ranktxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.winnernametxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.giftcardtxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.winningvalue.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.claimText.setText("You are the Winner!");
                holder.relativeLayout.setVisibility(View.VISIBLE);
                holder.claimPrizeButton.setVisibility(View.VISIBLE);
            } else if (Integer.parseInt(item.getIs_winner()) == 1 && Integer.parseInt(item.getOrder_placed()) == 1) {
                holder.mainlayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010E1F")));
                holder.ranktxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.winnernametxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.giftcardtxt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.winningvalue.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.claimText.setText("You have claimed it!");
                holder.relativeLayout.setVisibility(View.VISIBLE);
                holder.claimPrizeButton.setVisibility(GONE);
                holder.viewOrderButton.setVisibility(View.VISIBLE);
            } else {
                holder.relativeLayout.setVisibility(GONE);
                holder.claimPrizeButton.setVisibility(GONE);
                holder.viewOrderButton.setVisibility(GONE);
            }

        }catch (Exception ignore){

        }
        holder.claimPrizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(new SavePref(context).getUserId(), "0")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    context.startActivity(intent);

                }else{
                    Intent intent = new Intent(context, SelectAddress.class);
                    intent.putExtra("itemid",item.getItem_id() );
                    intent.putExtra("oid",oid);
                    Log.d("itemid",item.getItem_id() );
                    intent.putExtra("otype", otype);
                    String oamt=item.getItem_worth();
                    if ("5".equals(otype)) {
                        oamt = "0";
                    } else if (Arrays.asList("1", "2", "7", "8", "10", "11").contains(otype)) {
                        oamt = item.getWinning_value();
                    } else if (Arrays.asList("3", "9").contains(otype)) {
                        oamt = item.getItem_worth(); // Assuming getO_amount() exists in your model
                    }
                    intent.putExtra("oamt",oamt );
                    context.startActivity(intent);
                }

            }
        });
        holder.viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(new SavePref(context).getUserId(), "0")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    context.startActivity(intent);

                }else{
                    Intent i = new Intent(context, GetOrderActivity.class);
                    context.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView ranktxt, winnernametxt, giftcardtxt, winningvalue;
        ImageView winnerimg;
        RelativeLayout relativeLayout;
        LinearLayout mainlayout;
        TextView claimText;
        Button claimPrizeButton;
        Button viewOrderButton;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ranktxt = itemView.findViewById(R.id.ranktxt);
            winnerimg = itemView.findViewById(R.id.winnerimg);
            winnernametxt = itemView.findViewById(R.id.winnernametxt);
            giftcardtxt = itemView.findViewById(R.id.giftcardtxt);
            winningvalue = itemView.findViewById(R.id.winningvalue);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
            claimText = itemView.findViewById(R.id.claimtxt);
            claimPrizeButton = itemView.findViewById(R.id.claim_prizebtn);
            viewOrderButton = itemView.findViewById(R.id.view_orderbtn);
            mainlayout=itemView.findViewById(R.id.mainlinearll);
        }
    }

}
