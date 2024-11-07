package store.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringParser {
    private static final String VALIDATE_FORMAT_MESSAGE = " 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public static Map<String,Integer> validateOrderFormat(String input){
        List<String> inputs = Arrays.stream(input.split(",")).toList();
        Map<String,Integer> orders = new HashMap<>();
        for(String orderString : inputs){
            String order = removeSquareBrackets(orderString);
            validateHyphenFormat(order);
            String[] removeHyphen = order.split("-");
            validateNumeric(removeHyphen[1]);
            orders.put(removeHyphen[0],Integer.parseInt(removeHyphen[1]));
        }
        return orders;
    }

    private static String removeSquareBrackets(String order){
        if(!order.startsWith("[")){
            throw new IllegalArgumentException(VALIDATE_FORMAT_MESSAGE);
        }

        if(order.charAt(order.length()-1) != ']'){
            throw new IllegalArgumentException(VALIDATE_FORMAT_MESSAGE);
        }
        return order.substring(1,order.length()-1);
    }

    private static void validateHyphenFormat(String input){
        String regex = "^[0-9\\-가-힣]*$";
        if(input.contains(regex)){
            throw new IllegalArgumentException(VALIDATE_FORMAT_MESSAGE);
        }
    }

    private static void validateNumeric(String number){
        try{
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(VALIDATE_FORMAT_MESSAGE);
        }
    }
}
