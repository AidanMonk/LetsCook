package com.example.letscook;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterView extends AppCompatActivity {

    @Override
    Button btnRegister;
    EditText firstName , lastName, email, password, confirmPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_view);

         btnRegister = findViewById(R.id.btnRegister);
         btnRegister.setOnClickListener(v -> {
             firstName = findViewById(R.id.edtFirstName);
             lastName = findViewById(R.id.edtLastName);
             email = findViewById(R.id.edtEmail);
             password = findViewById(R.id.edtpassword);
             confirmPassword = findViewById(R.id.edtConfirmPassword);

           //I need to validate the inputs here.
             // confirm that email is not already registered
             // confirm that passwords match
             // and so on...


             // if it succeeds need an intent to the login screen
         });


    }
}