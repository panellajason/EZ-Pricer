package codingsharks.ezpricer;

public class Items {
    private int mImageResource;
    private String mItemName;
    private String mPrice;

    public Items(int imageResrouce, String text1, String text2){
        mImageResource = imageResrouce;
        mItemName = text1;
        mPrice = text2;

    }
    public int getmImageResource() {
        return mImageResource;
    }
    public String getmItemName(){
        return mItemName;
    }

    public String getmPrice(){
        return mPrice;
    }
}
