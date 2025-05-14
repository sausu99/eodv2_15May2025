package com.wowcodes.supreme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Modelclas.AllReviewer;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private ArrayList<AllReviewer> reviews;

    public ReviewAdapter(Context context, ArrayList<AllReviewer> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public ImageView reviewerImage;
        public TextView reviewerName;
        public TextView reviewDate;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView star4;
        public ImageView star5;
        public TextView reviewDesc;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerImage = itemView.findViewById(R.id.reviewerimg);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            reviewDate = itemView.findViewById(R.id.reviewdate);
            star1 =itemView.findViewById(R.id.star1);
            star2 =itemView.findViewById(R.id.star2);
            star3 =itemView.findViewById(R.id.star3);
            star4 =itemView.findViewById(R.id.star4);
            star5 =itemView.findViewById(R.id.star5);
            reviewDesc = itemView.findViewById(R.id.reviewdesc);
        }
    }



    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        AllReviewer currentReview = reviews.get(position);
        holder.reviewerName.setText(currentReview.getUser());
        holder.reviewDate.setText("Reviewed on : "+currentReview.getReviewed_on());
        holder.reviewDesc.setText(currentReview.getReview());
        Glide.with(context)
                .load(currentReview.getUser_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.reviewerImage);
        String reviewRating = currentReview.getRating();
        if (reviewRating != null && !reviewRating.isEmpty()) {
            int rating = Integer.parseInt(reviewRating);
            // Set star visibility based on the rating
            setStarVisibility(holder, rating);
        } else {
            // Handle the case where the reviewRating is null or empty
            setStarVisibility(holder, 0); // Assuming 0 means no stars
        }
    }


    private void setStarVisibility(ReviewViewHolder holder, int rating) {
        switch (rating) {
            case 1:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.INVISIBLE);
                holder.star3.setVisibility(View.INVISIBLE);
                holder.star4.setVisibility(View.INVISIBLE);
                holder.star5.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.INVISIBLE);
                holder.star4.setVisibility(View.INVISIBLE);
                holder.star5.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.INVISIBLE);
                holder.star5.setVisibility(View.INVISIBLE);
                break;
            case 4:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.INVISIBLE);
                break;
            case 5:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.VISIBLE);
                break;
            default:
                holder.star1.setVisibility(View.INVISIBLE);
                holder.star2.setVisibility(View.INVISIBLE);
                holder.star3.setVisibility(View.INVISIBLE);
                holder.star4.setVisibility(View.INVISIBLE);
                holder.star5.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}

