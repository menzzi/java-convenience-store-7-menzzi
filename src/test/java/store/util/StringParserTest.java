package store.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class StringParserTest {

    @ParameterizedTest
    @CsvSource({
            "콜라-2,사이다-3",
            "(콜라-2),(사이다-3)",
            "[콜라-2,사이다-3]"
    })
    void 대괄호_형식을_지키지_않으면_예외가_발생한다(String userInput) {
        String input = userInput;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            StringParser.validateOrderFormat(input);
        });
        assertEquals(" 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", thrown.getMessage());
    }

    @Test
    void 하이픈_형식을_지키지_않으면_예외가_발생한다() {
        String input = "[콜라,2],[사이다-3]";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            StringParser.validateOrderFormat(input);
        });
        assertEquals(" 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", thrown.getMessage());
    }

    @Test
    void 개수입력이_숫자가_아니면_예외가_발생한다() {
        String input = "[콜라-사이다],[사이다-3]";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            StringParser.validateOrderFormat(input);
        });
        assertEquals(" 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", thrown.getMessage());
    }

    @Test
    void 올바른_형식_입력() {
        String input = "[콜라-2],[사이다-3]";
        Map<String, Integer> orders = StringParser.validateOrderFormat(input);

        assertEquals(2, orders.get("콜라"));
        assertEquals(3, orders.get("사이다"));
    }
}
