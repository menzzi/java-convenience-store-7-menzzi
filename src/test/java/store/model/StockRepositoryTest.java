package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Stock;
import store.model.repository.StockRepository;

public class StockRepositoryTest {
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository = new StockRepository();
    }

    @Test
    void testUpdateStockFromFile() throws IOException {
        stockRepository.updateStockFromFile("src/main/resources/products.md");

        List<Stock> stocks = stockRepository.getAllStocks();
        assertEquals(16, stocks.size());
        assertEquals("콜라", stocks.get(0).getName());
        assertEquals(1000, stocks.get(0).getPrice());
        assertEquals(10, stocks.get(0).getQuantity());
        assertEquals("탄산2+1", stocks.get(0).getPromotion());

        assertEquals("콜라", stocks.get(1).getName());
        assertEquals(1000, stocks.get(1).getPrice());
        assertEquals(10, stocks.get(1).getQuantity());
        assertEquals("null", stocks.get(1).getPromotion());
    }
}
