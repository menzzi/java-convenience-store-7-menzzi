package store.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Promotion;

public class PromotionRepositoryTest {
    private PromotionRepository promotionRepository;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepository();
    }

    @Test
    void 파일을_읽어와_프로모션을_저장한다() throws IOException {
        promotionRepository.updatePromotionFromFile("src/main/resources/promotions.md");

        List<Promotion> promotions = promotionRepository.getAllPromotions();
        assertEquals(3, promotions.size());
        assertEquals("탄산2+1", promotions.getFirst().name());
        assertEquals(2, promotions.getFirst().buy());
        assertEquals(1, promotions.getFirst().get());
        assertEquals("2024-01-01", promotions.getFirst().start_date());
        assertEquals("2024-12-31", promotions.getFirst().end_date());
    }
}
