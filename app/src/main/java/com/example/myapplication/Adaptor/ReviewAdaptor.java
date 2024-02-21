package com.example.myapplication.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activityUser.ReviewItem;

import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.MyViewHolder> {
    private List<ReviewItem> reviewList;
    private Context context;

    public ReviewAdaptor(Context context, List<ReviewItem> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdaptor.MyViewHolder holder, int position) {

        ReviewItem reviewItem = reviewList.get(position);
        holder.reviewDate.setText(reviewItem.getReviewDate());
        holder.reviewText.setText(reviewItem.getReviewText());
        holder.reviewRating.setRating(reviewItem.getReviewRating());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewDate, reviewText;
        public RatingBar reviewRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewText = itemView.findViewById(R.id.review_text);
            reviewRating = itemView.findViewById(R.id.review_rating);
        }
    }
}
