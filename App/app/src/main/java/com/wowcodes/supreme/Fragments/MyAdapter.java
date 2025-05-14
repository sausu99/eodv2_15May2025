/**
 * The `MyAdapter` class is a RecyclerView adapter that displays a list of categories horizontally and
 * allows for item click events.
 */
package com.wowcodes.supreme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.CategorySelected;
import com.wowcodes.supreme.Modelclas.CategoryHorizontal;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    public categorySel catSel;
    String from;
    List<GetCategories.JSONDATum> dataList;
    List<CategoryHorizontal> categoryList;
    public MyAdapter(List<CategoryHorizontal> categoryList , List<GetCategories.JSONDATum> dataList, categorySel catSel,String from) {
        this.categoryList = categoryList;
        this.dataList = dataList;
        this.catSel = catSel;
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (!from.equalsIgnoreCase("shop"))
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryhorizontalrecyleshop, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryhorizontalrecyle, parent, false);
        context = view.getContext();
        return new ViewHolder(view , catSel);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryHorizontal data = categoryList.get(position);


        if(context == null)
            return;
        Glide.with(context)
                .load(data.getImage())
                .error(R.drawable.img_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.imageViewCircle);
        holder.textViewName.setText(data.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    catSel.sendData(data.getName(),data.getId(),data.getType());
                } catch (Exception e) {
                    Intent intent = new Intent(context, CategorySelected.class);
                    intent.putExtra("category" , data.getName());
                    intent.putExtra("c_id" , data.getId());
                    intent.putExtra("type" , from);

                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCircle;
        TextView textViewName;
        categorySel catS;

        public ViewHolder(@NonNull View itemView , categorySel catS ) {
            super(itemView);
            this.catS = catS;
            imageViewCircle = itemView.findViewById(R.id.imageViewCircle);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }

    public interface categorySel{
        void sendData(String cateName,String c_id,String from) throws IllegalAccessException, InstantiationException;
    }
}