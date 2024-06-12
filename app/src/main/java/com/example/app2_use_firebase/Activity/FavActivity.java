package com.example.app2_use_firebase.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Adapter.FavAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityFavBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FavActivity extends BaseActivity{
    ActivityFavBinding binding;
    private RecyclerView recyclerView;
    private FavAdapter adapter;
    private ArrayList<ItemsDomain> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityFavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayUserFav(this);

        binding.backBtnfav.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.favView);
        itemList = new ArrayList<>();


    }
    private void displayUserFav(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Lấy danh sách sản phẩm trong giỏ hàng của người dùng hiện tại

            db.collection("users").document(userId).collection("favs")
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
                            showFavItems(context,cartItems);
                        } else {
                            // Lỗi khi truy vấn dữ liệu từ Firestore
                        }
                    });
        } else {
        }
    }

    // Phương thức để hiển thị thông tin giỏ hàng trên giao diện người dùng
    private void showFavItems(Context context,ArrayList<ItemsDomain> cartItems) {
        // Sử dụng danh sách sản phẩm giỏ hàng để cập nhật RecyclerView hoặc ListView
        if (cartItems.isEmpty()) {
            // Nếu giỏ hàng trống, hiển thị thông báo hoặc xử lý tương ứng
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.favView.setVisibility(View.GONE);
        } else {
            // Nếu giỏ hàng không trống, hiển thị danh sách sản phẩm
            binding.emptyTxt.setVisibility(View.GONE);
            binding.favView.setVisibility(View.VISIBLE);
        }
        // Cập nhật RecyclerView hoặc ListView
        binding.favView.setLayoutManager(new LinearLayoutManager(FavActivity.this, RecyclerView.VERTICAL,false));
        binding.favView.setAdapter(new FavAdapter(this,cartItems));
    }
}
