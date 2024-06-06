package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivitySearchBinding;

public class SearchHomeActivity extends BaseActivity{
    ActivitySearchBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);
        ConstraintLayout bthbackHome = findViewById(R.id.back_home);
        bthbackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
