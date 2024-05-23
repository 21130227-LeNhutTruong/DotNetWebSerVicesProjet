package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityNotifiBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotifiActivity extends BaseActivity {

    ActivityNotifiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityNotifiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);
        bottomNavigation();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    public void bottomNavigation() {
        LinearLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifiActivity.this,CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifiActivity.this,MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifiActivity.this,ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }

}