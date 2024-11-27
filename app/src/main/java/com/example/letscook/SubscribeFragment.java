package com.example.letscook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubscribeFragment extends Fragment {
     CheckBox agreeCheckBox;
     Button subscribeButton, cancelSubscribeButton;
     SharedPreferences sharedPreferences;
     DatabaseReference userRef;
     String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        // Initialize SharedPreferences using the same name as in LoginView
        sharedPreferences = requireActivity().getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Initialize Firebase Database
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Find UI elements
        agreeCheckBox = view.findViewById(R.id.agreeCheckBox);
        subscribeButton = view.findViewById(R.id.subscribeButton);
        cancelSubscribeButton = view.findViewById(R.id.cancelSubscribeButton);

        userEmail = sharedPreferences.getString(LoginView.KEY_EMAIL, null);

        // Check if user is logged in
        if (userEmail == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Set up subscription listeners
        setupSubscriptionListeners();

        // Initial check of subscription status
        checkSubscriptionStatus();

        return view;
    }

    private void setupSubscriptionListeners() {
        // Subscribe button logic
        subscribeButton.setOnClickListener(v -> {
            if (agreeCheckBox.isChecked()) {
                updateSubscriptionStatus(true);
            } else {
                Toast.makeText(getContext(), "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel subscription button logic
        cancelSubscribeButton.setOnClickListener(v -> updateSubscriptionStatus(false));
    }

    private void updateSubscriptionStatus(boolean isSubscribed) {
        if (userEmail == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query to find user by email (considering the "." to "," replacement)
        userRef.orderByChild("email").equalTo(userEmail.replace(",", "."))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                // Update premium status in Firebase
                                userSnapshot.getRef().child("premium").setValue(isSubscribed)
                                        .addOnSuccessListener(aVoid -> {
                                            // Update SharedPreferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(LoginView.KEY_PREMIUM, isSubscribed);
                                            editor.apply();

                                            // Update UI
                                            updateSubscriptionUI(isSubscribed);

                                            // Show success message
                                            String message = isSubscribed
                                                    ? "Subscribed successfully!"
                                                    : "Subscription cancelled successfully!";
                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getContext(),
                                                    "Failed to update subscription: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),
                                "Database error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkSubscriptionStatus() {
        // Check subscription status from SharedPreferences first
        boolean isPremium = sharedPreferences.getBoolean(LoginView.KEY_PREMIUM, false);
        updateSubscriptionUI(isPremium);

        // Verify and sync with Firebase
        if (userEmail != null) {
            userRef.orderByChild("email").equalTo(userEmail.replace(",", "."))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    Boolean firebasePremiumStatus =
                                            userSnapshot.child("premium").getValue(Boolean.class);

                                    // Ensure local SharedPreferences matches Firebase
                                    if (firebasePremiumStatus != null &&
                                            isPremium != firebasePremiumStatus) {

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(LoginView.KEY_PREMIUM, firebasePremiumStatus);
                                        editor.apply();

                                        updateSubscriptionUI(firebasePremiumStatus);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void updateSubscriptionUI(boolean isPremium) {
        // Toggle visibility of subscribe and cancel buttons
        subscribeButton.setVisibility(isPremium ? View.GONE : View.VISIBLE);
        cancelSubscribeButton.setVisibility(isPremium ? View.VISIBLE : View.GONE);

        if (!isPremium) {
            agreeCheckBox.setChecked(false);
        }
    }
}
