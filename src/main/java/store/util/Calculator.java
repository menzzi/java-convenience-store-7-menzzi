package store.util;

import java.util.List;
import store.model.domain.MembershipDiscount;
import store.model.domain.ReceiptItem;

public class Calculator {

    public static int calculateMembershipAmount(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift,
                                          String membershipStatus) {
        int total = 0;
        for (ReceiptItem receiptItem : receiptItems) {
            boolean isFreeGift = freeGift.stream()
                    .anyMatch(gift -> gift.getItemName().equals(receiptItem.getItemName()));
            if (!isFreeGift) {
                total += receiptItem.getTotalPrice();
            }
        }
        return MembershipDiscount.applyMembershipDiscount(membershipStatus, total);
    }

    public static int calculateTotalMoneyToBePaid(int totalAmount, int totalPromotionAmount, int membershipAmount) {
        return totalAmount - totalPromotionAmount - membershipAmount;
    }
}
