package store.model.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import store.model.domain.Promotion;
import store.model.domain.PromotionResult;
import store.model.repository.PromotionRepository;

public class PromotionService {
    private static PromotionRepository promotionRepository;

    public static boolean findPromotionStatus(String start_date, String end_date){
        LocalDateTime now_date = DateTimes.now();

        return now_date.isAfter(LocalDate.parse(start_date).atStartOfDay()) && now_date.isBefore(
                LocalDate.parse(end_date).atTime(LocalTime.MAX));
    }

    public static Promotion findPromotionByName(String promotionName){
        List<Promotion> promotions = promotionRepository.getAllPromotions();
        for(Promotion promotion : promotions){
            if(promotion.getName().equals(promotionName)){
                return promotion;
            }
        }
        return null;
    }

    public static PromotionResult applyPromotion(String promotionName, int stockQuantity, int quantity){
        Promotion promotion = findPromotionByName(promotionName);
        if(findPromotionStatus(promotion.getStart_date(),promotion.getEnd_date())){

            if(promotion.getGet()==2 && promotion.getBuy()==1){
                return calculateAvailableQuantityTwoPlusOne(stockQuantity,quantity);
            }
            return calculateAvailableQuantityOnePlusOne(stockQuantity,quantity);
        }
        return new PromotionResult("만료",0,0);
    }

    public static PromotionResult calculateAvailableQuantityTwoPlusOne(int stockQuantity, int quantity){
        int remainder = stockQuantity % 3;
        if(stockQuantity >= quantity){
            return checkRemainderTwoPlusOne(stockQuantity,quantity,remainder);
        }
        int applyQuantity = (stockQuantity/3) * 3;
        return new PromotionResult("초과", applyQuantity,stockQuantity-applyQuantity);
    }

    public static PromotionResult calculateAvailableQuantityOnePlusOne(int stockQuantity, int quantity){
        int remainder = stockQuantity % 2;
        if(stockQuantity >= quantity){
            if(remainder == 0){
                return new PromotionResult("",quantity,0);
            }
            return checkAdditionalAvailabilityOne(stockQuantity,quantity,false);
        }
        int applyQuantity = (stockQuantity/2) * 2;
        return new PromotionResult("초과", applyQuantity,stockQuantity-applyQuantity);
    }

    public static PromotionResult checkRemainderTwoPlusOne(int stockQuantity,int quantity, int remainder){
        if(remainder == 0){
            return new PromotionResult("",quantity, 0);
        }
        if(remainder == 1){
            return checkAdditionalAvailabilityTwo(stockQuantity,quantity);
        }
        return checkAdditionalAvailabilityOne(stockQuantity,quantity,true);
    }

    public static PromotionResult checkAdditionalAvailabilityTwo(int stockQuantity,int quantity){
        if(stockQuantity >= quantity+2){
            return new PromotionResult("추가",quantity, 2);
        }
        return new PromotionResult("포기",quantity, 1);
    }

    public static PromotionResult checkAdditionalAvailabilityOne(int stockQuantity, int quantity, boolean isTwoPlusOne){
        if(stockQuantity >= quantity+1){
            return new PromotionResult("추가",quantity, 1);
        }
        if(isTwoPlusOne){
            return new PromotionResult("포기",quantity, 2);
        }
        return new PromotionResult("포기",quantity, 1);
    }

}
