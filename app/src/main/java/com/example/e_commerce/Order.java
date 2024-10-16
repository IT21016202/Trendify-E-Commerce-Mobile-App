package com.example.e_commerce;

import java.util.List;

public class Order {

    private String id;
    private String userId;
    private String shippingAddress;
    private String status;
    private String orderDate;
    private int orderTotal;
    private List<OrderItem> orderItems;

    // Getters and Setters for each field
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public int getOrderTotal() { return orderTotal; }
    public void setOrderTotal(int orderTotal) { this.orderTotal = orderTotal; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
