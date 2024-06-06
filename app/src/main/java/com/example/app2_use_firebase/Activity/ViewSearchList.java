package com.example.app2_use_firebase.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app2_use_firebase.R;

public class ViewSearchList extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView, priceView;
    public ViewSearchList(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgView);
        nameView = itemView.findViewById(R.id.nameView);
        priceView = itemView.findViewById(R.id.priceView);
    }
}
