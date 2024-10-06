package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

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
                        Toast.makeText(Register.this, "Registration failed ", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}