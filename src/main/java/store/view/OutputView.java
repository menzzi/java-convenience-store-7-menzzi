package store.view;

import java.text.DecimalFormat;
import java.util.List;
import store.model.domain.ReceiptItem;

public class OutputView {
    private final String ERROR_FORMAT = "[ERROR]";
    private final String UNAVAILBLE_PROMOTION_INSTRUCTIONS_MESSAGE = "프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private final String ADD_PRODUCT_INSTRUCTIONS_MESSAGE = "무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private final String MEMBERSHIP_INSTRUCTIONS_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private final String ADDITIONAL_PURCHASE_INSTRUCTIONS_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public void printWelcomeGreeting() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printStockInformation(String stockDetails) {
        System.out.println(stockDetails);
    }

    public void printInstructionsAboutUnavailablePromotion(String productName, int quantity) {
        System.out.printf("현재 %s %d개는 %s", productName, quantity, UNAVAILBLE_PROMOTION_INSTRUCTIONS_MESSAGE);
        System.out.println();
    }

    public void printInstructionsAboutAddProduct(String productName, int quantity) {
        System.out.printf("현재 %s은(는) %d개를 %s", productName, quantity, ADD_PRODUCT_INSTRUCTIONS_MESSAGE);
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

        System.out.println("===========W 편의점=============");
        System.out.printf("%-12s\t%-5s\t%-10s\n", "상품명", "수량", "금액");
        for (ReceiptItem item : receiptItems) {
            System.out.printf("%-12s\t%-5d\t%-10s\n", item.getItemName(), item.getItemQuantity(),
                    df.format(item.getTotalPrice()));
        }
    }

    public void printPromotionInfomation(List<ReceiptItem> freeGift) {
        System.out.println("===========증\t정=============");
        for (ReceiptItem item : freeGift) {
            System.out.printf("%-12s\t%-5d\n", item.getItemName(), item.getItemQuantity());
        }
    }

    public void printMoneyInformation(int totalAmount, int totalCount, int totalPromotionAmount, int membershipAmount,
                                      int totalMoneyToBePaid) {
        DecimalFormat df = new DecimalFormat("###,###");

        System.out.println("==============================");
        System.out.printf("%-12s\t%-5d\t%-10s\n", "총구매액", totalCount, df.format(totalAmount));
        System.out.printf("%-20s\t%-10s\n", "행사할인", "-" + df.format(totalPromotionAmount));
        System.out.printf("%-20s\t%-10s\n", "멤버십할인", "-" + df.format(membershipAmount));
        System.out.printf("%-20s\t%-10s\n", "내실돈", df.format(totalMoneyToBePaid));
        System.out.println();
    }
}
