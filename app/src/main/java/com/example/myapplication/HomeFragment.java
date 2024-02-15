package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView categoryRecycler;
    private RecyclerView productRecycler;

    private MyDatabaseHelper myDB;
    private UserCategoryAdaptor userCategoryAdaptor;
    private UserProductAdaptor userProductAdaptor;
    private ImageSlider image_slider_banner;

    private ArrayList<String> categoryIds, categoryNames;
    private ArrayList<byte[]> categoryImages;
    private ArrayList<String> productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory;
    private ArrayList<byte[]> productcov_img, productselected_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        myDB = new MyDatabaseHelper(getActivity());
        categoryRecycler = rootView.findViewById(R.id.categoryRecycler);
        productRecycler = rootView.findViewById(R.id.productRecycler);
        image_slider_banner = rootView.findViewById(R.id.image_slider_banner); // Initializing ImageSlider

        categoryIds = new ArrayList<>();
        categoryNames = new ArrayList<>();
        categoryImages = new ArrayList<>();
        productIds = new ArrayList<>();
        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productMRPs = new ArrayList<>();
        productSPs = new ArrayList<>();
        productcategory = new ArrayList<>();
        productcov_img = new ArrayList<>();
        productselected_img = new ArrayList<>();

        retrieveSliderImagePaths();

        try {
            storeDataInArrays();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            storeDataInArraysproducts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupRecyclerView();

        return rootView;
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.viewCategoryDetails();
        if (cursor != null && cursor.getCount() > 0) {
            int imagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.CATEGORY_IMG_PATH);
            while (cursor.moveToNext()) {
                categoryIds.add(cursor.getString(0));
                categoryNames.add(cursor.getString(1));
                byte[] imageBytes = cursor.getBlob(imagePathColumnIndex);
                categoryImages.add(imageBytes);
            }
            cursor.close();
        }
    }

    private void storeDataInArraysproducts() {
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
                byte[] coverImageBytes = cursor.getBlob(coverImagePathColumnIndex);
                productcov_img.add(coverImageBytes);
            }
            cursor.close();
        } else {
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        }
    }

//    private void retrieveSliderImagePaths() {
//        // Add a static image path to the sliderImageList
//        ArrayList<SlideModel> sliderImageList = new ArrayList<>();
//        sliderImageList.add(new SlideModel("/storage/emulated/0/iotdirectory/slider170793482149920240214_235021.jpg", null));
//
//        // Check if sliderImageList is not empty before setting it
//        if (!sliderImageList.isEmpty()) {
//            image_slider_banner.setImageList(sliderImageList);
//        } else {
//            // Log a warning if no images were found
//            Log.w("Slider Image Path", "No images found in database");
//        }
//    }

    private void retrieveSliderImagePaths() {
        Cursor cursor = myDB.viewSliderDetails();
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<SlideModel> sliderImageList = new ArrayList<>();
            int imagePathColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.SLIDER_IMAGE);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(imagePathColumnIndex);
                if (imagePath != null) {
                    // Log the retrieved image path
                    Log.d("Slider Image Path", imagePath);
                    // Create SlideModel object and add to the list
                    sliderImageList.add(new SlideModel(imagePath, null));
                }
            }
            // Check if sliderImageList is not empty before setting it
            Log.d("Slider Image List", "Size: " + sliderImageList.size());
            for (SlideModel model : sliderImageList) {
                Log.d("Slider Image List", "Image Path: " + model.getImageUrl());
            }
            if (!sliderImageList.isEmpty()) {
               image_slider_banner.setImageList(sliderImageList);
            } else {
                // Log a warning if no images were found
                Log.w("Slider Image Path", "No images found in database");
            }
        } else {
            // Log an error if cursor is null or empty
            Log.e("Slider Image Path", "No slider images found in database");
        }
        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }
    }
    private void setupRecyclerView() {
        userCategoryAdaptor = new UserCategoryAdaptor(getActivity(), categoryIds, categoryNames, categoryImages);
        categoryRecycler.setAdapter(userCategoryAdaptor);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        userProductAdaptor = new UserProductAdaptor(getActivity(), myDB, productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory, productcov_img, productselected_img);
        productRecycler.setAdapter(userProductAdaptor);
        productRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
