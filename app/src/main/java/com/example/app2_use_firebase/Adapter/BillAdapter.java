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

import java.util.ArrayList;
import java.util.Map;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private ArrayList<Bill> billList;
    private Context context;


    public BillAdapter(ArrayList<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.tvDate.setText("Ngày đặt: " + bill.getDate());
        holder.tvName.setText("Họ tên: " + bill.getHoten());
        holder.tvAddress.setText("Địa chỉ: " + bill.getDiachi());
        holder.tvPhone.setText("Số điện thoại: " + bill.getSdt());
        holder.tvPaymentMethod.setText("Phương thức: " + bill.getPhuongthuc());
        holder.tvTotalAmount.setText("Tổng tiền: $" + bill.getTotalAmount());


    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName, tvAddress, tvPhone, tvPaymentMethod, tvTotalAmount, tvItems;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);




        }
    }
}