package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderDetails extends AppCompatActivity {

    TextView trackingNumber, date, address, total, status;

    private RecyclerView recyclerOrderItems;
    private OrderItemsAdapter orderItemsAdapter;
    private List<OrderItem> orderItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        trackingNumber = findViewById(R.id.txtTrackingNumber);

        date = findViewById(R.id.txtOrderDate);
        address = findViewById(R.id.txtOrderAddress);
        total = findViewById(R.id.txtOrderTotal);
        status = findViewById(R.id.txtOrderStatus);

        // Initialize RecyclerView
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list
        orderItemList = new ArrayList<>();

        // Get the Order ID passed from the previous activity
        String orderId = getIntent().getStringExtra("ORDER_ID");

        // Trust all certificates for localhost development
        SSLHelper.trustAllCertificates();

        getOrderDetails(orderId);
    }

    private void getOrderDetails(String id){
        // URL of the API on your localhost
        String url = "https://10.0.2.2:7022/api/Order/"+id; // Use 10.0.2.2 to access localhost in Android emulator

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayOrderDetails(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetails.this, "Failed to retrieve order details", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    private void displayOrderDetails(JSONObject response) {
        try{
            String id_o = response.getString("id");
            String date_o = response.getString("orderDate");
            String address_o = response.getString("shippingAddress");
            String total_o = response.getString("orderTotal");
            String status_o = response.getString("status");

            trackingNumber.setText(id_o.toString());
            date.setText(formatDate(date_o.toString()));
            address.setText(address_o.toString());
            status.setText(status_o.toString());
            total.setText(total_o.toString());

            // Extract and loop through the order items
            JSONArray orderItemsArray = response.getJSONArray("orderItems");
            for (int i = 0; i < orderItemsArray.length(); i++) {
                JSONObject item = orderItemsArray.getJSONObject(i);
                String productId = item.getString("productId");
                String productName = item.getString("productName");
                int quantity = item.getInt("quantity");
                int price = item.getInt("price");
                String vendorId = item.getString("vendorId");

                // Add the item to the list
                orderItemList.add(new OrderItem(productId, productName, quantity, price, vendorId));
            }

            // Set up the adapter and attach it to the RecyclerView
            orderItemsAdapter = new OrderItemsAdapter(OrderDetails.this, orderItemList);
            recyclerOrderItems.setAdapter(orderItemsAdapter);

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(OrderDetails.this, "Error parsing order details", Toast.LENGTH_SHORT).show();
        }
    }

    // Format Date
    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy, hh:mm a", Locale.ENGLISH);

        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}