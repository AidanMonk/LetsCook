package com.example.letscook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class RegisterView extends AppCompatActivity {


    Button btnRegister;
    EditText firstName, lastName, email, password, confirmPassword, username;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_view);
        //get the db
        reference = FirebaseDatabase.getInstance().getReference("users");
        btnRegister = findViewById(R.id.btnRegister);

        username = findViewById(R.id.edtUsernameR);
        firstName = findViewById(R.id.edtFirstName);
        lastName = findViewById(R.id.edtLastName);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtpassword);
        confirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister.setOnClickListener(v -> {

            if (validateInputs()) {
                checkUniqueUserAndRegister();
            }

        });
    }
//In this validation , it make sure that all the fields entered are not empty + password matches
    private boolean validateInputs() {
        // Check if fields are empty

        if (TextUtils.isEmpty(firstName.getText())) {
            firstName.setError("First name is required");
            return false;
        }
        if (TextUtils.isEmpty(lastName.getText())) {
            lastName.setError("Last name is required");
            return false;
        }
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Enter a valid email address");
            return false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password is required");
            return false;
        }
        if (password.getText().length() < 4) {
            password.setError("Password must be at least 4 characters");
            return false;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        if (TextUtils.isEmpty(username.getText())) {
            username.setError("Username is required");
            return false;
        }
        return true;
    }

    private void checkUniqueUserAndRegister() {
        // Check if the email and username is unique in Firebase
        reference.orderByChild("email").equalTo(email.getText().toString().toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            email.setError("Email already registered");
                            Toast.makeText(RegisterView.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            reference.orderByChild("username").equalTo(username.getText().toString().toLowerCase())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                username.setError("Username already registered");
                                                Toast.makeText(RegisterView.this, "Username already registered", Toast.LENGTH_SHORT).show();
                                            } else {
                                                registerUser();
                                            }
                                        }

                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(RegisterView.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(RegisterView.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void registerUser() {
        // Create a new user object with the input data
        User newUser = new User(
                username.getText().toString().toLowerCase(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString().toLowerCase(),
                password.getText().toString()
        );

        // Push the new user to the "users" collection
        reference.push().setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterView.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterView.this, LoginView.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterView.this, "Failed to register user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}