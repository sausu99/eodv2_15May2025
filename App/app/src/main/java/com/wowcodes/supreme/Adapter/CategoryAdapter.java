/**
 * The CategoryAdapter class is a RecyclerView adapter that binds data to the item_category layout and
 * handles the creation and binding of CategoryViewHolder instances.
 */
package com.wowcodes.supreme.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Modelclas.ItemCategory;
import com.wowcodes.supreme.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<ItemCategory> itemCategories;
    public CategorySel catsel;

    public CategoryAdapter(Context context, List<ItemCategory> itemCategories, CategorySel catsel) {
        this.context = context;
        this.itemCategories = itemCategories;
        this.catsel=catsel;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ItemCategory category = itemCategories.get(position);
        holder.mtvCatTitle.setText(category.getTitle());
        holder.mtvCatTitle.setTextColor(Color.parseColor("#"+category.getColor()));

        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(context, category.getItems());
        holder.rvItemCat.setAdapter(categoryItemAdapter);
        holder.rvItemCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    catsel.sendData(category.getTitle());
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override public int getItemCount() {
        return itemCategories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView mtvCatTitle;
        LinearLayout seeall;
        public RecyclerView rvItemCat;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mtvCatTitle = itemView.findViewById(R.id.tvCatTitle);
            rvItemCat = itemView.findViewById(R.id.rvCategoryItem);
            seeall=itemView.findViewById(R.id.seeall);
        }
    }

    public interface CategorySel{
        void sendData(String cateName) throws IllegalAccessException, InstantiationException;
    }
}


