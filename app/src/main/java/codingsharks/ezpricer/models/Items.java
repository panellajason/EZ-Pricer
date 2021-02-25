package codingsharks.ezpricer.models;

import codingsharks.ezpricer.R;

public class Items {
    private int mImageResource;
    private String item_name;
    private double item_price;
    private String userId;
    public Items(){
    }
    public Items(String text1, double text2){
        item_name = text1;
        item_price = text2;

    }
    public int getmImageResource() {
        return R.drawable.ic_android_black;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getItem_name() {
        return item_name;
    }

    public double getItem_price() {
        return item_price;
    }
}