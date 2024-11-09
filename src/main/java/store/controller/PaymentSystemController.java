package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.model.domain.MembershipDiscount;
import store.model.domain.PromotionResult;
import store.model.domain.Receipt;
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

        order();
    }

    private void order(){
       output. printWelcomeGreeting();
       printStocks(stocks);
       inputAndProceedOrder();
    }

    private void inputAndProceedOrder(){
        try{
            processOrder(StringParser.validateOrderFormat(input.inputOrder()));
        }catch (IllegalArgumentException e){
            output.printErrorMessage(e.getMessage());
            inputAndProceedOrder();
        }
        output.printInstructionsAboutAdditionalPurchase();
        String continueOrder = inputYesOrNo();
        if (continueOrder.equals("Y")) {
            order();
        }
    }

    private String inputYesOrNo(){
        try{
            return input.inputYesOrNo();
        }catch (IllegalArgumentException e){
            output.printErrorMessage(e.getMessage());
            return inputYesOrNo();
        }
    }

    public void processOrder(Map<String, Integer> orders) {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        List<ReceiptItem> freeGift = new ArrayList<>();

        for (String stockName : orders.keySet()) {
            int quantity = orders.get(stockName);
            List<Stock> sameNameStock = StockService.findStockByName(stocks,stockName,quantity);
            applyPromotionPriority(receiptItems,freeGift,sameNameStock,quantity);
        }
        String membershipStatus = askMembership(receiptItems,freeGift);
        printReceipt(receiptItems,freeGift,membershipStatus);
    }

    public void printStocks(List<Stock> stocks){
        for(Stock stock : stocks){
            output.printStockInformation(stock.toString());
        }
    }

    public void applyPromotionPriority(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,List<Stock> sameNameStock, int quantity){
        if(sameNameStock.size()==1){
            purchaseGeneralProduct(receiptItems,sameNameStock.getFirst(),quantity);
            return;
        }
        int remainQuantity = quantity;
        boolean isRemain = false;
        for(Stock stock : sameNameStock){
            if(!stock.getPromotion().equals("null")){
                PromotionResult promotionResult = PromotionService.applyPromotion(stock.getPromotion(),stock.getQuantity(),quantity);
                if(promotionResult.getMessage().equals("만료")) {
                    isRemain = true;
                    continue;
                }
                int ActualNumberOfPurchases = applyPromotionResult(promotionResult, receiptItems, freeGift, stock, quantity);
                if(ActualNumberOfPurchases > stock.getQuantity()){
                    stock.decreaseQuantity(stock.getQuantity());
                    remainQuantity = applyPromotionResult(promotionResult, receiptItems, freeGift, stock, quantity) - stock.getQuantity();
                    continue;
                }
                stock.decreaseQuantity(ActualNumberOfPurchases);
            }
            if(isRemain){
                remainQuantity -= purchaseGeneralProduct(receiptItems,stock,quantity);
            }
            if(remainQuantity != 0){
                stock.decreaseQuantity(remainQuantity);
            }
        }
    }

    public int purchaseGeneralProduct(List<ReceiptItem> receiptItems, Stock stock,int quantity){
        if(quantity > stock.getQuantity()){
            throw new IllegalArgumentException(INVALIDATE_AVAILABLE_STOCK_MESSAGE);
        }
        receiptItems.add(new ReceiptItem(stock.getName(),stock.getPrice(),quantity));
        stock.decreaseQuantity(quantity);
        return quantity;
    }

    public int applyPromotionResult(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock, int quantity){
        if(promotionResult.getMessage().equals("추가")){
            return addPromotionProduct(promotionResult,receiptItems,freeGift,stock);
        }
        if(promotionResult.getMessage().equals("포기")){
            return givingUpPromotionProduct(promotionResult,receiptItems,freeGift,stock);
        }
       return purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
    }

    private int purchaseByApplyingPromotion(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift, Stock stock, int quantity){
        receiptItems.add(new ReceiptItem(stock.getName(),quantity,stock.getPrice()));
        addFreeGift(freeGift,stock,quantity);
        return quantity;
    }

    private int addPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock){
        output.printInstructionsAboutAddProduct(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = inputYesOrNo();
        if(userInput.equals("Y")){
            purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getTotalAddQuantity());
            return promotionResult.getTotalAddQuantity();
        }
        purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
        return promotionResult.getCurrentQuantity();
    }

    private int givingUpPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,Stock stock){
        output.printInstructionsAboutUnavailablePromotion(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = inputYesOrNo();
        if(userInput.equals("Y")){
            purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getCurrentQuantity());
            return promotionResult.getCurrentQuantity();
        }
        purchaseByApplyingPromotion(receiptItems,freeGift,stock,promotionResult.getTotalMinusQuantity());
        return promotionResult.getTotalMinusQuantity();
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

    private String askMembership(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift){
        if(receiptItems.size() > freeGift.size()){
            output.printInstructionsAboutMembership();
            return inputYesOrNo();
        }
        return "N";
    }

    private int calculateMembershipAmount(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,String membershipStatus){
        int total = 0;
        for (ReceiptItem receiptItem : receiptItems) {
            boolean isFreeGift = freeGift.stream()
                    .anyMatch(gift -> gift.getItemName().equals(receiptItem.getItemName()));
            if (!isFreeGift) {
                total += receiptItem.getTotalPrice();
            }
        }
        return MembershipDiscount.applyMembershipDiscount(membershipStatus,total);
    }

    private void printReceipt(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,String membershipStatus){
        Receipt receipt = new Receipt(receiptItems,freeGift,membershipStatus);
        output.printPurchaseInformation(receiptItems);
        calculate(receiptItems,freeGift,membershipStatus,receipt);
    }

    private void calculate(List<ReceiptItem> receiptItems,List<ReceiptItem> freeGift,String membershipStatus,Receipt receipt){
        int totalPromotionAmount = 0;
        if(!freeGift.isEmpty()){
            output.printPromotionInfomation(freeGift);
            totalPromotionAmount = receipt.getTotalPromotionAmount();
        }
        int totalAmount = receipt.getTotalAmount();
        int membershipAmount = calculateMembershipAmount(receiptItems,freeGift,membershipStatus);
        int totalMoneyToBePaid = totalAmount - totalPromotionAmount - membershipAmount;
        output.printMoneyInformation(totalAmount,receipt.getTotalQuantity(),totalPromotionAmount,membershipAmount,totalMoneyToBePaid);
    }
}
