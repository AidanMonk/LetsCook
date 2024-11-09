package com.example.letscook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LinksView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_view);

        // Find each button by its ID
        Button registerBtn = findViewById(R.id.registerBtn);
        Button loginBtn = findViewById(R.id.loginBtn);
        Button featuredBtn = findViewById(R.id.featuredBtn);
        Button createRecipeBtn = findViewById(R.id.createRecipeBtn);
        Button profileBtn = findViewById(R.id.profileBtn);

        // Set up click listeners for each button
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LinksView.this, RegisterView.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LinksView.this, LoginView.class);
            startActivity(intent);
        });

        featuredBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LinksView.this, FeaturedView.class);
            startActivity(intent);
        });

        createRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LinksView.this, CreateRecipe.class);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LinksView.this, UserProfile.class);
            startActivity(intent);
        });
    }
}