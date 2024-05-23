package com.example.app2_use_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.app2_use_firebase.Domain.SlideView;
import com.example.app2_use_firebase.R;

import java.util.ArrayList;

public class SlideViewAdapter extends RecyclerView.Adapter<SlideViewAdapter.slideViewHolder> {
    private ArrayList<SlideView> list;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            list.addAll(list);
            notifyDataSetChanged();
        }
    };

    public SlideViewAdapter(ArrayList<SlideView> list, ViewPager2 viewPager2) {
        this.list = list;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewAdapter.slideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new slideViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewAdapter.slideViewHolder holder, int position) {
        holder.setImageView(list.get(position));
        if (position==list.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class slideViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public slideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
        }
        void setImageView( SlideView slideView){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions=requestOptions.transform(new CenterCrop());
            Glide.with(context)
                    .load(slideView.getUrl())
                    .apply(requestOptions)
                    .into(imageView);

        }
    }
}
