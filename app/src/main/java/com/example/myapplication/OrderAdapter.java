// OrderAdapter.java
package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderStatusUpdateListener listener;

    public interface OnOrderStatusUpdateListener {
        void onOrderStatusUpdate(int position, String newStatus);
    }

    public OrderAdapter(List<Order> orderList, OnOrderStatusUpdateListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOrderNumber;
        private TextView textViewOrderDate;
        private TextView textViewOrderStatus;
        private TextView textViewTotalAmount;
        private Button buttonUpdateStatus;
        private Spinner spinnerOrderStatus;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderNumber = itemView.findViewById(R.id.textViewOrderNumber);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
            buttonUpdateStatus = itemView.findViewById(R.id.buttonUpdateStatus);
            spinnerOrderStatus = itemView.findViewById(R.id.spinnerOrderStatus);

        }

        public void bind(Order order) {
            textViewOrderNumber.setText("Order Number: " + String.valueOf(order.getOrderId()));
            textViewOrderDate.setText("Order Date: " + order.getOrderDate());
         //   textViewOrderStatus.setText("Order Status: " + order.getStatus());
            textViewTotalAmount.setText("Total Amount: ₹" + order.getTotalAmount());

            // Set OnClickListener for the button
            // Inside the OrderAdapter's bind method
            buttonUpdateStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the item in the RecyclerView
                    int position = getAdapterPosition();
                    // Call the interface method to handle the click event
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the selected status from the spinner
                        String newStatus = spinnerOrderStatus.getSelectedItem().toString();
                        // Pass the new status to the listener
                        listener.onOrderStatusUpdate(position, newStatus);
                    }
                }
            });

        }
    }
}
