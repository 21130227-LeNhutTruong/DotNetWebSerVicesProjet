package com.example.app2_use_firebase.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app2_use_firebase.Adapter.CartAdapter;
import com.example.app2_use_firebase.Adapter.PopularAdapter;
import com.example.app2_use_firebase.Adapter.SliderImgAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ViewPager2 viewPager2, viewPager3;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable,sliderRunnable2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        setVariable();
        bottomNavigation();
        calculatorCart();

        db = FirebaseFirestore.getInstance();
        cartList = managmentCart.getListCart();

        binding.checkOutButton.setOnClickListener(v -> showPaymentDialog());

        displayUserCart(this);
        initPopular();
        initSliderImage();
        initSliderImage2();


    }

    private void showPaymentDialog() {
        // Tạo dialog
        Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.dialog_thanhtoan);
        dialog.show();

        spinner = dialog.findViewById(R.id.spinerphguongthuc);
        EditText edithoten = dialog.findViewById(R.id.edithoten);
        EditText editdiachi = dialog.findViewById(R.id.editdiachi);
        EditText editsdt = dialog.findViewById(R.id.editsdt);
        TextView txttongtien = dialog.findViewById(R.id.txttongtien);
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);

        // Đổ dữ liệu vào spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_list_item_1, paymentMethods);
        spinner.setAdapter(arrayAdapter);

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());

        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()* percentTax * 100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee()+tax+delivery)* 100.0)/100.0;
        txttongtien.setText("Tổng Tiền: " + total);

        // Xử lý sự kiện khi người dùng nhấn nút "Xác nhận"
        btnxacnhan.setOnClickListener(v -> {
            // Lấy dữ liệu từ dialog
            hoten = edithoten.getText().toString().trim();
            diachi = editdiachi.getText().toString().trim();
            sdt = editsdt.getText().toString().trim();

            // Kiểm tra dữ liệu
            if (validateInputs(hoten, diachi, sdt)) {
                // Nếu dữ liệu hợp lệ, lưu hóa đơn vào Firestore
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

        // Tạo đối tượng Map để lưu thông tin hóa đơn
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


        // Lấy danh sách sản phẩm trong giỏ hàng
        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemsDomain item : cartList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("picUrl", item.getPicUrl());
            itemMap.put("name", item.getTitle());
            itemMap.put("price", item.getPrice());
            itemMap.put("quantity", item.getNumberinCart());
            items.add(itemMap);
        }
        // Lưu danh sách sản phẩm vào Firestore
        bill.put("items",items);
        // Lưu hóa đơn vào Firestore
        db.collection("users").document(userId).collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> {Toast.makeText(CartActivity.this, "Hóa đơn đã được lưu", Toast.LENGTH_SHORT).show();
                    // Xóa giỏ hàng
                    managmentCart.clearCart();
                    startActivity(new Intent(CartActivity.this,BillActivity.class));
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
    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();
        // Lấy danh sách sản phẩm trong giỏ hàng của người dùng hiện tại

        db.collection("users").document(userId).collection("carts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<ItemsDomain> cartItems = new ArrayList<>();
                        // Lặp qua danh sách sản phẩm và thêm vào danh sách
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
        if (cartItems.isEmpty()) {
            // Nếu giỏ hàng trống, hiển thị thông báo hoặc xử lý tương ứng
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.cartView.setVisibility(View.GONE);
        } else {
            // Nếu giỏ hàng không trống, hiển thị danh sách sản phẩm
            binding.emptyTxt.setVisibility(View.GONE);
            binding.cartView.setVisibility(View.VISIBLE);
        }
        // Cập nhật RecyclerView hoặc ListView
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(cartItems, this, this::calculatorCart));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
    @SuppressLint("SetTextI18n")
    private void calculatorCart() {
        // Tính tổng số lượng và tổng tiền của giỏ hàng
        double percentTax = 0.02;
        // Tính tổng số lượng
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()* percentTax * 100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee()+tax+delivery)* 100.0)/100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee())*100.0)/100.0;

        binding.totalFeeTxt.setText(""+itemTotal);
        binding.taxTxt.setText(""+tax);
        binding.deliveryTxt.setText(""+delivery);
        binding.totalTxt.setText(""+total);
    }
    private void initPopular() {
        DatabaseReference myRef = database.getReference("ItemsPopular");
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.itemsPopular.setLayoutManager(new GridLayoutManager(CartActivity.this, 2));
                        binding.itemsPopular.setAdapter(new PopularAdapter(items));


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initSliderImage(){
        viewPager2 = binding.viewPager2;
// Thêm danh sách các hình ảnh cho slide
        List<Integer> slideItems   = Arrays.asList(
                R.drawable.imgslide_2,
                R.drawable.imgslide_4,
                R.drawable.imgslide_7
        );
        SliderImgAdapter slideAdapter = new SliderImgAdapter(this, slideItems);
        viewPager2.setAdapter(slideAdapter);

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager2.getCurrentItem() < slideItems.size() - 1) {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    viewPager2.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        sliderHandler.postDelayed(sliderRunnable, 5000);
    }
    private void initSliderImage2(){
        viewPager3 = binding.viewPager3;
// Thêm danh sách các hình ảnh cho slide
        List<Integer> slideItems   = Arrays.asList(
                R.drawable.imgslide_4,
                R.drawable.imgslide_5,
                R.drawable.imgslide_6

        );
        SliderImgAdapter  slideAdapter = new SliderImgAdapter(this, slideItems);
        viewPager3.setAdapter(slideAdapter);

        sliderRunnable2 = new Runnable() {
            @Override
            public void run() {
                if (viewPager3.getCurrentItem() < slideItems.size() - 1) {
                    viewPager3.setCurrentItem(viewPager3.getCurrentItem() + 1);
                } else {
                    viewPager3.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };

        viewPager3.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable2);
                sliderHandler.postDelayed(sliderRunnable2, 3000);
            }
        });

        sliderHandler.postDelayed(sliderRunnable2, 3000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.removeCallbacks(sliderRunnable2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        sliderHandler.postDelayed(sliderRunnable2, 3000);
    }

}