package com.example.app2_use_firebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.example.app2_use_firebase.Domain.SliderItems;
import com.example.app2_use_firebase.databinding.ViewholderPopListBinding;
import com.example.app2_use_firebase.databinding.ViewholderSliderBinding;

import java.util.ArrayList;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {
    ArrayList<SliderItems> items;
    Context context;

    public SlideAdapter(ArrayList<SliderItems> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public SlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        ViewholderSliderBinding binding = ViewholderSliderBinding.inflate(LayoutInflater.from(context),parent,false);

        return new SlideAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.title.setText(items.get(position).getTitle());
        holder.binding.descTxt.setText(items.get(position).getDescription());
        holder.binding.priceTxt.setText("$"+items.get(position).getPrice());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide .with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewholderSliderBinding binding;

        public ViewHolder(@NonNull ViewholderSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
