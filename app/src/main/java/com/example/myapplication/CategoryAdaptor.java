package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.MyViewHolder> {

    private Context context;
    private ArrayList<String> category_id;
    private ArrayList<String> category_name;
    private ArrayList<byte[]> category_img;

    private OnItemClickListener onItemClickListener;

    CategoryAdaptor(Context context, ArrayList category_id, ArrayList category_name, ArrayList category_img, OnItemClickListener listener) {
        this.context = context;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_img = category_img;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryAdaptor.MyViewHolder holder, int position) {
        // Check if the position is within the bounds of the lists
        if (position >= 0 && position < category_id.size()
                && position < category_name.size() && position < category_img.size()) {

            // Access elements from the ArrayList using position
            String currentCategoryId = category_id.get(position);
            String currentCategoryName = category_name.get(position);
            byte[] currentCategoryImage = category_img.get(position);
            Log.d("AdapterDebug", "Position: " + position);
            Log.d("AdapterDebug", "Category ID: " + currentCategoryId);
            Log.d("AdapterDebug", "Category Name: " + currentCategoryName);
            Log.d("AdapterDebug", "Category Image: " + Arrays.toString(currentCategoryImage));
            // Assuming category_img is a list of byte arrays representing images
            holder.category_name_txt.setText(currentCategoryName);

            // Update your ViewHolder with the data
            if (currentCategoryImage != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(currentCategoryImage, 0, currentCategoryImage.length);

                    if (bitmap != null) {
                        holder.category_img.setImageBitmap(bitmap);
                    } else {
                        Log.e("ImageDecodeError", "Bitmap is null after decoding");
                    }
                } catch (Exception e) {
                    Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                }
            } else {
                Log.d("ImageLoading", "Using default image for position: " + position);
                holder.category_img.setImageResource(R.drawable.cust); // Set a default image or handle accordingly
            }
        } else {
            Log.e("AdapterError", "Invalid position: " + position);
        }
    }


    @Override
    public int getItemCount() {
        return category_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView category_name_txt;
        ImageView category_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name_txt = itemView.findViewById(R.id.category_name_txt);
            category_img = itemView.findViewById(R.id.category_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(category_id.get(getAdapterPosition()));
            }
        }
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(String categoryId);
    }
}
