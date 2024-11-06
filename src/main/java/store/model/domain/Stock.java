package store.model.domain;

import java.text.DecimalFormat;

public class Stock {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Stock(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "- " + name + " " + df.format(price) + "원 " +  quantity + "개 " + promotion;
    }
}
