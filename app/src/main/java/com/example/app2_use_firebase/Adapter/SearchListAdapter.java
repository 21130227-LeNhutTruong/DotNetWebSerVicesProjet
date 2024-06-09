package com.example.app2_use_firebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.app2_use_firebase.databinding.ViewholderSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{

    Context context;
    ArrayList<ItemsDomain> items;

    public SearchListAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    public void setFilteredList(List<ItemsDomain> filteredList){
        this.items = (ArrayList<ItemsDomain>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderSearchBinding binding = ViewholderSearchBinding.inflate(LayoutInflater.from(context),parent,false);
        return new SearchListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.nameView.setText(items.get(position).getTitle());
        holder.binding.priceView.setText(items.get(position).getPrice()+"");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.imgView);
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewholderSearchBinding binding;
        public ViewHolder(@NonNull ViewholderSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
