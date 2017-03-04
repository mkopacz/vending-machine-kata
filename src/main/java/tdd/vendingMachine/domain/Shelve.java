package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public class Shelve {

    private final int number;
    private final Product product;

    public Shelve(int number, Product product) {
        this.number = number;
        this.product = product;
    }

    public int getNumber() {
        return number;
    }

    public String getProductName() {
        return product.getName();
    }

    public BigDecimal getProductPrice() {
        return product.getPrice();
    }

}
