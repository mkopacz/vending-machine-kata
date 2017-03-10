package tdd.vendingMachine.exception;

import java.math.BigDecimal;

public class UnacceptableCoinException extends Exception {

    public UnacceptableCoinException(BigDecimal coinValue) {
        super("Coin of value " + coinValue + " is not acceptable!");
    }

}
