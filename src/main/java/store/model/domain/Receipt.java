package store.model.domain;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItems;
    private ReceiptItem freeGift;
    private final int eventDiscount;
    private final int membershipDiscount;

    public Receipt(List<ReceiptItem> receiptItems, ReceiptItem freeGift, int eventDiscount, int membershipDiscount){
        this.receiptItems = receiptItems;
        this.freeGift = freeGift;
        this.eventDiscount = eventDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int getTotalAmount(){
        return receiptItems.stream().mapToInt(ReceiptItem::getTotalPrice).sum();
    }

    public int getTotalQuantity(){
        return receiptItems.stream().mapToInt(ReceiptItem::getItemQuantity).sum();
    }

    public int getFinalAmount(){
        return getTotalAmount() - eventDiscount - membershipDiscount;
    }

    public ReceiptItem getFreeGift() {
        return freeGift;
    }
}
