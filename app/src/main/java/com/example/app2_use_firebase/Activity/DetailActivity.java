package com.example.app2_use_firebase.Activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.app2_use_firebase.Adapter.SizeAdapter;
import com.example.app2_use_firebase.Adapter.SlideViewAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.Domain.SlideView;
import com.example.app2_use_firebase.Fragment.DescriptionFragment;
import com.example.app2_use_firebase.Fragment.ReviewFragment;
import com.example.app2_use_firebase.Fragment.SoldFragment;
import com.example.app2_use_firebase.Helper.ManagmentCart;
import com.example.app2_use_firebase.databinding.ActivityDetailBinding;

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
        setupViewPager();
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
        binding.priceTxt.setText("$"+object.getOldPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating()+"Rating");


        binding.addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertFood(object);
        });
        binding.btnBack.setOnClickListener(v -> startActivity(new Intent(this,MainActivity.class)));
    }

    private void setupViewPager(){

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();

        bundle1.putString("Description", object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Description");
        adapter.addFrag(tab2,"Reviews");
        adapter.addFrag(tab3,"Sold");

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        private void addFrag( Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}