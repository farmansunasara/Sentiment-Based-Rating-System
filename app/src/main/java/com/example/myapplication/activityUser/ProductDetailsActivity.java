package com.example.myapplication.activityUser;

import static com.example.myapplication.MyDatabaseHelper.MAX_PRODUCT_IMAGES;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;

    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        myDB = new MyDatabaseHelper(this);

        imageSlider = findViewById(R.id.image_slider); // Initializing ImageSlider

        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);

        // Creating image list
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageSlider.setImageList(imageList);

        String currentProductId = getIntent().getStringExtra("currentProductId");
        Log.d("ProductId", "Received product ID: " + currentProductId);
        getProductDetails(currentProductId);
    }

    private void getProductDetails(String currentProductId) {
        Cursor cursor = myDB.viewProductDetailswithdescription(currentProductId);

        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<SlideModel> imageList = new ArrayList<>(); // Create a new image list

            while (cursor.moveToNext()) {
                String productId = cursor.getString(0);
                String productName = cursor.getString(1);
                String productDescription = cursor.getString(2);
                String productMRP = cursor.getString(3);
                String productSP = cursor.getString(4);
                String productCategory = cursor.getString(5);

                productNameTextView.setText(productName);
                productDescriptionTextView.setText(productDescription);
                productPriceTextView.setText(productSP);

                // For selected images
                for (int i = 0; i < MAX_PRODUCT_IMAGES; i++) {
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

    private void handleImage(String imagePath, ArrayList<SlideModel> imageList) {
        File f = new File(imagePath);
        if (f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            if (myBitmap != null) {
                try {
                    // Convert Bitmap to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();

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
