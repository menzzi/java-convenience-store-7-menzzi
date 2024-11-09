package store.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Stock;
import store.service.StockService;

public class StockServiceTest {
    private StockService stockService;

    @BeforeEach
    public void setup() {
        stockService = new StockService(null); // Repository는 테스트에서 사용하지 않음
    }
    @Test
    void 일치하는_이름_다_가져오기() {
        List<Stock> stockList = Arrays.asList(
                new Stock("콜라",1000,10,"2+1"),
                new Stock("콜라",1000,10,"null")
        );

        List<Stock> sameNameStocks = stockService.findStockByName(stockList, "콜라", 9);


        assertEquals("2+1",sameNameStocks.get(0).getPromotion());
        assertEquals("null",sameNameStocks.get(1).getPromotion());
    }
}
