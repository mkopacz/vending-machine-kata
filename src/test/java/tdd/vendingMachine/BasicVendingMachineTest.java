package tdd.vendingMachine;

import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.CoinCassette;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BasicVendingMachineTest {

    private Display displaySpy;
    private CoinCassette cassetteMock;

    private List<Shelve> shelves;

    @Before
    public void setUp() {
        displaySpy = spy(Display.class);
        cassetteMock = mock(CoinCassette.class);

        shelves = new ArrayList<>();
        shelves.add(new Shelve(1, new Product("cola drink", "2.44")));
        shelves.add(new Shelve(2, new Product("chocolate bar", "9.99")));
        shelves.add(new Shelve(3, new Product("mineral water", "109.90")));
    }

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        List<Integer> shelveNumbers = vendingMachine.listShelveNumbers();

        assertThat(shelveNumbers).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldDisplayProductPriceWhenSelectedShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(2);

        verify(displaySpy).displayMessage("chocolate bar 9.99");
    }

    @Test(expected = InvalidShelveException.class)
    public void shouldThrowExceptionWhenSelectedInvalidShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(0);
    }

    @Test
    public void shouldStoreCoinInsideMachineWhenSelectedShelveAndInsertedCoin() throws InvalidShelveException {
        Map<Coin, Integer> coins = new HashMap<>();
        CoinCassette cassette = new CoinCassette(coins);
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.ONE_DOLLAR);

        assertThat(coins).containsExactly(MapEntry.entry(Coin.ONE_DOLLAR, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInsertedCoinWithoutSelectingShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveAfterInsertingCoins() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.selectShelve(2);
    }

    @Test
    public void shouldDisplayAmountOfMoneyNeededToCoverProductPrice() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
        vendingMachine.insertCoin(Coin.TWENTY_CENTS);

        verify(displaySpy).displayMessage("mineral water 104.70");
    }

    @Test
    public void shouldShowThatInsertedNotEnoughMoney() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isFalse();
    }

    @Test
    public void shouldShowThatInsertedEnoughMoney() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCheckedInsertedMoneyWithoutSelectingShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

}
