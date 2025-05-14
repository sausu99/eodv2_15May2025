/**
 * The CoinAdapter class is a RecyclerView adapter used to display a list of coins with their names,
 * coin amounts, and prices, and handle the payment process using Razorpay.
 */
package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.PaymentResultListener;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.RazorpayActivity;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.ViewHolder> implements PaymentResultListener {
    ViewHolder viewHolder;
    Context mContext;
    SavePref savePref;
    BindingService videoService;
    String mainamount;
    String o_id,package_id,wallet;
    ArrayList<GetCoin.Get_Coin_Inner> coinModelArrayList;

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    public CoinAdapter(Context context, ArrayList<GetCoin.Get_Coin_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
        savePref=new SavePref(mContext);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_coinadapter, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.coins.setText(coinModelArrayList.get(position).getC_coin()+" Coins");
        holder.og_amt.setText(MainActivity.currency+coinModelArrayList.get(position).getC_full_price());
        holder.og_amt.setPaintFlags(holder.og_amt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.dis_amt.setText(MainActivity.currency+coinModelArrayList.get(position).getC_amt());
        holder.disc.setText(coinModelArrayList.get(position).getC_discount()+" % OFF   ");
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wallet=coinModelArrayList.get(position).getC_coin();
                package_id=coinModelArrayList.get(position).getC_id();
                Intent i=new Intent(mContext, RazorpayActivity.class);
                i.putExtra("list",coinModelArrayList.get(position));
                i.putExtra("activity","CoinAdapter");
                mContext.startActivity(i);
            }
        });
    }

    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }
    @Override public void onPaymentSuccess(final String razorpayPaymentID) {o_id=razorpayPaymentID;}
    @Override public void onPaymentError(int i, String s) {}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coins,og_amt,dis_amt,disc,buy;
        ViewHolder(View itemView) {
            super(itemView);
            coins=itemView.findViewById(R.id.coins);
            og_amt=itemView.findViewById(R.id.og_amt);
            dis_amt=itemView.findViewById(R.id.dis_amt);
            disc=itemView.findViewById(R.id.disc);
            buy=itemView.findViewById(R.id.buy);
        }
    }
}