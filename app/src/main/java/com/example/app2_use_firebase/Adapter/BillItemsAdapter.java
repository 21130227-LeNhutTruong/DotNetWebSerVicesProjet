package com.example.app2_use_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app2_use_firebase.Domain.BillItems;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;

import java.util.ArrayList;
import java.util.List;

// Tạo BillItemsAdapter để hiển thị các mục trong hóa đơn
public class BillItemsAdapter extends RecyclerView.Adapter<BillItemsAdapter.ViewHolder> {
    private ArrayList<BillItems> itemsList;


    public BillItemsAdapter(ArrayList<BillItems> itemsList) {
        this.itemsList =itemsList;


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_my_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillItems item = itemsList.get(position);
        holder.bind(item);
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lấy các View trong mục và gán vào ViewHolder
            productImage = itemView.findViewById(R.id.picOrder);
            productName = itemView.findViewById(R.id.titleTxtOrder);
            productPrice = itemView.findViewById(R.id.totalEachItemOrder);
            productQuantity = itemView.findViewById(R.id.feeEachItemOrder);

        }
        public void bind(BillItems item) {
            // Gán giá trị cho các View trong mục
            Glide.with(productImage.getContext()).load(item.getPicUrl()).into(productImage);
            productName.setText(item.getName());
            productPrice.setText(String.format("$%.2f", item.getPrice()));
            productQuantity.setText(String.format("Quantity: %d", item.getQuantity()));
        }

    }
}

