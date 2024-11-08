package store.controller;

import java.util.List;
import java.util.Map;
import store.model.domain.Stock;
import store.model.service.StockService;
import store.util.StringParser;
import store.view.InputView;
import store.view.OutputView;

public class PaymentSystemController {
    private final InputView input;
    private final OutputView output;

    public PaymentSystemController(InputView input, OutputView output) {
        this.input = input;
        this.output = output;
    }

    public void run(){

    }

    private void order(){
       output. printWelcomeGreeting();
       List<Stock> stocks = StockService.readStocks("src/main/resources/products.md");
       printStocks(stocks);
       Map<String,Integer> orders = inputOrder();
       processOrder(orders);
    }

    private Map<String,Integer> inputOrder(){
        try{
            return StringParser.validateOrderFormat(input.inputOrder());
        }catch (IllegalArgumentException e){
            output.printErrorMessage(e.getMessage());
            return inputOrder();
        }
    }

    public void processOrder(Map<String, Integer> orders) {
        for (String stockName : orders.keySet()) {
            int quantity = orders.get(stockName);
            List<Stock> sameNameStock = StockService.findStockByName(stockName,quantity);
            // 프로모션이랑 List<Stock> 가지고 프로모션 서비스(프로모션 우선 적용 구현)랑 상호작용해서 최종 수량 가져오면
            // 재고서비스에서 수량 업데이트하는 메서드 구현
        }
    }

    public void printStocks(List<Stock> stocks){
        for(Stock stock : stocks){
            output.printStockInformation(stock.toString());
        }
    }
}
