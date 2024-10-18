package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderFormActivity extends AppCompatActivity {
    private EditText editTextShippingAddress, editTextQuantity;
    private Button buttonPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        editTextShippingAddress = findViewById(R.id.editTextShippingAddress);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);

        // Get product details from intent
        Intent intent = getIntent();
        String productId = intent.getStringExtra("product_id");
        String productName = intent.getStringExtra("product_name");
        double productPrice = intent.getDoubleExtra("product_price", 0);
        String vendorId = intent.getStringExtra("vendor_id"); // Make sure this is being passed

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shippingAddress = editTextShippingAddress.getText().toString().trim();
                String quantityStr = editTextQuantity.getText().toString().trim();

                if (shippingAddress.isEmpty() || quantityStr.isEmpty()) {
                    Toast.makeText(OrderFormActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    double totalPrice = productPrice * quantity;

                    // Debug log to check values
                    Log.d("OrderDebug", "ProductId: " + productId);
                    Log.d("OrderDebug", "VendorId: " + vendorId);
                    Log.d("OrderDebug", "Quantity: " + quantity);
                    Log.d("OrderDebug", "TotalPrice: " + totalPrice);

                    placeOrder(productId, productName, quantity, totalPrice, shippingAddress, vendorId);
                } catch (NumberFormatException e) {
                    Toast.makeText(OrderFormActivity.this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void placeOrder(String productId, String productName, int quantity, double totalPrice,
                            String shippingAddress, String vendorId) {
        String url = "https://10.0.2.2:7022/api/Order";

        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String USER_ID = prf.getString("id", null);

        if (USER_ID == null) {
            Toast.makeText(this, "User session not found", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject orderObject = new JSONObject();
        try {
            JSONObject orderItem = new JSONObject();
            orderItem.put("ProductId", productId);
            orderItem.put("ProductName", productName);
            orderItem.put("Quantity", quantity);
            orderItem.put("Price", totalPrice);
            orderItem.put("VendorId", vendorId); // Make sure VendorId is included

            JSONArray orderItemsArray = new JSONArray();
            orderItemsArray.put(orderItem);

            orderObject.put("UserId", USER_ID);
            orderObject.put("ShippingAddress", shippingAddress);
            orderObject.put("Status", "Pending");
            orderObject.put("OrderTotal", totalPrice);
            orderObject.put("OrderItems", orderItemsArray);

            // Debug log to check final JSON
            Log.d("OrderRequest", "Final JSON: " + orderObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating order data", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                orderObject,
                response -> {
                    Log.d("OrderResponse", "Success: " + response.toString());
                    new AlertDialog.Builder(OrderFormActivity.this)
                            .setTitle("Order Successful")
                            .setMessage("Your order has been placed successfully!")
                            .setPositiveButton("OK", (dialog, which) -> {
                                Intent intent = new Intent(OrderFormActivity.this, HomePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .show();
                },
                error -> {
                    String errorMessage = "Failed to place order";
                    if (error.networkResponse != null) {
                        Log.e("OrderError", "Error Status Code: " + error.networkResponse.statusCode);
                        Log.e("OrderError", "Error Data: " + new String(error.networkResponse.data));
                        errorMessage += " (Status: " + error.networkResponse.statusCode + ")";
                    }
                    Log.e("OrderError", "Error: " + error.toString());
                    Toast.makeText(OrderFormActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
