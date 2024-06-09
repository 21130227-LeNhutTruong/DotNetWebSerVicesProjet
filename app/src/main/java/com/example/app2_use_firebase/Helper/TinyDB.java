package com.example.app2_use_firebase.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.app2_use_firebase.Domain.ItemsDomain;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class TinyDB {
    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public ArrayList<ItemsDomain> getListObject(String key, Class<ItemsDomain> itemsDomainClass) {

        Gson gson = new Gson();

        // Nhận mảng chuỗi từ SharedPreferences
        ArrayList<String> objStrings = getListString(key);
        ArrayList<ItemsDomain> playerList = new ArrayList<ItemsDomain>();

        // Chuyển chuỗi thành đối tượng
        for (String jObjString : objStrings) {
            ItemsDomain player = gson.fromJson(jObjString, ItemsDomain.class);
            // Add vào mảng
            playerList.add(player);
        }
        // Trả về mảng
        return playerList;
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }


    public void putListObject(String key, ArrayList<ItemsDomain> playerList) {
        // Chuyển đối tượng thành chuỗi
        checkForNullKey(key);
        // Tạo mảng chuỗi
        Gson gson = new Gson();
        // Chuyển mảng đối tượng thành chuỗi
        ArrayList<String> objStrings = new ArrayList<String>();
        // Thêm chuỗi vào mảng
        for (ItemsDomain player : playerList) {
            // Add vào mảng
            objStrings.add(gson.toJson(player));

        }
        // Thêm mảng chuỗi vào SharedPreferences
        putListString(key, objStrings);
    }


    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

}