package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.app2_use_firebase.databinding.ActivityHomeAdminBinding;

public class AdminHomeActivity extends BaseActivity{
    ActivityHomeAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);

        // cài đặt sự kiện click cho các button
        binding.cHoaDon.setOnClickListener(v -> startActivity(new Intent(AdminHomeActivity.this, BillAdminActivity.class)));
        binding.cSignOut.setOnClickListener(v -> startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class)));

    }

}
