package com.wowcodes.supreme.Adapter;

import static com.wowcodes.supreme.Constants.imageurl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wowcodes.supreme.Activity.GetOrderActivity;
import com.wowcodes.supreme.Activity.KycUpdateActivity;
import com.wowcodes.supreme.Activity.ReferralsActivity;
import com.wowcodes.supreme.Activity.ScratchRewardsActivity;
import com.wowcodes.supreme.Activity.WebViewActivity;
import com.wowcodes.supreme.Modelclas.GetNotification;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<GetNotification.Get_notification_Inner> chaptersModelArrayList;

    public NotificationsAdapter(Context context, ArrayList<GetNotification.Get_notification_Inner> chaptersModelArrayList) {
        this.mContext = context;
        this.chaptersModelArrayList = chaptersModelArrayList;
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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext)
                .load((chaptersModelArrayList.get(position).getImage().contains("//")?"":imageurl)+chaptersModelArrayList.get(position).getImage())
                .error(R.drawable.img_logo)
                .fitCenter()
                .into(holder.icon_img);

        if(chaptersModelArrayList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.card.getBackground().setColorFilter(Color.parseColor("#f5f4fa"), PorterDuff.Mode.SRC_ATOP);
            holder.new_notification.setVisibility(View.VISIBLE);
        }
        else {
            holder.card.getBackground().setColorFilter(Color.parseColor("#fbfaf8"), PorterDuff.Mode.SRC_ATOP);
            holder.new_notification.setVisibility(View.INVISIBLE);
        }

        holder.title.setText(chaptersModelArrayList.get(position).getTittle());
        holder.desc.setText(chaptersModelArrayList.get(position).getBody());
        holder.date.setText(chaptersModelArrayList.get(position).getTime()==null?"A While Ago":chaptersModelArrayList.get(position).getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int action=Integer.parseInt(chaptersModelArrayList.get(position).getAction());
                String fields=chaptersModelArrayList.get(position).getLink();

                if(action == 1)
                    mContext.startActivity(new Intent(mContext, KycUpdateActivity.class));
                else if(action == 2)
                    mContext.startActivity(new Intent(mContext, ScratchRewardsActivity.class));
                else if(action == 3){
                    if (!fields.isEmpty()){
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("url", fields);
                        intent.putExtra("from", "main");
                        intent.putExtra("title", fields.substring(fields.indexOf(".")+1));
                        mContext.startActivity(intent);
                    }
                }
                else if(action == 4)
                    mContext.startActivity(new Intent(mContext, GetOrderActivity.class));
                else if(action == 5)
                    mContext.startActivity(new Intent(mContext, ReferralsActivity.class));

                if(action!=0) {
                    chaptersModelArrayList.get(position).setStatus("2");
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chaptersModelArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon_img,new_notification;
        TextView title,desc,date;
        LinearLayout card;

        ViewHolder(View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            new_notification=itemView.findViewById(R.id.new_notification);
            icon_img=itemView.findViewById(R.id.icon_img);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            date=itemView.findViewById(R.id.date);
        }
    }
}  