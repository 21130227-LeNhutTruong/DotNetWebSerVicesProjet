package com.example.app2_use_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Adapter.BillAdapter;
import com.example.app2_use_firebase.Adapter.BillItemsAdapter;
import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.Domain.BillItems;
import com.example.app2_use_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {

    private RecyclerView rvBillList;
    private ArrayList<Bill> billList;
    private BillAdapter billAdapter;
    private FirebaseFirestore db;
    private RecyclerView rvBillItems;
    private BillItemsAdapter billItemsAdapter;
    private ArrayList<BillItems> billItems;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        rvBillList = findViewById(R.id.rvBillList);
        db = FirebaseFirestore.getInstance();
        billList = new ArrayList<>();

        billAdapter = new BillAdapter(billList, this);
        rvBillList.setLayoutManager(new LinearLayoutManager(this));
        rvBillList.setAdapter(billAdapter);

        loadBillsFromFirebase();
        setVariable();
        bottomNavigation();




    }

private void loadBillsFromFirebase() {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser != null ? currentUser.getUid() : null;
    if (userId != null) {
        db.collection("bills")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Bill bill = document.toObject(Bill.class);
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

}
