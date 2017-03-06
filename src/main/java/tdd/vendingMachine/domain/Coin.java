package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public enum Coin {

    FIVE_DOLLARS("5.0"),
    TWO_DOLLARS("2.0"),
    ONE_DOLLAR("1.0"),
    FIFTY_CENTS("0.5"),
    TWENTY_CENTS("0.2"),
    TEN_CENTS("0.1");

    private final BigDecimal value;

    Coin(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}
