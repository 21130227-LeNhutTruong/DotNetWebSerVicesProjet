package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivitySettingProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingProfileActivity extends BaseActivity{
    ActivitySettingProfileBinding binding;

    TextView tvUserName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySettingProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        initProfile();

        super.onCreate(savedInstanceState);
        ImageView btnBackdetail = findViewById(R.id.btnBack);
        btnBackdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initUI() {
        tvUserName = findViewById(R.id.tv_userName);
        tvEmail = findViewById(R.id.tv_email);
    }
    private void initProfile() {
        // Lấy thông tin người dùng hiện tại từ Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Hiển thị thông tin người dùng lên UI
            String name = user.getDisplayName();
            String email = user.getEmail();
            Toast.makeText(SettingProfileActivity.this, "" + name, Toast.LENGTH_SHORT).show();
            tvUserName.setText(name);
            tvEmail.setText(email);
        }
    }
}
