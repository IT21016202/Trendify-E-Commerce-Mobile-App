// Product.java
package com.example.e_commerce;

public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    private String vendor_id;

    public Product(String id, String name, String description, double price, String imageUrl, String vendor_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.vendor_id = vendor_id;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    public String getVendorId() {
        return vendor_id;
    }

    public void setQuantity(int quantity) {
    }
}