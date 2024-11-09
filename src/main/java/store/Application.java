package store;

import store.controller.PaymentSystemController;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        PaymentSystemController paymentSystemController = new PaymentSystemController(inputView, outputView);
        paymentSystemController.run();
    }
}
