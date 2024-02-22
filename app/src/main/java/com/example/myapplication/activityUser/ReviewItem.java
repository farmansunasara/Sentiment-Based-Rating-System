package com.example.myapplication.activityUser;

public class ReviewItem {
    private String reviewDate;
    private String reviewText;
    private String productName;
    private float reviewRating;


    public ReviewItem(String productName,String reviewText,float reviewRating,String reviewDate) {
        this.productName=productName;
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