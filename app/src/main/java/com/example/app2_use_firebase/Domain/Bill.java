package com.example.app2_use_firebase.Domain;

import java.util.List;
import java.util.Map;

public class Bill {
    private String date;
    private String hoten;
    private String diachi;
    private String sdt;
    private String phuongthuc;
    private double totalAmount;
    private String status;

    private List<ItemsDomain> items;

    public Bill() {
        // Constructor mặc định cần thiết cho Firestore
    }

    public Bill(String date, String hoten, String diachi, String sdt, String phuongthuc, double totalAmount, List<ItemsDomain> items) {
        this.date = date;
        this.hoten = hoten;
        this.diachi = diachi;
        this.sdt = sdt;
        this.phuongthuc = phuongthuc;
        this.totalAmount = totalAmount;
        this.status = "đang xử lý";  // Trạng thái mặc định là đang xử lý

        this.items = items;
    }

    // Getter và setter cho tất cả các thuộc tính

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }
    public String getDiachi() { return diachi; }
    public void setDiachi(String diachi) { this.diachi = diachi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getPhuongthuc() { return phuongthuc; }
    public void setPhuongthuc(String phuongthuc) { this.phuongthuc = phuongthuc; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public List<ItemsDomain> getItems() { return items; }
    public void setItems(List<ItemsDomain> items) { this.items = items; }
}