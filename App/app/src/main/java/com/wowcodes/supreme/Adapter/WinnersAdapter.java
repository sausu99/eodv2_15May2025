package com.wowcodes.supreme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.Multiplewinners;
import com.wowcodes.supreme.Activity.SingleWinner;
import com.wowcodes.supreme.Modelclas.WinnerMultiple;
import com.wowcodes.supreme.Modelclas.Winners;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.ItemViewHolder>{

    private Context context;
    ArrayList<Winners.winners_inner> items;

    public WinnersAdapter(Context context, ArrayList<Winners.winners_inner> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        view= LayoutInflater.from(context).inflate(R.layout.winnersrecycleview, parent, false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Winners.winners_inner item = items.get(position);



        if (item.getMultiple_winner() == "1"||Integer.parseInt(item.getMultiple_winner())!=0) {
            holder.multiplewin.setVisibility(View.VISIBLE);
            holder.singlewin.setVisibility(View.GONE);
            holder.prizepool.setText(MainActivity.currency +item.getPrize_pool());
            if (items != null) {
                holder.viewdetails2.setText(item.getWinners().size() + " Winners");
            } else {
                holder.viewdetails2.setText("0 Winners"); // Default text if items is null
            }
            holder.winnerslistp1.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            holder.winnerslistp1.setAdapter(new WinnerListHoriAdapter(context, (ArrayList<WinnerMultiple>) item.getWinners()));
            holder.winningdate2.setText(convertDate(item.getO_edate()));
            holder.viewwinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  viewdetail=new Intent(context, Multiplewinners.class);
                    viewdetail.putExtra("O_id",item.getO_id());
                    context.startActivity(viewdetail);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  viewdetail=new Intent(context, Multiplewinners.class);
                    viewdetail.putExtra("O_id",item.getO_id());
                    context.startActivity(viewdetail);
                }
            });


        }else {
            holder.multiplewin.setVisibility(View.GONE);
            holder.singlewin.setVisibility(View.VISIBLE);
            holder.productname.setText(item.getO_name());
            holder.winnername.setText(item.getWinner_name());

            if (Integer.parseInt(item.getIs_winner()) == 1) {
                holder.youwon.setVisibility(View.VISIBLE);
            }
            holder.winningdate.setText(convertDate(item.getO_edate()));
            Glide.with(context)
                    .load(item.getO_image())
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.productimage);
            String username=new SavePref(context).getName();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  viewdetail=new Intent(context, SingleWinner.class);
                    viewdetail.putExtra("O_id",item.getO_id());
                    viewdetail.putExtra("productname",item.getO_name());
                    viewdetail.putExtra("winnername",item.getWinner_name());
                    context.startActivity(viewdetail);

                }
            });
            holder.viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  viewdetail=new Intent(context, SingleWinner.class);
                    viewdetail.putExtra("O_id",item.getO_id());
                    viewdetail.putExtra("productname",item.getO_name());
                    viewdetail.putExtra("winnername",item.getWinner_name());
                    context.startActivity(viewdetail);
                }
            });


        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productname,winnername,winningdate,winningdate2,viewdetails,viewdetails2,prizepool,viewwinner;
        ImageView productimage,youwon;
        RecyclerView winnerslistp1;
        LinearLayout multiplewin,singlewin;
        View multipleview;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productname=itemView.findViewById(R.id.prod_name);
            winnername=itemView.findViewById(R.id.winnernametxt);
            winningdate=itemView.findViewById(R.id.windatetxt);
            winningdate2=itemView.findViewById(R.id.windatetxt2);
            prizepool=itemView.findViewById(R.id.prizetxt);
            viewdetails=itemView.findViewById(R.id.viewdetailstext);
            viewdetails2=itemView.findViewById(R.id.viewdetailstext2);
            viewwinner=itemView.findViewById(R.id.viewwinnertxt);
            productimage=itemView.findViewById(R.id.prod_img);
            youwon=itemView.findViewById(R.id.youwonimg);
            winnerslistp1=itemView.findViewById(R.id.listwinners);
            multiplewin=itemView.findViewById(R.id.multiplewin);
            singlewin=itemView.findViewById(R.id.singlewin);
            multipleview=itemView.findViewById(R.id.mulipleview);




        }
    }
    private String convertDate(String dateString) {
        if (dateString == null || dateString.equals("0000-00-00")) {
            return "No date ";
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        try {
            Date date = inputFormat.parse(dateString);
            String day = new SimpleDateFormat("d", Locale.US).format(date);
            String suffix = getDayOfMonthSuffix(Integer.parseInt(day));
            return day + suffix + " " + outputFormat.format(date).substring(outputFormat.format(date).indexOf(' ') + 1);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    private String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
