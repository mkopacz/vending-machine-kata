package tdd.vendingMachine.domain;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CoinCassetteTest {

    @Test
    public void shouldStoreCoinsInsideCassetteWhenPutCoins() {
        Map<Coin, Integer> coins = new HashMap<>();
        CoinCassette cassette = new CoinCassette(coins);

        cassette.putCoin(Coin.TEN_CENTS);
        cassette.putCoin(Coin.TEN_CENTS);

        assertThat(coins).containsExactly(MapEntry.entry(Coin.TEN_CENTS, 2));
    }

}
