package com.example.app2_use_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Domain.Bill;
import com.example.app2_use_firebase.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdminBillAdapter extends RecyclerView.Adapter<AdminBillAdapter.BillViewHolder> {

    private ArrayList<Bill> billList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AdminBillAdapter(ArrayList<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_admin, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        //liên kết dữ liệu
        Bill bill = billList.get(position);
        holder.tvDate.setText("Ngày đặt: " + bill.getDate());
        holder.tvName.setText("Họ tên: " + bill.getHoten());
        holder.tvAddress.setText("Địa chỉ: " + bill.getDiachi());
        holder.tvPhone.setText("Số điện thoại: " + bill.getSdt());
        holder.tvPaymentMethod.setText("Phương thức: " + bill.getPhuongthuc());
        holder.tvTotalAmount.setText("Tổng tiền: $" + bill.getTotalAmount());
        holder.tvStatus.setText("Trạng thái: " + bill.getStatus());
        holder.tvUserName.setText("Tên người dùng: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        //sự kiện click
        holder.itemView.setOnClickListener(v -> {
            //truyền dữ liệu vào activity
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName, tvAddress, tvPhone, tvPaymentMethod, tvTotalAmount, tvStatus, tvUserName;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            //truyền id vào các view
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Bill bill);
    }
}
