package com.example.app2_use_firebase.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app2_use_firebase.Adapter.BillAdapter;
import com.example.app2_use_firebase.Adapter.BillItemsAdapter;
import com.example.app2_use_firebase.Adapter.CartAdapter;
import com.example.app2_use_firebase.Adapter.SliderImgAdapter;
import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.Domain.BillItems;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.TinyDB;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityBillListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BillActivity extends BaseActivity {

    private RecyclerView rvBillList;
    private ArrayList<Bill> billList;
    private BillAdapter billAdapter;
    private FirebaseFirestore db;
    private ArrayList<BillItems> itemsBill;
    ActivityBillListBinding binding;
    private RecyclerView recyclerView;
    private BillItemsAdapter productAdapter;

    private ViewPager2 viewPager2, viewPager3;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable,sliderRunnable2;

    private TinyDB tinyDB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();


        billList = new ArrayList<>();
        rvBillList = binding.rvBillList;;
        billAdapter = new BillAdapter(billList, this,itemsBill);
        rvBillList.setLayoutManager(new LinearLayoutManager(this));
        rvBillList.setAdapter(billAdapter);

//
//        itemsBill = new ArrayList<>();
//        recyclerView = findViewById(R.id.rvBillItems);;
//       productAdapter = new BillItemsAdapter(itemsBill);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//       recyclerView.setAdapter(productAdapter);



//loadBillsItemsFromFirebase();
        loadBillsFromFirebase();
        bottomNavigation();
        listenToBillUpdates();
        initSliderImage();


    }

    private void initSliderImage(){
        viewPager2 = binding.viewPager2;
// Thêm danh sách các hình ảnh cho slide
        List<Integer> slideItems   = Arrays.asList(
                R.drawable.imgslide_2,
                R.drawable.imgslide_4,
                R.drawable.imgslide_5,
                R.drawable.imgslide_6,
                R.drawable.imgslide_9,
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
private void loadBillsFromFirebase() {
        // lấy thông tin người dùng hiện tại
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    // Kiểm tra xem người dùng có đăng nhập hay không
    String userId = currentUser != null ? currentUser.getUid() : null;
    if (userId != null) {
        // Lấy danh sách hóa đơn từ Firestore
        db.collection("users").document(userId).collection("bills")
                // Lọc ra các hóa đơn của người dùng hiện tại
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Xử lý kết quả lấy dữ liệu
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Tạo đối tượng Bill từ document
                            Bill bill = document.toObject(Bill.class);
                            bill.setId(document.getId()); // Thiết lập id từ document ID
                            // Thêm đối tượng Bill vào danh sách
                            billList.add(bill);
                        }
                        // Cập nhật dữ liệu vào adapter
                        billAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(BillActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                });
    } else {
        Toast.makeText(BillActivity.this, "Không thể xác định người dùng hiện tại", Toast.LENGTH_SHORT).show();
    }
}
//    private void loadBillsItemsFromFirebase() {
//        // lấy thông tin người dùng hiện tại
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        // Kiểm tra xem người dùng có đăng nhập hay không
//        String userId = currentUser != null ? currentUser.getUid() : null;
//        if (userId != null) {
//            // Lấy danh sách hóa đơn từ Firestore
//            db.collection("users").document(userId).collection("cartsBill")
//                    // Lọc ra các hóa đơn của người dùng hiện tại
//                    .whereEqualTo("userId", userId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            // Xử lý kết quả lấy dữ liệu
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Tạo đối tượng Bill từ document
//                                BillItems billItems = document.toObject(BillItems.class);
//                                billItems.setId(document.getId()); // Thiết lập id từ document ID
//
//                                // Thêm đối tượng Bill vào danh sách
//                                itemsBill.add(billItems);
//                            }
//                            // Cập nhật dữ liệu vào adapter
//                            productAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(BillActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(BillActivity.this, "Không thể xác định người dùng hiện tại", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void bottomNavigation() {
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this,MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this,ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }


    private void listenToBillUpdates() {
        // lấy thông tin người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;
        if (userId != null) {
            // Lấy danh sách hóa đơn từ Firestore
            db.collection("bills")
                    .whereEqualTo("userId", userId)
                    .addSnapshotListener((snapshots, e) -> {
                        if (e != null) {
                            Toast.makeText(BillActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Xử lý sự kiện khi dữ liệu thay đổi
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {

                            switch (dc.getType()) {
                                // Thêm, sửa, xóa hóa đơn
                                case ADDED:
                                    Bill billAdded = dc.getDocument().toObject(Bill.class);
                                    billList.add(billAdded);
                                    break;
                                case MODIFIED:
                                    // Cập nhật hóa đơn
                                    Bill billModified = dc.getDocument().toObject(Bill.class);
                                    for (int i = 0; i < billList.size(); i++) {
                                        if (billList.get(i).getId().equals(billModified.getId())) {
                                            billList.set(i, billModified);
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    // Xóa hóa đơn
                                    Bill billRemoved = dc.getDocument().toObject(Bill.class);
                                    billList.removeIf(bill -> bill.getId().equals(billRemoved.getId()));
                                    break;
                            }
                        }
                        billAdapter.notifyDataSetChanged();
                    });
        } else {
            Toast.makeText(BillActivity.this, "Không thể xác định người dùng hiện tại", Toast.LENGTH_SHORT).show();
        }
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


//    private void displayUserBill(Context context) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        // Kiểm tra xem người dùng đã đăng nhập hay chưa
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            // Lấy danh sách sản phẩm trong giỏ hàng của người dùng hiện tại
//
//            db.collection("users").document(userId).collection("cartsBill")
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            ArrayList<ItemsDomain> cartItems = new ArrayList<>();
//                            // Lặp qua danh sách sản phẩm và thêm vào danh sách
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Convert document to ItemsDomain object
//                                ItemsDomain item = document.toObject(ItemsDomain.class);
//                                cartItems.add(item);
//                            }
//                            // Hiển thị danh sách sản phẩm trong giỏ hàng
//
//                        } else {
//                            // Lỗi khi truy vấn dữ liệu từ Firestore
//                            Toast.makeText(context, "Error getting user's cart items", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Phương thức để hiển thị thông tin giỏ hàng trên giao diện người dùng
//


}
