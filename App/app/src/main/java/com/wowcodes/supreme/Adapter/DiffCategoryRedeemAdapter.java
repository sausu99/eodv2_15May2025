/**
 * The CategoryAdapter class is a RecyclerView adapter that binds data to the item_category layout and
 * handles the creation and binding of CategoryViewHolder instances.
 */
package com.wowcodes.supreme.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.Modelclas.GetRedeem;
import com.wowcodes.supreme.Modelclas.ItemRedeem;
import com.wowcodes.supreme.R;

import java.util.ArrayList;
import java.util.List;

public class DiffCategoryRedeemAdapter extends RecyclerView.Adapter<DiffCategoryRedeemAdapter.CategoryViewHolder>{
    private Context context;
    private List<ItemRedeem> itemCategories;
    public CategorySel catsel;
    public String from;

    public DiffCategoryRedeemAdapter(Context context, List<ItemRedeem> itemCategories, CategorySel catsel, String from) {
        this.context = context;
        this.itemCategories = itemCategories;
        this.catsel=catsel;
        this.from=from;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        ItemRedeem category = itemCategories.get(position);
        holder.mtvCatTitle.setText(category.getTitle());


        newitemadapterredeem diffCategoryItemAdapter = new newitemadapterredeem(context, (ArrayList<GetRedeem.JSONDATum>) category.getItems(),from,false);
        holder.rvItemCat.setAdapter(diffCategoryItemAdapter);

        holder.rvItemCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        holder.seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    catsel.sendData(category.getTitle(),category.getCatId());
                } catch (Exception ignore) {}
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
        void sendData(String cateName,String  c_id) throws IllegalAccessException, InstantiationException;
    }
}