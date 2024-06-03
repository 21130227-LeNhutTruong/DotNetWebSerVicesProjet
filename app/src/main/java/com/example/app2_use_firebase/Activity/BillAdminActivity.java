package com.example.app2_use_firebase.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Adapter.AdminBillAdapter;
import com.example.app2_use_firebase.Adapter.BillAdapter;
import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityAdminBillListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BillAdminActivity extends BaseActivity {
    private RecyclerView rvBillList;
    private ArrayList<Bill> billList;
    private AdminBillAdapter billAdapter;
    private FirebaseFirestore db;
    ActivityAdminBillListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBillListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        rvBillList = findViewById(R.id.rvBillList);
        db = FirebaseFirestore.getInstance();
        billList = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView
        billAdapter = new AdminBillAdapter(billList, this);
        rvBillList.setLayoutManager(new LinearLayoutManager(this));
        rvBillList.setAdapter(billAdapter);

        // Set the OnItemClickListener before loading the bills
        billAdapter.setOnItemClickListener(new AdminBillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bill bill) {
                showStatusUpdateDialog(bill);
            }
        });

            loadBillsFromFirebase();


    }
    private void loadBillsFromFirebase() {
        db.collectionGroup("bills")
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
                        Toast.makeText(BillAdminActivity.this, "Lỗi khi tải hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showStatusUpdateDialog(Bill bill) {
        // Dialog for admin to update the bill status
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_status);
        dialog.show();

        Spinner spinner = dialog.findViewById(R.id.spinnerStatus);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Đã xác nhận", "Đang giao hàng", "Hoàn thành"});
        spinner.setAdapter(arrayAdapter);

        btnUpdate.setOnClickListener(v -> {
            String selectedStatus = spinner.getSelectedItem().toString();
            updateBillStatus(bill, selectedStatus);
            dialog.dismiss();
        });

}
    private void updateBillStatus(Bill bill, String status) {
        DocumentReference billRef = db.collection("users").document(bill.getUserId()).collection("bills").document(bill.getId());
        billRef.update("status", status)
                .addOnSuccessListener(aVoid -> Toast.makeText(BillAdminActivity.this, "Trạng thái đã được cập nhật", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(BillAdminActivity.this, "Lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show());

    }

}
