package com.example.letscook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginView extends AppCompatActivity {
    private ProgressBar progressBar;
    Button login;
    EditText usernameL, password;
    DatabaseReference reference;
    TextView signup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFERENCES_NAME = "UserSession";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PREMIUM = "premium";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        login = findViewById(R.id.btnlogin);
        usernameL = findViewById(R.id.edtEmailL);
        password = findViewById(R.id.edtpasswordL);
        signup = findViewById(R.id.txtSignUp);
        reference = FirebaseDatabase.getInstance().getReference("users");


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login.setOnClickListener(v -> {
            if (validateInputs()) {
                authenticateUser();
            }
        });

        //to register a new user
        signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginView.this, RegisterView.class));
        });

    }

    private boolean validateInputs() {
        // Check if email and password fields are empty
        if (TextUtils.isEmpty(usernameL.getText())) {
            usernameL.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password is required");
            return false;
        }
        return true;
    }

    private void authenticateUser() {
        // Check if the email and password match a user in Firebase
        Log.d("LoginView", "Starting authentication");

// Prepare email key by replacing "." with ","
        String emailKey = usernameL.getText().toString().toLowerCase().replace(".", ",");
        Log.d("LoginView", "Email key: " + emailKey);

        reference.orderByChild("email")
                .equalTo(usernameL.getText().toString().toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("LoginView", "onDataChange called");

                        if (dataSnapshot.exists()) {
                            Log.d("LoginView", "User exists in Firebase: " + dataSnapshot.getValue());

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // Retrieve the stored password
                                String storedPassword = userSnapshot.child("password").getValue(String.class);
                                Log.d("LoginView", "Stored password: " + storedPassword);

                                if (storedPassword != null && storedPassword.equals(password.getText().toString())) {
                                    Log.d("LoginView", "Password matches, logging in");

                                    // Retrieve user details
                                    String username = userSnapshot.child("username").getValue(String.class);
                                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                                    String lastName = userSnapshot.child("lastName").getValue(String.class);
                                    String email = userSnapshot.child("email").getValue(String.class);
                                    Boolean isPremium = userSnapshot.child("premium").getValue(Boolean.class);

                                    Log.d("LoginView", "User details - Username: " + username +
                                            ", First Name: " + firstName +
                                            ", Last Name: " + lastName +
                                            ", Email: " + email +
                                            ", Premium: " + isPremium);

                                    // Handle null for isPremium
                                    if (isPremium == null) {
                                        Log.d("LoginView", "premium field is null, setting default value");
                                        isPremium = false;
                                    }

                                    // Store user data in SharedPreferences
                                    editor.putString(KEY_USERNAME, username);
                                    editor.putString(KEY_FIRST_NAME, firstName);
                                    editor.putString(KEY_LAST_NAME, lastName);
                                    editor.putString(KEY_EMAIL, email.replace(".", ",")); // Replace "." for Firebase compatibilit
                                    editor.putBoolean(KEY_PREMIUM, isPremium);
                                    editor.apply();

                                    Log.d("LoginView", "Login successful, moving to main screen");
                                    Toast.makeText(LoginView.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    // Navigate to the main screen
                                    startActivity(new Intent(LoginView.this, CreateRecipe.class)); // Update to your main activity
                                    finish();
                                } else {
                                    Log.e("LoginView", "Password does not match");
                                    Toast.makeText(LoginView.this, "Username or password is invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.e("LoginView", "User not found in Firebase");
                            Toast.makeText(LoginView.this, "Username or password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("LoginView", "Database error: " + databaseError.getMessage());
                        Toast.makeText(LoginView.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //this is a test
    //test

}

