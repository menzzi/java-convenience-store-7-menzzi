package store.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MembershipDiscountTest {

    @Test
    public void 멤버십_할인률을_구한다() {
        String membershipStatus = "Y";
        int amount = 5000;

        int discount = MembershipDiscount.applyMembershipDiscount(membershipStatus, amount);

        int expectedDiscount = (int) (5000 * 0.3);
        assertEquals(expectedDiscount, discount);
    }

    @Test
    public void 멤버십_할인_한도를_적용한다() {
        String membershipStatus = "Y";
        int amount = 40000;

        int discount = MembershipDiscount.applyMembershipDiscount(membershipStatus, amount);

        int expectedDiscount = 8000; // 최대 한도 8,000원
        assertEquals(expectedDiscount, discount);
    }

    @Test
    public void 멤버십_할인을_적용하지_않는다() {
        String membershipStatus = "N";
        int amount = 5000;

        int discount = MembershipDiscount.applyMembershipDiscount(membershipStatus, amount);

        assertEquals(0, discount);
    }
}
