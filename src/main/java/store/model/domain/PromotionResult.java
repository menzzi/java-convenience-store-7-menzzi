package store.model.domain;

public class PromotionResult {
    private final String message;
    private final int currentQuantity;
    private final int relateQuantity;

    public PromotionResult(String message, int applicableQuantity, int bonusQuantity) {
        this.message = message;
        this.currentQuantity = applicableQuantity;
        this.relateQuantity = bonusQuantity;
    }

    public String getMessage() {
        return message;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int getRelateQuantity() {
        return relateQuantity;
    }

    public int getTotalAddQuantity(){
        return currentQuantity+relateQuantity;
    }

    public int getTotalMinusQuantity(){
        return currentQuantity-relateQuantity;
    }
}
