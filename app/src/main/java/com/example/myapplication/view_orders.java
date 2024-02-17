package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class view_orders extends AppCompatActivity implements OrderAdapter.OnOrderStatusUpdateListener {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        TextView headerText = findViewById(R.id.header_text);
        headerText.setText("New Orders");

        recyclerView = findViewById(R.id.admin_new_product_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList, this); // Pass the listener reference
        recyclerView.setAdapter(orderAdapter);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        // Initialize your database helper
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this);

        // Retrieve order list from the database
        List<Order> ordersFromDatabase = databaseHelper.getOrderListFromDatabase();

        // Clear the existing list and add new orders
        orderList.clear();
        orderList.addAll(ordersFromDatabase);

        // Notify the adapter that the dataset has changed
        orderAdapter.notifyDataSetChanged();
    }

    // Implement the interface method to handle order status update
    @Override
    public void onOrderStatusUpdate(int position, String newStatus) {
        // Update the order status in the database or perform any other action
        Order order = orderList.get(position);
        order.setStatus(newStatus);

        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this);
        databaseHelper.updateOrderStatus(order.getOrderId(), newStatus);
        // Notify the adapter that the data has changed
        orderAdapter.notifyItemChanged(position);
        // Update the order status in the database (call your database update method here)
        // For demonstration, we'll just log the updated status
        System.out.println("Updated status for order " + order.getOrderId() + ": " + order.getStatus());
    }
}
