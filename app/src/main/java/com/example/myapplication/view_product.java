package com.example.myapplication;

import static com.example.myapplication.MyDatabaseHelper.MAX_PRODUCT_IMAGES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class view_product extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    FloatingActionButton add_button_product;
    MyDatabaseHelper myDB;
    RecyclerView view_product_recyclerview;
    ProductAdaptor productAdapter;
    ArrayList<String> productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory;

    ArrayList<byte[]> productcov_img, productselected_img;
    ImageView left_icon_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        checkAndRequestPermissions();
        initViewsAndDBHelper();
        initAdaptorLists();

        try {
            storeDataInArrays();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setupRecyclerView();

        setClickListeners();
    }



    // Check and request permissions if needed
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    // Initialize views and database helper
    private void initViewsAndDBHelper() {
        add_button_product = findViewById(R.id.add_button_product);
        left_icon_product=findViewById(R.id.left_icon_product);
        view_product_recyclerview = findViewById(R.id.view_product_recyclerview);
        myDB = new MyDatabaseHelper(this);

    }

    private void initAdaptorLists() {
        // Initialize adaptor lists here
        productIds = new ArrayList<>();
        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productMRPs = new ArrayList<>();
        productSPs = new ArrayList<>();
        productcategory = new ArrayList<>();
        productcov_img = new ArrayList<>();
        productselected_img = new ArrayList<>();
    }


    private void setupRecyclerView() {
        productAdapter = new ProductAdaptor(this, productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory, productcov_img, productselected_img);
        view_product_recyclerview.setAdapter(productAdapter);
        view_product_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    void storeDataInArrays() throws IOException {
        Cursor cursor = myDB.viewProductDetails();

        if (cursor != null && cursor.getCount() > 0) {
            int coverImagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.PRODUCT_COVER_IMAGE);

            while (cursor.moveToNext()) {
                productIds.add(cursor.getString(0));
                productNames.add(cursor.getString(1));
                productDescriptions.add(cursor.getString(2));
                productMRPs.add(cursor.getString(3));
                productSPs.add(cursor.getString(4));
                productcategory.add(cursor.getString(5));

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
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
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

    private void setClickListeners() {

        left_icon_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view_product.this, "Back", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view_product.this, Admin.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity to remove it from the stack
            }
        });
        add_button_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_product.this, Add_product_form.class);
                startActivity(intent);
            }
        });
    }
}
