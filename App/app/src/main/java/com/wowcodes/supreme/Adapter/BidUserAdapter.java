/**
 /**
 * The BidUserAdapter class is a RecyclerView adapter used to display bid user data in a list format.
 */
package com.wowcodes.supreme.Adapter;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.AllUserBidderActivity;
import com.wowcodes.supreme.Activity.ViewAllTicketsActivity;
import com.wowcodes.supreme.Modelclas.GetBidUser;
import com.wowcodes.supreme.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BidUserAdapter extends RecyclerView.Adapter<BidUserAdapter.ViewHolder> {
    ViewHolder viewHolder;
    Context mContext;
    ArrayList<GetBidUser.Get_biduser_Inner> coinModelArrayList;

    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    public BidUserAdapter(Context context, ArrayList<GetBidUser.Get_biduser_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_bidadapter, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(coinModelArrayList.get(holder.getAdapterPosition()).getO_type().equalsIgnoreCase("4") || coinModelArrayList.get(holder.getAdapterPosition()).getO_type().equalsIgnoreCase("5"))
                    i =new Intent(mContext, ViewAllTicketsActivity.class);
                else
                    i = new Intent(mContext, AllUserBidderActivity.class);

                i.putExtra("o_id", coinModelArrayList.get(holder.getAdapterPosition()).getO_id());
                mContext.startActivity(i);
            }
        });

        holder.txtSetName.setText(coinModelArrayList.get(position).getO_name());

        if(coinModelArrayList.get(position).getStatus().equalsIgnoreCase("1")) {
            Thread myThread = new Thread(new CountDownRunner(holder.tx_date, coinModelArrayList.get(position).getO_edate() + " " + coinModelArrayList.get(position).getO_etime()));
            myThread.start();
            holder.endsin.setVisibility(VISIBLE);
        }
        else {
            holder.tx_date.setText(mContext.getString(R.string.ended));
            holder.endsin.setVisibility(INVISIBLE);
        }

        if (coinModelArrayList.get(position).getO_type().equalsIgnoreCase("4") || coinModelArrayList.get(position).getO_type().equalsIgnoreCase("5")) {
            holder.txtBids.setText(coinModelArrayList.get(position).getTotal_bids() + " " + mContext.getResources().getString(R.string.ticketsbought));
            holder.txtIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_ticket));
        }
        else {
            holder.txtBids.setText(coinModelArrayList.get(position).getTotal_bids() + " " + mContext.getResources().getString(R.string.bidsplaced));
            holder.txtIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_auction));
        }

        try {
            Glide.with(mContext)
                    .load(coinModelArrayList.get(position).getO_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.imageview);
        }catch (Exception ignore){}
    }

    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview,txtIcon;
        TextView txtSetName,tx_date,txtBids,endsin;
        RelativeLayout card;
        LinearLayout bid_back;

        ViewHolder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageview);
            txtSetName=itemView.findViewById(R.id.txtSetName);
            tx_date=itemView.findViewById(R.id.tx_date);
            bid_back=itemView.findViewById(R.id.bid_back);
            txtIcon=itemView.findViewById(R.id.txtIcon);
            card=itemView.findViewById(R.id.card);
            txtBids=itemView.findViewById(R.id.txtBids);
            endsin=itemView.findViewById(R.id.endsin);
        }
    }

    class CountDownRunner implements Runnable {
        TextView textView;
        String o_etime;

        public CountDownRunner(TextView tx_time, String o_etime) {
            this.textView = tx_time;
            this.o_etime = o_etime;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(textView, o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception ignore) {}
            }
        }
    }

    public void doWork(final TextView textView, final String o_etime) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                String currentDate= String.valueOf(java.time.LocalDate.now());
                String currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
                String curr_dt_time= currentDate + " " + currentTime;
                findDifference(curr_dt_time, textView, o_etime);
            }
        });
    }

    void findDifference(String start_date, TextView textView, String end_date) {
        textView.setText(mContext.getText(R.string.ended));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            if(difference_In_Days<=0) {
                String hours = difference_In_Hours < 10 ? "0" + difference_In_Hours : difference_In_Hours + "";
                String mins = difference_In_Minutes < 10 ? "0" + difference_In_Minutes : difference_In_Minutes + "";
                String secs = difference_In_Seconds < 10 ? "0" + difference_In_Seconds : difference_In_Seconds + "";
                textView.setText(hours + ":" + mins + ":" + secs);
            }
            else
                textView.setText(difference_In_Days+" "+mContext.getString(R.string.days));

        }
        catch (Exception ignore) {}
    }
}