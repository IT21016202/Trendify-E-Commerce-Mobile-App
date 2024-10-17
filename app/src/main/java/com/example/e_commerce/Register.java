package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Patterns;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 simple letter
                    "(?=.*[A-Z])" +         //at least 1 capital letter
                    ".{8,}" +               //at least 8 characters
                    "$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("[0][0-9]{9}");

    private EditText textRegName, textRegEmail, textRegMobile, textRegPassword, textRegRePassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        textRegName = findViewById(R.id.textRegName);
        textRegEmail = findViewById(R.id.textRegEmail);
        textRegMobile = findViewById(R.id.textRegMobile);
        textRegPassword = findViewById(R.id.textRegPassword);
        textRegRePassword = findViewById(R.id.textRegRePassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trust all certificates for localhost development
                SSLHelper.trustAllCertificates();
                register();
            }
        });

    }

    private void register(){
        if(validateName() && validateEmail() && validateMobile() && validatePassword()){
            String name = textRegName.getText().toString();
            String email = textRegEmail.getText().toString();
            String mobile = textRegMobile.getText().toString();
            String password = textRegPassword.getText().toString();
            String rePassword = textRegRePassword.getText().toString();
            String status = "InActive";

            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a JSON object for the request body
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("customerName", name);
                jsonBody.put("email", email);
                //jsonBody.put("mobile", mobile);
                jsonBody.put("status", status);
                jsonBody.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            // Define the URL and the request
            String url = "https://10.0.2.2:7022/register"; // 10.0.2.2 for emulator to access localhost
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Registration failed: ";
                            if (error.networkResponse != null) {
                                errorMessage += "Status code: " + error.networkResponse.statusCode;
                            } else if (error instanceof com.android.volley.TimeoutError) {
                                errorMessage += "Request timed out";
                            } else if (error instanceof com.android.volley.NoConnectionError) {
                                errorMessage += "No network connection";
                            } else {
                                errorMessage += error.getMessage();
                            }
                            Toast.makeText(Register.this, errorMessage, Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }
            );

            // Set a custom retry policy
            int MY_SOCKET_TIMEOUT_MS = 15000; // 15 seconds
            int MY_MAX_RETRIES = 1; // 1 retry
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    MY_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            // Add the request to the Volley request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    public boolean validateEmail(){
        String userEmail = textRegEmail.getText().toString();
        if (userEmail.isEmpty()){
            textRegEmail.setError("Field Cannot be Empty !");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            textRegEmail.setError("Please Enter Valid Email Address !");
            return false;
        }
        else {
            return true;
        }
    }

    public boolean validatePassword(){
        String userPassword = textRegPassword.getText().toString();
        String userRePassword = textRegRePassword.getText().toString();

        if (userPassword.isEmpty() || userRePassword.isEmpty()) {
            textRegPassword.setError("Fields Cannot be Empty !");
            textRegRePassword.setError("Fields Cannot be Empty !");
            return false;
        }
        else if (!userPassword.equals(userRePassword)){
            textRegPassword.setError("Password Mismatch !");
            textRegRePassword.setError("Password Mismatch !");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(userPassword).matches() || !PASSWORD_PATTERN.matcher(userRePassword).matches()){
            textRegPassword.setError("Password Must Contain Minimum 8 characters, At least One Digit and At least One Letter");
            textRegRePassword.setError("Password Must Contain Minimum 8 characters, At least One Digit and At least One Letter");
            return false;
        }
        else
            return true;
    }

    public boolean validateName(){
        String userFullName = textRegName.getText().toString();

        if (userFullName.isEmpty()) {
            textRegName.setError("Field Cannot be Empty !");
            return false;
        }
        else
            return true;
    }

    public boolean validateMobile(){
        String userMobile = textRegMobile.getText().toString();

        if (userMobile.isEmpty()) {
            textRegMobile.setError("Field Cannot be Empty !");
            return false;
        }
        else if (!MOBILE_PATTERN.matcher(userMobile).matches()) {
            textRegMobile.setError("Please Enter a Valid Mobile Number !");
            return false;
        }
        else
            return true;
    }
}