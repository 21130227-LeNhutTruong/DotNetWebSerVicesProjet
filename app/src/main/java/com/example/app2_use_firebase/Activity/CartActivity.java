package com.example.app2_use_firebase.Activity;

import android.app.Dialog;
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
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityCartBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        initCartList();
        bottomNavigation();

        db = FirebaseFirestore.getInstance();
        cartList = managmentCart.getListCart();

        binding.checkOutButton.setOnClickListener(v -> showPaymentDialog());

        setupCartList();


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
        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100.0) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        txttongtien.setText("Tổng Tiền: $" + total);

        btnxacnhan.setOnClickListener(v -> {
            hoten = edithoten.getText().toString().trim();
            diachi = editdiachi.getText().toString().trim();
            sdt = editsdt.getText().toString().trim();

            if (validateInputs(hoten, diachi, sdt)) {
                String phuongthuc = spinner.getSelectedItem().toString();
                double totalAmount = managmentCart.getTotalFee() + tax + delivery;
                saveBillToFirebase(hoten, diachi, sdt, phuongthuc, totalAmount);
                dialog.cancel();
            }
        });
    }
    private void saveBillToFirebase(String hoten, String diachi, String sdt, String paymentMethod, double totalAmount) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());

        Map<String, Object> bill = new HashMap<>();
        bill.put("date", date);
        bill.put("hoten", hoten);
        bill.put("diachi", diachi);
        bill.put("sdt", sdt);
        bill.put("paymentMethod", paymentMethod);
        bill.put("totalAmount", totalAmount);


        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemsDomain item : cartList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.getTitle());
            itemMap.put("price", item.getPrice());
            itemMap.put("quantity", item.getNumberinCart());
            items.add(itemMap);
        }
        db.collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> {Toast.makeText(CartActivity.this, "Hóa đơn đã được lưu", Toast.LENGTH_SHORT).show();

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
        if (sdt.isEmpty() || sdt.length() < 10) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void setupCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, this::calculatorCart));
    }


    public void bottomNavigation() {
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout notifi = findViewById(R.id.notifi);
        notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,NotifiActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout bill = findViewById(R.id.bill);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, BillActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }


    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));


    }
    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(this,MainActivity.class)));
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