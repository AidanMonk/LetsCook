package com.example.letscook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UserProfile extends Base_activity {

    private ImageButton btnRecipe, btnSetting, btnSubscribe;
    private TextView usernameTextView;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_user_profile);
        getLayoutInflater().inflate(R.layout.activity_user_profile, findViewById(R.id.frame_layout));
        EdgeToEdge.enable(this);

        // Initialize buttons
        btnRecipe = findViewById(R.id.btnRecipe);
        btnSetting = findViewById(R.id.btnSetting);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        usernameTextView = findViewById(R.id.userName);
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);
        // Set username
        String username = sharedPreferences.getString(LoginView.KEY_USERNAME, "User");
        usernameTextView.setText(username);




        // Set default fragment
        loadFragment(new RecipeFragment());

        // Set button click listeners
        btnRecipe.setOnClickListener(view -> loadFragment(new RecipeFragment()));
        btnSetting.setOnClickListener(view -> loadFragment(new SettingFragment()));
        btnSubscribe.setOnClickListener(view -> loadFragment(new SubscribeFragment()));
    }

    // Method to replace the current fragment
//    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.userProfileFragmentContainer, fragment)
//                .commit();
//    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.userProfileFragmentContainer, fragment)
                .commitNow();
    }

}
