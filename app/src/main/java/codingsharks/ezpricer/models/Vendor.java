package codingsharks.ezpricer.models;

import codingsharks.ezpricer.R;

public class Vendor {
    private String name;
    private int vendorLogoImageResource;
    private Items item;

    public Vendor(String name, Items item) {
        this.name = name;
        if("Walmart".equals(name)){
            this.vendorLogoImageResource = R.drawable.walmart_logo;
        }
        else if ("Bestbuy".equals(name)){
            this.vendorLogoImageResource = R.drawable.bestbuy_logo;
        }
        else if ("Amazon".equals(name)){
            this.vendorLogoImageResource = R.drawable.shark;
        }
        else{
            this.vendorLogoImageResource = R.drawable.ic_android_black;
        }
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public int getVendorLogoImageResource() {
        return vendorLogoImageResource;
    }

    public Items getItem() {
        return item;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVendorLogoImageResource(int vendorLogoImageResource) {
        this.vendorLogoImageResource = vendorLogoImageResource;
    }

    public void setItem(Items item) {
        this.item = item;
    }
}
