package store.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Stock;

public class StockRepositoryTest {
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository = new StockRepository();
    }

    @Test
    void 파일을_읽어와_재고_저장한다() throws IOException {
        stockRepository.updateStockFromFile("src/main/resources/products.md");

        List<Stock> stocks = stockRepository.getAllStocks();
        assertEquals(18, stocks.size());
        assertEquals("콜라", stocks.getFirst().getName());
        assertEquals(1000, stocks.getFirst().getPrice());
        assertEquals(10, stocks.getFirst().getQuantity());
        assertEquals("탄산2+1", stocks.getFirst().getPromotion());

        assertEquals("컵라면", stocks.getLast().getName());
        assertEquals(1700, stocks.getLast().getPrice());
        assertEquals(10, stocks.getLast().getQuantity());
        assertEquals("null", stocks.getLast().getPromotion());
    }
}
