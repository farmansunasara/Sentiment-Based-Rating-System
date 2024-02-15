package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView categoryRecycler;
    private RecyclerView productRecycler;

    MyDatabaseHelper myDB;
    private UserCategoryAdaptor userCategoryAdaptor;
    private UserProductAdaptor userProductAdaptor;
    private ImageSlider imageSlider;

    ArrayList<String> categoryIds, categoryNames;
    ArrayList<byte[]> categoryImages;
    ArrayList<String> productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory;

    ArrayList<byte[]> productcov_img, productselected_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        myDB = new MyDatabaseHelper(getActivity());
        categoryRecycler =rootView.findViewById(R.id.categoryRecycler);
        productRecycler=rootView.findViewById(R.id.productRecycler);
        categoryIds = new ArrayList<>();
        categoryNames = new ArrayList<>();
        categoryImages = new ArrayList<byte[]>();
        productIds = new ArrayList<>();
        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productMRPs = new ArrayList<>();
        productSPs = new ArrayList<>();
        productcategory = new ArrayList<>();
        productcov_img = new ArrayList<>();
        productselected_img = new ArrayList<>();

        try {
            storeDataInArrays();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            storeDataInArraysproducts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setupRecyclerView();

        return rootView;
    }
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


    void storeDataInArraysproducts() throws IOException {
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
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
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

    private void setupRecyclerView() {
        userCategoryAdaptor= new UserCategoryAdaptor(getActivity(), categoryIds, categoryNames, categoryImages);
        categoryRecycler.setAdapter(userCategoryAdaptor);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        userProductAdaptor = new UserProductAdaptor(getActivity(),myDB, productIds, productNames, productDescriptions, productMRPs, productSPs, productcategory, productcov_img, productselected_img);
        productRecycler.setAdapter(userProductAdaptor);
        productRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }




}
