/**
 * The UserLottoAdapter class is a RecyclerView adapter used to display a list of UserBid objects in a
 * card layout.
 */
package com.wowcodes.supreme.Adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.Ticket;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Userticketsadapter extends RecyclerView.Adapter<Userticketsadapter.ViewHolder> {
    Context mContext;
    BindingService videoService;
    String contactEmail = "";
    String userName = "";

    ArrayList<Ticket.Ticket_inner> userticketsModelArrayList;

    public Userticketsadapter(Context context, ArrayList<Ticket.Ticket_inner> userticketsModelArrayList) {
        this.mContext = context;
        this.userticketsModelArrayList = userticketsModelArrayList;
        fetchContactEmail();

    }

    @Override
    public Userticketsadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_tickets_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Userticketsadapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            RetrofitVideoApiBaseUrl.getClient().create(BindingService.class).settings().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                    contactEmail=arrayList.get(0).getSupport_email();
                }

                @Override
                public void onFailure(Call<SettingModel> call, Throwable t) {
                }
            });
        } catch (Exception ignore) {}
        holder.ball1txt.setText(userticketsModelArrayList.get(position).getBall_1());
        holder.ball2txt.setText(userticketsModelArrayList.get(position).getBall_2());
        holder.ball3txt.setText(userticketsModelArrayList.get(position).getBall_3());
        holder.ball4txt.setText(userticketsModelArrayList.get(position).getBall_4());
        holder.ball5txt.setText(userticketsModelArrayList.get(position).getBall_5());
        holder.ball6txt.setText(userticketsModelArrayList.get(position).getBall_6());
        holder.ball7txt.setText(userticketsModelArrayList.get(position).getBall_7());
        holder.ball8txt.setText(userticketsModelArrayList.get(position).getBall_8());
        int totalballs=Integer.parseInt(userticketsModelArrayList.get(position).getNormal_ball_limit())+Integer.parseInt(userticketsModelArrayList.get(position).getPremium_ball_limit());
        int premiumballs=Integer.parseInt(userticketsModelArrayList.get(position).getPremium_ball_limit());
        for (int i = 0; i <totalballs ; i++) {
            holder.ballbg[i].setVisibility(View.VISIBLE);
            if (i <premiumballs) {
                holder.ballbg[i].setBackgroundResource(R.drawable.premium);
            }
        }

        // Set ticket price and purchase date
        holder.ticketPrize.setText(userticketsModelArrayList.get(position).getTicket_price() + " coins");
        holder.purchaseDt.setText(userticketsModelArrayList.get(position).getPurchase_date());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTicketDialog(position);  // Pass position only
            }
        });

         }

    @Override public int getItemCount() {
        return userticketsModelArrayList.size();
    }
    private void showTicketDialog(int position) {
        String uniqueTicketId = userticketsModelArrayList.get(position).getUnique_ticket_id();
        int normalBalls = 0;
        int premium_balls = 0;

        try {
            String normalBallLimit = userticketsModelArrayList.get(position).getNormal_ball_limit();
            if (normalBallLimit != null && !normalBallLimit.isEmpty()) {
                normalBalls = Integer.parseInt(normalBallLimit);
            }
        } catch (NumberFormatException e) {
            normalBalls = 0; // Default to 0 if parsing fails
        }

        try {
            String premiumBallLimit = userticketsModelArrayList.get(position).getPremium_ball_limit();
            if (premiumBallLimit != null && !premiumBallLimit.isEmpty()) {
                premium_balls = Integer.parseInt(premiumBallLimit);
            }
        } catch (NumberFormatException e) {
            premium_balls = 0; // Default to 0 if parsing fails
        }

        int total_balls = normalBalls + premium_balls;


        LayoutInflater inflater = LayoutInflater.from(mContext);
        Dialog dialogView = new Dialog(mContext);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.setContentView(R.layout.dialog_ticket_purchased);
        Window window = dialogView.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView qrImageView = dialogView.findViewById(R.id.qrImg);
        ProgressBar progressBar = dialogView.findViewById(R.id.qr_progress_bar);
        TextView purchaseDt=dialogView.findViewById(R.id.purchaseDt);
        TextView priceTkt=dialogView.findViewById(R.id.priceTkt);
        TextView drawDt=dialogView.findViewById(R.id.drawDt);
        TextView username=dialogView.findViewById(R.id.username);
        TextView ticket0=dialogView.findViewById(R.id.ticket0);
        TextView ticket1=dialogView.findViewById(R.id.ticket1);
        TextView ticket2=dialogView.findViewById(R.id.ticket2);
        TextView ticket3=dialogView.findViewById(R.id.ticket3);
        TextView ticket4=dialogView.findViewById(R.id.ticket4);
        TextView ticket5=dialogView.findViewById(R.id.ticket5);
        TextView ticket6=dialogView.findViewById(R.id.ticket6);
        TextView ticket7=dialogView.findViewById(R.id.ticket7);
        TextView printBtn=dialogView.findViewById(R.id.printBtn);
        TextView contactEmailTxt=dialogView.findViewById(R.id.contactEmail);
        Button closeBtn=dialogView.findViewById(R.id.closeBtn);
        String[] ball = new String[8];

// Assign values to each element of the array
        ball[0] =userticketsModelArrayList.get(position).getBall_1();
        ball[1] =userticketsModelArrayList.get(position).getBall_2();
        ball[2] =userticketsModelArrayList.get(position).getBall_3();
        ball[3] =userticketsModelArrayList.get(position).getBall_4();
        ball[4] =userticketsModelArrayList.get(position).getBall_5();
        ball[5] =userticketsModelArrayList.get(position).getBall_6();
        ball[6] =userticketsModelArrayList.get(position).getBall_7();
        ball[7] =userticketsModelArrayList.get(position).getBall_8();
        ConstraintLayout[] ballConsll = new ConstraintLayout[]{
                dialogView.findViewById(R.id.ball1bg), dialogView.findViewById(R.id.ball2bg), dialogView.findViewById(R.id.ball3bg),
                dialogView.findViewById(R.id.ball4bg), dialogView.findViewById(R.id.ball5bg), dialogView.findViewById(R.id.ball6bg),
                dialogView.findViewById(R.id.ball7bg), dialogView.findViewById(R.id.ball8bg)
        };

        printBtn.setOnClickListener(v -> {
            printBtn.setVisibility(GONE);
            closeBtn.setVisibility(View.GONE); // Hide the Close button
            printTicket(dialogView.findViewById(R.id.dialog_container));
            printBtn.setVisibility(VISIBLE);
            closeBtn.setVisibility(VISIBLE);// Pass the root view of the dialog
        });
        priceTkt.setText(userticketsModelArrayList.get(position).getTicket_price() +" "+mContext.getString(R.string.coinstxt));
        contactEmailTxt.setText(contactEmail);
       purchaseDt.setText(userticketsModelArrayList.get(position).getPurchase_date());


        for (int i = 0; i <total_balls ; i++) {
            ballConsll[i].setVisibility(View.VISIBLE);
            if (i <premium_balls) {
                ballConsll[i].setBackgroundResource(R.drawable.premium);
            }
        }
        ticket0.setText(ball[0]);
        ticket1.setText(ball[1]);
        ticket2.setText(ball[2]);
        ticket3.setText(ball[3]);
        ticket4.setText(ball[4]);
        ticket5.setText(ball[5]);
        ticket6.setText(ball[6]);
        ticket7.setText(ball[7]);


        drawDt.setText(userticketsModelArrayList.get(position).getDraw_date());
        username.setText(new SavePref(mContext).getName());
        String qrCodeUrl = Constants.main_url + "/generate_qr.php?text=" + uniqueTicketId;

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(qrCodeUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // Enable disk caching
                .error(R.drawable.ic_error_black_24dp)                // Error image if loading fails
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(qrImageView);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

            }
        });

        dialogView.show();
    }

    private void fetchContactEmail() {
        RetrofitVideoApiBaseUrl.getClient().create(BindingService.class).settings().enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();
                    contactEmail = arrayList.get(0).getSupport_email();
                    notifyDataSetChanged(); // Notify adapter to refresh views
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable t) {
                Log.e("API Error", "Failed to fetch contact email", t);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout[] ballbg;
        TextView ball1txt, ball2txt, ball3txt, ball4txt, ball5txt, ball6txt, ball7txt, ball8txt;
        TextView ticketPrize, purchaseDt;




        public ViewHolder(View itemView) {
             super(itemView);

            // Initialize the views
            ballbg = new ConstraintLayout[8];
            ballbg[0] = itemView.findViewById(R.id.ball1bg);
            ballbg[1] = itemView.findViewById(R.id.ball2bg);
            ballbg[2] = itemView.findViewById(R.id.ball3bg);
            ballbg[3] = itemView.findViewById(R.id.ball4bg);
            ballbg[4] = itemView.findViewById(R.id.ball5bg);
            ballbg[5] = itemView.findViewById(R.id.ball6bg);
            ballbg[6] = itemView.findViewById(R.id.ball7bg);
            ballbg[7] = itemView.findViewById(R.id.ball8bg);

            ball1txt = itemView.findViewById(R.id.ball1txt);
            ball2txt = itemView.findViewById(R.id.ball2txt);
            ball3txt = itemView.findViewById(R.id.ball3txt);
            ball4txt = itemView.findViewById(R.id.ball4txt);
            ball5txt = itemView.findViewById(R.id.ball5txt);
            ball6txt = itemView.findViewById(R.id.ball6txt);
            ball7txt = itemView.findViewById(R.id.ball7txt);
            ball8txt = itemView.findViewById(R.id.ball8txt);

            ticketPrize = itemView.findViewById(R.id.ticketprize);
            purchaseDt = itemView.findViewById(R.id.purchasedt);
           }
    }


    private void printTicket(View view) {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        // Create a bitmap of the view to print
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        // Print the bitmap
        photoPrinter.printBitmap(String.valueOf(R.string.ticket_print), bitmap);
    }


}