package com.example.letscook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginView extends AppCompatActivity {

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
        reference.orderByChild("email").equalTo(usernameL.getText().toString().toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String storedPassword = userSnapshot.child("password").getValue(String.class);

                                if (storedPassword != null && storedPassword.equals(password.getText().toString())) {
                                    // Login successful -> main activity
                                    //getting the value from the database
                                    String username = userSnapshot.child("username").getValue(String.class);
                                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                                    String lastName = userSnapshot.child("lastName").getValue(String.class);
                                    String email = userSnapshot.child("email").getValue(String.class);

                                    //store the data in shared preferences
                                    editor.putString(KEY_USERNAME, username);
                                    editor.putString(KEY_FIRST_NAME, firstName);
                                    editor.putString(KEY_LAST_NAME, lastName);
                                    editor.putString(KEY_EMAIL, email);
                                    editor.apply();

                                    Toast.makeText(LoginView.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginView.this, CreateRecipe.class)); // Change HomeActivity to your main screen
                                    finish();
                                } else {
                                    Toast.makeText(LoginView.this, "Username or password is invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginView.this, "Username or password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginView.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //this is a test
    //test

}