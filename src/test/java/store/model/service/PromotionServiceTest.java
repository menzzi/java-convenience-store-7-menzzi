package store.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Promotion;
import store.model.domain.PromotionResult;
import store.service.PromotionService;

public class PromotionServiceTest {
    private PromotionService promotionService;

    @BeforeEach
    public void setup() {
        promotionService = new PromotionService(null); // Repository는 테스트에서 사용하지 않음
    }

    @Test
    public void 일치하는_프로모션을_찾는다() {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2,1,"2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1,1,"2024-11-01", "2024-11-30")
        );

        Promotion result = promotionService.findPromotionByName(promotions, "반짝할인");

        assertNotNull(result);
        assertEquals("반짝할인", result.getName());
    }

    @Test
    public void 투플러스원_프로모션_적용1() {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2,1,"2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1,1,"2024-11-01", "2024-11-30")
        );

        PromotionResult result = promotionService.applyPromotion(promotions, "2+1", 6, 4);

        assertEquals("추가", result.getMessage());
        assertEquals(4,result.getCurrentQuantity());
        assertEquals(2,result.getRelateQuantity());
    }

    @Test
    public void 투플러스원_프로모션_적용2() {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2,1,"2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1,1,"2024-11-01", "2024-11-30")
        );

        PromotionResult result = promotionService.applyPromotion(promotions, "2+1", 5, 4);

        assertEquals("포기", result.getMessage());
        assertEquals(4,result.getCurrentQuantity());
        assertEquals(1,result.getRelateQuantity());
    }
}
