package store.model.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import store.model.domain.Promotion;
import store.model.domain.PromotionStatus;
import store.model.repository.PromotionRepository;

public class PromotionService {
    private static PromotionRepository promotionRepository;

    public PromotionStatus findPromotionStatus(String start_date, String end_date){
        LocalDateTime now_date = DateTimes.now();

        if(now_date.isAfter(LocalDate.parse(start_date).atStartOfDay()) && now_date.isBefore(
                LocalDate.parse(end_date).atTime(LocalTime.MAX))){
            return PromotionStatus.ONGOING;
        }

        return PromotionStatus.EXPIRED;
    }

    public Promotion findPromotionByName(String promotionName){
        List<Promotion> promotions = promotionRepository.getAllPromotions();
        for(Promotion promotion : promotions){
            if(promotion.getName().equals(promotionName)){
                return promotion;
            }
        }
        return null;
    }
}
