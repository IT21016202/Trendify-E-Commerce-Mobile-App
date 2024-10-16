package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        //


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
                    try {
                        // Loop through the JSON array
                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject product = response.getJSONObject(i);
                            String name = product.getString("name");
                            result.append(name).append("\n");
                        }

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