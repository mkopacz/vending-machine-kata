package tdd.vendingMachine.domain;

import java.util.Map;

public class CoinCassette {

    private final Map<Coin, Integer> coins;

    public CoinCassette(Map<Coin, Integer> coins) {
        this.coins = coins;
    }

    public void putCoin(Coin coin) {
        coins.compute(coin, (k, v) -> v == null ? 1 : v + 1);
    }

}
