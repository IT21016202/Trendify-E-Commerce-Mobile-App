package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewStatus, textViewOrderDate, textViewShippingAddress, textViewOrderTotal;

        public OrderViewHolder(View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewShippingAddress = itemView.findViewById(R.id.textViewShippingAddress);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderTotal);
        }
    }
}
