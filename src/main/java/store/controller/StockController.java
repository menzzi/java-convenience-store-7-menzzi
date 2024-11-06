package store.controller;

import java.util.List;
import store.model.domain.Stock;
import store.model.repository.StockRepository;
import store.view.OutputView;

public class StockController {
    private final StockRepository stockRepository;
    private final OutputView output;

    public StockController(StockRepository stockRepository,OutputView output){
        this.stockRepository = stockRepository;
        this.output = output;
    }

    public void readAndPrintStocks(String filePath){
        stockRepository.updateStockFromFile(filePath);

        List<Stock> stocks = stockRepository.getAllStocks();
        output.printWelcomeGreeting();
        for(Stock stock: stocks){
            output.printStockInformation(stock.toString());
        }
    }
}
