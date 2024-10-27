package com.example.firebasedemo;

import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView userPasswordTV, userAgeTV;
    EditText userNameET, passwordET, ageET, getUserET;
    Button submitBtn, retrieveBtn;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        userNameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.passwordET);
        ageET = findViewById(R.id.ageET);
        submitBtn = findViewById(R.id.submitBtn);

        retrieveBtn = findViewById(R.id.retrieveBtn);
        getUserET = findViewById(R.id.getUserET);
        userPasswordTV = findViewById(R.id.userPasswordTV);
        userAgeTV = findViewById(R.id.userAgeTV);

        //add user
        submitBtn.setOnClickListener(view -> {
            User newUser = new User(userNameET.getText().toString(), passwordET.getText().toString(), Integer.parseInt(ageET.getText().toString()));

            //create database reference
            db = FirebaseDatabase.getInstance();
            reference = db.getReference("Users");

            //add user to database
            reference.child(newUser.getUserName()).setValue(newUser);

            //clear fields
            userNameET.setText("");
            passwordET.setText("");
            ageET.setText("");

            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
        });

        //retrieve user
        retrieveBtn.setOnClickListener(view -> {
            //create database reference
            db = FirebaseDatabase.getInstance();
            reference = db.getReference("Users");

            //get user name from edit text
            String userName = getUserET.getText().toString();

            //retrieve user from database
            reference.child(userName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        User user = dataSnapshot.getValue(User.class);

                    //set fields to user values
                    userPasswordTV.setText(user.getPassword());
                    userAgeTV.setText(String.valueOf(user.getAge()));
                }
            });




        });
    }
}