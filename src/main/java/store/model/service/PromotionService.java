package store.model.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import store.model.domain.PromotionStatus;

public class PromotionService {
    public PromotionStatus findPromotionStatus(String start_date, String end_date){
        LocalDateTime now_date = DateTimes.now();

        if(now_date.isAfter(LocalDate.parse(start_date).atStartOfDay()) && now_date.isBefore(
                LocalDate.parse(end_date).atTime(LocalTime.MAX))){
            return PromotionStatus.ONGOING;
        }

        return PromotionStatus.EXPIRED;
    }
}
