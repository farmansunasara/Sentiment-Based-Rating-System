package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adaptor.CartAdaptor;
import com.example.myapplication.Adaptor.CartItem;
import com.example.myapplication.activityUser.AddressActivity;
import com.example.myapplication.activityUser.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCartItems;
    private CartAdaptor cartAdapter;
    private List<CartItem> cartItemList;
    private TextView textViewTotal;
    private Button buttonCheckout;

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

        // Initialize total text view and checkout button
        textViewTotal = view.findViewById(R.id.textViewTotal);
        buttonCheckout = view.findViewById(R.id.buttonCheckout);

        // Populate cart items (you can add your own logic to fetch cart items)
        populateCartItems();

        return view;
    }

    private void populateCartItems() {
        // Clear existing cart items
        cartItemList.clear();

        // Initialize total amount
        double totalAmount = 0; // Initialize total amount

        // Retrieve cart items from the database
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        Cursor cursor = dbHelper.viewCartItems();

        // Check if the cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract cart item details from the cursor
                int cartItemId = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_ID));
                byte[] coverImageData = cursor.getBlob(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_COVER_IMAGE));
                Bitmap coverImage = BitmapFactory.decodeByteArray(coverImageData, 0, coverImageData.length);
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_NAME));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_QUANTITY));
                double totalPrice = productPrice * quantity;
                String coverImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_COVER_IMAGE));

                // Create a CartItem object and add it to the cartItemList
                cartItemList.add(new CartItem(cartItemId, coverImage, coverImagePath, productName, productPrice, quantity, totalPrice));

                totalAmount += totalPrice; // Add to total amount
            } while (cursor.moveToNext());

            // Close the cursor after use
            cursor.close();
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("totalAmount", (float) totalAmount);
        editor.apply();

        // Notify the adapter about the data change
        cartAdapter.notifyDataSetChanged();

        // Set total amount
        textViewTotal.setText("Total: $" + String.format("%.2f", totalAmount));
        final double finalTotalAmount = totalAmount;
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), AddressActivity.class);
                intent.putExtra("Total Amount", finalTotalAmount);
                requireContext().startActivity(intent);
            }
        });

        // Enable or disable checkout button based on cart items availability
        if (cartItemList.isEmpty()) {
            buttonCheckout.setEnabled(false);
        } else {
            buttonCheckout.setEnabled(true);
        }
    }
}
