package com.example.letscook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    EditText edtUsername, edtPassword;
    TextView txtFirstName, txtLastName, txtPremiumStatus, txtEmail;
    Button btnUpdate;
    DatabaseReference userRef;

    private SharedPreferences sharedPreferences;
    private String loggedInEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        txtEmail = view.findViewById(R.id.txtEmail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtPremiumStatus = view.findViewById(R.id.txtPremiumStatus);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        // Firebase Initialization
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // SharedPreferences is a tool for storing small local data.
        sharedPreferences = getActivity().getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);
        loggedInEmail = sharedPreferences.getString(LoginView.KEY_EMAIL, null);

        // checking user login status
        if (!TextUtils.isEmpty(loggedInEmail)) {
            loadUserData(loggedInEmail);
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        // update user data
        btnUpdate.setOnClickListener(v -> {
            if (validateInputs()) {
                updateUserData(loggedInEmail);
            }
        });

        return view;
    }

//get user data from fire base
    private void loadUserData(String email) {
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Extracting user data
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String lastName = userSnapshot.child("lastName").getValue(String.class);
                        String username = userSnapshot.child("username").getValue(String.class);
                        Boolean isPremium = userSnapshot.child("premium").getValue(Boolean.class);

                        // renew UI
                        txtFirstName.setText(firstName != null ? firstName : "N/A");
                        txtLastName.setText(lastName != null ? lastName : "N/A");
                        txtEmail.setText(email);
                        txtPremiumStatus.setText(isPremium != null && isPremium ? "Premium User" : "Regular User");
                        edtUsername.setText(username != null ? username : "");
                    }
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//verify input
    private boolean validateInputs() {
        if (!TextUtils.isEmpty(edtPassword.getText()) && edtPassword.getText().length() < 4) {
            edtPassword.setError("Password must be at least 4 characters");
            return false;
        }
        return true;
    }


    //upload new data to Firebase
    private void updateUserData(String email) {
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String newUsername = edtUsername.getText().toString();
                        String newPassword = edtPassword.getText().toString();

                        // change username
                        if (!TextUtils.isEmpty(newUsername)) {
                            userSnapshot.getRef().child("username").setValue(newUsername);
                        }

                        // change code
                        if (!TextUtils.isEmpty(newPassword)) {
                            userSnapshot.getRef().child("password").setValue(newPassword);
                        }

                        Toast.makeText(getContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to find user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to update user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
