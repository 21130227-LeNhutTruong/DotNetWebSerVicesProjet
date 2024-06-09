package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app2_use_firebase.Adapter.PopularAdapter;
import com.example.app2_use_firebase.Adapter.SliderImgAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityListQuanNuBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuanNuActivity extends BaseActivity{
    ActivityListQuanNuBinding binding;
    private ViewPager2 viewPager2, viewPager3;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable,sliderRunnable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityListQuanNuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        initBags();
        initSliderImage();
        initSliderImage2();
        setClickAction();


    }
    private void initBags() {
        DatabaseReference myRef = database.getReference("ItemsQuanNu");
        binding.progressAo.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewListQuanNu.setLayoutManager(new GridLayoutManager(QuanNuActivity.this, 2));
                        binding.recyclerViewListQuanNu.setAdapter(new PopularAdapter(items));


                    }
                    binding.progressAo.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initSliderImage(){
        viewPager2 = binding.viewPager2;
// Thêm danh sách các hình ảnh cho slide
        List<Integer> slideItems   = Arrays.asList(
                R.drawable.imgslide_2,
                R.drawable.imgslide_4,
                R.drawable.imgslide_5,
                R.drawable.imgslide_6,
                R.drawable.imgslide_9,
                R.drawable.imgslide_7
        );
        SliderImgAdapter slideAdapter = new SliderImgAdapter(this, slideItems);
        viewPager2.setAdapter(slideAdapter);

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager2.getCurrentItem() < slideItems.size() - 1) {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    viewPager2.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        sliderHandler.postDelayed(sliderRunnable, 5000);
    }
    private void initSliderImage2(){
        viewPager3 = binding.viewPager3;
// Thêm danh sách các hình ảnh cho slide
        List<Integer> slideItems   = Arrays.asList(
                R.drawable.imgslide_9,
                R.drawable.imgslide_7,
                R.drawable.imgslide_2,
                R.drawable.imgslide_4,
                R.drawable.imgslide_5,
                R.drawable.imgslide_6

        );
        SliderImgAdapter  slideAdapter = new SliderImgAdapter(this, slideItems);
        viewPager3.setAdapter(slideAdapter);

        sliderRunnable2 = new Runnable() {
            @Override
            public void run() {
                if (viewPager3.getCurrentItem() < slideItems.size() - 1) {
                    viewPager3.setCurrentItem(viewPager3.getCurrentItem() + 1);
                } else {
                    viewPager3.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };

        viewPager3.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable2);
                sliderHandler.postDelayed(sliderRunnable2, 5000);
            }
        });

        sliderHandler.postDelayed(sliderRunnable2, 5000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.removeCallbacks(sliderRunnable2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        sliderHandler.postDelayed(sliderRunnable2, 3000);
    }
    private void setClickAction(){
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
