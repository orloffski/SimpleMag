package model;

public class ItemsCount {
    private int itemId;
    private double itemCount;
    private double vendor_price;
    private String itemName;

    public ItemsCount(int id, double count){
        this(id, count, -1, "");
    }

    public ItemsCount(int id, double count, double vendor_price, String itemName){
        this.itemId = id;
        this.itemCount = count;
        this.vendor_price = vendor_price;
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getItemCount() {
        return itemCount;
    }

    public void setItemCount(double itemCount) {
        this.itemCount = itemCount;
    }

    public double getVendor_price() {
        return vendor_price;
    }

    public void setVendor_price(double vendor_price) {
        this.vendor_price = vendor_price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
