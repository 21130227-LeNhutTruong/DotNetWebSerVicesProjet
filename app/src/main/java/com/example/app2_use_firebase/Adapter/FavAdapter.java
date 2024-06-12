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
import com.example.app2_use_firebase.Activity.DetailActivity;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.databinding.ViewholderMyFavsBinding;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    ArrayList<ItemsDomain> items;
    public Context context;
    private ManagmentCart managmentCart;

    public FavAdapter(Context context,ArrayList<ItemsDomain> items) {
        this.items = items;
        this.context = context;

    }
    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        } else {
            Log.e("ItemsAdapter", "Invalid position: " + position);
        }
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderMyFavsBinding binding = ViewholderMyFavsBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.titleTxtOrder.setText(items.get(position).getTitle());
        holder.binding.totalEachItemOrder.setText(items.get(position).getPrice()+"");
        holder.binding.feeEachItemOrder.setText(items.get(position).getOldPrice()+"");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide .with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.picOrder);
        holder.binding.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart = new ManagmentCart(context);
                managmentCart.delectProductFav(items.get(position));
                removeItem(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderMyFavsBinding binding;

        public ViewHolder(@NonNull ViewholderMyFavsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
