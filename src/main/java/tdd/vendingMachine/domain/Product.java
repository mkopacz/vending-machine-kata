package tdd.vendingMachine.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    private static final int PRICE_SCALE = 2;
    private static final RoundingMode PRICE_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final String name;
    private final BigDecimal price;

    public Product(String name, String price) {
        this.name = name;
        this.price = new BigDecimal(price).setScale(PRICE_SCALE, PRICE_ROUNDING_MODE);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
