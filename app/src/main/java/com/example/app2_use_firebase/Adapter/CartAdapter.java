package com.example.app2_use_firebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.app2_use_firebase.Activity.CartActivity;
import com.example.app2_use_firebase.Activity.DetailActivity;
import com.example.app2_use_firebase.Activity.FavActivity;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.ChangeNumberItemsListener;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    ArrayList<ItemsDomain> listItemSelected ;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;
    Context context;

    public CartAdapter(ArrayList<ItemsDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart = new ManagmentCart(context);


    }

    @NonNull
    @Override
    public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.titleTxt.setText(listItemSelected.get(position).getTitle());
        holder.binding.feeEachItem.setText("$"+listItemSelected.get(position).getPrice());
        holder.binding.totalEachItem.setText("$"+Math.round(listItemSelected.get(position).getNumberinCart()*listItemSelected.get(position).getPrice()));
        holder.binding.numberItems.setText(String.valueOf(listItemSelected.get(position).getNumberinCart()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(holder.itemView.getContext())
                .load(listItemSelected.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",listItemSelected.get(position));
                context.startActivity(intent);
            }
        });

        holder.binding.plusCartBtn.setOnClickListener(v -> managmentCart.plusItem(listItemSelected,position, () ->{
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }));
        holder.binding.minusCartBtn.setOnClickListener(v -> managmentCart.minusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }));
        holder.binding.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart = new ManagmentCart(context);
                managmentCart.delectProductCart(listItemSelected.get(position));
                removeItem(position);
            }
        });


    }
    public void removeItem(int position) {
        if (position >= 0 && position < listItemSelected.size()) {
            listItemSelected.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItemSelected.size());

        } else {
            Log.e("ItemsAdapter", "Invalid position: " + position);
        }
    }


    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;
        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
