package com.wowcodes.supreme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Modelclas.WinnerMultiple;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;


public class WinnerListHoriAdapter extends RecyclerView.Adapter<WinnerListHoriAdapter.ItemViewHolder>{

    private Context context;
    ArrayList<WinnerMultiple> items;

    public WinnerListHoriAdapter(Context context, ArrayList<WinnerMultiple> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        view= LayoutInflater.from(context).inflate(R.layout.multiple_winnerlist_hori_item, parent, false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        WinnerMultiple item=items.get(position);
        holder.rankNo.setText(item.getRank());
        if (Integer.parseInt(item.getIs_winner()) == 1) {
            holder.youWonH.setVisibility(View.VISIBLE);
        }
        holder.price.setText("Worth"+" "+ MainActivity.currency+item.getItem_worth());
        Glide.with(context)
                .load(item.getO_image())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.obImg);
        holder.winnerName.setText(item.getWinner_name());
        if (item.getWinner_name() == new SavePref(context).getName()) {
            holder.youWonH.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView rankNo;
        TextView winnerName;
        ImageView obImg;
        TextView price;
        ImageView youWonH;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rankNo = itemView.findViewById(R.id.rankno);
            winnerName = itemView.findViewById(R.id.winnername);
            obImg = itemView.findViewById(R.id.obimg);
            price = itemView.findViewById(R.id.price);
            youWonH = itemView.findViewById(R.id.youwonh);

        }
    }

}
