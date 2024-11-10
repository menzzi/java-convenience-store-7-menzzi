package store.model.domain;

public class PromotionResult {
    private final PromotionStatus status;
    private final int currentQuantity;
    private final int relateQuantity;

    public PromotionResult(PromotionStatus status, int applicableQuantity, int bonusQuantity) {
        this.status = status;
        this.currentQuantity = applicableQuantity;
        this.relateQuantity = bonusQuantity;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int getRelateQuantity() {
        return relateQuantity;
    }

    public int getTotalAddQuantity() {
        return currentQuantity + relateQuantity;
    }

    public int getTotalMinusQuantity() {
        return currentQuantity - relateQuantity;
    }
}
