package com.example.myapplication;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adaptor.CartAdaptor;
import com.example.myapplication.Adaptor.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCartItems;
    private CartAdaptor cartAdapter;
    private List<CartItem> cartItemList;
    ImageView buttonRemove;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        recyclerViewCartItems = view.findViewById(R.id.recyclerViewCartItems);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize cart item list
        cartItemList = new ArrayList<>();

        // Initialize adapter
        cartAdapter = new CartAdaptor(cartItemList, requireContext());
        recyclerViewCartItems.setAdapter(cartAdapter);

        // Populate cart items (you can add your own logic to fetch cart items)
        populateCartItems();

        return view;
    }


    private void populateCartItems() {
        // Clear existing cart items
        cartItemList.clear();

        // Retrieve cart items from the database
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        Cursor cursor = dbHelper.viewCartItems();

        // Check if the cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Log the column names for debugging
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("ColumnNames", columnName);
            }

            do {
                // Extract cart item details from the cursor
                int cartItemId = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_ID)); // Assuming CART_ID is the column name for the cart item ID
                byte[] coverImageData = cursor.getBlob(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_COVER_IMAGE));
                // Convert the byte array back to a Bitmap
                Bitmap coverImage = BitmapFactory.decodeByteArray(coverImageData, 0, coverImageData.length);
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_NAME));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_QUANTITY));
                double totalPrice = productPrice * quantity;
                String coverImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_COVER_IMAGE));


                // Create a CartItem object and add it to the cartItemList
                cartItemList.add(new CartItem(cartItemId, coverImage, coverImagePath, productName, productPrice, quantity, totalPrice));
            } while (cursor.moveToNext());

            // Close the cursor after use
            cursor.close();
        } else {
            // Handle the case when cursor is null or empty
            Log.d("CartFragment", "No cart items found in the database");
            // You can show a message or take appropriate action
        }

        // Notify the adapter about the data change
        cartAdapter.notifyDataSetChanged();
    }




}
