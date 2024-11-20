package com.example.letscook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Base_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_nav) {
                if (!(this instanceof FeaturedView)) {
                    startActivity(new Intent(this, FeaturedView.class));
                }
                return true;

            } else if (itemId == R.id.createRecipe) {
                if (!(this instanceof CreateRecipe)) {
                    startActivity(new Intent(this, CreateRecipe.class));
                }
                return true;
            } else if (itemId == R.id.account) {
                if (!(this instanceof UserProfile)) {
                    startActivity(new Intent(this, UserProfile.class));
                }
                return true;
            }
            return false;
        });
    }
}

