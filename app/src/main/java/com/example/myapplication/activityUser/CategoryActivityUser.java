package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Adaptor.CategoryProductAdaptor;
import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.UserProductAdaptor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class CategoryActivityUser extends AppCompatActivity {

    MyDatabaseHelper myDB;
    private CategoryProductAdaptor categoryProductAdaptor;
    RecyclerView usercategoryrecycler;
    ArrayList<String> productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory;

    ArrayList<byte[]> productcov_img, productselected_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_user);
        myDB = new MyDatabaseHelper(this);
        usercategoryrecycler=findViewById(R.id.usercategoryrecycler);
        productIds = new ArrayList<>();
        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productMRPs = new ArrayList<>();
        productSPs = new ArrayList<>();
        productcategory = new ArrayList<>();
        productcov_img = new ArrayList<>();
        productselected_img = new ArrayList<>();
        String currentCategoryName = getIntent().getStringExtra("currentCategoryName");
        Log.d("CategoryName", "Received category name: " + currentCategoryName);


        getProducts(currentCategoryName);
        setupRecyclerView();
    }



    private void setupRecyclerView() {

        categoryProductAdaptor = new CategoryProductAdaptor (this, productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory, productcov_img, productselected_img);
        usercategoryrecycler.setAdapter(categoryProductAdaptor);
        usercategoryrecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getProducts(String currentCategoryName) {
        Cursor cursor = myDB.viewProductDetailsbycategory(currentCategoryName);

        if (cursor != null && cursor.getCount() > 0) {
            int coverImagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.PRODUCT_COVER_IMAGE);

            while (cursor.moveToNext()) {
                String productId = cursor.getString(0);
                String productName = cursor.getString(1);
                String productDescription = cursor.getString(2);
                String productMRP = cursor.getString(3);
                String productSP = cursor.getString(4);
                String productCategory = cursor.getString(5);

                Log.d("ProductDetails", "Product ID: " + productId);
                Log.d("ProductDetails", "Product Name: " + productName);
                Log.d("ProductDetails", "Product Description: " + productDescription);
                Log.d("ProductDetails", "Product MRP: " + productMRP);
                Log.d("ProductDetails", "Product SP: " + productSP);
                Log.d("ProductDetails", "Product Category: " + productCategory);
                productIds.add(productId);
                productNames.add(productName);
                productDescriptions.add(productDescription);
                productMRPs.add(productMRP);
                productSPs.add(productSP);
                productcategory.add(productCategory);

                // For cover image
                if (coverImagePathColumnIndex != -1) {
                    String coverImagePath = cursor.getString(coverImagePathColumnIndex);
                    if (coverImagePath != null) {
                        handleImage(coverImagePath, productcov_img);
                    }
                }

                // For selected images
//                for (int i = 0; i < MAX_PRODUCT_IMAGES; i++) {
//                    String imagePathColumnName = MyDatabaseHelper.PRODUCT_IMAGE + "_" + i;
//                    int imagePathColumnIndexForSelectedImages = cursor.getColumnIndex(imagePathColumnName);
//
//                    if (imagePathColumnIndexForSelectedImages != -1) {
//                        String imagePath = cursor.getString(imagePathColumnIndexForSelectedImages);
//                        if (imagePath != null) {
//                            handleImage(imagePath, productselected_img);
//                        }
//                    }
//                }
            }
        } else {
            Toast.makeText(this, "getproductNo data", Toast.LENGTH_SHORT).show();
        }

    }
    private void handleImage(String imagePath, ArrayList<byte[]> imageList) {
        File f = new File(imagePath);
        if (f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            imageList.add(bytes);
            Log.d("Image File Size", String.valueOf(f.length()));
        } else {
            Log.e("Image File", "File does not exist: " + imagePath);
        }
    }
}