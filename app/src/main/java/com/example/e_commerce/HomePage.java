//package com.example.e_commerce;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.annotation.NonNull;
//
//import android.content.SharedPreferences;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.view.MenuItem;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class HomePage extends AppCompatActivity {
//
//    private TextView textViewResult;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_page);
//
//        textViewResult = findViewById(R.id.textViewResult);
//
//        // Trust all certificates for localhost development
//        SSLHelper.trustAllCertificates();
//
//        // Make the API call
//        fetchJsonArrayData();
//
//        // Get Session Data
//        getSessionData();
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home); // Highlight 'Home' item
//                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        Intent intent;
//                        int itemId = item.getItemId();
//
//                        if(itemId == R.id.nav_home) {
//                    intent = new Intent(HomePage.this, HomePage.class);
//                    startActivity(intent);
//                    return true;
//                }
//
//                else if(itemId == R.id.nav_products) {
//                    intent = new Intent(HomePage.this, Products.class);
//                    startActivity(intent);
//                    return true;
//                }
//
//                else if(itemId == R.id.nav_cart) {
//                    intent = new Intent(HomePage.this, Cart.class);
//                    startActivity(intent);
//                    return true;
//                }
//
//                else if(itemId == R.id.nav_profile) {
//                    intent = new Intent(HomePage.this, Profile.class);
//                    startActivity(intent);
//                    return true;
//                }
//
//                else {
//                    return false;
//                }
//            }
//        });
//    }
//
//    private void fetchJsonArrayData() {
//        // URL of the API on your localhost
//        String url = "https://10.0.2.2:7022/api/Product"; // Use 10.0.2.2 to access localhost in Android emulator
//
//        // Create a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        // Create a JSON array request
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            // Loop through the JSON array
//                            StringBuilder result = new StringBuilder();
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject product = response.getJSONObject(i);
//                                String name = product.getString("name");  // Replace 'name' with the key you need
//                                result.append(name).append("\n");
//                            }
//                            // Display the result in the TextView
//                            textViewResult.setText(result.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(HomePage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors here
//                        System.out.println(error);
//                        Toast.makeText(HomePage.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        // Add the request to the request queue
//        requestQueue.add(jsonArrayRequest);
//    }
//
//
//    private void getSessionData(){
//        // Access SharedPreferences
//        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
//        String id = prf.getString("id", null);
//        String name = prf.getString("name", null);
//        String email = prf.getString("email", null);
//
//        Toast.makeText(this, "Welcome : " + name, Toast.LENGTH_SHORT).show();
//    }
//}




// HomePage.java
package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.recyclerView);

        // Set up the GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Trust all certificates for localhost development
        SSLHelper.trustAllCertificates();

        // Make the API call
        fetchProducts();

        // Get Session Data
        getSessionData();

        setupBottomNavigation();
    }

    private void fetchProducts() {
        String url = "https://10.0.2.2:7022/api/Product/getAllProducts"; // Adjust the URL for your local development environment

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through the product array from the response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productJson = response.getJSONObject(i);

                                // Parse individual product details
                                Product product = new Product(
                                        productJson.getString("id"),
                                        productJson.getString("productName"),
                                        productJson.getString("productDescription"),
                                        productJson.getDouble("unitPrice"),  // unitPrice should be parsed as double
                                        productJson.optString("image", ""),  // Handle image which can be null or empty
                                        productJson.getString("vendorId")
                                );
                                productList.add(product);
                            }
                            // Notify the adapter that the data has changed
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomePage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomePage.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }




    private void getSessionData() {
        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String name = prf.getString("name", null);
        Toast.makeText(this, "Welcome : " + name, Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                intent = new Intent(HomePage.this, HomePage.class); // Update context here
            } else if (itemId == R.id.nav_products) {
                intent = new Intent(HomePage.this, Products.class); // Update context here
            } else if (itemId == R.id.nav_cart) {
                return true; // Already on the cart page
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(HomePage.this, Profile.class); // Update context here
            } else {
                return false;
            }

            startActivity(intent);
            finish(); // Finish current activity to prevent going back to it
            return true;
        });
    }

}
