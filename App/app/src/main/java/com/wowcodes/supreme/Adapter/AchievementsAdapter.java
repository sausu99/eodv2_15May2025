/**
 * The CoinAdapter class is a RecyclerView adapter used to display a list of coins with their names,
 * coin amounts, and prices, and handle the payment process using Razorpay.
 */
package com.wowcodes.supreme.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wowcodes.supreme.Modelclas.GetAchievements;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder>{
    ViewHolder viewHolder;
    Context mContext;
    SavePref savePref;
    ArrayList<GetAchievements.Get_Achievements_Inner> coinModelArrayList;
    BindingService videoService;

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    public AchievementsAdapter(Context context, ArrayList<GetAchievements.Get_Achievements_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
        savePref=new SavePref(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_achievements, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        holder.card.getBackground().setColorFilter(Color.parseColor(coinModelArrayList.get(position).getColor()), PorterDuff.Mode.SRC_ATOP);
        holder.name.setText(coinModelArrayList.get(position).getName());
        holder.desc.setText(coinModelArrayList.get(position).getDescription());
        holder.amt.setText(coinModelArrayList.get(position).getPoints()+" "+mContext.getString(R.string.coins));
        holder.progress.setText(coinModelArrayList.get(position).getProgress()+"/"+coinModelArrayList.get(position).getGoal());
        if(coinModelArrayList.get(position).getStatus().equalsIgnoreCase("0"))
            holder.claim.setText(mContext.getString(R.string.locked2));
        else if(coinModelArrayList.get(position).getStatus().equalsIgnoreCase("1"))
            holder.claim.setText(mContext.getString(R.string.in_progress));
        else if(coinModelArrayList.get(position).getStatus().equalsIgnoreCase("2")){
            holder.claim.setText(mContext.getString(R.string.claim));
            holder.claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatemoney(coinModelArrayList.get(position).getPoints());
                    update_api(coinModelArrayList.get(position).getAchievement_id(),savePref.getUserId());
                    coinModelArrayList.get(position).setStatus("3");
                    holder.claim.setText(mContext.getString(R.string.claimed2));
                    holder.claim.setClickable(false);
                }
            });
        }
        else
            holder.claim.setText(mContext.getString(R.string.claimed2));

        try {
            Glide.with(mContext)
                    .load(coinModelArrayList.get(position).getImage())
                    .placeholder(R.drawable.img_user)
                    .fitCenter()
                    .into(holder.img);
        }catch (Exception ignore){}
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

    public void update_api(String aid,String user_id){
        try {
            videoService.update_achievement(aid,user_id).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {}
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,desc,amt,progress,claim;
        ImageView img;
        LinearLayout card;
        ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            desc=itemView.findViewById(R.id.goal);
            amt=itemView.findViewById(R.id.amt);
            progress=itemView.findViewById(R.id.progress);
            claim=itemView.findViewById(R.id.claim);
            card=itemView.findViewById(R.id.card);
        }
    }
}