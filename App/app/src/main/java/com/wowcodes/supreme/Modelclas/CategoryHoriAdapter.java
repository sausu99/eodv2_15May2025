package com.wowcodes.supreme.Modelclas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;

import com.wowcodes.supreme.Adapter.AuctionItemAdapter;
import com.wowcodes.supreme.Adapter.LotteryItemAdapter;
import com.wowcodes.supreme.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryHoriAdapter extends RecyclerView.Adapter<CategoryHoriAdapter.CategoryViewHolder> {

    private Context context;
    private List<ItemCategory> itemCategories;
    private String from;

    public CategoryHoriAdapter(Context context, List<ItemCategory> itemCategories, String from) {
        this.context = context;
        this.itemCategories = new ArrayList<>(itemCategories); // Initialize with a copy of the list
        this.from = from;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryhoriitem, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ItemCategory category = itemCategories.get(position);
        if ("upcoming".equals(from)) {
            LotteryItemAdapter categoryItemAdapter = new LotteryItemAdapter(
                    context,
                    new ArrayList<>(category.getItems()),
                    from,
                    true
            );
            categoryItemAdapter.setShowAllItems(true);
            holder.rvItemCat.setAdapter(categoryItemAdapter);
            holder.rvItemCat.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            AuctionItemAdapter categoryItemAdapter = new AuctionItemAdapter(
                    context,
                    new ArrayList<>(category.getItems()), // Make a copy of the list
                    from,
                    true
            );
            holder.rvItemCat.setAdapter(categoryItemAdapter);
            holder.rvItemCat.setLayoutManager(new GridLayoutManager(context, 2));
        }
    }

    @Override
    public int getItemCount() {
        return itemCategories.size();
    }

    public void updateCategories(List<ItemCategory> newCategories) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(this.itemCategories, newCategories));
        this.itemCategories.clear();
        this.itemCategories.addAll(newCategories);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvItemCat;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            rvItemCat = itemView.findViewById(R.id.rvCategoryItem);
        }
    }

    static class CategoryDiffCallback extends DiffUtil.Callback {
        private final List<ItemCategory> oldList;
        private final List<ItemCategory> newList;

        public CategoryDiffCallback(List<ItemCategory> oldList, List<ItemCategory> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getCatId().equals(newList.get(newItemPosition).getCatId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
