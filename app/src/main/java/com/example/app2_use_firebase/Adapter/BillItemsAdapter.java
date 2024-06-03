package com.example.app2_use_firebase.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app2_use_firebase.Activity.BillActivity;
import com.example.app2_use_firebase.Domain.BillItems;
import com.example.app2_use_firebase.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Tạo BillItemsAdapter để hiển thị các mục trong hóa đơn
public class BillItemsAdapter extends RecyclerView.Adapter<BillItemsAdapter.ViewHolder> {
    private List<BillItems> billItems;


    public void setBillItems(List<BillItems> billItems) {
        this.billItems = billItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillItems billItem = billItems.get(position);
        // Đặt thông tin sản phẩm trong mục này vào ViewHolder
        holder.bind(billItem);
    }

    @Override
    public int getItemCount() {
        return billItems != null ? billItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemImage;
        private TextView tvItemName;
        private TextView tvItemPrice;
        private TextView tvItemQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
        }

        public void bind(BillItems billItem) {
            // Đặt hình ảnh, tên, giá và số lượng sản phẩm vào ViewHolder
            Glide.with(itemView.getContext()).load(billItem.getPicUrl()).into(ivItemImage);
            tvItemName.setText(billItem.getName());
            tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", billItem.getPrice()));
            tvItemQuantity.setText(String.valueOf(billItem.getQuantity()));
        }
    }
}

