package store.controller;

import java.util.List;
import java.util.Map;
import store.model.domain.PromotionResult;
import store.model.domain.Stock;
import store.model.service.PromotionService;
import store.model.service.StockService;
import store.util.StringParser;
import store.view.InputView;
import store.view.OutputView;

public class PaymentSystemController {
    private final InputView input;
    private final OutputView output;
    private List<Stock> stocks;

    public PaymentSystemController(InputView input, OutputView output) {
        this.input = input;
        this.output = output;
    }

    public void run(){
        stocks = StockService.readStocks("src/main/resources/products.md");
    }

    private void order(){
       output. printWelcomeGreeting();
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
            List<Stock> sameNameStock = StockService.findStockByName(stocks,stockName,quantity);


            // 프로모션이랑 List<Stock> 가지고 프로모션 서비스(프로모션 우선 적용 구현)랑 상호작용해서 최종 수량 가져오면
            // 재고서비스에서 수량 업데이트하는 메서드 구현
        }
    }

    public void printStocks(List<Stock> stocks){
        for(Stock stock : stocks){
            output.printStockInformation(stock.toString());
        }
    }

    public void applyPromotionPriority(List<Stock> sameNameStock, int quantity){
        int remainQuantity = quantity;
        for(Stock stock : sameNameStock){
            if(!stock.getPromotion().equals("null")){
                PromotionResult promotionResult = PromotionService.applyPromotion(stock.getPromotion(),stock.getQuantity(),quantity);

            }
            if(remainQuantity > stock.getQuantity()){
                // 수량초과
            }
            //remainQuantity 만큼 일반 상품 구매
        }
    }

    public void applyPromotion(PromotionResult promotionResult){
        if(promotionResult.getMessage().equals("만료")){
            // 만료됨.. 일반 상품 개수랑 비교해서 초과면 초과 출력 아니면 일반에서 제거
        }
        if(promotionResult.getMessage().equals("추가")){
            // 메세지 출력 & 추가안하면 그냥 손해보게 계산, 추가하면 추가한거 반영
        }
        if(promotionResult.getMessage().equals("포기")){
            //메세지 출력 & 포기하면 가능한 만큼만 구매하는 것으로 포기 안하면 프로모션 먼저 빼고 일반에서 빼고
        }
        // 할인적용해서 구매
    }
}
