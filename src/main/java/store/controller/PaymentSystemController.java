package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.model.domain.MembershipDiscount;
import store.model.domain.Promotion;
import store.model.domain.PromotionResult;
import store.model.domain.PromotionStatus;
import store.model.domain.Receipt;
import store.model.domain.ReceiptItem;
import store.model.domain.Stock;
import store.model.repository.PromotionRepository;
import store.model.repository.StockRepository;
import store.service.PromotionService;
import store.service.StockService;
import store.util.StringParser;
import store.view.InputView;
import store.view.OutputView;

public class PaymentSystemController {
    private final InputView input;
    private final OutputView output;
    private List<Stock> stocks;
    private List<Promotion> promotions;
    private StockService stockService;
    private PromotionService promotionService;

    private static final String INVALIDATE_AVAILABLE_STOCK_MESSAGE = " 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    public PaymentSystemController(InputView input, OutputView output) {
        this.input = input;
        this.output = output;
    }

    private void clearStock() {
        stocks.clear();
        promotions.clear();
    }

    public void run() {
        StockRepository stockRepository = new StockRepository();
        stockService = new StockService(stockRepository);
        stocks = stockService.readStocks("src/main/resources/products.md");

        PromotionRepository promotionRepository = new PromotionRepository();
        promotionService = new PromotionService(promotionRepository);
        promotions = promotionService.readPromotions("src/main/resources/promotions.md");

        order();
        clearStock();
    }

    private void order() {
        output.printWelcomeGreeting();
        printStocks(stocks);
        inputAndProceedOrder();
    }

    private void printStocks(List<Stock> stocks) {
        for (Stock stock : stocks) {
            output.printStockInformation(stock.toString());
        }
        System.out.println();
    }

    private void inputAndProceedOrder() {
        try {
            processOrder(StringParser.validateOrderFormat(input.inputOrder()));
        } catch (IllegalArgumentException e) {
            output.printErrorMessage(e.getMessage());
            inputAndProceedOrder();
        }
        output.printInstructionsAboutAdditionalPurchase();
        if (!inputYesOrNo().equals("Y")) return;
        order();
    }

    private String inputYesOrNo() {
        try {
            return input.inputYesOrNo();
        } catch (IllegalArgumentException e) {
            output.printErrorMessage(e.getMessage());
            return inputYesOrNo();
        }
    }

    private void processOrder(Map<String, Integer> orders) {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        List<ReceiptItem> freeGift = new ArrayList<>();

        for (String stockName : orders.keySet()) {
            int quantity = orders.get(stockName);
            List<Stock> sameNameStock = stockService.findStockByName(stocks, stockName, quantity);
            applyPromotionPriority(receiptItems, freeGift, sameNameStock, quantity);
        }
        String membershipStatus = askMembership();
        printReceipt(receiptItems, freeGift, membershipStatus);
    }

    private String askMembership() {
        output.printInstructionsAboutMembership();
        return inputYesOrNo();
    }

    private void applyPromotionPriority(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift,
                                       List<Stock> sameNameStock, int quantity) {
        if (sameNameStock.size() == 1) {
            purchaseGeneralProduct(receiptItems, sameNameStock.getFirst(), quantity);
            return;
        }
        Stock promotionStock = sameNameStock.getFirst();
        int remainQuantity = applyPromotionStock(receiptItems, freeGift, promotionStock, quantity);

        Stock generalStock = sameNameStock.get(1);
        handlingRemainingQuantity(remainQuantity, quantity, receiptItems, generalStock);
    }

    private void handlingRemainingQuantity(int remainQuantity, int quantity, List<ReceiptItem> receiptItems,
                                           Stock generalStock) {
        if (remainQuantity != 0) {
            if (remainQuantity == quantity) {
                purchaseGeneralProduct(receiptItems, generalStock, remainQuantity);
                return;
            }
            generalStock.decreaseQuantity(remainQuantity);
        }
    }

    private void purchaseGeneralProduct(List<ReceiptItem> receiptItems, Stock stock, int quantity) {
        if (quantity > stock.getQuantity()) {
            throw new IllegalArgumentException(INVALIDATE_AVAILABLE_STOCK_MESSAGE);
        }
        receiptItems.add(new ReceiptItem(stock.getName(), quantity, stock.getPrice()));
        stock.decreaseQuantity(quantity);
    }

    private int applyPromotionStock(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift, Stock promotionStock,
                                   int quantity) {
        PromotionResult promotionResult = promotionService.applyPromotion(promotions, promotionStock.getPromotion(),
                promotionStock.getQuantity(), quantity);
        if (promotionResult.getStatus() == PromotionStatus.EXPIRED) {
            return quantity;
        }
        int actualNumberOfPurchases = applyPromotionResult(promotionResult, receiptItems, freeGift, promotionStock,
                quantity);
        return deductPromotionStock(actualNumberOfPurchases, promotionStock, quantity);
    }

    private int deductPromotionStock(int actualNumberOfPurchases, Stock promotionStock, int remainQuantity) {
        if (actualNumberOfPurchases > promotionStock.getQuantity()) {
            remainQuantity = actualNumberOfPurchases - promotionStock.getQuantity();
            promotionStock.decreaseQuantity(promotionStock.getQuantity());
            return remainQuantity;
        }
        promotionStock.decreaseQuantity(actualNumberOfPurchases);
        return 0;
    }

    private int applyPromotionResult(PromotionResult promotionResult, List<ReceiptItem> receiptItems,
                                    List<ReceiptItem> freeGift, Stock stock, int quantity) {
        if (promotionResult.getStatus() == PromotionStatus.ADDITIONAL) {
            return addPromotionProduct(promotionResult, receiptItems, freeGift, stock);
        }
        if (promotionResult.getStatus() == PromotionStatus.GIVE_UP) {
            return givingUpPromotionProduct(promotionResult, receiptItems, freeGift, stock);
        }
        return purchaseByApplyingPromotion(receiptItems, freeGift, stock, promotionResult.getCurrentQuantity());
    }

    private int purchaseByApplyingPromotion(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift, Stock stock,
                                            int quantity) {
        receiptItems.add(new ReceiptItem(stock.getName(), quantity, stock.getPrice()));
        addFreeGift(freeGift, stock, quantity);
        return quantity;
    }

    private int addPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,
                                    List<ReceiptItem> freeGift, Stock stock) {
        output.printInstructionsAboutAddProduct(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = inputYesOrNo();
        if (userInput.equals("Y")) {
            purchaseByApplyingPromotion(receiptItems, freeGift, stock, promotionResult.getTotalAddQuantity());
            return promotionResult.getTotalAddQuantity();
        }
        purchaseByApplyingPromotion(receiptItems, freeGift, stock, promotionResult.getCurrentQuantity());
        return promotionResult.getCurrentQuantity();
    }

    private int givingUpPromotionProduct(PromotionResult promotionResult, List<ReceiptItem> receiptItems,
                                         List<ReceiptItem> freeGift, Stock stock) {
        output.printInstructionsAboutUnavailablePromotion(stock.getName(), promotionResult.getRelateQuantity());
        String userInput = inputYesOrNo();
        if (userInput.equals("Y")) {
            purchaseByApplyingPromotion(receiptItems, freeGift, stock, promotionResult.getCurrentQuantity());
            return promotionResult.getCurrentQuantity();
        }
        purchaseByApplyingPromotion(receiptItems, freeGift, stock, promotionResult.getTotalMinusQuantity());
        return promotionResult.getTotalMinusQuantity();
    }

    private void addFreeGift(List<ReceiptItem> freeGift, Stock stock, int quantity) {
        if (stock.getPromotion().matches(".*2\\+1.*")) { // 시간 되면 수정
            addFreeGiftCommonPart(freeGift,stock,quantity,3);
            return;
        }
        addFreeGiftCommonPart(freeGift,stock,quantity,2);
    }

    private void addFreeGiftCommonPart(List<ReceiptItem> freeGift, Stock stock, int quantity, int dividingNumber){
        if (quantity > stock.getQuantity()) {
            addFreeGiftRealPart(freeGift, stock, stock.getQuantity(), dividingNumber);
            return;
        }
        addFreeGiftRealPart(freeGift, stock, quantity, dividingNumber);
    }

    private void addFreeGiftRealPart(List<ReceiptItem> freeGift, Stock stock, int quantity, int dividingNumber) {
        int freeQuantity = quantity / dividingNumber;
        if (freeQuantity > 0) {
            freeGift.add(new ReceiptItem(stock.getName(), freeQuantity, stock.getPrice()));
        }
    }

    private int calculateMembershipAmount(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGift,
                                          String membershipStatus) {
        int total = 0;
        for (ReceiptItem receiptItem : receiptItems) {
            boolean isFreeGift = freeGift.stream()
                    .anyMatch(gift -> gift.getItemName().equals(receiptItem.getItemName()));
            if (!isFreeGift) {
                total += receiptItem.getTotalPrice();
            }
        }
        return MembershipDiscount.applyMembershipDiscount(membershipStatus, total);
    }

    private void printReceipt(List<ReceiptItem> receiptItems, List<ReceiptItem> freeGifts, String membershipStatus) {
        Receipt receipt = new Receipt(receiptItems, freeGifts, membershipStatus);
        output.printPurchaseInformation(receiptItems);
        calculate(receipt);
    }

    private void calculate(Receipt receipt) {
        int totalPromotionAmount = 0;

        if (!receipt.freeGifts().isEmpty()) {
            output.printPromotionInfomation(receipt.freeGifts());
            totalPromotionAmount = receipt.getTotalPromotionAmount();
        }
        int totalAmount = receipt.getTotalAmount();
        int membershipAmount = calculateMembershipAmount(receipt.receiptItems(), receipt.freeGifts(), receipt.membershipDiscount());
        int totalMoneyToBePaid = totalAmount - totalPromotionAmount - membershipAmount;
        output.printMoneyInformation(totalAmount, receipt.getTotalQuantity(), totalPromotionAmount, membershipAmount,
                totalMoneyToBePaid);
    }
}