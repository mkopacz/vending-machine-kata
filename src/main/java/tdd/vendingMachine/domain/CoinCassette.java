package tdd.vendingMachine.domain;

import java.math.BigDecimal;
import java.util.*;

public class CoinCassette {

    private final Map<Coin, Integer> coins;

    public CoinCassette(Map<Coin, Integer> coins) {
        this.coins = coins;
    }

    public void putCoin(Coin coin) {
        coins.compute(coin, (k, v) -> v == null ? 1 : v + 1);
    }

    public Optional<List<Coin>> getCoins(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            List<Coin> result = new ArrayList<>();
            return Optional.of(result);
        }

        SortedMap<Coin, Integer> coinsToUse = new TreeMap(coins);
        return collectMoneyUsingGivenCoins(amount, coinsToUse);
    }

    private Optional<List<Coin>> collectMoneyUsingGivenCoins(BigDecimal amount, SortedMap<Coin, Integer> coinsToUse) {
        if (coinsToUse.isEmpty()) {
            return Optional.empty();
        }

        List<Coin> result = new ArrayList<>();
        SortedMap<Coin, Integer> coinsToUseCopy = new TreeMap(coinsToUse);
        BigDecimal amountLeft = doCollectMoneyUsingGivenCoins(amount, coinsToUseCopy, result);

        if (BigDecimal.ZERO.compareTo(amountLeft) != 0) {
            Coin highestCoin = coinsToUse.lastKey();
            SortedMap<Coin, Integer> lowerCoins = coinsToUse.headMap(highestCoin);
            return collectMoneyUsingGivenCoins(amount, lowerCoins);
        } else {
            coins.putAll(coinsToUseCopy);
            return Optional.of(result);
        }
    }

    private BigDecimal doCollectMoneyUsingGivenCoins(BigDecimal amount, SortedMap<Coin, Integer> coinsToUse,
                                                     List<Coin> result) {
        if (coinsToUse.isEmpty()) {
            return amount;
        }

        Coin highestCoin = coinsToUse.lastKey();
        BigDecimal highestCoinValue = highestCoin.getValue();
        Integer highestCoinCount = coinsToUse.get(highestCoin);

        while (highestCoinValue.compareTo(amount) <= 0 && highestCoinCount > 0) {
            amount = amount.subtract(highestCoinValue);
            coinsToUse.put(highestCoin, --highestCoinCount);
            result.add(highestCoin);
        }

        if (BigDecimal.ZERO.compareTo(amount) != 0) {
            SortedMap<Coin, Integer> lowerCoins = coinsToUse.headMap(highestCoin);
            return doCollectMoneyUsingGivenCoins(amount, lowerCoins, result);
        } else {
            return amount;
        }
    }

}
