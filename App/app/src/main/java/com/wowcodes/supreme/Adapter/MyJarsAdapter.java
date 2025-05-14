/**
 * The CityAdapter class is a RecyclerView adapter used to display a list of cities with their
 * corresponding flag images in an Android application.
 */
package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.wowcodes.supreme.Activity.CancelJarActivity;
import com.wowcodes.supreme.Modelclas.GetMyPlans;
import com.wowcodes.supreme.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MyJarsAdapter extends RecyclerView.Adapter<MyJarsAdapter.ViewHolder> {
    Context context;
    ArrayList<GetMyPlans.Get_My_Plans_Inner> arrayList;

    public MyJarsAdapter(Context context, ArrayList<GetMyPlans.Get_My_Plans_Inner> arrayList) {
        this.context = context;
        this.arrayList =arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_jars_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetMyPlans.Get_My_Plans_Inner plan=arrayList.get(position);

        holder.plan_name.setText(plan.getPlan_name());
        holder.amt.setText(plan.getInvestment_amount() +" ");
        holder.upcoming.setTextColor(Color.parseColor("#"+plan.getPlan_color()));
        holder.border.getBackground().setColorFilter(Color.parseColor("#"+plan.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
        holder.card.getBackground().setColorFilter(Color.parseColor("#"+plan.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
        holder.progress.setIndicatorColor(Color.parseColor("#"+plan.getPlan_color()));
        holder.cancel.getBackground().setColorFilter(Color.parseColor("#"+plan.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
        holder.cancel.setVisibility(View.GONE);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, CancelJarActivity.class);
                i.putExtra("plan_name",plan.getPlan_name());
                i.putExtra("invested_amount",plan.getInvestment_amount());
                i.putExtra("current_amount",plan.getCurrent_value());
                i.putExtra("charge",plan.getPlan_cancel_charge());
                i.putExtra("plan_id",plan.getPlan_id());
                context.startActivity(i);
            }
        });

        String inttxt=plan.getPlan_interest();
        if(plan.getPlan_interest_frequency().equalsIgnoreCase("1"))
            inttxt+=context.getString(R.string.per_min);
        else if(plan.getPlan_interest_frequency().equalsIgnoreCase("2"))
            inttxt+=context.getString(R.string.per_hour);
        else if(plan.getPlan_interest_frequency().equalsIgnoreCase("3"))
            inttxt+=context.getString(R.string.per_day);
        else if(plan.getPlan_interest_frequency().equalsIgnoreCase("4"))
            inttxt+=context.getString(R.string.per_week);
        else if(plan.getPlan_interest_frequency().equalsIgnoreCase("5"))
            inttxt+=context.getString(R.string.per_mon);
        else if(plan.getPlan_interest_frequency().equalsIgnoreCase("6"))
            inttxt+=context.getString(R.string.per_year);
        holder.interest.setText(inttxt);
        holder.rec_amt.setText(plan.getCurrent_value()+" ");

        if(plan.getPlan_status().equalsIgnoreCase("1")) {
            holder.upcoming_txt.setText(context.getString(R.string.upcoming_payment));
            holder.upcoming.setText(plan.getNext_update());
            holder.progress.setVisibility(View.VISIBLE);
            if(plan.getPlan_cancelable().equalsIgnoreCase("1"))
                holder.cancel.setVisibility(View.VISIBLE);
            else
                holder.cancel.setVisibility(View.GONE);

            SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = obj.parse(plan.getLast_update());
                Date date2 = obj.parse(plan.getNext_update());
                Date date3 = null;
                DateTimeFormatter dtf;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    date3 = obj.parse(dtf.format(now));
                }
                long time_difference = date2.getTime() - date1.getTime();
                int difference_In_Minutes = (int) ((time_difference / (1000 * 60)) % 60);
                holder.progress.setMax(difference_In_Minutes);

                time_difference = date2.getTime() - date3.getTime();
                difference_In_Minutes = (int) ((time_difference / (1000 * 60)) % 60);
                holder.progress.setProgress(difference_In_Minutes);
            }catch (Exception ignore){}
        }
        else{
            if(plan.getPlan_status().equalsIgnoreCase("2"))
                holder.upcoming_txt.setText(context.getString(R.string.cancelled_on));
            else
                holder.upcoming_txt.setText(context.getString(R.string.completed_on));

            holder.upcoming.setText(plan.getLast_update());
            holder.progress.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plan_name,amt,interest,rec_amt,upcoming_txt,upcoming;
        LinearProgressIndicator progress;
        LinearLayout card,border,cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plan_name=itemView.findViewById(R.id.plan_name);
            amt=itemView.findViewById(R.id.amt);
            interest=itemView.findViewById(R.id.interest);
            rec_amt=itemView.findViewById(R.id.rec_amt);
            upcoming=itemView.findViewById(R.id.upcoming);
            upcoming_txt=itemView.findViewById(R.id.upcoming_txt);
            progress=itemView.findViewById(R.id.progress);
            card=itemView.findViewById(R.id.card);
            border=itemView.findViewById(R.id.border);
            cancel=itemView.findViewById(R.id.cancel);
        }
    }
}