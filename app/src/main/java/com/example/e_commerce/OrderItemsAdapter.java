package com.example.e_commerce;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;

    // Constructor
    public OrderItemsAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data
        OrderItem item = orderItems.get(position);
        holder.txtProductName.setText(item.getProductName());
        holder.txtQuantity.setText("Quantity: " + item.getQuantity());
        holder.txtPrice.setText("Price: $" + item.getPrice());
        holder.txtVendor.setText("Vendor : " + getVendorName(item.getVendorId()));

        holder.btnRateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog.Builder to open a custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Inflate the custom layout
                View dialogView = LayoutInflater.from(context).inflate(R.layout.rate_product_dialog, null);
                builder.setView(dialogView);

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                // Access the RatingBar and Submit button from the dialog layout
                RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
                Button btnSubmitRating = dialogView.findViewById(R.id.btnSubmitRating);
                EditText reviewInput = dialogView.findViewById(R.id.etReview);

                // Set onClickListener for Submit button
                btnSubmitRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

                        String userId = sharedPreferences.getString("id", "null");
                        String vendorId = item.getVendorId();
                        String productId = item.getProductId();
                        float rating = ratingBar.getRating();
                        String review = String.valueOf(reviewInput.getText());

                        // Create a JSON object for the request body
                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("vendorId", vendorId);
                            jsonBody.put("customerId", userId);
                            jsonBody.put("productId", productId);
                            jsonBody.put("rating", rating);
                            jsonBody.put("review", review);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Creating JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        System.out.println(jsonBody.toString());

                        // Trust all certificates for localhost development
                        SSLHelper.trustAllCertificates();

                        // URL of the API on your localhost
                        String url = "https://10.0.2.2:7022/api/VendorRatings"; // Use 10.0.2.2 to access localhost in Android emulator
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST, url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(context, "Rating added successfully", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context, "Rating adding failed ", Toast.LENGTH_LONG).show();
                                        //error.printStackTrace();
                                        if (error.networkResponse != null) {
                                            String responseData = new String(error.networkResponse.data);
                                            System.out.println("Error Response: " + responseData);
                                            Toast.makeText(context, "Error: " + responseData, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );

                        // Add the request to the Volley request queue
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(jsonObjectRequest);

                        // Do something with the rating (e.g., submit to API, show a Toast, etc.)
                        Toast.makeText(context, "Rating: " + rating, Toast.LENGTH_SHORT).show();

                        // Close the dialog
                        dialog.dismiss();
                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductName, txtQuantity, txtPrice, txtVendor;
        Button btnRateProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtVendor = itemView.findViewById(R.id.txtVendor);
            btnRateProduct = itemView.findViewById(R.id.btnRateProduct);
        }
    }

    public String getVendorName(String id){
        String name = "N/A";
        // URL of the API on your localhost
        String url = "https://10.0.2.2:7022/api/Vendor/"+id; // Use 10.0.2.2 to access localhost in Android emulator

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String name = response.getString(2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);

        return name;
    }
}
