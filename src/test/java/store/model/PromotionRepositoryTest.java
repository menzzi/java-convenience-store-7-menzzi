package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Promotion;
import store.model.repository.PromotionRepository;

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
        assertEquals("탄산2+1", promotions.getFirst().getName());
        assertEquals(2, promotions.getFirst().getBuy());
        assertEquals(1, promotions.getFirst().getGet());
        assertEquals("2024-01-01", promotions.getFirst().getStart_date());
        assertEquals("2024-12-31", promotions.getFirst().getEnd_date());
    }
}
