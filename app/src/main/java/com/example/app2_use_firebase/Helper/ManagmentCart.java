package com.example.app2_use_firebase.Helper;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;
    private FirebaseFirestore db;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);


    }

public void insertProduct(ItemsDomain item) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Lấy thông tin đăng nhập của người dùng
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        // Lấy id của người dùng
        String userId = currentUser.getUid();

        // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng
        ArrayList<ItemsDomain> listProduct = getListCart();
        boolean existAlready = false;
        int n = 0;

        for (int y = 0; y < listProduct.size(); y++) {
            // Kiểm tra nếu sản phẩm đã tồn tại trong giỏ hàng
            if (listProduct.get(y).getTitle().equals(item.getTitle())) {
                // Cập nhật số lượng sản phẩm trong giỏ hàng
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            // Cập nhật số lượng sản phẩm trong giỏ hàng
            listProduct.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            // Thêm sản phẩm vào giỏ hàng
            listProduct.add(item);
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
        tinyDB.putListObject("CartList", listProduct);
    }
}
    public void insertProductFav(ItemsDomain item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            ArrayList<ItemsDomain> listProduct = getListCart();
            boolean existAlready = false;
            int n = 0;

            for (int y = 0; y < listProduct.size(); y++) {
                if (listProduct.get(y).getTitle().equals(item.getTitle())) {
                    existAlready = true;
                    n = y;
                    break;
                }
            }
            if (existAlready) {
                listProduct.get(n).setNumberinCart(item.getNumberinCart());
            } else {
                listProduct.add(item);
            }

            db.collection("users").document(userId).collection("favs")
                    .document(item.getId())
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    public void clearCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy thông tin đăng nhập của người dùng
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Truy vấn tất cả các tài liệu trong sub-collection "carts"
            db.collection("users").document(userId).collection("carts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    // Xóa từng tài liệu
                                    document.getReference().delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Thành công khi xóa tài liệu
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Lỗi khi xóa tài liệu
                                                }
                                            });
                                }
                                // Thông báo thành công khi đã xóa toàn bộ giỏ hàng
                                Toast.makeText(context, "All items removed from your Cart", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý lỗi khi truy vấn không thành công
                                Toast.makeText(context, "Error clearing Cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Người dùng chưa đăng nhập
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    public void delectProductFav(ItemsDomain item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).collection("favs")
                    .document(item.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    public void delectProductCart(ItemsDomain item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).collection("carts")
                    .document(item.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    });
        }
    }


    public ArrayList<ItemsDomain> getListCart() {
        // Lấy danh sách sản phẩm từ SharedPreferences
        return tinyDB.getListObject("CartList", ItemsDomain.class);
    }


    public void minusItem(ArrayList<ItemsDomain> listProduct, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        // Kiểm tra số lượng sản phẩm trong giỏ hàng
        if (listProduct.get(position).getNumberinCart() == 1) {
            listProduct.remove(position);
        } else {
            // Giảm số lượng sản phẩm trong giỏ hàng
            listProduct.get(position).setNumberinCart(listProduct.get(position).getNumberinCart() - 1);
        }
        // Lưu danh sách sản phẩm vào SharedPreferences
        tinyDB.putListObject("CartList", listProduct);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listProduct, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        // Tăng số lượng sản phẩm trong giỏ hàng
        listProduct.get(position).setNumberinCart(listProduct.get(position).getNumberinCart() + 1);
        // Lưu danh sách sản phẩm vào SharedPreferences
        tinyDB.putListObject("CartList", listProduct);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        // Lấy danh sách sản phẩm từ SharedPreferences
        ArrayList<ItemsDomain> listProduct2 = getListCart();
        // Tính tổng tiền
        double fee = 0;
        // Lặp qua danh sách sản phẩm và tính tổng tiền
        for (int i = 0; i < listProduct2.size(); i++) {
            // Tính tổng tiền của mỗi sản phẩm
            fee = fee + (listProduct2.get(i).getPrice() * listProduct2.get(i).getNumberinCart());
        }
        return fee;
    }
}
