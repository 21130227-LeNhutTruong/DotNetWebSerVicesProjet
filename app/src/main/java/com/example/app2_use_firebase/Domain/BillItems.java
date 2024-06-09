package com.example.app2_use_firebase.Domain;

import java.util.List;

public class BillItems {
    private String name;
    private int quantity;
    private double price;
    private String picUrl;
    private String id;
    private List<ItemsDomain> items;

    public BillItems() {
    }

    public BillItems(List<ItemsDomain> items,String name, int quantity, double price, String picUrl, String id) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.picUrl = picUrl;
        this.items = items;
        this.id = id;
    }

    public List<ItemsDomain> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItems(List<ItemsDomain> items) {
        this.items = items;
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
