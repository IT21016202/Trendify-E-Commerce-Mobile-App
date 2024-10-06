package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}