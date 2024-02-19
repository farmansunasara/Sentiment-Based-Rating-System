package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class SettingFragment extends Fragment {

    private LinearLayout linearlayoutProducts;
    private MyDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearlayoutProducts = view.findViewById(R.id.linearlayoutOrderProducts);
        databaseHelper = new MyDatabaseHelper(getContext());

        displayOrders();
    }

    private void displayOrders() {
        List<Order> orders = databaseHelper.getOrderDataFromDatabase();

        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                View orderView = getLayoutInflater().inflate(R.layout.order_item, null);

                TextView textViewOrderNumber = orderView.findViewById(R.id.textViewOrderNumber);
                TextView textViewOrderDate = orderView.findViewById(R.id.textViewOrderDate);
                TextView textViewOrderStatus = orderView.findViewById(R.id.textViewOrderStatus);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayoutOrderProducts = orderView.findViewById(R.id.linearlayoutOrderProducts);
                TextView textViewTotalAmount = orderView.findViewById(R.id.textViewTotalAmount);

                textViewOrderNumber.setText("Order Number: " + order.getOrderId());
                textViewOrderDate.setText("Order Date: " + order.getOrderDate());
                textViewOrderStatus.setText("Order Status: " + order.getStatus());

                List<String> products = order.getProducts();
                for (String product : products) {
                    TextView textViewProduct = new TextView(getContext());
                    textViewProduct.setText(product);
                    linearLayoutOrderProducts.addView(textViewProduct);
                }

                textViewTotalAmount.setText("Total Amount: $" + order.getTotalAmount());

                linearlayoutProducts.addView(orderView);
            }
        } else {
            TextView noOrdersTextView = new TextView(getContext());
            noOrdersTextView.setText("No orders found.");
            linearlayoutProducts.addView(noOrdersTextView);
        }
    }
}
