package com.example.letscook;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    private EditText edtEmail, edtUsername, edtPassword;
    private TextView txtFirstName, txtLastName, txtPremiumStatus;
    private Button btnUpdate;
    private DatabaseReference databaseReference; //Firebase data
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize UI elements
        edtEmail = view.findViewById(R.id.edtEmail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtPremiumStatus = view.findViewById(R.id.txtPremiumStatus);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        // Firebase initialization
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Load current user data
        if (firebaseUser != null) {
            loadUserData();
        }

        // Update user data
        btnUpdate.setOnClickListener(v -> {
            if (validateInputs())
            {
                updateUserData();
            }
        });

        return view;
    }

    // Load user data method
    private void loadUserData() {
        String userId = firebaseUser.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    boolean premium = snapshot.child("premium").getValue(Boolean.class);

                    // Populate UI with user data
                    txtFirstName.setText(firstName);
                    txtLastName.setText(lastName);
                    edtUsername.setText(username);
                    edtEmail.setText(email);
                    txtPremiumStatus.setText(premium ? "Premium User" : "Regular User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Verify that the user input is correct
    private boolean validateInputs() {
        if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError("Username is required");
            return false;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError("Password is required");
            return false;
        }
        return true;
    }

    //update the new data to the firebase
    private void updateUserData() {
        String newEmail = edtEmail.getText().toString();
        String newUsername = edtUsername.getText().toString();
        String newPassword = edtPassword.getText().toString();

        // Update email
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Email updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
            }
        });

        // Update password
        if (!TextUtils.isEmpty(newPassword)) {
            firebaseUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Update database
        String userId = firebaseUser.getUid();
        databaseReference.child(userId).child("username").setValue(newUsername).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "User data updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
