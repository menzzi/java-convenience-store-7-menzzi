package store.view;

public class OutputView {
    private final String BENEFIT_INSTRUCTIONS_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private final String PROMOTION_INSTRUCTIONS_MESSAGE = "프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private final String MEMBERSHIP_INSTRUCTIONS_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private final String ADDITIONAL_PURCHASE_INSTRUCTIONS_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public void printWelcomeGreeting(){
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printStockInformation(String stockDetails){
        System.out.println(stockDetails);
    }

    public void printInstructionsAboutBenefit(){
        System.out.println(BENEFIT_INSTRUCTIONS_MESSAGE);
    }

    public void printInstructionsAboutPromotion(String productName, int quantity){
        System.out.printf("현재 %s %d개는 %s",productName,quantity,PROMOTION_INSTRUCTIONS_MESSAGE);
    }

    public void printInstructionsAboutMembership(){
        System.out.println(MEMBERSHIP_INSTRUCTIONS_MESSAGE);
    }

    public void printInstructionsAboutAdditionalPurchase(){
        System.out.println(ADDITIONAL_PURCHASE_INSTRUCTIONS_MESSAGE);
    }
}
