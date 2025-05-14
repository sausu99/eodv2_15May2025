/**
 * The `MyAdapter` class is a RecyclerView adapter that displays a list of categories horizontally and
 * allows for item click events.
 */
package com.wowcodes.supreme.Adapter;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final Log log = LogFactory.getLog(MyAdapter.class);
    private Context context;
    public categorySel catSel;
    List<GetCategories.JSONDATum> dataList;
    List<CategoryHorizontal> categoryList;

    // Constructor without passing "from", we use the category's Otype instead
    public MyAdapter(List<CategoryHorizontal> categoryList, List<GetCategories.JSONDATum> dataList, categorySel catSel) {
        this.categoryList = categoryList;
        this.dataList = dataList;
        this.catSel = catSel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the appropriate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryhorizontalrecyle, parent, false);
        context = parent.getContext();  // Initialize context here
        return new ViewHolder(view, catSel);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Retrieve the current category data
        CategoryHorizontal data = categoryList.get(position);

        // Context check
        if (context == null) return;

        // Load image using Glide
        Glide.with(context)
                .load(data.getImage())
                .error(R.drawable.img_background)  // Fallback image
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.imageViewCircle);

        // Set the category name text
        holder.textViewName.setText(data.getName());

        // Set item click listener
        holder.itemView.setOnClickListener(view -> {
            String otype = data.getOtype();  // Use the Otype set in the category
            try {
                // Send data back to the interface based on the item's oType (upcoming or live)
                catSel.sendData(data.getName(), data.getId(), otype);
            } catch (Exception e) {
                Intent intent = new Intent(context, CategorySelected.class);
                intent.putExtra("category", data.getName());
                intent.putExtra("c_id", data.getId());
                intent.putExtra("type", otype);  // Pass oType as the type (either "upcoming" or "live")
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the category list
        return categoryList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCircle;
        TextView textViewName;
        categorySel catS;

        public ViewHolder(@NonNull View itemView, categorySel catS) {
            super(itemView);
            this.catS = catS;
            imageViewCircle = itemView.findViewById(R.id.imageViewCircle);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }

    // Interface to send data back when a category is clicked
    public interface categorySel {
        void sendData(String cateName, String c_id, String from) throws IllegalAccessException, InstantiationException;
    }
}
