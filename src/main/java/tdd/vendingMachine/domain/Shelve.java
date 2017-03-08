package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public class Shelve {

    private final int number;
    private final Product product;

    private int quantity;

    public Shelve(int number, Product product, int quantity) {
        this.number = number;
        this.product = product;
        this.quantity = quantity;
    }

    public boolean isProductAvailable() {
        return quantity > 0;
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
