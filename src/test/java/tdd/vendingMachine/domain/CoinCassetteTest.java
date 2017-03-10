package tdd.vendingMachine.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.data.MapEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CoinCassetteTest {

    @Test
    public void shouldStoreCoinsInsideCassetteWhenPutCoins() throws UnacceptableCoinException {
        Map<Coin, Integer> coins = new HashMap<>();
        CoinCassette cassette = new CoinCassette(coins);

        cassette.putCoin(Coin.TEN_CENTS);
        cassette.putCoin(Coin.TEN_CENTS);

        assertThat(coins).containsExactly(MapEntry.entry(Coin.TEN_CENTS, 2));
    }

    @Test(expected = UnacceptableCoinException.class)
    @Parameters({"ONE_CENT", "TWO_CENTS", "FIVE_CENTS"})
    public void shouldThrowExceptionWhenPutCoinThatIsNotAcceptable(Coin unacceptableCoin)
        throws UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());

        cassette.putCoin(unacceptableCoin);
    }

    @Test
    @Parameters(method = "inputForSuccessfulCasesOfGettingCoins")
    public void shouldReturnCoinsForGivenCoinsAndAmountOfMoney(Map<Coin, Integer> coins, BigDecimal amount,
                                                               List<Coin> expectedOutput) {
        CoinCassette cassette = new CoinCassette(coins);

        List<Coin> actualOutput = cassette.getCoins(amount).get();

        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @Test
    @Parameters(method = "inputForUnSuccessfulCasesOfGettingCoins")
    public void shouldReturnNoCoinsForGivenCoinsAndAmountOfMoney(Map<Coin, Integer> coins, BigDecimal amount) {
        CoinCassette cassette = new CoinCassette(coins);

        Optional<List<Coin>> actualOutput = cassette.getCoins(amount);

        assertThat(actualOutput).isEmpty();
    }

    private Object[] inputForSuccessfulCasesOfGettingCoins() {
        return new Object[][] {
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("8.00"),
                Arrays.asList(
                    Coin.FIVE_DOLLARS,
                    Coin.TWO_DOLLARS,
                    Coin.ONE_DOLLAR
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("7.00"),
                Arrays.asList(
                    Coin.FIVE_DOLLARS,
                    Coin.TWO_DOLLARS
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("6.00"),
                Arrays.asList(
                    Coin.FIVE_DOLLARS,
                    Coin.ONE_DOLLAR
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("3.00"),
                Arrays.asList(
                    Coin.TWO_DOLLARS,
                    Coin.ONE_DOLLAR
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("5.00"),
                Arrays.asList(
                    Coin.FIVE_DOLLARS
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("2.00"),
                Arrays.asList(
                    Coin.TWO_DOLLARS
                )
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("1.00"),
                Arrays.asList(
                    Coin.ONE_DOLLAR
                )
            },
            {
                Collections.emptyMap(),
                BigDecimal.ZERO,
                Collections.emptyList()
            }
        };
    }

    private Object[] inputForUnSuccessfulCasesOfGettingCoins() {
        return new Object[][] {
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("8.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("7.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("6.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("3.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("5.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("2.50")
            },
            {
                new HashMap<Coin, Integer>() {{
                    put(Coin.FIVE_DOLLARS, 1);
                    put(Coin.TWO_DOLLARS, 1);
                    put(Coin.ONE_DOLLAR, 1);
                }},
                new BigDecimal("1.50")
            },
            {
                new HashMap<Coin, Integer>(),
                new BigDecimal("1.00")
            },
        };
    }

}
