package com.example.myapplication.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;

import java.util.List;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartAdaptor(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Log.d("CartItem", "Product Name: " + cartItem.getProductName());
        Log.d("CartItem", "Product Price: " + cartItem.getProductPrice());
        holder.textViewProductName.setText(cartItem.getProductName());
        holder.textViewProductPrice.setText("Price:₹" + cartItem.getProductPrice()); // Set product price
        holder.textViewProductQuantity.setText("Quantity: " + cartItem.getProductQuantity());
        holder.textViewSubtotal.setText("Subtotal:₹" + cartItem.getProductSubtotal());

        // Convert byte array to Bitmap
        String coverImagePath = cartItem.getCoverImagePath();
        if (coverImagePath != null && !coverImagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(coverImagePath);
            if (bitmap != null) {
                // Set the Bitmap as the cover image
                holder.coverImageView.setImageBitmap(bitmap);
            } else {
                // If bitmap is null, you can set a default image or handle the error
                holder.coverImageView.setImageResource(R.drawable.avtar);
            }
        } else {
            // If the cover image path is empty or null, set a default image or handle the error
            holder.coverImageView.setImageResource(R.drawable.avtar);
        }

        // Set onClickListener for the remove button
        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the removeItem method to remove the item at the clicked position
                removeItem(holder);
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductQuantity;
        TextView textViewSubtotal;
        ImageView buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.cover_img_txt);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            textViewSubtotal = itemView.findViewById(R.id.textViewSubtotal);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
    public void removeItem(CartViewHolder holder) {
        int position = holder.getAdapterPosition();
        CartItem removedItem = cartItemList.get(position);
        boolean isRemoved = removeFromCartInDatabase(removedItem.getCartItemId());
        if (isRemoved) {
            cartItemList.remove(position);
            notifyItemRemoved(position);
        } else {
            // Handle error or notify the user that the item could not be removed
        }
    }
    private boolean removeFromCartInDatabase(int cartItemId) {
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(context);
        return databaseHelper.removeFromCart(cartItemId);
    }
}
