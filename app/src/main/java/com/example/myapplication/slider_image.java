package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class slider_image extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;

    private RecyclerView recyclerView;
    private ImageView imgGallery;
    private Button btnGalleryPick;
    private Button btnAddToSlider;
    private Uri selectedImageUri;
    private ImageAdapter imageAdapter;
    private List<ImageModel> imageList = new ArrayList<>();

    private MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image);

        // Initialize the MyDatabaseHelper instance
        myDB = new MyDatabaseHelper(this);

        imgGallery = findViewById(R.id.imgGallery);
        btnGalleryPick = findViewById(R.id.btngallery);
        btnAddToSlider=findViewById(R.id.addimagetoslider);
        imageAdapter = new ImageAdapter(imageList);

        btnGalleryPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
            }
        });

        btnAddToSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbanner();
            }
        });
    }

    private void addbanner() {
        try {
            byte[] coverImageData = convertImageToByteArray(selectedImageUri);

            List<byte[]> imageListData = new ArrayList<>();
            for (ImageModel imageModel : imageList) {
                Uri imageUri = imageModel.getImageUri();
                byte[] imageData = convertImageToByteArray(imageUri);
                if (imageData != null) {
                    imageListData.add(imageData);
                }
            }

            Log.d("SliderImage", "Converted images to byte array");

            boolean isSliderAdded = myDB.addSlider(coverImageData);

            Log.d("SliderImage", "Inserted images into database");

            if (isSliderAdded) {
                Toast.makeText(this, "Images added to slider successfully", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Toast.makeText(this, "Failed to add images to slider", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            handleDatabaseInsertionError(e);
        }
    }

    private void clearInputFields() {
        imageList.clear();
        imageAdapter.notifyDataSetChanged();
    }

    private void handleImageConversionError(Exception e) {
        e.printStackTrace();
        Toast.makeText(slider_image.this, "Error converting image", Toast.LENGTH_SHORT).show();
    }

    private void handleDatabaseInsertionError(Exception e) {
        e.printStackTrace();
        Toast.makeText(slider_image.this, "Failed to add slider", Toast.LENGTH_SHORT).show();
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
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            } else {
                Toast.makeText(this, "Failed to read image", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error converting image to byte array", Toast.LENGTH_SHORT).show();
            return null;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null) {
                    selectedImageUri = data.getData(); // Assign value to class-level variable
                    if (selectedImageUri != null) {
                        imgGallery.setImageURI(selectedImageUri);
                        byte[] imageData = convertImageToByteArray(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    } else {
                        Log.e("SliderImage", "Selected image URI is null");
                        Toast.makeText(this, "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("SliderImage", "Gallery Intent data is null");
                    Toast.makeText(this, "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("SliderImage", "Unexpected request code: " + requestCode);
                Toast.makeText(this, "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("SliderImage", "Result code is not RESULT_OK");
            Toast.makeText(this, "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
        }
    }

}
