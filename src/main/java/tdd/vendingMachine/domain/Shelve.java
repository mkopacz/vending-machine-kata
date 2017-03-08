package tdd.vendingMachine.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class Shelve {

    private final int number;
    private final Product productType;

    private int quantity;

    public Shelve(int number, Product productType, int quantity) {
        this.number = number;
        this.productType = productType;
        this.quantity = quantity;
    }

    public boolean isProductAvailable() {
        return quantity > 0;
    }

    public Optional<Product> releaseProduct() {
        if (!isProductAvailable()) {
            return Optional.empty();
        } else {
            decreaseQuantity();
            Product product = new Product(productType);
            return Optional.of(product);
        }
    }

    public int getNumber() {
        return number;
    }

    public String getProductName() {
        return productType.getName();
    }

    public BigDecimal getProductPrice() {
        return productType.getPrice();
    }

    private void decreaseQuantity() {
        quantity--;
    }

}
