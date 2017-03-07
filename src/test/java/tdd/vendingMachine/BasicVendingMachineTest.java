package tdd.vendingMachine;

import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BasicVendingMachineTest {

    private Display display;
    private List<Shelve> shelves;

    @Before
    public void setUp() {
        display = spy(Display.class);

        shelves = new ArrayList<>();
        shelves.add(new Shelve(1, new Product("cola drink", "2.44")));
        shelves.add(new Shelve(2, new Product("chocolate bar", "9.99")));
        shelves.add(new Shelve(3, new Product("mineral water", "109.90")));
    }

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, Collections.EMPTY_MAP, display);

        List<Integer> shelveNumbers = vendingMachine.listShelveNumbers();

        assertThat(shelveNumbers).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldDisplayProductPriceWhenSelectedShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, Collections.EMPTY_MAP, display);

        vendingMachine.selectShelve(2);

        verify(display).displayMessage("chocolate bar 9.99");
    }

    @Test(expected = InvalidShelveException.class)
    public void shouldThrowExceptionWhenSelectedInvalidShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, Collections.EMPTY_MAP, display);

        vendingMachine.selectShelve(0);
    }

    @Test
    public void shouldStoreCoinInsideMachineWhenSelectedShelveAndInsertedCoin() throws InvalidShelveException {
        Map<Coin, Integer> coins = new HashMap<>();
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, coins, display);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.ONE_DOLLAR);

        assertThat(coins).containsExactly(MapEntry.entry(Coin.ONE_DOLLAR, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInsertedCoinWithoutSelectingShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, Collections.EMPTY_MAP, display);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveAfterInsertingCoins() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, new HashMap<>(), display);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.selectShelve(2);
    }

    @Test
    public void shouldDisplayAmountOfMoneyNeededToCoverProductPrice() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, new HashMap<>(), display);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
        vendingMachine.insertCoin(Coin.TWENTY_CENTS);

        verify(display).displayMessage("mineral water 104.70");
    }

    @Test
    public void shouldShowThatInsertedNotEnoughMoney() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, new HashMap<>(), display);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isFalse();
    }

    @Test
    public void shouldShowThatInsertedEnoughMoney() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, new HashMap<>(), display);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCheckedInsertedMoneyWithoutSelectingShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, Collections.EMPTY_MAP, display);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

}
