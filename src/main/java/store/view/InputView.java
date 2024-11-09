package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private final String ORDER_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private final String INVALIDATE_MESSAGE = " 잘못된 입력입니다. 다시 입력해 주세요.";


    public String inputOrder(){
        System.out.println(ORDER_MESSAGE);
        String input = Console.readLine();
        System.out.println();
        return input;
    }

    public String inputYesOrNo(){
        String input = Console.readLine();
        System.out.println();
        validateYesOrNo(input);
        return input;
    }

    private void validateYesOrNo(String input){
        if(!input.equals("Y") && !input.equals("N")){
            throw new IllegalArgumentException(INVALIDATE_MESSAGE);
        }
    }
}
