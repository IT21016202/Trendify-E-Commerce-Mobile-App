package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        textViewResult = findViewById(R.id.textViewResult);

        // Trust all certificates for localhost development
        SSLHelper.trustAllCertificates();

        // Make the API call
        fetchJsonArrayData();

        // Get Session Data
        getSessionData();
    }

    private void fetchJsonArrayData() {
        // URL of the API on your localhost
        String url = "https://10.0.2.2:7022/api/Product"; // Use 10.0.2.2 to access localhost in Android emulator

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
                                String name = product.getString("name");  // Replace 'name' with the key you need
                                result.append(name).append("\n");
                            }
                            // Display the result in the TextView
                            textViewResult.setText(result.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomePage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        System.out.println(error);
                        Toast.makeText(HomePage.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }


    private void getSessionData(){
        // Access SharedPreferences
        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String id = prf.getString("id", null);
        String name = prf.getString("name", null);
        String email = prf.getString("email", null);

        Toast.makeText(this, "Welcome : " + name, Toast.LENGTH_SHORT).show();
    }
}
