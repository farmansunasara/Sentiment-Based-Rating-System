package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adaptor.ReviewAdaptor;
import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReviewRatingDialog extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView reviewTitle;
    private TextView selectedRating;
    private TextView reviewMessage;
    private TextView characterCount;
    private RatingBar ratingBar;
    private EditText reviewTextField;
    private Button submitMediaButton;
    MyDatabaseHelper myDB;
    private RecyclerView reviewRecyclerView;
    private ReviewAdaptor reviewAdapter;
    private List<ReviewItem> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating_dialog);
        myDB = new MyDatabaseHelper(ReviewRatingDialog.this);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerview);
        RatingReviews ratingReviews = findViewById(R.id.rating_reviews);

        productName = findViewById(R.id.product_name);
        reviewTitle = findViewById(R.id.review_rating_dialog_title);
        ratingBar = findViewById(R.id.review_rating_dialog_ratingBar);
        reviewTextField = findViewById(R.id.review_text_field);
        submitMediaButton = findViewById(R.id.add_media_button);
        String productNames = getIntent().getStringExtra("productName");
        productName.setText(productNames);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewList = myDB.getAllReviews();
        reviewAdapter = new ReviewAdaptor(this, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        int[] ratings = fetchRatingsFromDatabase();

        int raters[] = new int[]{
                ratings[0], // Assuming ratings are stored in the same order as in the colors array
                ratings[1],
                ratings[2],
                ratings[3],
                ratings[4]
        };

        int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")
        };

        ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters);



        submitMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the review text field
                String reviewText = reviewTextField.getText().toString();
                // Get the product name (assuming it's already retrieved from the intent)
                String productNameText = productName.getText().toString();
                // Perform sentiment analysis on the review text in a background thread
                new PerformSentimentAnalysisTask().execute(reviewText);
            }
        });
        setTitle("Leave a review");
       // setMessage("Your review message goes here.");
        reviewAdapter.notifyDataSetChanged();
    }

    private class PerformSentimentAnalysisTask extends AsyncTask<String, Void, Float> {

        @Override
        protected Float doInBackground(String... params) {
            String reviewText = params[0];
            return performSentimentAnalysis(reviewText);
        }

        @Override
        protected void onPostExecute(Float rating) {
            super.onPostExecute(rating);
            // Set the calculated rating to the RatingBar
            setRating(rating);
            // Insert the review into the database
            boolean result = myDB.addReview(productName.getText().toString(), reviewTextField.getText().toString(), rating);
            if (result) {
                // Review inserted successfully
                Toast.makeText(ReviewRatingDialog.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                // Close the dialog or perform any other action
            } else {
                // Failed to insert review
                Toast.makeText(ReviewRatingDialog.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
            }
        }

        private float performSentimentAnalysis(String reviewText) {
            try {
                String apiKey = "e92bbc23d3a94b058114445099f81eb5";
                String apiUrl = "https://mynewresourceiot.cognitiveservices.azure.com/text/analytics/v3.1/sentiment";
                // Construct the request body
                JsonObject requestBody = new JsonObject();
                JsonArray documents = new JsonArray();
                JsonObject document = new JsonObject();
                document.addProperty("id", "1");
                document.addProperty("text", reviewText);
                document.addProperty("language", "en");
                documents.add(document);
                requestBody.add("documents", documents);
                OkHttpClient client = new OkHttpClient();
                // Make the HTTP request with the constructed request body
                Request request = new Request.Builder()
                        .url(apiUrl)
                        .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                        .header("Ocp-Apim-Subscription-Key", apiKey)
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                float sentimentScore = jsonObject.get("documents").getAsJsonArray().get(0).getAsJsonObject().get("confidenceScores").getAsJsonObject().get("positive").getAsFloat();
                // Map sentiment score to a rating
                // Adjust this logic based on the sentiment analysis service you're using
                if (sentimentScore > 0.5) {
                    return 5.0f; // Positive sentiment
                } else if (sentimentScore < 0.5) {
                    return 2.0f; // Negative sentiment
                } else {
                    return 3.0f; // Neutral sentiment
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception
                return 0.0f; // Return default rating if sentiment analysis fails
            }
        }
    }

    public void setTitle(String title) {
        reviewTitle.setText(title);
    }

    public void setMessage(String message) {
   //     reviewMessage.setText(message);
    }

    public void setRating(float rating) {
        ratingBar.setRating(rating);
    }

    private int[] fetchRatingsFromDatabase() {
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this); // Assuming DatabaseHelper is your database helper class
        return databaseHelper.fetchRatings();
    }
}
