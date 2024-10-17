package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        fetchOrders();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile); // Highlight 'Home' item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();

                if(itemId == R.id.nav_home) {
                    intent = new Intent(Orders.this, HomePage.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_products) {
                    intent = new Intent(Orders.this, Products.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_cart) {
                    intent = new Intent(Orders.this, Cart.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_profile) {
                    intent = new Intent(Orders.this, Profile.class);
                    startActivity(intent);
                    return true;
                }

                else {
                    return false;
                }
            }
        });
    }

    private void fetchOrders(){

        // Access SharedPreferences
        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String id = prf.getString("id", null);

        // URL of the API on your localhost
        String url = "https://10.0.2.2:7022/api/Order/user/"+id; // Use 10.0.2.2 to access localhost in Android emulator

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("API Response", response.toString()); // Log the raw response

                    try {
                        List<Order> orderList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject orderObject = response.getJSONObject(i);

                            String id = orderObject.getString("id");
                            String userId = orderObject.getString("userId");
                            String shippingAddress = orderObject.getString("shippingAddress");
                            String status = orderObject.getString("status");
                            String orderDate = orderObject.getString("orderDate");
                            int orderTotal = orderObject.getInt("orderTotal");

                            JSONArray itemsArray = orderObject.getJSONArray("orderItems");
                            List<OrderItem> orderItems = new ArrayList<>();

                            for (int j = 0; j < itemsArray.length(); j++) {
                                JSONObject itemObject = itemsArray.getJSONObject(j);

                                String productId = itemObject.getString("productId");
                                String productName = itemObject.getString("productName");
                                int quantity = itemObject.getInt("quantity");
                                int price = itemObject.getInt("price");
                                String vendorId = itemObject.isNull("vendorId") ? null : itemObject.getString("vendorId");

                                orderItems.add(new OrderItem(productId, productName, quantity, price, vendorId));
                            }

                            orderList.add(new Order(id, userId, shippingAddress, status, orderDate, orderTotal, orderItems));
                        }

                        // Set up RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        OrderAdapter orderAdapter = new OrderAdapter(Orders.this, orderList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Orders.this));
                        recyclerView.setAdapter(orderAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Orders.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle errors here
                    System.out.println(error);
                    Toast.makeText(Orders.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }
}