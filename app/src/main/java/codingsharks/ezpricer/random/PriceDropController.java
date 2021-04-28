package codingsharks.ezpricer.random;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class PriceDropController {
    private double priceDifference = 5.24;
    private String itemName;
    private double itemPrice;

    PriceDropController(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void createNotification() {

    }

    public String getPriceDropTitle() {
        return "Item Price In Your Watchlist Has Dropped!";
    }
    public String getPriceDropContent() {
        return itemName + " has dropped to $" + (itemPrice - priceDifference) + "!";
    }
    public String getPriceDropSubject() {
        return "An item in your watchlist dropped in price!";
    }
    public String getPriceDropEmailMessage() {
        return "The item " + itemName + " in your watch list has dropped in price!\n\n" +
                "The original price of " + itemName + " was: $" + itemPrice + "\n" +
                "Now the new price of the item is: $" + (itemPrice - priceDifference) +
                "\n\n\nThank you for using EZPricer!";
    }
}
