package store.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Stock;

public class StockServiceTest {
    private StockService stockService;

    @BeforeEach
    public void setup() {
        stockService = new StockService(null);
    }

    @Test
    void 존재하지_않는_상품을_입력하면_예외가_발생한다() {
        List<Stock> stockList = Arrays.asList(
                new Stock("콜라", 1000, 10, "2+1"),
                new Stock("콜라", 1000, 10, "null")
        );

        assertThatThrownBy(() -> stockService.findStockByName(stockList, "사이다", 9))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 수량을_초과하면_예외가_발생한다() {
        List<Stock> stockList = Arrays.asList(
                new Stock("콜라", 1000, 5, "2+1"),
                new Stock("콜라", 1000, 5, "null")
        );

        assertThatThrownBy(() -> stockService.findStockByName(stockList, "콜라", 11))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 일치하는_이름_다_가져오기() {
        List<Stock> stockList = Arrays.asList(
                new Stock("콜라", 1000, 10, "2+1"),
                new Stock("콜라", 1000, 10, "null")
        );

        List<Stock> sameNameStocks = stockService.findStockByName(stockList, "콜라", 9);

        assertEquals("2+1", sameNameStocks.get(0).getPromotion());
        assertEquals("null", sameNameStocks.get(1).getPromotion());
    }
}
