package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.UserMain;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView textViewOrderSummary;
    private MyDatabaseHelper dbHelper;
    private Button buttonConfirmOrder;
    private double totalAmount; // Variable to store total amount
    private String address;
    private String paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        textViewOrderSummary = findViewById(R.id.textViewOrderSummary);
        buttonConfirmOrder = findViewById(R.id.buttonConfirmOrder);

        // Initialize database helper
        dbHelper = new MyDatabaseHelper(this);
        retrieveTotalAmount();
        address = getAddressFromSharedPreferences();
        paymentMethod = getPaymentTypeFromSharedPreferences();

        // Fetch cart items and populate order summary
        populateOrderSummary();
        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderToDatabase();
                //startActivity(new Intent(OrderSummaryActivity.this, OrderConfirmationActivity.class));
                Toast.makeText(OrderSummaryActivity.this, "Order is Placed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveTotalAmount() {
        // Retrieve total amount from SharedPreferences
        totalAmount = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getFloat("totalAmount", 0); // Default value is 0 if not found
    }

    private String getAddressFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("address", "");
    }

    private String getPaymentTypeFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("paymentMethod", "");
    }

    private void saveOrderToDatabase() {
        // Set initial status to Pending
        String status = "Pending";

        long customerId = getCustomerIdFromSharedPreferences(); // Example method to retrieve customer ID

        // Insert order details into the database
        long orderId = dbHelper.insertOrder(customerId, totalAmount, address, paymentMethod, status);

        if (orderId != -1) {
            // Order saved successfully
            dbHelper.clearCart();

            SharedPreferences preference = this.getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean("isCart", true);
            editor.apply();
            startActivity(new Intent(this, UserMain.class));
            finish();


        } else {
            // Failed to save order
            // Handle error or notify the user
        }
    }

    private long getCustomerIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("customerId", -1); // Default value -1 if not found
    }
    private void populateOrderSummary() {
        StringBuilder orderSummaryBuilder = new StringBuilder();
        double totalAmount = 0;

        // Retrieve cart items from the database
        Cursor cursor = dbHelper.viewCartItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract cart item details from the cursor
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_NAME));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.CART_PRODUCT_QUANTITY));
                double totalPrice = productPrice * quantity;

                // Append cart item details to the order summary
                orderSummaryBuilder.append(productName).append(" - ₹").append(productPrice).append(" x ").append(quantity).append(" = ₹").append(totalPrice).append("\n");

                // Update total amount
                totalAmount += totalPrice;
            } while (cursor.moveToNext());

            // Close the cursor after use
            cursor.close();
        }

        // Display total amount in the order summary
        orderSummaryBuilder.append("\nTotal: ₹").append(totalAmount);

        // Set the order summary text
        textViewOrderSummary.setText(orderSummaryBuilder.toString());
    }
}
