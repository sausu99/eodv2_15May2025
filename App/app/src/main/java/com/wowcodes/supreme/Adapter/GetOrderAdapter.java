/**
 * The GetOrderAdapter class is a RecyclerView adapter used to display a list of orders in an Android
 * application.
 */
package com.wowcodes.supreme.Adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Modelclas.GetOrderUser;
import com.wowcodes.supreme.Modelclas.GetStatusHistory;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class GetOrderAdapter extends RecyclerView.Adapter<GetOrderAdapter.ViewHolder> {
    ViewHolder viewHolder;
    Context mContext;
    BindingService videoService;
    ArrayList<GetOrderUser.Get_order_user_Inner> coinModelArrayList;
    String rating="0";

    public GetOrderAdapter(Context context, ArrayList<GetOrderUser.Get_order_user_Inner> coinModelArrayList) {
        this.mContext = context;
        this.coinModelArrayList = coinModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_getorderadapter, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        holder.txtSetName.setText(coinModelArrayList.get(position).getO_name());
        holder.tx_date.setText(coinModelArrayList.get(position).getOrder_date());
        holder.txtAmount.setText(coinModelArrayList.get(position).getDis_amount());
        holder.txtPayamount.setText(coinModelArrayList.get(position).getPay_amount());

        switch (coinModelArrayList.get(position).getOrder_status()) {
            case "1":
                holder.txtStatus.setText(R.string.string48);
                holder.txtStatusd.setText(R.string.string220);
                holder.txtStatus.setTextColor(Color.parseColor("#4131D1"));
                break;
            case "2":
                holder.txtStatus.setText(R.string.string221);
                holder.txtStatusd.setText(R.string.string218);
                holder.txtStatus.setTextColor(Color.parseColor("#CCD131"));
                break;
            case "3":
                holder.txtStatus.setText(R.string.string217);
                holder.txtStatusd.setText(R.string.string216);
                holder.txtStatus.setTextColor(Color.parseColor("#D18C31"));
                break;
            case "4":
                holder.txtStatus.setText(R.string.string199);
                holder.txtStatusd.setText(R.string.string200);
                holder.txtStatus.setTextColor(Color.parseColor("#279832"));
                break;
            case "5":
                holder.txtStatus.setText(R.string.string49);
                holder.txtStatusd.setText(R.string.string50);
                holder.txtStatus.setTextColor(Color.parseColor("#EE1111"));
                break;
        }
        try {
            Glide.with(mContext)
                    .load(coinModelArrayList.get(position).getO_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.imageview);
        }catch (Exception ignore){}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(mContext);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_order_details);
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int position=holder.getAdapterPosition();

                ImageView cancel=dialog.findViewById(R.id.cancel);
                TextView order_id=dialog.findViewById(R.id.order_id);
                TextView prod_name=dialog.findViewById(R.id.prod_name);
                TextView seller_name=dialog.findViewById(R.id.seller_name);
                ImageView prod_img=dialog.findViewById(R.id.prod_img);
                TextView price=dialog.findViewById(R.id.price);
                ImageView price_coin=dialog.findViewById(R.id.price_coin);
                TextView order_status=dialog.findViewById(R.id.order_status);
                ImageView order_status_img=dialog.findViewById(R.id.order_status_img);
                LinearLayout map_delivered=dialog.findViewById(R.id.map_delivered);
                LinearLayout map_rejected=dialog.findViewById(R.id.map_rejected);
                LinearLayout map_shipped=dialog.findViewById(R.id.map_shipped);
                LinearLayout map_processing=dialog.findViewById(R.id.map_processing);
                ImageView icon_received=dialog.findViewById(R.id.icon_received);
                ImageView icon_processing=dialog.findViewById(R.id.icon_processing);
                ImageView icon_shipped=dialog.findViewById(R.id.icon_shipped);
                ImageView icon_delivered=dialog.findViewById(R.id.icon_delivered);
                View track_processing=dialog.findViewById(R.id.track_processing);
                View track_shipped=dialog.findViewById(R.id.track_shipped);
                View track_delivered=dialog.findViewById(R.id.track_delivered);
                TextView txt_delivered=dialog.findViewById(R.id.txt_delivered);
                TextView txt_rejected=dialog.findViewById(R.id.txt_rejected);
                TextView txt_shipped=dialog.findViewById(R.id.txt_shipped);
                TextView txt_processed=dialog.findViewById(R.id.txt_processed);
                TextView txt_received=dialog.findViewById(R.id.txt_received);
                TextView address=dialog.findViewById(R.id.address);
                ImageView star1=dialog.findViewById(R.id.star1);
                ImageView star2=dialog.findViewById(R.id.star2);
                ImageView star3=dialog.findViewById(R.id.star3);
                ImageView star4=dialog.findViewById(R.id.star4);
                ImageView star5=dialog.findViewById(R.id.star5);
                TextView review=dialog.findViewById(R.id.review);
                TextView submit=dialog.findViewById(R.id.submit);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                order_id.setText(mContext.getString(R.string.orderid)+coinModelArrayList.get(position).getO_id());
                prod_name.setText(coinModelArrayList.get(position).getO_name());
                seller_name.setText(mContext.getString(R.string.sellername)+" "+coinModelArrayList.get(position).getSeller_name());

                try {
                    Glide.with(mContext)
                            .load(coinModelArrayList.get(position).getO_image())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()
                            .into(prod_img);
                }catch (Exception ignore){}

                price.setText(mContext.getString(R.string.price)+": "+(coinModelArrayList.get(position).getO_type().equalsIgnoreCase("3")?"":MainActivity.currency)+coinModelArrayList.get(position).getPay_amount());
                if(coinModelArrayList.get(position).getO_type().equalsIgnoreCase("3"))
                    price_coin.setVisibility(View.VISIBLE);
                else
                    price_coin.setVisibility(View.GONE);

                if(coinModelArrayList.get(position).getOrder_status().equalsIgnoreCase("1")){
                    order_status.setText(mContext.getString(R.string.received));
                    order_status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_received));
                }
                else if(coinModelArrayList.get(position).getOrder_status().equalsIgnoreCase("2")){
                    order_status.setText(mContext.getString(R.string.processed));
                    order_status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_processed));
                }
                else if(coinModelArrayList.get(position).getOrder_status().equalsIgnoreCase("3")){
                    order_status.setText(mContext.getString(R.string.shipped));
                }
                else if(coinModelArrayList.get(position).getOrder_status().equalsIgnoreCase("4")){
                    order_status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_shipped));
                    order_status.setText(mContext.getString(R.string.delivered));
                    order_status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_delivered));
                }
                else{
                    order_status.setText(mContext.getString(R.string.rejected2));
                    order_status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_cancelled));
                }




                String last_stage="";
                for(GetStatusHistory.Get_Status_History_Inner status : coinModelArrayList.get(position).getStatus_history()){
                    String[] dates=status.getDate().substring(0,status.getDate().indexOf(" ")).split("-");
                    String month="";
                    if(Integer.parseInt(dates[1]) == 1)
                        month="Jan";
                    else if(Integer.parseInt(dates[1]) == 2)
                        month="Feb";
                    else if(Integer.parseInt(dates[1]) == 3)
                        month="March";
                    else if(Integer.parseInt(dates[1]) == 4)
                        month="April";
                    else if(Integer.parseInt(dates[1]) == 5)
                        month="May";
                    else if(Integer.parseInt(dates[1]) == 6)
                        month="June";
                    else if(Integer.parseInt(dates[1]) == 7)
                        month="July";
                    else if(Integer.parseInt(dates[1]) == 8)
                        month="Aug";
                    else if(Integer.parseInt(dates[1]) == 9)
                        month="Sept";
                    else if(Integer.parseInt(dates[1]) == 10)
                        month="Oct";
                    else if(Integer.parseInt(dates[1]) == 11)
                        month="Nov";
                    else if(Integer.parseInt(dates[1]) == 12)
                        month="Dec";
                    else
                        month="Jan";

                    if(status.getStatus().equalsIgnoreCase("1")) {
                        icon_received.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_processing.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_processing.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));
                        track_shipped.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_shipped.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));
                        track_delivered.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_delivered.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));

                        txt_received.setText(mContext.getString(R.string.os_received)+" , "+dates[2]+" "+month+" "+dates[0]);
                        txt_processed.setText(mContext.getString(R.string.os_processed));
                        txt_shipped.setText(mContext.getString(R.string.os_shipped));
                        txt_delivered.setText(mContext.getString(R.string.os_delivered));
                        last_stage+="1";
                    }
                    else if(status.getStatus().equalsIgnoreCase("2")){
                        icon_received.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_processing.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_processing.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_shipped.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_shipped.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));
                        track_delivered.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_delivered.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));

                        txt_processed.setText(mContext.getString(R.string.os_processed)+" , "+dates[2]+" "+month+" "+dates[0]);
                        txt_shipped.setText(mContext.getString(R.string.os_shipped));
                        txt_delivered.setText(mContext.getString(R.string.os_delivered));
                        last_stage+="2";
                    } else if (status.getStatus().equalsIgnoreCase("3")) {
                        icon_received.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_processing.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_processing.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_shipped.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_shipped.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_delivered.setBackground(new ColorDrawable(Color.parseColor("#8D8D8D")));
                        icon_delivered.setImageDrawable(mContext.getDrawable(R.drawable.ic_uncheck));

                        txt_shipped.setText(mContext.getString(R.string.os_shipped)+" , "+dates[2]+" "+month+" "+dates[0]);
                        txt_delivered.setText(mContext.getString(R.string.os_delivered));
                        last_stage+="3";
                    } else if (status.getStatus().equalsIgnoreCase("4")) {
                        icon_received.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_processing.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_processing.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_shipped.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_shipped.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
                        track_delivered.setBackground(new ColorDrawable(Color.parseColor("#44B86F")));
                        icon_delivered.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));

                        txt_delivered.setText(mContext.getString(R.string.os_delivered)+" , "+dates[2]+" "+month+" "+dates[0]);
                        last_stage+="4";
                    }
                    else {
                        if(!last_stage.contains("2"))
                            map_processing.setVisibility(View.GONE);
                        if(!last_stage.contains("3"))
                            map_shipped.setVisibility(View.GONE);
                        if(!last_stage.contains("4"))
                            map_delivered.setVisibility(View.GONE);

                        map_rejected.setVisibility(View.VISIBLE);
                        txt_rejected.setText(mContext.getString(R.string.os_rejected)+" , "+dates[2]+" "+month+" "+dates[0]);
                    }
                }

                address.setText(coinModelArrayList.get(position).getO_address());
                review.setText(coinModelArrayList.get(position).getReview());
                rating=coinModelArrayList.get(position).getRating().isEmpty()?"0":coinModelArrayList.get(position).getRating();
                if(coinModelArrayList.get(position).getRating().isEmpty() && coinModelArrayList.get(position).getReview().isEmpty())
                    submit.setText(mContext.getString(R.string.submitreview));
                else
                    submit.setText(mContext.getString(R.string.editreview));

                if(coinModelArrayList.get(position).getRating().equalsIgnoreCase("1")){
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                }
                else if(coinModelArrayList.get(position).getRating().equalsIgnoreCase("2")){
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                }
                else if(coinModelArrayList.get(position).getRating().equalsIgnoreCase("3")){
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                }
                else if(coinModelArrayList.get(position).getRating().equalsIgnoreCase("4")){
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                }
                else if(coinModelArrayList.get(position).getRating().equalsIgnoreCase("5")){
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                }
                else{
                    star1.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star2.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star3.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                    star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                }

                star1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star2.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star3.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        rating="1";
                    }
                });
                star2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star3.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        rating="2";
                    }
                });
                star3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star4.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        rating="3";
                    }
                });
                star4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star4.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star5.setImageDrawable(mContext.getDrawable(R.drawable.gray_star));
                        rating="4";
                    }
                });
                star5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        star1.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star2.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star3.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star4.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        star5.setImageDrawable(mContext.getDrawable(R.drawable.gold_star));
                        rating="5";
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!rating.equalsIgnoreCase("0")) {
                            if (submit.getText().toString().equals(mContext.getString(R.string.submitreview)))
                                add_review(new SavePref(mContext).getUserId(),coinModelArrayList.get(position).getOffer_id(),rating,review.getText().toString(),dialog);
                            else
                                update_review(new SavePref(mContext).getUserId(),coinModelArrayList.get(position).getOffer_id(),rating,review.getText().toString(),dialog);
                        }
                        else
                            Toast.makeText(mContext, mContext.getString(R.string.ratefirst), Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });
    }

    public void add_review(String user_id,String item_id,String rating,String review,Dialog dialog){
        try {
            videoService.add_review(user_id,item_id,rating,review).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    Toast.makeText(mContext, mContext.getString(R.string.reviewsuccessfull), Toast.LENGTH_SHORT).show();
                    ((Activity) mContext).finish();
                    mContext.startActivity(((Activity) mContext).getIntent());
                    dialog.cancel();
                }
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {
                    Toast.makeText(mContext, mContext.getString(R.string.reviewsuccessfull), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ignore) {}
    }

    public void update_review(String user_id,String item_id,String rating,String review,Dialog dialog){
        try {
            videoService.update_review(user_id, item_id, rating, review).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    Toast.makeText(mContext, mContext.getString(R.string.reviewsuccessfull), Toast.LENGTH_SHORT).show();
                    ((Activity) mContext).finish();
                    mContext.startActivity(((Activity) mContext).getIntent());
                    dialog.cancel();
                }
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {
                    Toast.makeText(mContext, mContext.getString(R.string.reviewsuccessfull), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ignore) {}
    }

    @Override public int getItemCount() {
        return coinModelArrayList.size();
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtSetName,txtGetCoin,txtAmount,tx_date,txtPayamount,txtStatus,txtStatusd;
        CardView card;
        LinearLayout card1;

        ViewHolder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageview);
            txtSetName=itemView.findViewById(R.id.txtSetName);
            txtGetCoin=itemView.findViewById(R.id.txtGetCoin);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            txtStatusd=itemView.findViewById(R.id.txtStatusd);
            card=itemView.findViewById(R.id.card);
            tx_date=itemView.findViewById(R.id.tx_date);
            card1=itemView.findViewById(R.id.card1);
            txtPayamount=itemView.findViewById(R.id.txtPayamount);
        }
    }
}