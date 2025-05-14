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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.ItemCategory;
import com.wowcodes.supreme.R;

import java.util.ArrayList;
import java.util.List;

public class DiffCategoryAdapter extends RecyclerView.Adapter<DiffCategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<ItemCategory> itemCategories;
    public CategorySel catsel;
    public String from;


    public DiffCategoryAdapter(Context context, List<ItemCategory> itemCategories, CategorySel catsel,String from) {
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
        ItemCategory category = itemCategories.get(position);
        holder.mtvCatTitle.setText(category.getTitle());

        List<GetCategories.JSONDATum> items = category.getItems();

        int otype=Integer.parseInt(items.get(0).getoType());
        if (otype == 4 || otype == 5) {
            from = "upcoming";  // Update the 'from' value for the whole adapter
        }
        if (otype == 4 || otype == 5 || "upcoming".equals(from)) {
            holder.dotIndicatorLayout.setVisibility(View.VISIBLE);
            LotteryItemAdapter lotteryItemAdapter = new LotteryItemAdapter(context, new ArrayList<>(items), from, false);
            holder.rvItemCat.setAdapter(lotteryItemAdapter);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(holder.rvItemCat);

            lotteryItemAdapter.setCategorySel(catsel);
            lotteryItemAdapter.setCategoryDetails(category.getTitle(), category.getCatId(), false);

            holder.rvItemCat.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    View snappedView = snapHelper.findSnapView(layoutManager);
                    int snappedPosition = layoutManager.getPosition(snappedView);

                    updateDotIndicators(holder, snappedPosition);
                }
            });

            holder.seeall.setOnClickListener(view -> {
                lotteryItemAdapter.setShowAllItems(true);
                holder.seeall.setVisibility(View.GONE);
                lotteryItemAdapter.setCategoryDetails(category.getTitle(), category.getCatId(), true);
                try {
                    lotteryItemAdapter.setShowAllItems(true);
                    catsel.sendData(category.getTitle(), category.getCatId(), from);
                } catch (Exception ignore) {}
            });

        } else {
            AuctionItemAdapter auctionItemAdapter = new AuctionItemAdapter(context, (ArrayList<GetCategories.JSONDATum>) category.getItems(), from, false);
            holder.rvItemCat.setAdapter(auctionItemAdapter);
            holder.seeall.setOnClickListener(view -> {
                // Pass category details and show all items
                try {
                    catsel.sendData(category.getTitle(), category.getCatId(), from);
                } catch (Exception ignore) {}
            });

        }


        holder.rvItemCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        updateDotIndicators(holder, 0);
    }

    private void updateDotIndicators(CategoryViewHolder holder, int position) {
        // Reset all dots to inactive state
        holder.dot1.setBackgroundResource(R.drawable.dot_inactive);
        holder.dot2.setBackgroundResource(R.drawable.dot_inactive);
        holder.dot3.setBackgroundResource(R.drawable.dot_inactive);
        holder.dot4.setBackgroundResource(R.drawable.dot_inactive);

        int itemCount = holder.rvItemCat.getAdapter().getItemCount();

        // Ensure only the necessary number of dots are visible
        holder.dot1.setVisibility(itemCount >= 1 ? View.VISIBLE : View.GONE);
        holder.dot2.setVisibility(itemCount >= 2 ? View.VISIBLE : View.GONE);
        holder.dot3.setVisibility(itemCount >= 3 ? View.VISIBLE : View.GONE);
        holder.dot4.setVisibility(itemCount >= 4 ? View.VISIBLE : View.GONE);

        // Activate the dot corresponding to the current position
        if (position == 0) {
            holder.dot1.setBackgroundResource(R.drawable.dot_active);
        } else if (position == 1) {
            holder.dot2.setBackgroundResource(R.drawable.dot_active);
        } else if (position == 2) {
            holder.dot3.setBackgroundResource(R.drawable.dot_active);
        } else if (position == 3) {
            holder.dot4.setBackgroundResource(R.drawable.dot_active);
        }
    }
    @Override public int getItemCount() {
        return itemCategories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView mtvCatTitle;
        LinearLayout seeall,dotIndicatorLayout;
        public RecyclerView rvItemCat;

        View dot1,dot2,dot3,dot4;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mtvCatTitle = itemView.findViewById(R.id.tvCatTitle);
            rvItemCat = itemView.findViewById(R.id.rvCategoryItem);
            seeall=itemView.findViewById(R.id.seeall);
            dotIndicatorLayout=itemView.findViewById(R.id.dotIndicatorLayout);
            dot1=itemView.findViewById(R.id.dot1);
            dot2=itemView.findViewById(R.id.dot2);
            dot3=itemView.findViewById(R.id.dot3);
            dot4=itemView.findViewById(R.id.dot4);

        }
    }

    public interface CategorySel{
        void sendData(String cateName,String  c_id,String from) throws IllegalAccessException, InstantiationException;
    }
}