/**
 * The TansactionDetailAdapter class is a RecyclerView adapter used to display transaction details in a
 * list format.
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.GetTransaction;
import com.wowcodes.supreme.R;
import java.util.ArrayList;

public class TansactionDetailAdapter extends RecyclerView.Adapter<TansactionDetailAdapter.ViewHolder> {
    ViewHolder viewHolder;
    Context mContext;
    ArrayList<GetTransaction.Get_transaction_Inner> coinModelArrayList;

    public TansactionDetailAdapter(Context context, ArrayList<GetTransaction.Get_transaction_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
    }

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_tradetailadapter, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String money = coinModelArrayList.get(position).getMoney();

        if (money.startsWith("-")) {
            holder.txtSetName.setText(R.string.string189);
            holder.txtAmount.setText(money); // No need to add '-' again
            holder.txtAmount.setTextColor(ContextCompat.getColor(mContext, R.color.red)); // Set negative color
            holder.txtCoins.setTextColor(ContextCompat.getColor(mContext, R.color.red)); // Set negative color
        } else if (money.startsWith("+")) {
            holder.txtSetName.setText(coinModelArrayList.get(position).getType_name());
            holder.txtAmount.setText(money); // No need to add '+' again
            holder.txtAmount.setTextColor(ContextCompat.getColor(mContext, R.color.green_forest_primary_dark));
            holder.txtCoins.setTextColor(ContextCompat.getColor(mContext, R.color.green_forest_primary_dark));
        } else {
            // Handle cases where `money` doesn't start with '+' or '-'
            holder.txtSetName.setText(coinModelArrayList.get(position).getType_name());
            holder.txtAmount.setText(money); // Handle it as you see fit
            holder.txtAmount.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary)); // Default color
            holder.txtCoins.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary)); // Default color
        }

        holder.tx_date.setText(coinModelArrayList.get(position).getType_details());

        try {
            Glide.with(mContext)
                    .load(Constants.imageurl + coinModelArrayList.get(position).getType_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.img_coins)
                    .fitCenter()
                    .into(holder.imageview);
        } catch (Exception ignore) {}
    }


    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtSetName, tx_date, txtAmount, txtCoins;
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            txtSetName = itemView.findViewById(R.id.txtSetName);
            tx_date = itemView.findViewById(R.id.tx_date);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtCoins = itemView.findViewById(R.id.txtCoins);
            card = itemView.findViewById(R.id.card);
        }
    }
}