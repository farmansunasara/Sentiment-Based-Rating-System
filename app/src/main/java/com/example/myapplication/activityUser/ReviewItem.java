package com.example.myapplication.activityUser;

public class ReviewItem {
    private String reviewDate;
    private String reviewText;
    private float reviewRating;

    public ReviewItem(String reviewDate, String reviewText, float reviewRating) {
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.reviewRating = reviewRating;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getReviewRating() {
        return reviewRating;
    }
}