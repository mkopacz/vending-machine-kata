package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public enum Coin {

    ONE_CENT("0.01"),
    TWO_CENTS("0.02"),
    FIVE_CENTS("0.05"),
    TEN_CENTS("0.10"),
    TWENTY_CENTS("0.20"),
    FIFTY_CENTS("0.50"),
    ONE_DOLLAR("1.00"),
    TWO_DOLLARS("2.00"),
    FIVE_DOLLARS("5.00");

    private final BigDecimal value;

    Coin(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}
