package com.example.flixster.adapters;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Review;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReviewAuthor;
        TextView tvReviewDate;
        TextView tvReviewContent;
        RatingBar rbReviewRating;
        ImageView ivReviewerProfile;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            rbReviewRating = itemView.findViewById(R.id.rbReviewRating);
            ivReviewerProfile = itemView.findViewById(R.id.ivReviewerProfile);
        }

        public void bind(Review review){
            tvReviewAuthor.setText(review.getAuthorUsername());
            tvReviewDate.setText(review.getCreationDate());
            tvReviewContent.setText(review.getContent());
            String hold = review.getContent();
            rbReviewRating.setRating(review.getRating().floatValue() / 2.0f);
            Glide.with(context)
                    .load(review.getAvatarPath())
                    .placeholder(R.drawable.placeholder_avatar)
                    .circleCrop()
                    .into(ivReviewerProfile);
        }
    }
}
