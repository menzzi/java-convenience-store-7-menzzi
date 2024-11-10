package store.model.domain;

import java.util.List;

public record Receipt(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGifts, String membershipDiscount) {

    public int getTotalAmount() {
        return receiptItems.stream().mapToInt(ReceiptItem::getTotalPrice).sum();
    }

    public int getTotalPromotionAmount() {
        return freeGifts.stream().mapToInt(ReceiptItem::getTotalPrice).sum();
    }

    public int getTotalQuantity() {
        return receiptItems.stream().mapToInt(ReceiptItem::getItemQuantity).sum();
    }
}
