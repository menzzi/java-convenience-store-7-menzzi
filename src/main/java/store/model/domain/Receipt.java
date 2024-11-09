package store.model.domain;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItems;
    private List<ReceiptItem> freeGifts;
    private final String membershipDiscount;

    public Receipt(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGifs, String membershipDiscount){
        this.receiptItems = receiptItems;
        this.freeGifts = freeGifts;
        this.membershipDiscount = membershipDiscount;
    }

    public int getTotalAmount(){
        return receiptItems.stream().mapToInt(ReceiptItem::getTotalPrice).sum();
    }

    public int getTotalPromotionAmount(){
        return freeGifts.stream().mapToInt(ReceiptItem::getTotalPrice).sum();
    }

    public int getTotalQuantity(){
        return receiptItems.stream().mapToInt(ReceiptItem::getItemQuantity).sum();
    }

    public String membershipDiscount() {
        return membershipDiscount;
    }
}
