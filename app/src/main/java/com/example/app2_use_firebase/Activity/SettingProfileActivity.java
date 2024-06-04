package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import com.example.app2_use_firebase.databinding.ActivitySettingProfileBinding;

public class SettingProfileActivity extends BaseActivity{
    ActivitySettingProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySettingProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);

    }
}
