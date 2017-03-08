package tdd.vendingMachine.exception;

import java.math.BigDecimal;

public class CoinNotAcceptableException extends Exception {

    public CoinNotAcceptableException(BigDecimal coinValue) {
        super("Coin of value " + coinValue + " is not acceptable!");
    }

}
