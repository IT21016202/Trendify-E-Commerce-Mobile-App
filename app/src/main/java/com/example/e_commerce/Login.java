package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class Login extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 simple letter
                    "(?=.*[A-Z])" +         //at least 1 capital letter
                    ".{8,}" +               //at least 8 characters
                    "$");

    private Button loginbtn;
    private EditText txtLogEmail, txtLogPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.btnLogin);
        txtLogEmail = findViewById(R.id.txtLogEmail);
        txtLogPassword = findViewById(R.id.txtLogPassword);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trust all certificates for localhost development
                SSLHelper.trustAllCertificates();
                login();
            }
        });
    }

    private void login(){
        if(validateEmail() && validatePassword()){
            String userEmail = txtLogEmail.getText().toString();
            String userPassword = txtLogPassword.getText().toString();

            // Create a JSON object for the request body
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("email", userEmail);
                jsonBody.put("password", userPassword);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            // Define the URL and the request
            String url = "https://10.0.2.2:7022/login"; // 10.0.2.2 for emulator to access localhost
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String id = response.optString("id", null);
                        String name = response.optString("customerName", null);
                        String email = response.optString("email", null);
                        //String mobile = response.optString("customerName", null);

                        //Save user session
                        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prf.edit();
                        editor.putString("id", id);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.apply();

                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        gotToHome();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                // Parse error response as string
                                String errorResponse = new String(error.networkResponse.data, "utf-8");
                                try {
                                    // Try to parse the error response as JSON
                                    JSONObject jsonError = new JSONObject(errorResponse);
                                    String message = jsonError.optString("message", "Unknown error");
                                    Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                                } catch (JSONException jsonException) {
                                    // If it's not JSON, show the raw string
                                    Toast.makeText(Login.this, errorResponse, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Login failed: Network error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            );

            // Add the request to the Volley request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void gotToHome(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public boolean validateEmail(){
        String userEmail = txtLogEmail.getText().toString();
        if (userEmail.isEmpty()){
            txtLogEmail.setError("Field Cannot be Empty !");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            txtLogEmail.setError("Please Enter Valid Email Address !");
            return false;
        }
        else {
            return true;
        }
    }

    public boolean validatePassword(){
        String userPassword = txtLogPassword.getText().toString();

        if (userPassword.isEmpty() || userPassword.isEmpty()) {
            txtLogPassword.setError("Fields Cannot be Empty !");
            return false;
        }
//        else if (!PASSWORD_PATTERN.matcher(userPassword).matches()){
//            txtLogPassword.setError("Password Must Contain Minimum 8 characters, At least One Digit and At least One Letter");
//            return false;
//        }
        else
            return true;
    }
}