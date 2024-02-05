package com.example.myapplication;

import android.net.Uri;

// Modify ImageModel class
public class ImageModel {
    private int id;
    private Uri imageUri;
    private String imagePath;  // Add this field

    public ImageModel(int id, Uri imageUri) {
        this.id = id;
        this.imageUri = imageUri;
    }

    // Getter and setter methods for id and imageUri
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
