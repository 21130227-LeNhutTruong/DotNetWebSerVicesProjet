package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.app2_use_firebase.databinding.ActivityHomeAdminBinding;
import com.example.app2_use_firebase.databinding.ActivityLoginAdminBinding;

public class AdminHomeActivity extends BaseActivity{
    ActivityHomeAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);

        binding.cHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, BillAdminActivity.class));
            }
        });
        binding.cSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
            }
        });

    }

}
