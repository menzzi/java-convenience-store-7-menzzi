package store.model.domain;

public class ReceiptItem {
    private final String itemName;
    private final int itemQuantity;
    private final int itemPrice;

    public ReceiptItem(String itemName, int itemQuantity, int itemAmount) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getTotalPrice() {
        return itemQuantity * itemPrice;
    }
}
