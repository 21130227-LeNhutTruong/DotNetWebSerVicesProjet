package com.example.app2_use_firebase.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.Adapter.SearchListAdapter;
import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.example.app2_use_firebase.R;
import com.example.app2_use_firebase.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHomeActivity extends BaseActivity{
    ActivitySearchBinding binding;
    private FirebaseDatabase database = FirebaseDatabase.getInstance(); // Khởi tạo instance database

    private SearchView searchView;

    ArrayList<ItemsDomain> items = new ArrayList<>();

    SearchListAdapter searchListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSearch();
        initSearch1();
        initSearch2();
        initSearch3();
        initSearch4();



        super.onCreate(savedInstanceState);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        ConstraintLayout bthbackHome = findViewById(R.id.back_home);
        bthbackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
    private void initSearch(){

        DatabaseReference myData = database.getReference("ItemsPopular");
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchHomeActivity.this,RecyclerView.VERTICAL,false));
                        binding.recyclerViewSearch.setAdapter(searchListAdapter = new SearchListAdapter(items));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void initSearch1(){

        DatabaseReference myData = database.getReference("ItemsQuan");
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchHomeActivity.this,RecyclerView.VERTICAL,false));
                        binding.recyclerViewSearch.setAdapter(searchListAdapter = new SearchListAdapter(items));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void initSearch2(){

        DatabaseReference myData = database.getReference("ItemsAo");
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchHomeActivity.this,RecyclerView.VERTICAL,false));
                        binding.recyclerViewSearch.setAdapter(searchListAdapter = new SearchListAdapter(items));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void initSearch3(){

        DatabaseReference myData = database.getReference("ItemsGiay");
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchHomeActivity.this,RecyclerView.VERTICAL,false));
                        binding.recyclerViewSearch.setAdapter(searchListAdapter = new SearchListAdapter(items));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void initSearch4(){

        DatabaseReference myData = database.getReference("ItemsTuiXach");
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchHomeActivity.this,RecyclerView.VERTICAL,false));
                        binding.recyclerViewSearch.setAdapter(searchListAdapter = new SearchListAdapter(items));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void filterList(String text){
        List<ItemsDomain> fiteredList = new ArrayList<>();
        for (ItemsDomain item : items){
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                fiteredList.add(item);
            }
        }
        if(fiteredList.isEmpty()){
           // Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            searchListAdapter.setFilteredList(fiteredList);
        }
    }
}
