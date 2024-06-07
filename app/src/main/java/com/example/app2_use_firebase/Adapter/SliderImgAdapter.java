package com.example.app2_use_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app2_use_firebase.databinding.ViewholderSlideItemBinding;
import com.example.app2_use_firebase.databinding.ViewholderSliderBinding;

import java.util.List;

public class SliderImgAdapter extends RecyclerView.Adapter<SliderImgAdapter.SlideViewHolder> {

    private List<Integer> slideItems;

    Context context ;
    public SliderImgAdapter(Context context, List<Integer> slideItems) {
        this.slideItems = slideItems;
        this.context = context;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        ViewholderSlideItemBinding binding = ViewholderSlideItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new SlideViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        int imageResId = slideItems.get(position);

        Glide.with(context).load(imageResId).into(holder.binding.imageViewSlide);
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {

        ViewholderSlideItemBinding binding;

        public SlideViewHolder(@NonNull  ViewholderSlideItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
