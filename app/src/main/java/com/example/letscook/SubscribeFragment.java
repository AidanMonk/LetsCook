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
    DatabaseReference userRef;
    SharedPreferences sharedPreferences;
    String loggedInEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);


        agreeCheckBox = view.findViewById(R.id.agreeCheckBox);
        subscribeButton = view.findViewById(R.id.subscribeButton);
        cancelSubscribeButton = view.findViewById(R.id.cancelSubscribeButton);

        // Initialization Firebase Database
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // get SharedPreferences user's email
        sharedPreferences = getActivity().getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);
        loggedInEmail = sharedPreferences.getString(LoginView.KEY_EMAIL, null);

        // verify user login status
        if (TextUtils.isEmpty(loggedInEmail)) {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Checking the agreement check box to subscribe
        subscribeButton.setOnClickListener(v -> {
            if (agreeCheckBox.isChecked()) {
                updateSubscriptionStatus(true); // set premium as true
            } else {
                Toast.makeText(getContext(), "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
        });


        //Cancel the subscribe
        cancelSubscribeButton.setOnClickListener(v -> checkAndCancelSubscription());

        return view;
    }


    //Upload subscribe status
    private void updateSubscriptionStatus(boolean isSubscribed) {
        userRef.orderByChild("email").equalTo(loggedInEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().child("premium").setValue(isSubscribed)
                                .addOnSuccessListener(aVoid -> {
                                    String message = isSubscribed ? "Subscribed successfully!" : "Subscription cancelled successfully!";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update subscription: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Checking subscribe status and CANCEL it!
    private void checkAndCancelSubscription() {
        userRef.orderByChild("email").equalTo(loggedInEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Boolean isPremium = userSnapshot.child("premium").getValue(Boolean.class);
                        if (isPremium != null && isPremium) {
                            updateSubscriptionStatus(false); //setting premium as false
                        } else {
                            Toast.makeText(getContext(), "You are not subscribed. Cannot cancel.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
