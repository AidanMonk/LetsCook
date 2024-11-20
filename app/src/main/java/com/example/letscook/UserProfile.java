package com.example.letscook;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UserProfile extends Base_activity {

    private ImageButton btnRecipe, btnSetting, btnSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize buttons
        btnRecipe = findViewById(R.id.btnRecipe);
        btnSetting = findViewById(R.id.btnSetting);
        btnSubscribe = findViewById(R.id.btnSubscribe);

        // Set default fragment
        loadFragment(new RecipeFragment());

        // Set button click listeners
        btnRecipe.setOnClickListener(view -> loadFragment(new RecipeFragment()));
        btnSetting.setOnClickListener(view -> loadFragment(new SettingFragment()));
        btnSubscribe.setOnClickListener(view -> loadFragment(new SubscribeFragment()));
    }

    // Method to replace the current fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.userProfileFragmentContainer, fragment)
                .commit();
    }
}
