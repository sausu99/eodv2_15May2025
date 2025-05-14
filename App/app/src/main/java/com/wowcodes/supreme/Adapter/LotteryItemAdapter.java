package com.wowcodes.supreme.Adapter;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.BeforeRaffleActivity;
import com.wowcodes.supreme.Activity.CategoryDetailsActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.AlarmReceiver;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SavePref;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LotteryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String start_date;
    private Context context;
    public String from;
    SharedPreferences.Editor editor;
    String notified;
    ArrayList<GetCategories.JSONDATum> items;
    public boolean fromCategorySelected;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_VIEW_MORE = 1;
    private DiffCategoryAdapter.CategorySel catsel; // CategorySel instance
    private String categoryTitle;
    private String categoryId;
    private boolean showAllItems = true;



    public LotteryItemAdapter(Context context, ArrayList<GetCategories.JSONDATum> items, String from, boolean fromCategorySelected) {
        this.context = context;
        this.items = items;
        this.from=from;
        this.fromCategorySelected=fromCategorySelected;
    }
    @Override
    public int getItemCount() {
        if (showAllItems) {
            return items.size(); // Show all items
        } else {
            return Math.min(items.size(), 5); // Show a maximum of 4 items + 1 "View More" button
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (!showAllItems && position == 4 && items.size() > 5) {
            return VIEW_TYPE_VIEW_MORE; // "View More" button type
        } else {
            return VIEW_TYPE_ITEM; // Regular item type
        }
    }    public void setShowAllItems(boolean showAllItems) {
        this.showAllItems = showAllItems;
        notifyDataSetChanged(); // Notify adapter of data change
    }

    public void setCategorySel(DiffCategoryAdapter.CategorySel catsel) {
        this.catsel = catsel;
    }

    public void setCategoryDetails(String categoryTitle, String categoryId, boolean showAllItems) {
        this.categoryTitle = categoryTitle;
        this.categoryId = categoryId;
        this.showAllItems = showAllItems;
        notifyDataSetChanged(); // Refresh the adapter with the new state
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lottery_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_more_layout, parent, false);
            return new ViewMoreViewHolder(view);
        }
    }    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            GetCategories.JSONDATum item = items.get(position);
            String oType = item.getoType();
            createchannel();
            if (item.getoStatus().equalsIgnoreCase("0")) {
                itemViewHolder.notify1.setVisibility(VISIBLE);
                itemViewHolder.blocker1.setVisibility(VISIBLE);
                itemViewHolder.lock1.setVisibility(VISIBLE);
            } else {
                itemViewHolder.notify1.setVisibility(GONE);
                itemViewHolder.blocker1.setVisibility(GONE);
                itemViewHolder.lock1.setVisibility(GONE);
            }
            SharedPreferences notiprefs = context.getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
            editor = notiprefs.edit();
            notified = notiprefs.getString(item.getoId(), "");

            if (notified.isEmpty()) {
                itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notify));
                itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_notification));
                itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.coral_primary));
                itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            } else {
                itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notified));
                itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
                itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.white));
                itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.coral_primary), PorterDuff.Mode.SRC_ATOP);
            }

            if (item.getoStatus().equalsIgnoreCase("0")) {
                itemViewHolder.txtTime11.setVisibility(GONE);
                itemViewHolder.txtTime1.setVisibility(VISIBLE);
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    itemViewHolder.startCountdown(endDateTime,position,items,this);
                }
            } else{
                itemViewHolder.txtTime1.setVisibility(GONE);
                itemViewHolder.txtTime11.setVisibility(VISIBLE);
                String endDateTime = item.getoEdate() + " " + item.getoEtime();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    itemViewHolder.startCountdown(endDateTime,position,items,this);
                }

            }

            Glide.with(context)
                    .load(item.getoImage())
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(itemViewHolder.imageView1);
            if (item.getoColor() != null && !item.getoColor().isEmpty()){
                itemViewHolder.card1.getBackground().setColorFilter(Color.parseColor("#" + item.getoColor()), PorterDuff.Mode.SRC_ATOP);
            }else{
                itemViewHolder.card1.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            }
            itemViewHolder.txtPrice1.setText(MainActivity.currency + item.getoPrice() + " /-");
            if (item.getoAmount().equalsIgnoreCase("0")) {
                itemViewHolder.txtAmount1.setText(context.getString(R.string.free));
            } else {
                itemViewHolder.txtAmount1.setText(item.getoAmount());
            }
            try {

                itemViewHolder.txtTotal.setText(item.getoQty() + " " + context.getString(R.string.tickets));
                int remainingQty = Integer.parseInt(item.getoQty()) - Integer.parseInt(item.getTotalbids());
                if (remainingQty < 0) {
                    remainingQty = 0;
                }
                itemViewHolder.txtLeft.setText(remainingQty + " " + context.getString(R.string.string340));
                itemViewHolder.progressBar.setProgress(Integer.parseInt(item.getTotalbids()));
                itemViewHolder.progressBar.setMax(Integer.parseInt(item.getoQty()));
                itemViewHolder.txtName1.setText(item.getoName());
                itemViewHolder.txtTime1.setText("  " + item.getoEtime());
            } catch (Exception ignore) {
            }
            itemViewHolder.notify1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7);
                        } else {
                            if (notified.isEmpty()) {
                                itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notified));
                                itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
                                itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.white));
                                itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.coral_primary), PorterDuff.Mode.SRC_ATOP);

                                makenotification(item.getoId(), item.getoDate() + " " + item.getoStime(), holder.getAbsoluteAdapterPosition());
                                Toast.makeText(context, context.getResources().getString(R.string.notiset), Toast.LENGTH_SHORT).show();
                            } else {
                                itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notify));
                                itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_notification));
                                itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.coral_primary));
                                itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

                                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                am.cancel(PendingIntent.getBroadcast(context, Integer.parseInt(notified), new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_MUTABLE));

                                Toast.makeText(context, context.getResources().getString(R.string.noticancel), Toast.LENGTH_SHORT).show();

                                editor.remove(item.getoId());
                            }

                            editor.apply();
                            notified = notiprefs.getString(item.getoId(), "");
                        }
                    } else {
                        if (notified.isEmpty()) {
                            itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notified));
                            itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_alarm_on));
                            itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.white));
                            itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.coral_primary), PorterDuff.Mode.SRC_ATOP);

                            makenotification(item.getoId(), item.getoDate() + " " + item.getoStime(), holder.getAbsoluteAdapterPosition());
                            Toast.makeText(context, context.getResources().getString(R.string.notiset), Toast.LENGTH_SHORT).show();
                        } else {
                            itemViewHolder.txtnotify1.setText(context.getResources().getString(R.string.notify));
                            itemViewHolder.imgnotify1.setImageDrawable(context.getDrawable(R.drawable.ic_notification));
                            itemViewHolder.txtnotify1.setTextColor(context.getResources().getColor(R.color.coral_primary));
                            itemViewHolder.notify1.getBackground().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

                            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                            am.cancel(PendingIntent.getBroadcast(context, Integer.parseInt(notified), new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_MUTABLE));

                            Toast.makeText(context, context.getResources().getString(R.string.noticancel), Toast.LENGTH_SHORT).show();

                            editor.remove(item.getoId());
                        }

                        editor.apply();
                        notified = notiprefs.getString(item.getoId(), "");
                    }
                }
            });

            try {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i;
                        if (Objects.equals(new SavePref(context).getUserId(), "0")) {
                            i = new Intent(context, LoginActivity.class);
                            i.putExtra("Decider", "Category");
                            context.startActivity(i);
                        }
                        else if (item.getoStatus()!=null && item.getoStatus().equalsIgnoreCase("0")) {
                            if (oType.equalsIgnoreCase("4") || oType.equalsIgnoreCase("5"))
                                Toast.makeText(context, context.getResources().getString(R.string.pleasewaitlottery), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, context.getResources().getString(R.string.pleasewaitauction), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            switch (oType) {
                                case "4":
                                    i = new Intent(context, BeforeRaffleActivity.class);
                                    i.putExtra("check", "draw");
                                    i.putExtra("O_id", item.getoId());
                                    i.putExtra("total_bids", item.getTotalbids());
                                    i.putExtra("qty", item.getoQty());
                                    i.putExtra("type", item.getoType());
                                    i.putExtra("name", item.getoName());
                                    i.putExtra("etime", item.getoEtime());
                                    i.putExtra("edate", item.getoEdate());
                                    i.putExtra("image", item.getoImage());
                                    i.putExtra("desc", item.getoDesc());
                                    i.putExtra("coins", item.getoAmount());
                                    i.putExtra("oamt", item.getoAmount());
                                    i.putExtra("colorcode", item.getcColor());
                                    i.putExtra("umax", item.getoUmax());
                                    i.putExtra("cdesc", item.getcDesc());
                                    i.putExtra("link", item.getoLink());
                                    if (TextUtils.isEmpty(item.getoUlimit())) {
                                        i.putExtra("limit", "1");
                                    } else {
                                        i.putExtra("limit", item.getoUlimit());
                                    }
                                    i.putExtra("id", item.getId());
                                    break;
                                case "5":
                                    i = new Intent(context, BeforeRaffleActivity.class);
                                    i.putExtra("O_id", item.getoId());
                                    i.putExtra("check", "raffle");
                                    i.putExtra("total_bids", item.getTotalbids());
                                    i.putExtra("qty", item.getoQty());
                                    i.putExtra("type", item.getoType());
                                    i.putExtra("etime", item.getoEtime());
                                    i.putExtra("edate", item.getoEdate());
                                    break;

                                default:
                                    i = new Intent(context, CategoryDetailsActivity.class);
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
                                    i.putExtra("colorcode", item.getoColor());
                                    i.putExtra("cdesc", item.getcDesc());
                                    i.putExtra("umax", item.getoUmax());
                                    i.putExtra("limit", item.getoUlimit());
                                    i.putExtra("totalbids", item.getTotalbids());
                                    i.putExtra("id", item.getId());
                            }

                            context.startActivity(i);
                        }
                    }
                });
            }catch (Exception ignore){}}
        else if (holder instanceof ViewMoreViewHolder) {

            ((ViewMoreViewHolder) holder).itemView.setOnClickListener(v -> {
                showAllItems = true;
                notifyDataSetChanged(); // Refresh the adapter to show all items

                if (catsel != null) {
                    try {
                        catsel.sendData(categoryTitle, categoryId, from);
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }



    public void createchannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("CHANNEL_ID","Prizex", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Prizex Notification Channel");

            NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    public void makenotification(String name,String time,int pos){
        GetCategories.JSONDATum item = items.get(pos);

        int id=new Random().nextInt();
        editor.putString(name,String.valueOf(id));
        editor.putString("image", item.getoImage());
        editor.putString("image1", item.getoImage1());
        editor.putString("image2", item.getoImage2());
        editor.putString("image3", item.getoImage3());
        editor.putString("image4", item.getoImage4());
        editor.putString("name", item.getoName());
        editor.putString("type", item.getoType());
        editor.putString("desc", item.getoDesc());
        editor.putString("edate", item.getoEdate());
        editor.putString("etime", item.getoEtime());
        editor.putString("coins", item.getoPrice());
        editor.putString("oid", item.getoId());
        editor.putString("qty", item.getoQty());
        editor.putString("oamt", item.getoAmount());
        editor.putString("link", item.getoLink());
        editor.putString("colorcode", item.getoColor());
        editor.putString("cdesc", item.getcDesc());
        editor.putString("umax", item.getoUmax());
        editor.putString("limit", item.getoUlimit());
        editor.putString("totalbids", item.getTotalbids());
        editor.putString("id", item.getId());
        editor.apply();


        Intent i=new Intent(context, AlarmReceiver.class);
        PendingIntent pending=PendingIntent.getBroadcast(context,id,i,PendingIntent.FLAG_MUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d2 ;
            try {
                d2 = sdf.parse(time);
                Instant instant=d2.toInstant();
                long duration = Duration.between(Instant.now(),instant).toMillis();
                AlarmManager am= (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+duration,pending);
            } catch (Exception ignore) {}
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtName1, txtLeft, txtTotal, txtAmount1, txtPrice1, txtTime1, txtTime11, txtName, txtnotify1;
        ProgressBar progressBar;
        ImageView imageView1, imgnotify1, lock1, img;
        LinearLayout notify1, aucNameLL;
        View blocker1;
        RelativeLayout card1;

        private Handler handler;
        private Runnable updateTimeRunnable;
        private LocalDateTime endTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            card1 = itemView.findViewById(R.id.card1);
            imageView1 = itemView.findViewById(R.id.image1);
            txtTime1 = itemView.findViewById(R.id.txtTime10);
            txtTime11 = itemView.findViewById(R.id.txtTime11);
            progressBar = itemView.findViewById(R.id.progress_bar);
            txtName1 = itemView.findViewById(R.id.txtName1);
            txtLeft = itemView.findViewById(R.id.txt_left);
            txtTotal = itemView.findViewById(R.id.txt_total);
            txtPrice1 = itemView.findViewById(R.id.txtPrice1);
            txtAmount1 = itemView.findViewById(R.id.txtAmount1);
            notify1 = itemView.findViewById(R.id.notify1);
            blocker1 = itemView.findViewById(R.id.blocker1);
            lock1 = itemView.findViewById(R.id.lock1);
            txtnotify1 = itemView.findViewById(R.id.txtnotify1);
            imgnotify1 = itemView.findViewById(R.id.imgnotify1);
            txtName = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            aucNameLL = itemView.findViewById(R.id.aucNameLL);
            handler = new Handler(Looper.getMainLooper());


        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startCountdown(String endDateTime, final int position, final List<GetCategories.JSONDATum> itemList, final RecyclerView.Adapter<?> adapter) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            endTime = LocalDateTime.parse(endDateTime, formatter);

            updateTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    updateCountdown(position, itemList, adapter);
                    handler.postDelayed(this, 1000); // Update every second
                }
            };
            handler.post(updateTimeRunnable);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void updateCountdown(int position, List<GetCategories.JSONDATum> itemList, RecyclerView.Adapter<?> adapter) {
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(currentTime, endTime);

            if (duration.isNegative() || duration.isZero()) {
                txtTime11.setText("Ends: Expired");
                handler.removeCallbacks(updateTimeRunnable); // Stop updates when expired

                // Remove expired items after checking all items
                checkAndRemoveExpiredItems(itemList, adapter);
            } else {
                long seconds = duration.getSeconds();
                long days = seconds / (24 * 3600);
                seconds %= (24 * 3600);
                long hours = seconds / 3600;
                seconds %= 3600;
                long minutes = seconds / 60;
                seconds %= 60;

                String timeRemaining = String.format("Ends in: %dd %dh %dm %ds", days, hours, minutes, seconds);
                txtTime11.setText(timeRemaining);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void checkAndRemoveExpiredItems(List<GetCategories.JSONDATum> itemList, RecyclerView.Adapter<?> adapter) {
            LocalDateTime currentTime = LocalDateTime.now();
            List<Integer> expiredIndices = new ArrayList<>();

            // Iterate through the list and check for expired items
            for (int i = 0; i < itemList.size(); i++) {
                GetCategories.JSONDATum item = itemList.get(i);
                Duration duration = Duration.between(currentTime, endTime);


                if (duration.isNegative() || duration.isZero()) {
                    expiredIndices.add(i);
                }
            }

            // Remove expired items from the list in reverse order to avoid index shifting issues
            Collections.reverse(expiredIndices);
            for (int index : expiredIndices) {
                itemList.remove(index);
                adapter.notifyItemRemoved(index);
            }
        }



    }

    static class ViewMoreViewHolder extends RecyclerView.ViewHolder {

        public ViewMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void filterList(ArrayList<GetCategories.JSONDATum> filtered_list){
        this.items=filtered_list;
        notifyDataSetChanged();
    }

}
