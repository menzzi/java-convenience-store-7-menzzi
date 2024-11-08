package store.model.service;

import java.util.ArrayList;
import java.util.List;
import store.model.domain.Stock;
import store.model.repository.StockRepository;

public class StockService {
    private static StockRepository stockRepository;
    private static final String INVALIDATE_EXISTENT_STOCK_MESSAGE = " 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String INVALIDATE_AVAILABLE_STOCK_MESSAGE = " 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";


    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public static List<Stock> readStocks(String filePath){
        stockRepository.updateStockFromFile(filePath);
        return stockRepository.getAllStocks();
    }

    public static List<Stock> findStockByName(List<Stock> stocks, String stockName, int quantity){
        List<Stock> sameNameStock = new ArrayList<>();
        for(Stock stock : stocks){
            if(stock.getName().equals(stockName)){
                sameNameStock.add(stock);
            }
        }
        validateExistentStock(sameNameStock);
        validateAvailableStock(sameNameStock,quantity);
        return sameNameStock;
    }

    private static void validateExistentStock(List<Stock> sameNameStock) {
        if(sameNameStock.isEmpty()){
            throw new IllegalArgumentException(INVALIDATE_EXISTENT_STOCK_MESSAGE);
        }
    }

    private static void validateAvailableStock(List<Stock> sameNameStock, int quantity) {
        int totalQuantity = 0;
        for(Stock stock : sameNameStock){
            totalQuantity = stock.getQuantity();
        }
        if(quantity > totalQuantity){
            throw new IllegalArgumentException(INVALIDATE_AVAILABLE_STOCK_MESSAGE);
        }
    }
}
