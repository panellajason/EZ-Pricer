package codingsharks.ezpricer.models;

public class Vendor {
    private String name;
    private int vendorLogoImageResource;
    private Items item;


    public Vendor(String name, int vendorLogoImageResource, Items item) {
        this.name = name;
        this.vendorLogoImageResource = vendorLogoImageResource;
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
