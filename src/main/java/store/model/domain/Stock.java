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

    public String getPromotion() {
        return promotion;
    }

    public void decreaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.quantity) {
            throw new IllegalArgumentException();
        }
        this.quantity -= amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(makeCommonString());
        if (quantity == 0) {
            return outOfStockString(sb);
        }
        sb.append(quantity).append("개 ");
        if (promotion.equals("null")) {
            return sb.toString();
        }
        sb.append(promotion);
        return sb.toString();
    }

    private String makeCommonString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "- " + name + " " + df.format(price) + "원 ";
    }

    private String outOfStockString(StringBuilder sb) {
        sb.append("재고 없음 ");
        if (promotion.equals("null")) {
            return sb.toString();
        }
        return sb.append(promotion).toString();
    }
}
