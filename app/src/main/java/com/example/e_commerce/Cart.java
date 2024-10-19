package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart); // Highlight 'Home' item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();

                if(itemId == R.id.nav_home) {
                    intent = new Intent(Cart.this, HomePage.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_products) {
                    intent = new Intent(Cart.this, Products.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_cart) {
                    intent = new Intent(Cart.this, Cart.class);
                    startActivity(intent);
                    return true;
                }

                else if(itemId == R.id.nav_profile) {
                    intent = new Intent(Cart.this, Profile.class);
                    startActivity(intent);
                    return true;
                }

                else {
                    return false;
                }
            }
        });

    }

}