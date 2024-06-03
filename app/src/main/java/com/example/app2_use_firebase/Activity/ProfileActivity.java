package com.example.app2_use_firebase.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity {
    ActivityProfileBinding binding;
    TextView tvUserName, tvEmail;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigation();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initUI();
        initProfile();
        setonclickIcon();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }





    }

    private void setonclickIcon(){
        binding.prDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,SettingProfileActivity.class));
            }
        });
    }
    private void initUI() {
        tvUserName = findViewById(R.id.tv_userName);
        tvEmail = findViewById(R.id.tv_email);
    }

    private void initProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Toast.makeText(ProfileActivity.this, "" + name, Toast.LENGTH_SHORT).show();
            tvUserName.setText(name);
            tvEmail.setText(email);
        }
    }


    public void bottomNavigation() {
        LinearLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout notifi = findViewById(R.id.notifi);
        notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout bill = findViewById(R.id.bill);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,BillActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });


    }

    public void signOut(View view) {
       logoutUser();
    }



    private void logoutUser() {
        mAuth.signOut();
        clearLoginState();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void clearLoginState() {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("userId");
        editor.apply();
    }


}
