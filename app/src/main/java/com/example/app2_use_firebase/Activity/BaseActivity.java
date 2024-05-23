package com.example.app2_use_firebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.app2_use_firebase.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseActivity extends AppCompatActivity {
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case 0:
//                        startActivity(new Intent(BaseActivity.this, MainActivity.class));
//                        overridePendingTransition(0, 0);
//                        finish();
//                        return true;
//                    case 1:
//                        startActivity(new Intent(BaseActivity.this, CartActivity.class));
//                        overridePendingTransition(0, 0);
//                        finish();
//                        return true;
//                    case 2:
//                        startActivity(new Intent(BaseActivity.this, NotifiActivity.class));
//                        overridePendingTransition(0, 0);
//                        finish();
//                        return true;
//                    case 3:
//                        startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
//                        overridePendingTransition(0, 0);
//                        finish();
//                        return true;
//
//                }
//                return false;
//            }
//        });
//
//        // Đặt mục đã chọn dựa trên Activity hiện tại
//        int navItemId = getNavigationItemId();
//        if (navItemId != -1) {
//            bottomNavigationView.setSelectedItemId(navItemId);
//        }
//    }
//    // Phương thức trừu tượng để lấy layout resource của Activity con
//    protected abstract ConstraintLayout getLayoutResourceId();
//
//    // Phương thức trừu tượng để xác định mục đã chọn của BottomNavigationView
//    protected abstract int getNavigationItemId();
    }
}