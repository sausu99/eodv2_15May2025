/**
 * The CityAdapter class is a RecyclerView adapter used to display a list of cities with their
 * corresponding flag images in an Android application.
 */
package com.wowcodes.supreme.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Modelclas.GetPaymentGateway;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class PGAdapter extends RecyclerView.Adapter<PGAdapter.ViewHolder> {
    public Context context;
    public static ArrayList<GetPaymentGateway.Get_PG_Inner> arrayList;
    public static int selectedPosition=-1;

    public PGAdapter(Context context, ArrayList<GetPaymentGateway.Get_PG_Inner> arrayList) {
        this.context = context;
        PGAdapter.arrayList =arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pg_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetPaymentGateway.Get_PG_Inner PG=arrayList.get(position);
        holder.bind(PG,position,context);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static GetPaymentGateway.Get_PG_Inner getSelectedItem() {
        if (selectedPosition != -1) {
            return arrayList.get(selectedPosition);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        RadioButton radio;
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            radio = itemView.findViewById(R.id.radio);
        }

        public void bind(GetPaymentGateway.Get_PG_Inner PG,int pos,Context c){
            String PGName = PG.getPg_name();
            String imageUrl = PG.getPg_image();

            name.setText(PGName);
            radio.setClickable(false);
            radio.setChecked(PG.isSelected());
            Glide.with(c)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(image);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        radio.setChecked(true);
                        selectItem(position);
                    }
                }
            });
        }

        public void selectItem(int position) {
            if (selectedPosition != position) {
                if (selectedPosition != -1) {
                    arrayList.get(selectedPosition).setSelected(false);
                    notifyItemChanged(selectedPosition);
                }

                arrayList.get(position).setSelected(true);
                notifyItemChanged(position);
                selectedPosition = position;
            }
        }
    }
}