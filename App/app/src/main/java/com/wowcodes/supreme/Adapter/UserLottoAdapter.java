/**
 * The UserLottoAdapter class is a RecyclerView adapter used to display a list of UserBid objects in a
 * card layout.
 */
package com.wowcodes.supreme.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Modelclas.UserBid;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

public class UserLottoAdapter extends RecyclerView.Adapter<com.wowcodes.supreme.Adapter.UserLottoAdapter.ViewHolder> {
    com.wowcodes.supreme.Adapter.UserLottoAdapter.ViewHolder viewHolder;
    Context mContext;
    SavePref savePref;
    public BindingService videoService;
    ArrayList<UserBid> coinModelArrayList;

    public UserLottoAdapter(Context context, ArrayList<UserBid> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
        savePref=new SavePref(mContext);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    @Override
    public com.wowcodes.supreme.Adapter.UserLottoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_lottoadapter, parent, false);
        viewHolder = new com.wowcodes.supreme.Adapter.UserLottoAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.wowcodes.supreme.Adapter.UserLottoAdapter.ViewHolder holder, final int position) {
        holder.txtSetName.setText(coinModelArrayList.get(position).getName());
        holder.tx_date.setText(coinModelArrayList.get(position).getBd_date());
        holder.txtAmount.setText(coinModelArrayList.get(position).getBd_value());
        holder.txtBids.setText(coinModelArrayList.get(position).getValue());
    }

    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtSetName,txtBids,txtAmount,tx_date;
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageview);
            txtSetName=itemView.findViewById(R.id.txtSetName);
            txtBids=itemView.findViewById(R.id.txtBids);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            card=itemView.findViewById(R.id.card);
            tx_date=itemView.findViewById(R.id.tx_date);
        }
    }
}