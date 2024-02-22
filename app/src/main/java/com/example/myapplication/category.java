package com.example.myapplication;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class category extends AppCompatActivity implements CategoryAdaptor.OnItemClickListener {

    private final int GALLERY_REQ_CODE = 1000;
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory() + "/iotdirectory";

    private Uri selectedImageUri;
    private static final int PERMISSION_REQUEST_CODE = 200;

    MyDatabaseHelper myDB;
    RecyclerView categoryRecyclerView;
    CategoryAdaptor categoryAdapter;
    ArrayList<String> categoryIds, categoryNames;
    ArrayList<byte[]> categoryImages;

    CardView addImage;
    ImageView categoryImage;
    EditText categoryName;
    MaterialButton addCategoryBtn;
    ImageView left_icon_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Check and request permissions if needed
        checkAndRequestPermissions();

        // Initialize views and database helper
        initViewsAndDBHelper();

        // Load existing category data
        try {
            storeDataInArrays();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set up RecyclerView with adapter
        setupRecyclerView();

        // Set click listeners
        setClickListeners();
    }

    @Override
    public void onItemClick(String categoryId) {
        // Handle item click in the activity
        Toast.makeText(this, "Category clicked: " + categoryId, Toast.LENGTH_SHORT).show();
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
        addImage = findViewById(R.id.addimage);
        categoryImage = findViewById(R.id.categimg);
        categoryName = findViewById(R.id.categoryname);
        addCategoryBtn = findViewById(R.id.add_category_btn);
        left_icon_category=findViewById(R.id.left_icon_category);
        myDB = new MyDatabaseHelper(this);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerview);
        categoryIds = new ArrayList<>();
        categoryNames = new ArrayList<>();
        categoryImages = new ArrayList<byte[]>();
    }


    // Load existing category data
    private void storeDataInArrays() throws IOException {
        Cursor cursor = myDB.viewCategoryDetails();
        if (cursor != null && cursor.getCount() > 0) {
            int imagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.CATEGORY_IMG_PATH);

            if (imagePathColumnIndex != -1) {
                while (cursor.moveToNext()) {
                    categoryIds.add(cursor.getString(0));
                    categoryNames.add(cursor.getString(1));

                    String imagePath = cursor.getString(imagePathColumnIndex);

                    if (imagePath != null) {
                        Log.d("Image File Path", imagePath);
                        File f = new File(imagePath);

                        if (f.exists()) {
                            Bitmap myBitmap= BitmapFactory.decodeFile(imagePath);
                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
                            myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                            byte[] bytes=stream.toByteArray();

                            categoryImages.add(bytes);

                            Log.d("Image File Size", String.valueOf(f.length()));
                        } else {
                            Log.e("Image File", "File does not exist: " + imagePath);
                        }
                    } else {
                        Log.e("Image File", "Image path is null");
                    }
                }
            }
        }


    }

    // Set up RecyclerView with adapter

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdaptor(this, categoryIds, categoryNames, categoryImages, this);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setClickListeners() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
        left_icon_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(category.this, "Back", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(category.this, Admin.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity to remove it from the stack
            }
        });

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddCategory();
            }
        });
    }

    // add category to database hanler
    private void handleAddCategory() {
        String categoryNameText = categoryName.getText().toString().trim();

        if (!categoryNameText.isEmpty() && selectedImageUri != null) {
            byte[] imageData = convertImageToByteArray(selectedImageUri);
            String imagePath = saveImageToExternalStorage(categoryNameText, imageData);

            if (imagePath != null) {
                boolean result = myDB.addCategory(categoryNameText, imagePath);
                if (result) {
                    // Successfully added category
                    Toast.makeText(this, "Successfully added category", Toast.LENGTH_SHORT).show();

                    // Refresh data and update the RecyclerView if needed
                    try {
                        storeDataInArrays();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Error adding category", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a category name and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    //save image to dirctory
    private String saveImageToExternalStorage(String imageName, byte[] imageData) {
        try {
            File directory = new File(EXTERNAL_STORAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (imageData == null) {
                Log.e("SaveImage", "Image data is null for image: " + imageName);
                return null;
            }

            SimpleDateFormat edf =new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now=new Date();
            String timestamp =edf.format(now);
            String cleanedImageName = imageName.replaceAll("[^a-zA-Z0-9]", "");
            String filePath = directory.getAbsolutePath() + File.separator + cleanedImageName + timestamp +".jpg";
            Log.d("SaveImage", "File Path: " + filePath);

            OutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(imageData);
            outputStream.close();

            return filePath;
        } catch (Exception e) {
            Log.e("SaveImageException", "Error saving image: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                selectedImageUri = data.getData();
                String imagePath = getRealPathFromURI(selectedImageUri);
                Log.d("ImageFilePath", "URI: " + selectedImageUri.toString());
                Log.d("ImageFilePath", "RealPath: " + imagePath);

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                categoryImage.setImageBitmap(bitmap);
            } else if (requestCode == PERMISSION_REQUEST_CODE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Log.d("PermissionResult", "External storage permission granted");
                    } else {
                        Log.d("PermissionResult", "External storage permission denied");
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }

    private byte[] convertImageToByteArray(Uri imageUri) {
        try {
            java.io.InputStream inputStream = getContentResolver().openInputStream(imageUri);

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
                Log.e("ConvertImage", "InputStream is null");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
