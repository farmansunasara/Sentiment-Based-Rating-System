package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
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

import com.example.myapplication.activityUser.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class UserProductAdaptor extends RecyclerView.Adapter<UserProductAdaptor.MyViewHolder> {

    private Context context;
    private ArrayList<String> prod_id;
    private ArrayList<String> prod_name;
    private ArrayList<String> prod_desc;
    private ArrayList<String> prod_mrp;
    private ArrayList<String> prod_sp;
    private ArrayList<String> prod_category;
    private ArrayList<byte[]> cov_img;
    private ArrayList<byte[]> selected_img;


    UserProductAdaptor(Context context, ArrayList<String> prod_id, ArrayList<String> prod_name, ArrayList<String> prod_desc, ArrayList<String> prod_mrp, ArrayList<String> prod_sp, ArrayList<String> prod_category, ArrayList<byte[]> cov_img, ArrayList<byte[]> selected_img) {
        this.context = context;
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_desc = prod_desc;
        this.prod_mrp = prod_mrp;
        this.prod_sp = prod_sp;
        this.prod_category = prod_category;
        this.cov_img = cov_img;
        this.selected_img = selected_img;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position >= 0 && position < prod_id.size()){

            String currentprodId = prod_id.get(position);
            String currentprodName = prod_name.get(position);
           // String currentprod_desc = prod_desc.get(position);
            String currentprod_mrp = prod_mrp.get(position);
            String currentprod_sp = prod_sp.get(position);
            String currentprod_category = prod_category.get(position);
            byte[] currentcov_img = cov_img.get(position);
            //      byte[] currentselected_img = selected_img.get(position);

            Log.d("AdapterDebug", "Position: " + position);
            Log.d("AdapterDebug", "Product ID: " + currentprodId);
            Log.d("AdapterDebug", "Product Name: " + currentprodName);
          //  Log.d("AdapterDebug", "Product Desc: " + currentprod_desc);
            Log.d("AdapterDebug", "Product mrp: " + currentprod_mrp);
            Log.d("AdapterDebug", "Product sp: " + currentprod_sp);
            Log.d("AdapterDebug", "Product category: " + currentprod_category);

            Log.d("AdapterDebug", "Product cov Image: " + Arrays.toString(currentcov_img));
            // Log.d("AdapterDebug", "Product cov Image: " + Arrays.toString(currentselected_img));


            // Log.d("AdapterDebug", "Product Selected Image: " + Arrays.toString(currentselected_img));

            // Assuming category_img is a list of byte arrays representing images
            holder.product_name_txt.setText(currentprodName);
//            holder.product_desc_txt.setText(currentprod_desc);
            holder.prod_sp_txt.setText(currentprod_sp);
            holder.prod_MRP_txt.setText(currentprod_mrp);
            holder.prod_category_txt.setText(currentprod_category);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ProductId", "Clicked category name: " + currentprodId);

                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("currentProductId", currentprodId);
                    context.startActivity(intent);
                }

            });

            if (currentcov_img != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(currentcov_img, 0, currentcov_img.length);

                    if (bitmap != null) {
                        holder.cover_img_txt.setImageBitmap(bitmap);
                    } else {
                        Log.e("ImageDecodeError", "Bitmap is null after decoding");
                    }
                } catch (Exception e) {
                    Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                }
            } else {
                Log.d("ImageLoading", "Using default image for position: " + position);
                holder.cover_img_txt.setImageResource(R.drawable.cust); // Set a default image or handle accordingly
            }

//            //product Image
//            if (currentselected_img != null) {
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(currentcov_img, 0, currentselected_img.length);
//
//                    if (bitmap != null) {
//                    //    holder.prod_img_txt.setImageBitmap(bitmap);
//                    } else {
//                        Log.e("ImageDecodeError", "Bitmap is null after decoding");
//                    }
//                } catch (Exception e) {
//                    Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
//                }
//            } else {
//                Log.d("ImageLoading", "Using default image for position: " + position);
//                holder.prod_img_txt.setImageResource(R.drawable.cust); // Set a default image or handle accordingly
//            }

        } else {
            Log.e("AdapterError", "Invalid position: " + position);
        }

    }

    @Override
    public int getItemCount() {
        return prod_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView product_name_txt, product_desc_txt, prod_sp_txt, prod_MRP_txt,prod_category_txt;
        ImageView cover_img_txt,prod_img_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name_txt = itemView.findViewById(R.id. product_name_txt);
            prod_category_txt=itemView.findViewById(R.id.prod_category_txt);
//            product_desc_txt = itemView.findViewById(R.id.product_desc_txt);
           prod_sp_txt = itemView.findViewById(R.id.prod_sp_txt);
           prod_MRP_txt = itemView.findViewById(R.id.prod_MRP_txt);
            cover_img_txt = itemView.findViewById(R.id.cover_img_txt);
            // prod_img_txt=itemView.findViewById(R.id.prod_img_txt);


        }
    }
}
