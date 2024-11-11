package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.domain.Promotion;
import store.model.domain.PromotionResult;

public class PromotionServiceTest {
    private PromotionService promotionService;

    @BeforeEach
    public void setup() {
        promotionService = new PromotionService(null); // Repository는 테스트에서 사용하지 않음
    }

    @Test
    public void 일치하는_프로모션을_찾는다() {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2, 1, "2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1, 1, "2024-11-01", "2024-11-30")
        );

        Promotion result = promotionService.findPromotionByName(promotions, "반짝할인");

        assertNotNull(result);
        assertEquals("반짝할인", result.name());
    }

    @ParameterizedTest
    @CsvSource({
            "2+1, 6, 4, 추가, 4, 2",
            "2+1, 5, 4, 포기, 4, 1",
            "2+1, 7, 10, 포기, 10, 4"
    })
    public void 투플러스원_프로모션_적용(String promotionName, int stockQuantity, int quantity, String expected1, int expected2,
                               int expected3) {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2, 1, "2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1, 1, "2024-11-01", "2024-11-30")
        );

        PromotionResult result = promotionService.applyPromotion(promotions, promotionName, stockQuantity, quantity);

        assertEquals(expected1, result.getStatus().getStatusMessage());
        assertEquals(expected2, result.getCurrentQuantity());
        assertEquals(expected3, result.getRelateQuantity());
    }

    @Test
    public void 원플러스원_프로모션_적용() {
        List<Promotion> promotions = Arrays.asList(
                new Promotion("2+1", 2, 1, "2024-11-01", "2024-12-31"),
                new Promotion("반짝할인", 1, 1, "2024-11-01", "2024-11-30")
        );

        PromotionResult result = promotionService.applyPromotion(promotions, "반짝할인", 5, 3);

        assertEquals("추가", result.getStatus().getStatusMessage());
        assertEquals(3, result.getCurrentQuantity());
        assertEquals(1, result.getRelateQuantity());
    }
}
