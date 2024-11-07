package store.model.domain;

public class ReceiptItem {
    private final String itemName;
    private final int itemQuantity;
    private final int itemAmount;

    public ReceiptItem(String itemName, int itemQuantity, int itemAmount){
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemAmount = itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getItemAmount() {
        return itemAmount;
    }
}
