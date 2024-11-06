package store.view;

public class OutputView {
    public void printWelcomeGreeting(){
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printStockInformation(String stockDetails){
        System.out.println(stockDetails);
    }
}
