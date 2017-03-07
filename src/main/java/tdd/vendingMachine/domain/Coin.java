package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public enum Coin {

    TEN_CENTS("0.1"),
    TWENTY_CENTS("0.2"),
    FIFTY_CENTS("0.5"),
    ONE_DOLLAR("1.0"),
    TWO_DOLLARS("2.0"),
    FIVE_DOLLARS("5.0");

    private final BigDecimal value;

    Coin(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}
