package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityLoginAdminBinding;
import com.example.app2_use_firebase.databinding.ActivityNotifiBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAdminActivity extends BaseActivity{
    ActivityLoginAdminBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);
        btnLogin = findViewById(R.id.btndangnhap);
        etEmail = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.editmatkhau);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnLogin.setOnClickListener(v -> loginAdmin());

    }

    private void loginAdmin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkIfAdmin(user.getUid());
                        }
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfAdmin(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Boolean isAdmin = document.getBoolean("isAdmin");
                            if (isAdmin != null && isAdmin) {
                                startActivity(new Intent(LoginAdminActivity.this, AdminHomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginAdminActivity.this, "Tài khoản không có quyền admin", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginAdminActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Lỗi khi kiểm tra quyền admin", Toast.LENGTH_SHORT).show();
                        Log.e("LoginAdmin", "Error getting documents: ", task.getException());
                    }
                });
    }
}
