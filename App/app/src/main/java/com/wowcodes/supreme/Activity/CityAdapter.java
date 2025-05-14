/**
 * The CityAdapter class is a RecyclerView adapter used to display a list of cities with their
 * corresponding flag images in an Android application.
 */
package com.wowcodes.supreme.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Modelclas.getcity;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    public Context context;
    public ArrayList<getcity.get_city_Inner> arrayList;
    public int selectedPosition=-1;

    public CityAdapter(Context context,ArrayList<getcity.get_city_Inner> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        getcity.get_city_Inner city=arrayList.get(position);
        holder.bind(city,position,context);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public getcity.get_city_Inner getSelectedItem() {
        if (selectedPosition != -1) {
            return arrayList.get(selectedPosition);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        RadioButton radio;
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            radio = itemView.findViewById(R.id.radio);
        }

        public void bind(getcity.get_city_Inner city,int pos,Context c){
            String cityName = city.getCity_name();
            String imageUrl = city.getCity_image();
            String bwimageUrl = city.getCity_bw_image();

            name.setText(cityName);
            radio.setClickable(false);

            if(city.isSelected()) {
                card.getBackground().setColorFilter(Color.parseColor("#edf3ff"), PorterDuff.Mode.SRC_ATOP);
                radio.setChecked(true);

                Glide.with(c)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(image);
            }
            else{
                card.getBackground().setColorFilter(Color.parseColor("#f2f4f7"), PorterDuff.Mode.SRC_ATOP);
                radio.setChecked(false);

                Glide.with(c)
                        .load(bwimageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(image);
            }


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        card.getBackground().setColorFilter(Color.parseColor("#edf3ff"), PorterDuff.Mode.SRC_ATOP);
                        radio.setChecked(true);
                        Glide.with(c)
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .into(image);

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