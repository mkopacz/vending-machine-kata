package tdd.vendingMachine.domain;

import java.util.List;

public class Purchase {

    private final Product product;
    private final List<Coin> change;

    public Purchase(Product product, List<Coin> change) {
        this.product = product;
        this.change = change;
    }

    public Product getProduct() {
        return product;
    }

    public List<Coin> getChange() {
        return change;
    }

}
