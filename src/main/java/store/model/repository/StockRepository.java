package store.model.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.model.domain.Stock;

public class StockRepository {
    private static final List<Stock> stocks = new ArrayList<>();

    public void updateStockFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                readFile(line);
            }
        } catch (IOException ignored) {
        }
    }

    private void readFile(String line) {
        String[] values = line.split(",");
        if (values.length == 4) {
            addStocks(values);
        }
    }

    private void addStocks(String[] values) {
        String name = values[0].trim();
        int price = Integer.parseInt(values[1].trim());
        int quantity = Integer.parseInt(values[2].trim());
        String promotion = values[3].trim();

        if (!promotion.equals("null")) {
            addPromotionProduct(name, price, quantity, promotion);
            return;
        }
        addGeneralProduct(name, price, quantity, promotion);
    }

    private void addPromotionProduct(String name, int price, int quantity, String promotion) {
        stocks.add(new Stock(name, price, quantity, promotion));
        stocks.add(new Stock(name, price, 0, "null"));
    }

    private void addGeneralProduct(String name, int price, int quantity, String promotion) {
        Stock candidateSameName = stocks.getLast();
        if (candidateSameName.getName().equals(name) && candidateSameName.getPromotion().equals("null")) {
            stocks.remove(candidateSameName);
        }
        stocks.add(new Stock(name, price, quantity, promotion));
    }

    public List<Stock> getAllStocks() {
        return stocks;
    }
}
