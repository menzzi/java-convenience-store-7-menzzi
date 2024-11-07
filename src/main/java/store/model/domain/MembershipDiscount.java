package store.model.domain;

public enum MembershipDiscount {
    MEMBERSHIP_DISCOUNT("Y",0.3, 8000),
    NO_DISCOUNT("N",0,0);

    private final String YesOrNo;
    private final double discountRate;
    private final int maximumLimit;

    MembershipDiscount( String YesOrNo, double discountRate, int maximumLimit){
        this.YesOrNo = YesOrNo;
        this.discountRate = discountRate;
        this.maximumLimit = maximumLimit;
    }

    public int applyMembershipDiscount(String input, int amount){
        if(input.equals(MEMBERSHIP_DISCOUNT.YesOrNo)){
            int membershipDiscountAmount = (int)(amount * MEMBERSHIP_DISCOUNT.discountRate);
            return Math.min(membershipDiscountAmount, MEMBERSHIP_DISCOUNT.maximumLimit);
        }
        return NO_DISCOUNT.maximumLimit;
    }
}
