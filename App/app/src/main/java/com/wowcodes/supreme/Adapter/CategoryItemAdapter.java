/**
 * The CategoryItemAdapter class is a RecyclerView adapter that binds data to the item views and
 * handles click events for each item in the list.
 */
package com.wowcodes.supreme.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wowcodes.supreme.Activity.AuctionWorks;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.DynamicWorks;
import com.wowcodes.supreme.Activity.HighestBidWorks;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.LotoDetailActivity;
import com.wowcodes.supreme.Activity.LottoWorks;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.PennyWorks;
import com.wowcodes.supreme.Activity.RaffleDetailActivity;
import com.wowcodes.supreme.Activity.RaffleWorks;
import com.wowcodes.supreme.Activity.ShopItemsActivity;
import com.wowcodes.supreme.Activity.AuctionActivity;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder> {
    String start_date;
    private Context context;
    private List<GetCategories.JSONDATum> items;

    public CategoryItemAdapter(Context context, List<GetCategories.JSONDATum> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_adapteritem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        GetCategories.JSONDATum item = items.get(position);
        String oType = item.getoType();

        Thread myThread;
        Runnable myRunnableThread = new CategoryItemAdapter.CountDownRunner(holder.txtTime,item.getoEdate()+" "+item.getoEtime());
        myThread= new Thread(myRunnableThread);
        myThread.start();

        Glide.with(context)
                .load(item.getoImage())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.imageview);

        switch (oType) {
            case "1":
                holder.txtType.setText(context.getResources().getString(R.string.lub));
                break;
            case "2":
                holder.txtType.setText(context.getResources().getString(R.string.hub));
                break;
            case "3":
                holder.txtType.setText(context.getResources().getString(R.string.redeem));
                break;
            case "4":
                holder.txtType.setText(context.getResources().getString(R.string.raffle));
                break;
            case "5":
                holder.txtType.setText(context.getResources().getString(R.string.lotto));
                break;
            case "7":
                holder.txtType.setText(context.getResources().getString(R.string.engauc));
                break;
            case "8":
                holder.txtType.setText(context.getResources().getString(R.string.penny));
                break;
            default:
                holder.txtType.setText("");
                break;
        }
        holder.txtName.setText(items.get(position).getoName());
        holder.txtPrice.setText(MainActivity.currency +item.getoPrice()+"/-");
        holder.txtAmount.setText(item.getoAmount());
        holder.txtTime.setText("  "+item.getoEtime());
        holder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (oType) {
                    case "1":
                        context.startActivity(new Intent(context, AuctionWorks.class));
                        break;
                    case "2":
                        context.startActivity(new Intent(context, HighestBidWorks.class));
                        break;
                    case "4":
                        context.startActivity(new Intent(context, RaffleWorks.class));
                        break;
                    case "5":
                        context.startActivity(new Intent(context, LottoWorks.class));
                        break;
                    case "7":
                        context.startActivity(new Intent(context, DynamicWorks.class));
                        break;
                    case "8":
                        context.startActivity(new Intent(context, PennyWorks.class));
                        break;
                    default:
                        holder.txtType.setText("");
                        break;
                }
            }
        });

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;

                if (Objects.equals(new SavePref(context).getUserId(), "0")){
                    i=new Intent(context, LoginActivity.class);
                    i.putExtra("Decider", "Category");
                }
                else {
                    switch (oType){
                        case "1":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("check", "live");
                            i.putExtra("O_id", item.getoId());
                            break;
                        case "2":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            break;
                        case "4":
                            i = new Intent(context, RaffleDetailActivity.class);
                            i.putExtra("check", "draw");
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(item.getoQty()));
                            i.putExtra("type" , item.getoType());
                            i.putExtra("name" , item.getoName());
                            i.putExtra("etime" , item.getoEtime());
                            i.putExtra("edate" , item.getoEdate());
                            i.putExtra("image" , item.getoImage());
                            i.putExtra("desc" , item.getoDesc());
                            i.putExtra("coins" , item.getoAmount());
                            i.putExtra("oamt" , item.getoAmount());
                            i.putExtra("colorcode" , item.getcColor());
                            i.putExtra("umax" , item.getoUmax());
                            i.putExtra("cdesc" ,item.getcDesc());
                            i.putExtra("link" , item.getoLink());
                            if (TextUtils.isEmpty(item.getoUlimit()))
                                i.putExtra("limit" , "1");
                            else
                                i.putExtra("limit" , item.getoUlimit());
                            i.putExtra("id",item.getId());
                            break;
                        case "5":
                            i = new Intent(context, LotoDetailActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "raffle");
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(item.getoQty()));
                            i.putExtra("type" , item.getoType());
                            break;
                        case "7":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            break;
                        case "8":
                            i = new Intent(context, AuctionActivity.class);
                            i.putExtra("O_id", item.getoId());
                            i.putExtra("check", "live");
                            i.putExtra("total_bids" , item.getTotalbids());
                            i.putExtra("qty" , Integer.parseInt(item.getoQty()));
                            i.putExtra("type" , item.getoType());
                            break;
                        case "3":
                        case "9":
                            i= new Intent(context, ShopItemsActivity.class);
                            i.putExtra("itemId", item.getItem_id());
                            i.putExtra("image", item.getoImage());
                            i.putExtra("image1", item.getoImage1());
                            i.putExtra("image2", item.getoImage2());
                            i.putExtra("image3", item.getoImage3());
                            i.putExtra("image4", item.getoImage4());
                            i.putExtra("name", item.getoName());
                            i.putExtra("type", item.getoType());
                            i.putExtra("desc", item.getoDesc());
                            i.putExtra("edate", item.getoEdate());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("coins", item.getoPrice());
                            i.putExtra("oid", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("colorcode", item.getoColor());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("id", item.getId());
                            break;
                        default:
                            i= new Intent(context, CategoryDetailsActivity.class);
                            i.putExtra("image", item.getoImage());
                            i.putExtra("image1", item.getoImage1());
                            i.putExtra("image2", item.getoImage2());
                            i.putExtra("image3", item.getoImage3());
                            i.putExtra("image4", item.getoImage4());
                            i.putExtra("name", item.getoName());
                            i.putExtra("type", item.getoType());
                            i.putExtra("desc", item.getoDesc());
                            i.putExtra("edate", item.getoEdate());
                            i.putExtra("etime", item.getoEtime());
                            i.putExtra("coins", item.getoAmount());
                            i.putExtra("oid", item.getoId());
                            i.putExtra("qty", item.getoQty());
                            i.putExtra("oamt", item.getoAmount());
                            i.putExtra("link", item.getoLink());
                            i.putExtra("colorcode", item.getcColor());
                            i.putExtra("cdesc", item.getcDesc());
                            i.putExtra("umax", item.getoUmax());
                            i.putExtra("limit", item.getoUlimit());
                            i.putExtra("totalbids", item.getTotalbids());
                            i.putExtra("id", item.getId());
                    }
                }
                context.startActivity(i);
            }
        });
    }

    @Override public int getItemCount() {return items.size();}

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageview;
        ImageView type;
        TextView txtName,txtTime,txtType,txtPrice,txtAmount,txtBids;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.image);
            type=itemView.findViewById(R.id.type);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            txtName=itemView.findViewById(R.id.txtName);
            txtTime=itemView.findViewById(R.id.txtTime);
            txtType=itemView.findViewById(R.id.txtType);
            txtAmount=itemView.findViewById(R.id.txtAmount);
        }
    }

    public void doWork(final TextView textView, final String o_etime) {
        ((Activity)context).runOnUiThread(new Runnable() {
            public void run()
            {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                start_date = currentDate + " " + currentTime;
                findDifference(start_date, textView, o_etime);
            }
        });
    }

    class CountDownRunner implements Runnable {
        TextView textView;
        String o_etime;
        public CountDownRunner(TextView tx_time, String o_etime) {
            this.textView=tx_time;
            this.o_etime=o_etime;
        }

        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(textView,o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                }
                catch (InterruptedException e) { Thread.currentThread().interrupt();}
                catch(Exception ignore) {}
            }
        }
    }
    
    static void findDifference(String start_date, TextView textView, String end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            String diff = difference_In_Days + "d " + difference_In_Hours + "h " + difference_In_Minutes + "m " + difference_In_Seconds + "s";
            if (diff.equalsIgnoreCase("0d 0h 0m 0s"))
                textView.setText(" Ended");
            else
                textView.setText(" "+diff);
        }
        catch (Exception ignore) {}
    }
}