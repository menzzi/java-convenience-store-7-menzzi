package store.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ReceiptTest {

    @Test
    void 총구입금액을_가져온다(){
        ReceiptItem item1 = new ReceiptItem("item1", 2, 5000);
        ReceiptItem item2 = new ReceiptItem("item2", 3, 3000);
        List<ReceiptItem> receiptItems = List.of(item1, item2);

        List<ReceiptItem> freeGifts = List.of();
        Receipt receipt = new Receipt(receiptItems, freeGifts, "Y");

        int totalAmount = receipt.getTotalAmount();

        assertEquals(2 * 5000 + 3 * 3000, totalAmount);
    }

    @Test
    void 할인금액을_가져온다(){
        ReceiptItem item1 = new ReceiptItem("item1", 2, 5000);
        ReceiptItem item2 = new ReceiptItem("item2", 3, 3000);
        List<ReceiptItem> receiptItems = List.of(item1, item2);

        ReceiptItem item3 = new ReceiptItem("item1", 1, 5000);
        ReceiptItem item4 = new ReceiptItem("item1", 1, 3000);

        List<ReceiptItem> freeGifts = List.of(item3,item4);
        Receipt receipt = new Receipt(receiptItems, freeGifts, "N");

        int totalPromotionAmount = receipt.getTotalPromotionAmount();

        assertEquals(5000 + 3000, totalPromotionAmount);
    }

    @Test
    void 총수량을_가져온다(){
        ReceiptItem item1 = new ReceiptItem("item1", 2, 5000);
        ReceiptItem item2 = new ReceiptItem("item2", 3, 3000);
        List<ReceiptItem> receiptItems = List.of(item1, item2);

        List<ReceiptItem> freeGifts = List.of();
        Receipt receipt = new Receipt(receiptItems, freeGifts, "N");

        int totalTotalQuantity = receipt.getTotalQuantity();

        assertEquals(5, totalTotalQuantity);
    }
}
