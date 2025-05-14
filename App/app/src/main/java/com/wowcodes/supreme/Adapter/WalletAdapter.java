/**
 * The WalletAdapter class is a RecyclerView adapter used to display wallet transactions in a list
 * format and handle payment functionality using payment gateway.
 */
package com.wowcodes.supreme.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.razorpay.PaymentResultListener;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Modelclas.GetWallet;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> implements PaymentResultListener {
    ViewHolder viewHolder;
    Context mContext;
    ArrayList<GetWallet.Get_Wallet_Inner> coinModelArrayList;

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    public WalletAdapter(Context context, ArrayList<GetWallet.Get_Wallet_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_walletadapter, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtSetName.setText(coinModelArrayList.get(position).getC_name());
        if (coinModelArrayList.get(position).getWp_coins() == null)
            holder.txtGetCoin.setText(coinModelArrayList.get(position).getC_coin());
        else
            holder.txtGetCoin.setText(coinModelArrayList.get(position).getWp_coins());

        holder.tx_date.setText(coinModelArrayList.get(position).getWp_date());
        if (coinModelArrayList.get(position).getWp_amount() == null)
            holder.txtAmount.setText(MainActivity.currency + " " + coinModelArrayList.get(position).getC_amount());
        else
            holder.txtAmount.setText(MainActivity.currency + " " + coinModelArrayList.get(position).getWp_amount());

        if (coinModelArrayList.get(position).getC_image() != null) {
            try {
                Glide.with(mContext)
                        .load(coinModelArrayList.get(position).getC_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(holder.imageview);
            } catch (Exception ignore) {}
        }
    }

    @Override public int getItemCount() {return coinModelArrayList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtSetName, txtGetCoin, txtAmount, tx_date;
        CardView card;
        LinearLayout card1;

        ViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            txtSetName = itemView.findViewById(R.id.txtSetName);
            txtGetCoin = itemView.findViewById(R.id.txtGetCoin);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            card = itemView.findViewById(R.id.card);
            tx_date = itemView.findViewById(R.id.tx_date);
            card1 = itemView.findViewById(R.id.card1);
        }
    }

    public void onPaymentSuccess(String s) {}
    @Override public void onPaymentError(int i, String s) {}
}