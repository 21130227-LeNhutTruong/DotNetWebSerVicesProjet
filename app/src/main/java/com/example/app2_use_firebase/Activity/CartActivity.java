package com.example.app2_use_firebase.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.app2_use_firebase.Adapter.CartAdapter;
import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends  BaseActivity {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private  Spinner spinner;
    private List<ItemsDomain> cartList;
    private String[] paymentMethods = {"Thanh toán khi nhận hàng", "Thanh toán MOMO"};
    private String hoten, diachi, sdt;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        calculatorCart();
        setVariable();
        bottomNavigation();

        db = FirebaseFirestore.getInstance();
        cartList = managmentCart.getListCart();

        binding.checkOutButton.setOnClickListener(v -> showPaymentDialog());

        displayUserCart(this);


    }

    private void showPaymentDialog() {
        Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.dialog_thanhtoan);
        dialog.show();

        spinner = dialog.findViewById(R.id.spinerphguongthuc);
        EditText edithoten = dialog.findViewById(R.id.edithoten);
        EditText editdiachi = dialog.findViewById(R.id.editdiachi);
        EditText editsdt = dialog.findViewById(R.id.editsdt);
        TextView txttongtien = dialog.findViewById(R.id.txttongtien);
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_list_item_1, paymentMethods);
        spinner.setAdapter(arrayAdapter);

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());

        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()* percentTax * 100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee()+tax+delivery)* 100.0)/100.0;
        txttongtien.setText("Tổng Tiền: $" + total);

        btnxacnhan.setOnClickListener(v -> {
            hoten = edithoten.getText().toString().trim();
            diachi = editdiachi.getText().toString().trim();
            sdt = editsdt.getText().toString().trim();

            if (validateInputs(hoten, diachi, sdt)) {
                String paymentMethod = spinner.getSelectedItem().toString();
                double totalAmount = total;
                saveBillToFirebase(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), FirebaseAuth.getInstance().getCurrentUser().getUid(),hoten, diachi, sdt, paymentMethod, totalAmount);
                dialog.cancel();
            }
        });
    }
    private void saveBillToFirebase(String userName,String userId,String hoten, String diachi, String sdt, String paymentMethod, double totalAmount) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());

        Map<String, Object> bill = new HashMap<>();
        bill.put("userId", userId);
        bill.put("userName", userName); // Lưu tên người dùng
        bill.put("date", date);
        bill.put("hoten", hoten);
        bill.put("diachi", diachi);
        bill.put("sdt", sdt);
        bill.put("phuongthuc", paymentMethod);
        bill.put("totalAmount", totalAmount);
        bill.put("status", "Đang xử lý"); // Trạng thái mặc định ban đầu


        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemsDomain item : cartList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("picUrl", item.getPicUrl());
            itemMap.put("name", item.getTitle());
            itemMap.put("price", item.getPrice());
            itemMap.put("quantity", item.getNumberinCart());
            items.add(itemMap);
        }
        bill.put("items",items);
        db.collection("users").document(userId).collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> {Toast.makeText(CartActivity.this, "Hóa đơn đã được lưu", Toast.LENGTH_SHORT).show();
managmentCart.deleteItemFromCart();
displayUserCart(this);
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Lỗi khi lưu hóa đơn", Toast.LENGTH_SHORT).show());
    }
    private boolean validateInputs(String hoten, String diachi, String sdt) {
        if (hoten.isEmpty()) {
            Toast.makeText(this, "Họ tên không để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (diachi.isEmpty()) {
            Toast.makeText(this, "Địa chỉ không để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidPhoneNumber(sdt)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberPattern = "^(\\+84|0)\\d{9}$";
        return phoneNumber.matches(phoneNumberPattern);
    }

    public void bottomNavigation() {
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout notifi = findViewById(R.id.notifi);
        notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,NotifiActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout bill = findViewById(R.id.bill);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, BillActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }

private void displayUserCart(Context context) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("carts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<ItemsDomain> cartItems = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert document to ItemsDomain object
                            ItemsDomain item = document.toObject(ItemsDomain.class);
                            cartItems.add(item);
                        }
                        // Hiển thị danh sách sản phẩm trong giỏ hàng
                        showCartItems(context,cartItems);
                    } else {
                        // Lỗi khi truy vấn dữ liệu từ Firestore
                        Toast.makeText(context, "Error getting user's cart items", Toast.LENGTH_SHORT).show();
                    }
                });
    } else {
        Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show();
    }
}

    // Phương thức để hiển thị thông tin giỏ hàng trên giao diện người dùng
    private void showCartItems(Context context,ArrayList<ItemsDomain> cartItems) {
        // Sử dụng danh sách sản phẩm giỏ hàng để cập nhật RecyclerView hoặc ListView
        // Ví dụ:
        if (cartItems.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(cartItems, this, this::calculatorCart));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this,MainActivity.class)));
    }
    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()* percentTax * 100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee()+tax+delivery)* 100.0)/100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee())*100.0)/100.0;

        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }

}