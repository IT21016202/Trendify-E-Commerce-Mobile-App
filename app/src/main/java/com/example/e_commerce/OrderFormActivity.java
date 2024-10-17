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

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shippingAddress = editTextShippingAddress.getText().toString().trim();
                String quantityStr = editTextQuantity.getText().toString().trim();
                int quantity = Integer.parseInt(quantityStr);
                double totalPrice = productPrice * quantity;

                if (shippingAddress.isEmpty() || quantityStr.isEmpty()) {
                    Toast.makeText(OrderFormActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    placeOrder(productId, productName, quantity, totalPrice, shippingAddress);
                }
            }
        });
    }

    private void placeOrder(String productId, String productName, int quantity, double totalPrice, String shippingAddress) {
        String url = "https://10.0.2.2:7022/api/Order";

        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String USER_ID = prf.getString("id", null);

        JSONObject orderObject = new JSONObject();
        try {
            JSONObject orderItem = new JSONObject();
            orderItem.put("ProductId", productId);
            orderItem.put("ProductName", productName);
            orderItem.put("Quantity", quantity);
            orderItem.put("Price", totalPrice);

            JSONArray orderItemsArray = new JSONArray();
            orderItemsArray.put(orderItem);

            orderObject.put("UserId", USER_ID);
            orderObject.put("ShippingAddress", shippingAddress);
            orderObject.put("Status", "Pending");
            orderObject.put("OrderTotal", totalPrice);
            orderObject.put("OrderItems", orderItemsArray);

            // Log the request
            Log.d("OrderRequest", orderObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                orderObject,
                response -> {
                    // Log the response
                    Log.d("OrderResponse", response.toString());

                    // Show success alert
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
                    // Log the error
                    Log.e("OrderError", "Error: " + error.toString());
                    Toast.makeText(OrderFormActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
