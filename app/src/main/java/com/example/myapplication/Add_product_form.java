package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Add_product_form extends AppCompatActivity {

    MyDatabaseHelper myDB;
    AutoCompleteTextView categoryAutoComplete;
    List<String> categories;
    ImageView product_coverimage;

    private static final int COVER_IMAGE_REQUEST_CODE = 1;
    private static final int YOUR_IMAGE_REQUEST_CODE = 100;
    private Uri selectedImageUri;
    private ImageAdapter imageAdapter;
    private List<ImageModel> imageList = new ArrayList<>();

    TextInputEditText prodNameEditText, prodDescEditText, prodMrpEditText, prodSpEditText;
    Button selectCoverImgButton, selectImgButton, submitProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_form);

        myDB = new MyDatabaseHelper(this);
        categoryAutoComplete = findViewById(R.id.categoryna);

        prodNameEditText = findViewById(R.id.prodname);
        prodDescEditText = findViewById(R.id.proddesc);
        prodMrpEditText = findViewById(R.id.prodmrp);
        prodSpEditText = findViewById(R.id.prodsp);

        product_coverimage = findViewById(R.id.product_coverimage);
        selectCoverImgButton = findViewById(R.id.select_cover_img);
        selectImgButton = findViewById(R.id.product_img_btn);
        RecyclerView recyclerView = findViewById(R.id.prod_img_recyclerview);
        imageAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        submitProductButton = findViewById(R.id.submit_product_btn);

        categories = loadCategories();
        setupCategoryAutoComplete();

        selectCoverImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cover image selection
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, COVER_IMAGE_REQUEST_CODE);
            }
        });

        selectImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle image selection
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, YOUR_IMAGE_REQUEST_CODE);
            }
        });

        submitProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Existing code for product addition
                addProduct();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addProduct() {
        String productName = prodNameEditText.getText().toString();
        String productDesc = prodDescEditText.getText().toString();
        String productMrp = prodMrpEditText.getText().toString();
        String productSp = prodSpEditText.getText().toString();
        String selectedCategory = categoryAutoComplete.getText().toString();

        if (productName.isEmpty() || productDesc.isEmpty() || productMrp.isEmpty() || productSp.isEmpty() || selectedCategory.isEmpty()) {
            Toast.makeText(Add_product_form.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            byte[] coverImageData = convertImageToByteArray(selectedImageUri);
            List<byte[]> imageListData = new ArrayList<>();

            for (ImageModel imageModel : imageList) {
                Uri imageUri = imageModel.getImageUri();
                byte[] imageData = convertImageToByteArray(imageUri);
                imageListData.add(imageData);
            }

            boolean isProductAdded = myDB.addProduct(productName, productDesc, productMrp, productSp, selectedCategory, coverImageData, imageListData);
            Log.d("DatabaseInsertion", "Result: " + isProductAdded);

            if (isProductAdded) {
                Toast.makeText(Add_product_form.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                clearInputFields();
            } else {
                handleDatabaseInsertionError();
            }
        } catch (Exception e) {
            handleImageConversionError(e);

        }
    }

    private void clearInputFields() {
        prodNameEditText.getText().clear();
        prodDescEditText.getText().clear();
        prodMrpEditText.getText().clear();
        prodSpEditText.getText().clear();
        categoryAutoComplete.getText().clear();
        product_coverimage.setImageBitmap(null);
        product_coverimage.setVisibility(View.GONE);

        imageList.clear();
        imageAdapter.notifyDataSetChanged();
    }

    private void handleImageConversionError(Exception e) {
        e.printStackTrace();
        Toast.makeText(Add_product_form.this, "Error converting image", Toast.LENGTH_SHORT).show();
    }

    private void handleDatabaseInsertionError() {
        Toast.makeText(Add_product_form.this, "Failed to add product", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == COVER_IMAGE_REQUEST_CODE) {
                selectedImageUri = data.getData();
                byte[] imageData = convertImageToByteArray(selectedImageUri);

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                product_coverimage.setImageBitmap(bitmap);
                product_coverimage.setVisibility(View.VISIBLE);
            } else if (requestCode == YOUR_IMAGE_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                byte[] imageData = convertImageToByteArray(selectedImageUri);

                ImageModel newImage = new ImageModel(imageList.size() + 1, selectedImageUri);
                newImage.setImagePath("path/to/your/image"); // Set the actual path here
                imageList.add(newImage);

                imageAdapter.notifyDataSetChanged();
            }
        }
    }
    private byte[] convertImageToByteArray(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            if (inputStream != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            } else {
                // Handle the case where InputStream is null
                throw new IOException("InputStream is null for URI: " + imageUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IOException appropriately, e.g., show a toast message
            runOnUiThread(() -> Toast.makeText(this, "Error converting image to byte array", Toast.LENGTH_SHORT).show());
            return null;  // or throw the exception if you want to propagate it
        }
    }

    private List<String> loadCategories() {
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = myDB.viewCategoryDetails();

        if (cursor != null) {
            int columnIndexCategoryName = cursor.getColumnIndex("Category_name");

            while (cursor.moveToNext()) {
                if (columnIndexCategoryName != -1) {
                    String categoryName = cursor.getString(columnIndexCategoryName);
                    categoryList.add(categoryName);
                }
            }

            cursor.close();
        }

        return categoryList;
    }

    private void setupCategoryAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);

        categoryAutoComplete.setAdapter(adapter);
    }
}
