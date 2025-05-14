/**
 * The CityAdapter class is a RecyclerView adapter used to display a list of cities with their
 * corresponding flag images in an Android application.
 */
package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Fragments.InvestFragment;
import com.wowcodes.supreme.Modelclas.GetInvestPlans;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AllJarsAdapter extends RecyclerView.Adapter<AllJarsAdapter.ViewHolder> {
    public Context context;
    public ArrayList<GetInvestPlans.Get_Invest_Plans_Inner> arrayList;
    public BindingService videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
    int balance=0;

    public AllJarsAdapter(Context context, ArrayList<GetInvestPlans.Get_Invest_Plans_Inner> arrayList) {
        this.context = context;
        this.arrayList =arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_jars_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetInvestPlans.Get_Invest_Plans_Inner jar=arrayList.get(position);

        if(!jar.getPlan_color().isEmpty() || jar.getPlan_color()!=null) {
            holder.border.getBackground().setColorFilter(Color.parseColor("#"+jar.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
            holder.invest.getBackground().setColorFilter(Color.parseColor("#"+jar.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
            holder.plan_name.getBackground().setColorFilter(Color.parseColor("#"+jar.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
        }

        holder.plan_name.setText(jar.getPlan_name());
        holder.inv_desc.setText(jar.getInvestment()+context.getString(R.string.imgcoins2)+jar.getPlan_short_description());
        if(jar.getCompound_interest().equalsIgnoreCase("1"))
            holder.ci.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.green_tick,0);
        else
            holder.ci.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.red_cancel,0);

        if(jar.getPlan_capital_back().equalsIgnoreCase("1"))
            holder.cb.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.green_tick,0);
        else
            holder.cb.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.red_cancel,0);

        if(jar.getPlan_lifetime().equalsIgnoreCase("1"))
            holder.le.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.green_tick,0);
        else
            holder.le.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.red_cancel,0);

        holder.invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transprant);
                dialog.setContentView(R.layout.dialog_investment);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView plan_name = dialog.findViewById(R.id.plan_name);
                TextView desc = dialog.findViewById(R.id.desc);
                TextView investment_range = dialog.findViewById(R.id.range);
                TextView interest = dialog.findViewById(R.id.interest);
                TextView duration = dialog.findViewById(R.id.time);
                TextView txt_amount = dialog.findViewById(R.id.txt_amount);
                TextView investnow = dialog.findViewById(R.id.invest);
                EditText amount = dialog.findViewById(R.id.amount);
                ImageView close=dialog.findViewById(R.id.close);

                plan_name.setText(jar.getPlan_name());
                investment_range.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_coin,0);
                String inttxt=context.getString(R.string.interest)+jar.getPlan_interest();
                desc.setText(jar.getPlan_description());

                if(jar.getPlan_interest_frequency().equalsIgnoreCase("1"))
                    inttxt+=context.getString(R.string.per_min);
                else if(jar.getPlan_interest_frequency().equalsIgnoreCase("2"))
                    inttxt+=context.getString(R.string.per_hour);
                else if(jar.getPlan_interest_frequency().equalsIgnoreCase("3"))
                    inttxt+=context.getString(R.string.per_day);
                else if(jar.getPlan_interest_frequency().equalsIgnoreCase("4"))
                    inttxt+=context.getString(R.string.per_week);
                else if(jar.getPlan_interest_frequency().equalsIgnoreCase("5"))
                    inttxt+=context.getString(R.string.per_mon);
                else if(jar.getPlan_interest_frequency().equalsIgnoreCase("6"))
                    inttxt+=context.getString(R.string.per_year);
                interest.setText(inttxt);

                duration.setText(context.getString(R.string.duration)+jar.getPlan_duration());
                if(!jar.getPlan_maximum().equalsIgnoreCase(jar.getPlan_minimum())){
                    amount.setVisibility(View.VISIBLE);
                    amount.setHint(jar.getInvestment());
                    txt_amount.setText(context.getString(R.string.enter_amount));
                    txt_amount.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    txt_amount.setTypeface(Typeface.DEFAULT);
                    investment_range.setText(context.getString(R.string.investment_range)+jar.getInvestment()+" ");
                }
                else{
                    amount.setVisibility(View.GONE);
                    amount.setText(jar.getPlan_maximum());
                    txt_amount.setVisibility(View.GONE);
                    investment_range.setText(context.getString(R.string.investment)+jar.getInvestment()+" ");
                }
                investnow.getBackground().setColorFilter(Color.parseColor("#"+jar.getPlan_color()), PorterDuff.Mode.SRC_ATOP);
                investment_range.setTextColor(Color.parseColor("#"+jar.getPlan_color()));

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                try {
                    videoService.getUserProfile(new SavePref(context).getUserId()).enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                            ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                            if (!arrayList.get(0).getWallet().isEmpty() && arrayList.get(0).getWallet() != null)
                                balance=Integer.parseInt(arrayList.get(0).getWallet());
                            else
                                balance=-1;
                        }

                        @Override public void onFailure(Call<UserProfile> call, Throwable t) {
                            balance=-1;
                        }
                    });
                } catch (Exception ignore) {
                    balance=-1;
                }

                investnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int min=Integer.parseInt(jar.getPlan_minimum());
                        int max=Integer.parseInt(jar.getPlan_maximum());
                        int amt=0;
                        if(amount.getText().toString().isEmpty())
                            Toast.makeText(context, context.getString(R.string.enternoofcoins), Toast.LENGTH_SHORT).show();
                        else {
                            amt = Integer.parseInt(amount.getText().toString());
                            if (amt>max)
                                Toast.makeText(context, context.getString(R.string.maxamtallowed)+" "+max, Toast.LENGTH_SHORT).show();
                            else if(amt<min)
                                Toast.makeText(context, context.getString(R.string.minamtallowed)+" "+min, Toast.LENGTH_SHORT).show();
                            else{
                                if(balance==-1){
                                    Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                                else if(balance<amt)
                                    Toast.makeText(context, context.getString(R.string.donthaveenoughcoins), Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        videoService.add_hyip_order(new SavePref(context).getUserId(), "" + amt, jar.getPlan_id()).enqueue(new Callback<SuccessModel>() {
                                            @Override
                                            public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                                                try {
                                                    ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                                                    if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")) {
                                                        //Toast.makeText(context, context.getString(R.string.investment_complete), Toast.LENGTH_SHORT).show();
                                                        Dialog dialog = new Dialog(context);
                                                        dialog.getWindow().setBackgroundDrawableResource(R.color.transprant);
                                                        dialog.setContentView(R.layout.dialog_success);
                                                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                        dialog.show();
                                                        dialog.setCancelable(false);
                                                        TextView ok = dialog.findViewById(R.id.ok);
                                                        ok.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                dialog.cancel();
                                                                InvestFragment.reload=true;
                                                            }
                                                        });
                                                    } else
                                                        Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<SuccessModel> call, Throwable t) {
                                                Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } catch (Exception e) {
                                        Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                    } finally {
                                        dialog.cancel();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plan_name,inv_desc,ci,cb,le,invest;
        LinearLayout card,border;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plan_name=itemView.findViewById(R.id.plan_name);
            inv_desc=itemView.findViewById(R.id.inv_desc);
            ci=itemView.findViewById(R.id.ci);
            cb=itemView.findViewById(R.id.cb);
            le=itemView.findViewById(R.id.le);
            invest=itemView.findViewById(R.id.invest);
            card=itemView.findViewById(R.id.card);
            border=itemView.findViewById(R.id.border);
        }
    }
}