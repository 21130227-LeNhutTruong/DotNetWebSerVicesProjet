package com.example.app2_use_firebase.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Adapter.AdminBillAdapter;
import com.example.app2_use_firebase.Adapter.BillAdapter;
import com.example.app2_use_firebase.Adapter.BillItemsAdapter;
import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.Domain.BillItems;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityAdminBillListBinding;
import com.example.app2_use_firebase.databinding.ActivityBillListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BillActivity extends BaseActivity {

    private RecyclerView rvBillList;
    private ArrayList<Bill> billList;
    private ArrayList<BillItems> billItemsList;
    private BillAdapter billAdapter;
    private BillItemsAdapter billIAdapter;
    private RecyclerView rvBillItems;
    private FirebaseFirestore db;
    ActivityBillListBinding binding;

    private ArrayList<ItemsDomain> itemsList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rvBillList = findViewById(R.id.rvBillList);
        db = FirebaseFirestore.getInstance();
        billList = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView
        billAdapter = new BillAdapter(billList, this);
        rvBillList.setLayoutManager(new LinearLayoutManager(this));
        rvBillList.setAdapter(billAdapter);






        loadBillsFromFirebase();
        setVariable();
        bottomNavigation();
        listenToBillUpdates();

    }
private void loadBillsFromFirebase() {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser != null ? currentUser.getUid() : null;
    if (userId != null) {
        db.collection("users").document(userId).collection("bills")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Bill bill = document.toObject(Bill.class);
                            bill.setId(document.getId()); // Thiết lập id từ document ID
                            billList.add(bill);
                        }
                        billAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(BillActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                });
    } else {
        Toast.makeText(BillActivity.this, "Không thể xác định người dùng hiện tại", Toast.LENGTH_SHORT).show();
    }
}

    public void bottomNavigation() {
        LinearLayout home = findViewById(R.id.home_nav);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this,MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout notifi = findViewById(R.id.notifi);
        notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this,NotifiActivity.class));
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
    private void setVariable() {
        ImageView backBtn = findViewById(R.id.backBtnBill);
        backBtn.setOnClickListener(v -> startActivity(new Intent(BillActivity.this,CartActivity.class)));
    }

    private void listenToBillUpdates() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;
        if (userId != null) {
            db.collection("bills")
                    .whereEqualTo("userId", userId)
                    .addSnapshotListener((snapshots, e) -> {
                        if (e != null) {
                            Toast.makeText(BillActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Bill billAdded = dc.getDocument().toObject(Bill.class);
                                    billList.add(billAdded);
                                    break;
                                case MODIFIED:
                                    Bill billModified = dc.getDocument().toObject(Bill.class);
                                    for (int i = 0; i < billList.size(); i++) {
                                        if (billList.get(i).getId().equals(billModified.getId())) {
                                            billList.set(i, billModified);
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
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

}
