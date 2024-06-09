package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityLoginAdminBinding;
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
        // Lấy đối tượng FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Lấy đối tượng FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        // Đăng ký sự kiện cho nút đăng nhập
        btnLogin.setOnClickListener(v -> loginAdmin());

    }

    private void loginAdmin() {
        // Lấy dữ liệu từ EditText
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        // Kiểm tra dữ liệu có rỗng không

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        // Đăng nhập
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Kiểm tra kết quả đăng nhập
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Kiểm tra quyền admin
                        if (user != null) {
                            // Kiểm tra quyền admin
                            checkIfAdmin(user.getUid());
                            Toast.makeText(LoginAdminActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Kiểm tra quyền admin
    private void checkIfAdmin(String userId) {
        // Lấy thông tin người dùng từ Firestore
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Kiểm tra quyền admin
                        DocumentSnapshot document = task.getResult();
                        // Kiểm tra xem có tìm thấy thông tin người dùng không
                        if (document != null && document.exists()) {
                            // Lấy giá trị của "isAdmin"
                            Boolean isAdmin = document.getBoolean("isAdmin");
                            // Kiểm tra quyền admin
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
