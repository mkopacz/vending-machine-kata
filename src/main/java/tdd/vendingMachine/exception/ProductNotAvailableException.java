package tdd.vendingMachine.exception;

public class ProductNotAvailableException extends Exception {

    public ProductNotAvailableException(String productName) {
        super("Product " + productName + " is not available!");
    }

}
