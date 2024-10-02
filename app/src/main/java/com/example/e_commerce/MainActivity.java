package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button loginbtn;
    private Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button by its ID
        loginbtn = findViewById(R.id.btnToLogin);
        registerbutton = findViewById(R.id.btnToRegister);

        // Set an OnClickListener to handle button click
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to LoginPage
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);  // Start HomePage activity
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to RegisterPage
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);  // Start HomePage activity
            }
        });
    }
}