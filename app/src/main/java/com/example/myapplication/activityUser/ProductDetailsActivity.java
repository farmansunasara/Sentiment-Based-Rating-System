package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.Admin;
import com.example.myapplication.CartFragment;
import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.UserMain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView, addToCartTextView;

    private ImageSlider imageSlider;
    private byte[] productCoverImage;
    private boolean alreadyInCart = false;
    Button review;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        myDB = new MyDatabaseHelper(this);

        imageSlider = findViewById(R.id.image_slider); // Initializing ImageSlider

        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        addToCartTextView = findViewById(R.id.addToCartTextView);
        //review=findViewById(R.id.review);

        // Creating image list
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageSlider.setImageList(imageList);

        String currentProductId = getIntent().getStringExtra("currentProductId");
        Log.d("ProductId", "Received product ID: " + currentProductId);
        getProductDetails(currentProductId);

//        review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProductDetailsActivity.this, productRatingActivity.class);
//                startActivity(intent);
//            }
//        });

        addToCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to add the product to the cart
                addToCart(productCoverImage);
            }
        });
    }

    private void updateAddToCartButton() {
        if (alreadyInCart) {
            addToCartTextView.setText("Go to Cart");
        } else {
            addToCartTextView.setText("Add to Cart");
        }
    }
    private void addToCart(byte[] productCoverImage) {
        // Retrieve product details
        String productId = getIntent().getStringExtra("currentProductId");
        String productName = productNameTextView.getText().toString();
        String productPrice = productPriceTextView.getText().toString();

        // Check if the product is already in the cart
        alreadyInCart = myDB.checkIfProductInCart(productId);
        updateAddToCartButton();


        // If the product is already in the cart, show a message and return
        if (alreadyInCart) {

            Toast.makeText(ProductDetailsActivity.this, "Product is already in the cart", Toast.LENGTH_SHORT).show();
            // Optionally, you can implement code to navigate to the cart fragment here

            SharedPreferences preference = this.getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean("isCart", true);
            editor.apply();
            startActivity(new Intent(this, UserMain.class));
            finish();

//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, new CartFragment())
//                    .addToBackStack(null)
//                    .commit();
            return;
        }

        // Use MyDatabaseHelper to add the product to the cart
        boolean success = myDB.addToCart(Integer.parseInt(productId), productName, Double.parseDouble(productPrice), 1, productCoverImage);

        // Display a toast message based on the result
        if (success) {
            Toast.makeText(ProductDetailsActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProductDetailsActivity.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to retrieve product details from the database
    private void getProductDetails(String currentProductId) {
        Cursor cursor = myDB.viewProductDetailswithdescription(currentProductId);

        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<SlideModel> imageList = new ArrayList<>(); // Create a new image list

            while (cursor.moveToNext()) {
                String productName = cursor.getString(1);
                String productDescription = cursor.getString(2);
                String productPrice = cursor.getString(4);

                productNameTextView.setText(productName);
                productDescriptionTextView.setText(productDescription);
                productPriceTextView.setText(productPrice);

                // Get the cover image path from the cursor
                int coverImagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.PRODUCT_COVER_IMAGE);
                if (coverImagePathColumnIndex != -1) {
                    String coverImagePath = cursor.getString(coverImagePathColumnIndex);
                    if (coverImagePath != null) {
                        handleCoverImage(coverImagePath);
                    }
                }

                // For selected images
                for (int i = 0; i < MyDatabaseHelper.MAX_PRODUCT_IMAGES; i++) {
                    String imagePathColumnName = MyDatabaseHelper.PRODUCT_IMAGE + "_" + i;
                    int imagePathColumnIndexForSelectedImages = cursor.getColumnIndex(imagePathColumnName);

                    if (imagePathColumnIndexForSelectedImages != -1) {
                        String imagePath = cursor.getString(imagePathColumnIndexForSelectedImages);
                        if (imagePath != null) {
                            handleImage(imagePath, imageList); // Pass the imageList to handleImage method
                        }
                    }
                }
            }

            // Set the imageList to the ImageSlider after processing all images
            imageSlider.setImageList(imageList);
        } else {
            Toast.makeText(this, "No data found for this product", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle cover image
    private void handleCoverImage(String imagePath) {
        File f = new File(imagePath);
        if (f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            productCoverImage = stream.toByteArray();
            Log.d("Image File Size", String.valueOf(f.length()));
        } else {
            Log.e("Image File", "File does not exist: " + imagePath);
        }
    }

    // Method to handle other images
    private void handleImage(String imagePath, ArrayList<SlideModel> imageList) {
        File f = new File(imagePath);
        if (f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            if (myBitmap != null) {
                try {
                    // Create a SlideModel with the image URL and title
                    SlideModel slideModel = new SlideModel("file://" + imagePath, null);
                    slideModel.setScaleType(ScaleTypes.CENTER_CROP);

                    // Add the SlideModel to the imageList
                    imageList.add(slideModel);
                } catch (Exception e) {
                    Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                }
            } else {
                Log.e("ImageDecodeError", "Bitmap is null after decoding");
            }
        } else {
            Log.e("ImageDecodeError", "File does not exist: " + imagePath);
        }
    }
}
