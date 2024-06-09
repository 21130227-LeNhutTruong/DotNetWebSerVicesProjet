package com.example.app2_use_firebase.Activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app2_use_firebase.Adapter.PopularAdapter;
import com.example.app2_use_firebase.Adapter.SliderImgAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private ViewPager2 viewPager2, viewPager3,viewPager4;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable,sliderRunnable2,sliderRunnable3;
    private WindowManager windowManager;
    private View adView;
    private WindowManager.LayoutParams adParams;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initCategory();
        initPopular();
        bottomNavigation();
        initBags();
        initClothes();
        setonclicksearch();
        initSliderImage();
        initSliderImage2();
        setActionClick();
        initGiay();
        initSliderImage3();
//        navigationView.findViewById(R.id.navigationview);
        binding.btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });


checkAd();
    }



    public void setonclicksearch(){
        TextView search = findViewById(R.id.seach_home);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchHomeActivity.class));
            }
        });
    }
    public void bottomNavigation() {

        LinearLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        LinearLayout bill = findViewById(R.id.bill);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BillActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }



    private void initPopular() {
        DatabaseReference myRef = database.getReference("ItemsPopular");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewPopularProduct.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        binding.recyclerViewPopularProduct.setAdapter(new PopularAdapter(items));


                    }
                    binding.progressBarPopular.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initClothes() {
        DatabaseReference myRef = database.getReference("ItemsClothes");
        binding.progressBarClothes.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewClothes.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewClothes.setAdapter(new PopularAdapter(items));


                    }
                    binding.progressBarClothes.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initGiay() {
        DatabaseReference myRef = database.getReference("ItemsGiay");
        binding.progressBargiay.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewGiay.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
                        binding.recyclerViewGiay.setAdapter(new PopularAdapter(items));


                    }
                    binding.progressBargiay.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initBags() {
        DatabaseReference myRef = database.getReference("ItemsBags");
        binding.progressBarBag.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewBag.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
                        binding.recyclerViewBag.setAdapter(new PopularAdapter(items));


                    }
                    binding.progressBarBag.setVisibility(View.GONE);

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
       SliderImgAdapter  slideAdapter = new SliderImgAdapter(this, slideItems);
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
    private void initSliderImage3(){
        viewPager4 = binding.viewPager4;
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
        viewPager4.setAdapter(slideAdapter);

        sliderRunnable3= new Runnable() {
            @Override
            public void run() {
                if (viewPager4.getCurrentItem() < slideItems.size() - 1) {
                    viewPager4.setCurrentItem(viewPager4.getCurrentItem() + 1);
                } else {
                    viewPager4.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };

        viewPager4.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable3);
                sliderHandler.postDelayed(sliderRunnable3, 5000);
            }
        });

        sliderHandler.postDelayed(sliderRunnable3, 5000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.removeCallbacks(sliderRunnable2);
        sliderHandler.removeCallbacks(sliderRunnable3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        sliderHandler.postDelayed(sliderRunnable2, 3000);
        sliderHandler.postDelayed(sliderRunnable3, 3000);
    }
    // sự kiện click các items thể loại
    private void setActionClick(){
        binding.ao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AoActivity.class));
            }
        });
        binding.quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuanActivity.class));
            }
        });
        binding.bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BagsActivity.class));
            }
        });
        binding.giay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GiayActivity.class));
            }
        });
        binding.quannam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,QuanNamActivity.class));
            }
        });
        binding.quannu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,QuanNuActivity.class));
            }
        });
        binding.bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BagsActivity.class));
            }
        });
        binding.giay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GiayActivity.class));
            }
        });
        binding.aonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AoNuActivity.class));
            }
        });
        binding.aonam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AoNamActivity.class));
            }
        });
    }
    private void checkAd(){
        // Kiểm tra quyền truy cập
        // Nếu quyền đã được cấp khởi tạo chế độ xem quảng cáo
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu quyền đã được cấp khởi tạo chế độ xem quảng cáo
            initializeAdView();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // Đã cấp quyền, khởi tạo chế độ xem quảng cáo
                initializeAdView();
            } else {
                // Quyền không được cấp, hiển thị thông báo cho người dùng
            }
        }
    }
    @SuppressLint("WrongConstant")
    private void initializeAdView() {
// Khởi tạo WindowManager
windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Inflate the ad view layout
        adView = View.inflate(this, R.layout.ad_view, null);

        // Configure the layout parameters for the ad view
        adParams = new WindowManager.LayoutParams(
                200, // chiều rộng
                200, // chiều cao
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

// Thiết lập vị trí xuất hiện ở cạnh trái của màn hình và ở độ cao 100 pixels từ phía trên
        adParams.gravity = Gravity.LEFT | Gravity.TOP;
        adParams.x = 0; // Vị trí x = 0 (cạnh trái của màn hình)
        adParams.y = 100; // Vị trí y = 100 (cách phía trên 100 pixels)        // Add the ad view to the window
        windowManager.addView(adView, adParams);

        // Setup the close button
        ImageView closeButton = adView.findViewById(R.id.ad_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(adView);
            }
        });

        // Setup the touch listener for moving the ad view
        adView.setOnTouchListener(new View.OnTouchListener() {
            private float initialTouchX, initialTouchY;
            private int initialX, initialY;
            private long startClickTime;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = adParams.x;
                        initialY = adParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = System.currentTimeMillis();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        adParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        adParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(adView, adParams);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - startClickTime < 200) {
                            // Handle click event
                        } else {
                            snapToEdge();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void snapToEdge() {
        // Lấy chiều rộng và chiều cao màn hình
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Lấy chiều rộng và chiều cao một nửa của quảng cáo
        int halfAdWidth = adView.getWidth() / 2;
        int halfAdHeight = adView.getHeight() / 2;

        // Tính khoảng cách từ vị trí hiện tại của quảng cáo tới các cạnh của màn hình
        int leftEdgeDistance = adParams.x;
        int rightEdgeDistance = screenWidth - (adParams.x + adView.getWidth());
        int topEdgeDistance = adParams.y;
        int bottomEdgeDistance = screenHeight - (adParams.y + adView.getHeight());

        // Tìm khoảng cách ngắn nhất từ vị trí hiện tại của quảng cáo tới các cạnh
        int minDistance = Math.min(Math.min(leftEdgeDistance, rightEdgeDistance), Math.min(topEdgeDistance, bottomEdgeDistance));

        // Khởi tạo vị trí đích của quảng cáo bằng vị trí hiện tại
        int targetX = adParams.x;
        int targetY = adParams.y;

        // Xác định vị trí đích của quảng cáo dựa trên khoảng cách ngắn nhất tới cạnh
        if (minDistance == leftEdgeDistance) {
            // Nếu khoảng cách ngắn nhất là tới cạnh trái, đặt vị trí đích tới cạnh trái màn hình
            targetX = -halfAdWidth;
        } else if (minDistance == rightEdgeDistance) {
            // Nếu khoảng cách ngắn nhất là tới cạnh phải, đặt vị trí đích tới cạnh phải màn hình
            targetX = screenWidth - halfAdWidth;
        } else if (minDistance == topEdgeDistance) {
            // Nếu khoảng cách ngắn nhất là tới cạnh trên, đặt vị trí đích tới cạnh trên màn hình
            targetY = -halfAdHeight;
        } else {
            // Nếu khoảng cách ngắn nhất là tới cạnh dưới, đặt vị trí đích tới cạnh dưới màn hình
            targetY = screenHeight - halfAdHeight;
        }

        // Tạo và khởi động ValueAnimator để di chuyển quảng cáo theo trục X
        ValueAnimator animatorX = ValueAnimator.ofInt(adParams.x, targetX);
        animatorX.setDuration(100); // Thời gian di chuyển (milliseconds)
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Cập nhật vị trí X của quảng cáo khi ValueAnimator cập nhật giá trị
                adParams.x = (int) animation.getAnimatedValue();
                windowManager.updateViewLayout(adView, adParams);
            }
        });
        animatorX.start();

        // Tạo và khởi động ValueAnimator để di chuyển quảng cáo theo trục Y
        ValueAnimator animatorY = ValueAnimator.ofInt(adParams.y, targetY);
        animatorY.setDuration(100); // Thời gian di chuyển (milliseconds)
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Cập nhật vị trí Y của quảng cáo khi ValueAnimator cập nhật giá trị
                adParams.y = (int) animation.getAnimatedValue();
                windowManager.updateViewLayout(adView, adParams);
            }
        });
        animatorY.start();
    }


}