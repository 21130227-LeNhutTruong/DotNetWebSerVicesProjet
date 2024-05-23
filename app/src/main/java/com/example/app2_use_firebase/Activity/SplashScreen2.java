package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app2_use_firebase.R;

public class SplashScreen2 extends AppCompatActivity {
    Button btnLogin, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_2);

        initUI();
        initListener();
    }

    public void initUI(){
        btnLogin = findViewById(R.id.btn_Login);
        btnSignUp = findViewById(R.id.btn_Sign_Up);
    }

    public void initListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen2.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen2.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}