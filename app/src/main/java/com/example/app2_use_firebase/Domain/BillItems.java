package com.example.app2_use_firebase.Domain;

public class BillItems {
    private String name;
    private int quantity;
    private double price;
    private String picUrl;

    public BillItems() {
    }

    public BillItems(String name, int quantity, double price, String picUrl) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
