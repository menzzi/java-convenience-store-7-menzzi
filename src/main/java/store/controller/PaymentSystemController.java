package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.model.domain.PromotionResult;
import store.model.domain.ReceiptItem;
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

    private static final String INVALIDATE_AVAILABLE_STOCK_MESSAGE = " 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";


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
        List<ReceiptItem> receiptItems = new ArrayList<>();
        List<ReceiptItem> freeGift = new ArrayList<>();

        for (String stockName : orders.keySet()) {
            int quantity = orders.get(stockName);
            List<Stock> sameNameStock = StockService.findStockByName(stocks,stockName,quantity);
            applyPromotionPriority(receiptItems,freeGift,sameNameStock,quantity);
            // 프로모션이랑 List<Stock> 가지고 프로모션 서비스(프로모션 우선 적용 구현)랑 상호작용해서 최종 수량 가져오면
            // 재고서비스에서 수량 업데이트하는 메서드 구현
        }
    }

    public void printStocks(List<Stock> stocks){
        for(Stock stock : stocks){
            output.printStockInformation(stock.toString());
        }
    }

    public void applyPromotionPriority(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,List<Stock> sameNameStock, int quantity){
        int remainQuantity = quantity;
        if(sameNameStock.size()==1){
            purchaseGeneralProduct(receiptItems,sameNameStock.getFirst(),quantity);
            return;
        }
        for(Stock stock : sameNameStock){
            if(!stock.getPromotion().equals("null")){
                PromotionResult promotionResult = PromotionService.applyPromotion(stock.getPromotion(),stock.getQuantity(),quantity);
                applyPromotionResult(promotionResult,receiptItems,freeGift,stock,quantity);
            }
            purchaseGeneralProduct(receiptItems,stock,remainQuantity); //remainQuantity 만큼 일반 상품 구매
            // 재고 업데이트
        }
    }

    public void purchaseGeneralProduct(List<ReceiptItem> receiptItems, Stock stock,int quantity){
        if(quantity > stock.getQuantity()){
            throw new IllegalArgumentException(INVALIDATE_AVAILABLE_STOCK_MESSAGE);
        }
        receiptItems.add(new ReceiptItem(stock.getName(),stock.getPrice(),quantity));
        // 재고 업데이트
    }

    public void applyPromotionResult(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock, int quantity){
        if(promotionResult.getMessage().equals("만료")){
            purchaseGeneralProduct(receiptItems,stock,quantity);
        }
        if(promotionResult.getMessage().equals("추가")){
            addPromotionProduct(promotionResult,receiptItems,freeGift,stock);
        }
        if(promotionResult.getMessage().equals("포기")){
            givingUpPromotionProduct(promotionResult,receiptItems,freeGift,stock);
        }
        purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
    }

    private void purchaseByApplyingPromotion(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift, Stock stock, int quantity){
        receiptItems.add(new ReceiptItem(stock.getName(),quantity,stock.getPrice()));
        //재고 업데이트 (프로모션 상품 업데이트)
        addFreeGift(freeGift,stock,quantity);
    }

    private void addPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock){
        output.printInstructionsAboutAddProduct(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = input.inputYesOrNo();
        if(userInput.equals("Y")){
            purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getTotalAddQuantity());
        }
        purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
    }

    private void givingUpPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock){
        output.printInstructionsAboutUnavailablePromotion(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = input.inputYesOrNo();
        if(userInput.equals("Y")){
            purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
        }
        purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getTotalMinusQuantity());
    }

    private void addFreeGift(List<ReceiptItem> freeGift, Stock stock, int quantity){
        if(stock.getPromotion().contains("2+1")){ // 시간 되면 수정
            int freeQuantity = quantity / 3;
            if(freeQuantity > 0){
                freeGift.add(new ReceiptItem(stock.getName(),freeQuantity,stock.getPrice()));
            }
        }
        int freeQuantity = quantity / 2;
        if(freeQuantity > 0) {
            freeGift.add(new ReceiptItem(stock.getName(), freeQuantity, stock.getPrice()));
        }
    }
}
