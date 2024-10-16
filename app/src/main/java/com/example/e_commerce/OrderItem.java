package com.example.e_commerce;

public class OrderItem {

    private String productId;
    private String productName;
    private int quantity;
    private int price;
    private String vendorId;

    public OrderItem(String productId, String productName, int quantity, int price, String vendorId){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
    }
    // Getters and Setters for each field
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
}
