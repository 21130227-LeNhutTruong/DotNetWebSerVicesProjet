package com.example.app2_use_firebase.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    private FirebaseFirestore db;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);


    }


//    public void insertProduct(ItemsDomain item) {
//
//        ArrayList<ItemsDomain> listfood = getListCart();
//        boolean existAlready = false;
//        int n = 0;
//        for (int y = 0; y < listfood.size(); y++) {
//            if (listfood.get(y).getTitle().equals(item.getTitle())) {
//                existAlready = true;
//                n = y;
//                break;
//            }
//        }
//        if (existAlready) {
//            listfood.get(n).setNumberinCart(item.getNumberinCart());
//        } else {
//            listfood.add(item);
//        }
//        tinyDB.putListObject("CartList", listfood);
//        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
//    }
public void insertProduct(ItemsDomain item) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Lấy thông tin đăng nhập của người dùng
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();

        ArrayList<ItemsDomain> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listfood.add(item);
        }

        // Lưu sản phẩm cho từng người dùng vào Firestore
        db.collection("users").document(userId).collection("carts")
                .document(item.getId()) // Sử dụng id của sản phẩm làm id của document
                .set(item) // Lưu thông tin sản phẩm vào document
                .addOnSuccessListener(aVoid -> {
                    // Thành công khi lưu sản phẩm
                    Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Lỗi khi lưu sản phẩm
                    Toast.makeText(context, "Error adding to Cart", Toast.LENGTH_SHORT).show();
                });

        // Lưu danh sách sản phẩm vào SharedPreferences
//        tinyDB.putListObject("CartList", listfood);
    }
}
    public void deleteItemFromCart() {
        getListCart().clear();

    }



    public ArrayList<ItemsDomain> getListCart() {
        return tinyDB.getListObject("CartList", ItemsDomain.class);
    }


    public void minusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberinCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemsDomain> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberinCart());
        }
        return fee;
    }
}
