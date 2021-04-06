package codingsharks.ezpricer.models;

import codingsharks.ezpricer.R;

public class Items {
    private String imageUrl;
    private String item_name;
    private double item_price;
    private String userId;
    private String description;
    private String productUrl;
    public Items(){
    }
    public Items(String text1, double text2){
        item_name = text1;
        item_price = text2;

    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Items(String name, double price, String userid, String url, String productURL, String desc){
        item_name = name;
        item_price = price;
        userId = userid;
        imageUrl = url;
        description = desc;
        productUrl = productURL;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Items(String name, double price, String userid){
        item_name = name;
        item_price = price;
        userId = userid;
    }    // NEED TO CHANGE IMAGERESOURCE


    public String getImageUrl() {
        return imageUrl;
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

    public String toString(){ return item_name + ": " + item_price;
    }
}
