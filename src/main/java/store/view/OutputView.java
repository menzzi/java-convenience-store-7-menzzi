package store.view;

import static store.view.OutputMessage.*;

import java.text.DecimalFormat;
import java.util.List;
import store.model.domain.ReceiptItem;

public class OutputView {

    public void printWelcomeGreeting() {
        System.out.println(WELCOME_GREETING);
        System.out.println(STOCK_INFORMATION);
        System.out.println();
    }

    public void printStockInformation(String stockDetails) {
        System.out.println(stockDetails);
    }

    public void printInstructionsAboutUnavailablePromotion(String productName, int quantity) {
        System.out.printf(UNAVAILBLE_PROMOTION_INSTRUCTIONS_MESSAGE, productName, quantity);
        System.out.println();
    }

    public void printInstructionsAboutAddProduct(String productName, int quantity) {
        System.out.printf(ADD_PRODUCT_INSTRUCTIONS_MESSAGE, productName, quantity);
        System.out.println();
    }

    public void printInstructionsAboutMembership() {
        System.out.println(MEMBERSHIP_INSTRUCTIONS_MESSAGE);
    }

    public void printInstructionsAboutAdditionalPurchase() {
        System.out.println(ADDITIONAL_PURCHASE_INSTRUCTIONS_MESSAGE);
    }

    public void printErrorMessage(String errorMessage) {
        System.out.println(ERROR_FORMAT + errorMessage);
        System.out.println();
    }

    public void printPurchaseInformation(List<ReceiptItem> receiptItems) {
        DecimalFormat df = new DecimalFormat("###,###");

        System.out.println(FIRST_LINE_OF_RECEIPT);
        System.out.printf("%-15s\t%-6s\t%-5s\n", "상품명", "수량", "금액");
        for (ReceiptItem item : receiptItems) {
            System.out.printf("%-15s\t%-7d\t%-10s\n", item.getItemName(), item.getItemQuantity(),
                    df.format(item.getTotalPrice()));
        }
    }

    public void printPromotionInfomation(List<ReceiptItem> freeGift) {
        System.out.println(SECOND_LINE_OF_RECEIPT);
        for (ReceiptItem item : freeGift) {
            System.out.printf("%-15s\t%-7d\n", item.getItemName(), item.getItemQuantity());
        }
    }

    public void printMoneyInformation(int totalAmount, int totalCount, int totalPromotionAmount, int membershipAmount,
                                      int totalMoneyToBePaid) {
        DecimalFormat df = new DecimalFormat("###,###");

        System.out.println(THIRD_LINE_OF_RECEIPT);
        System.out.printf("%-15s\t%-7d\t%-10s\n", TOTAL_PURCHASE_AMOUNT, totalCount, df.format(totalAmount));
        System.out.printf("%-25s\t%-10s\n", PROMOTION_DISCOUNT, "-" + df.format(totalPromotionAmount));
        System.out.printf("%-25s\t%-10s\n", MEMBERSHIP_DISCOUNT, "-" + df.format(membershipAmount));
        System.out.printf("%-20s\t%10s\n", MONEY_TO_BE_PAY, df.format(totalMoneyToBePaid));
        System.out.println();
    }
}
