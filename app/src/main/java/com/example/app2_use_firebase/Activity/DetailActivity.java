package com.example.app2_use_firebase.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.app2_use_firebase.Adapter.ColorAdapter;
import com.example.app2_use_firebase.Adapter.SizeAdapter;
import com.example.app2_use_firebase.Adapter.SlideViewAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Domain.SlideView;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityDetailBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
        ActivityDetailBinding binding;
        private ItemsDomain object;

        private int numberOrder = 1;
        private ManagmentCart managmentCart;
        private Handler slidedHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        getBundles();
        banners();
        initSize();

    }


    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        binding.recycleViewSize.setAdapter(new SizeAdapter(list));
        binding.recycleViewSize.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        binding.recycleViewColor.setAdapter( new ColorAdapter(list));
        binding.recycleViewColor.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


    }
    private void saveCartForUser(String userId, List<ItemsDomain> cartItems) {
        SharedPreferences sharedPreferences = getSharedPreferences("users" + userId, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CartList", json);
        editor.apply();
    }

    private void banners() {
        ArrayList<SlideView> slideViews = new ArrayList<>();
        for (int i = 0; i< object.getPicUrl().size();i++) {
            slideViews.add(new SlideView(object.getPicUrl().get(i)));
        }
            binding.viewPage2Slider.setAdapter( new SlideViewAdapter(slideViews,binding.viewPage2Slider));
            binding.viewPage2Slider.setClipToPadding(false);
            binding.viewPage2Slider.setClipChildren(false);
            binding.viewPage2Slider.setOffscreenPageLimit(3);
            binding.viewPage2Slider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPage2Slider.setPageTransformer(compositePageTransformer);


    }

    private void getBundles() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(object.getPrice()+"vnd");
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating()+"Rating");
        binding.descDetailTxt.setText(object.getDescription());
        binding.desShort.setText(object.getDes());


        // xét sự kiện click vào button thêm vào giỏ hàng
        binding.addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertProduct(object);
        });
        ImageView btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(v ->  finish());
    }

}