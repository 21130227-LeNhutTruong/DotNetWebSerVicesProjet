package com.example.app2_use_firebase.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app2_use_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;


public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    String email, password;
    Button btnLogin, btnSignUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        initListener();
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_Email);
        edtPassword = findViewById(R.id.edt_Password);
        btnLogin = findViewById(R.id.btn_Login);
        btnSignUp = findViewById(R.id.btn_Sign_Up);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClick();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpOnClick();
            }
        });
    }

    private void loginOnClick() {
        progressDialog.show();
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (TextUtils.isEmpty(email)) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Successful login, proceed to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // Handle potential exceptions
                            try {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Handle invalid email format or non-existent email
                                    String message = "Invalid email or password.";
                                    if (e.getMessage().contains("invalid email format")) {
                                        message = "Invalid email format. Please check and try again.";
                                    } else if (e.getMessage().contains("user-not-found")) {
                                        message = "Email not found. Please check your email address or create an account.";
                                    }
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle other unexpected exceptions
                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                // Catch any other unexpected errors
                                Toast.makeText(LoginActivity.this, "An unexpected error occurred. Please try again later.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void signUpOnClick() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}