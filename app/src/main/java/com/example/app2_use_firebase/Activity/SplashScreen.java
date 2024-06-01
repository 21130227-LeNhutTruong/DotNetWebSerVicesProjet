package com.example.app2_use_firebase.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app2_use_firebase.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isUserLoggedIn()){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SplashScreen.this, "Đã Đăng Nhập", Toast.LENGTH_SHORT).show();
                }else {
                Intent intent = new Intent(SplashScreen.this, SplashScreen2.class);
                startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("userId", null);
        return userId != null;
    }
}