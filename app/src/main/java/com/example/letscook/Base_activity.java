package com.example.letscook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Base_activity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFERENCES_NAME = "UserSession";
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
            }else if (itemId == R.id.Logout) {
                showLogoutConfirmationDialog();
                return true;
            }

            return false;
        });
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Clear the user session data
            editor.clear();
            editor.apply();
            // Redirect to LoginView
            startActivity(new Intent(Base_activity.this, LoginView.class));
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing, just dismiss the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


