package store.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.domain.Stock;
import store.model.repository.StockRepository;
import store.view.OutputView;

public class StockControllerTest {
    private StockRepository stockRepository;
    private OutputView output;
    private StockController stockController;

    @BeforeEach
    void setUp() {
        stockRepository = new StockRepository();
        output = new OutputView();
        stockController = new StockController(stockRepository,output);
    }

    @Test
    void 재고를_읽어오고_출력형식에_맞춰_출력한다(){
        stockController.readAndPrintStocks("src/main/resources/products.md");
        List<Stock> stocks = stockRepository.getAllStocks();

        assertThat(stocks.get(0).toString().equals("- 콜라 1,000원 10개 탄산2+1"));
        assertThat(stocks.get(1).toString().equals("- 콜라 1,000원 10개"));
    }
}
