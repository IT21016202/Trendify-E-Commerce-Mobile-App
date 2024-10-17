package com.example.e_commerce;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textViewOrderId.setText("Tracking Number: " + order.getId());
        holder.textViewStatus.setText("Status: " + order.getStatus());
        holder.textViewOrderDate.setText("Date: " + order.getOrderDate());
        holder.textViewShippingAddress.setText("Shipping Address: " + order.getShippingAddress());
        holder.textViewOrderTotal.setText("Total: $" + order.getOrderTotal());

        // Additional binding for order items can go here

        // Set the visibility of the cancel button based on order status
        if ("Pending".equals(order.getStatus())) {
            holder.btnCancelOrder.setVisibility(View.VISIBLE); // Show the button
        } else {
            holder.btnCancelOrder.setVisibility(View.GONE); // Hide the button
        }

        // Set up click listener for the cancel button
        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to handle order cancellation
                showCancelConfirmation(order.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        Button btnCancelOrder;
        TextView textViewOrderId, textViewStatus, textViewOrderDate, textViewShippingAddress, textViewOrderTotal;

        public OrderViewHolder(View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewShippingAddress = itemView.findViewById(R.id.textViewShippingAddress);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderTotal);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }

    private void cancelOrder(String id){
        String url = "https://10.0.2.2:7022/api/Order/"+id+"/cancel";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    // Handle success response
                    Toast.makeText(context, "Order Number " + id + " has been canceled", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error response
                    Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showCancelConfirmation(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Order");
        builder.setMessage("Are you sure you want to cancel this order?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed, cancel the order
            cancelOrder(orderId);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled the dialog
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
