package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
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
import com.wowcodes.supreme.Modelclas.GetPrizes;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class PrizeListAdapter extends RecyclerView.Adapter<PrizeListAdapter.ViewHolder> {

    ViewHolder viewHolder;
    Context mContext;
    ArrayList<GetPrizes.Get_prizes_Inner> chaptersModelArrayList;

    public PrizeListAdapter(Context context, ArrayList<GetPrizes.Get_prizes_Inner> chaptersModelArrayList) {
        this.mContext = context;
        this.chaptersModelArrayList = chaptersModelArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.prizes_card, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.rank.setText(mContext.getResources().getString(R.string.rank)+" "+chaptersModelArrayList.get(position).getRank());
        holder.name.setText(chaptersModelArrayList.get(position).getPrize_name());
        Glide.with(mContext)
                .load(chaptersModelArrayList.get(position).getPrize_image())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
        return chaptersModelArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank,name;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            rank=itemView.findViewById(R.id.rank);
            name=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.image);
        }
    }
}  