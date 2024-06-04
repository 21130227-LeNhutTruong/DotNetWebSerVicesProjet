package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivitySettingProfileBinding;

public class SettingProfileActivity extends BaseActivity{
    ActivitySettingProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySettingProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);
        ImageView btnBackdetail = findViewById(R.id.btnBack);
        btnBackdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
