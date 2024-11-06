package store.model.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.model.domain.Promotion;

public class PromotionRepository {
    private final List<Promotion> Promotions = new ArrayList<>();

    public void updatePromotionFromFile(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                isHeader = readFile(line, isHeader);
            }
        } catch (IOException ignored) {
        }
    }

    public boolean readFile(String line, boolean isHeader){
        if (isHeader) {
            return false;
        }
        String[] values = line.split(",");
        if (values.length == 5) {
            addPromotions(values);
        }
        return false;
    }

    public void addPromotions(String[] values){
        String name = values[0].trim();
        int buy = Integer.parseInt(values[1].trim());
        int get = Integer.parseInt(values[2].trim());
        String start_date = values[3].trim();
        String end_date = values[4].trim();

        Promotions.add(new Promotion(name, buy, get, start_date, end_date));
    }

    public List<Promotion> getAllPromotions(){
        return Promotions;
    }
}
