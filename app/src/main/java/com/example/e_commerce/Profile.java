package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    private TextView name, email;
    private Button logout, orders, wishlist, wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.txtProfileTopName);
        email = findViewById(R.id.txtProfileTopEmail);
        logout = findViewById(R.id.btnLogout);
        orders = findViewById(R.id.btnProfileOrders);
        wishlist = findViewById(R.id.btnProfileWishList);
        wallet = findViewById(R.id.btnProfileWallet);

        // Logout button action
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Orders button action
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to orders page
                Intent intent;
                intent = new Intent(Profile.this, Orders.class);
                startActivity(intent);
            }
        });

        // Access SharedPreferences
        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        String u_name = prf.getString("name", null);
        String u_email = prf.getString("email", null);

        name.setText(u_name.toString());
        email.setText(u_email.toString());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile); // Highlight 'Home' item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();

                if(itemId == R.id.nav_home) {
                    intent = new Intent(Profile.this, HomePage.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_products) {
                    intent = new Intent(Profile.this, Products.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_cart) {
                    intent = new Intent(Profile.this, Cart.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_profile) {
                    intent = new Intent(Profile.this, Profile.class);
                    startActivity(intent);
                    return true;
                }

                else {
                    return false;
                }
            }
        });
    }

    private void logout(){
        //Save user session
        SharedPreferences prf = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prf.edit();
        editor.putString("id", null);
        editor.putString("name", null);
        editor.putString("email", null);
        editor.apply();

        Intent intent;
        intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }
}