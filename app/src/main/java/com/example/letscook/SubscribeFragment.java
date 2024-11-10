package com.example.letscook;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.letscook.R;

public class SubscribeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);


        Button subscribeButton = view.findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Please have your credit card on you side!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
