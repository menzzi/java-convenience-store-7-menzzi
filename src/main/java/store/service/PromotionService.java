package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import store.model.domain.Promotion;
import store.model.domain.PromotionResult;
import store.model.domain.PromotionStatus;
import store.model.repository.PromotionRepository;

public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> readPromotions(String filePath) {
        promotionRepository.updatePromotionFromFile(filePath);
        return promotionRepository.getAllPromotions();
    }

    public Promotion findPromotionByName(List<Promotion> promotions, String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.getName().equals(promotionName)) {
                return promotion;
            }
        }
        return null;
    }

    public PromotionResult applyPromotion(List<Promotion> promotions, String promotionName, int stockQuantity,
                                          int quantity) {
        Promotion promotion = findPromotionByName(promotions, promotionName);
        if (findPromotionStatus(promotion.getStart_date(), promotion.getEnd_date())) {

            if (promotion.getBuy() == 2 && promotion.getGet() == 1) {
                return calculateAvailableQuantity(stockQuantity, quantity,3);
            }
            return calculateAvailableQuantity(stockQuantity, quantity,2);
        }
        return new PromotionResult(PromotionStatus.EXPIRED, 0, 0);
    }

    private boolean findPromotionStatus(String start_date, String end_date) {
        LocalDateTime now_date = DateTimes.now();

        return now_date.isAfter(LocalDate.parse(start_date).atStartOfDay()) && now_date.isBefore(
                LocalDate.parse(end_date).atTime(LocalTime.MAX));
    }

    private PromotionResult calculateAvailableQuantity(int stockQuantity, int quantity, int dividingNumber) {
        int remainder = quantity % dividingNumber;
        if (stockQuantity >= quantity) {
            if(dividingNumber == 3){
                return calculateAvailableQuantityTwoPlusOne(stockQuantity, quantity, remainder);
            }
            return calculateAvailableQuantityOnePlusOne(stockQuantity, quantity, remainder);
        }
        int applyQuantity = (stockQuantity / dividingNumber) * dividingNumber;
        return new PromotionResult(PromotionStatus.GIVE_UP, quantity, quantity - applyQuantity);
    }

    private PromotionResult calculateAvailableQuantityTwoPlusOne(int stockQuantity, int quantity, int remainder) {
        if (remainder == 0) {
            return new PromotionResult(PromotionStatus.NONE, quantity, 0);
        }
        if (remainder == 1) {
            return checkAdditionalAvailabilityTwo(stockQuantity, quantity);
        }
        return checkAdditionalAvailabilityOne(stockQuantity, quantity, true);
    }

    private PromotionResult calculateAvailableQuantityOnePlusOne(int stockQuantity, int quantity, int remainder) {
        if (remainder == 0) {
            return new PromotionResult(PromotionStatus.NONE, quantity, 0);
        }
        return checkAdditionalAvailabilityOne(stockQuantity, quantity, false);
    }

    private PromotionResult checkAdditionalAvailabilityTwo(int stockQuantity, int quantity) {
        if (stockQuantity >= quantity + 2) {
            return new PromotionResult(PromotionStatus.ADDITIONAL, quantity, 2);
        }
        return new PromotionResult(PromotionStatus.GIVE_UP, quantity, 1);
    }

    private PromotionResult checkAdditionalAvailabilityOne(int stockQuantity, int quantity, boolean isTwoPlusOne) {
        if (stockQuantity >= quantity + 1) {
            return new PromotionResult(PromotionStatus.ADDITIONAL, quantity, 1);
        }
        if (isTwoPlusOne) {
            return new PromotionResult(PromotionStatus.GIVE_UP, quantity, 2);
        }
        return new PromotionResult(PromotionStatus.GIVE_UP, quantity, 1);
    }
}
