package tdd.vendingMachine;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class BasicVendingMachineTest {

    private List<Shelve> shelves;
    private CoinCassette cassette;
    private Display displaySpy;

    @Before
    public void setUp() {
        shelves = new ArrayList<>();
        shelves.add(new Shelve(1, new Product("cola drink", "2.50"), 1));
        shelves.add(new Shelve(2, new Product("chocolate bar", "3.30"), 1));
        shelves.add(new Shelve(3, new Product("mineral water", "1.90"), 0));

        Map<Coin, Integer> coinsInMachine = new HashMap<>();
        coinsInMachine.put(Coin.FIVE_DOLLARS, 1);
        coinsInMachine.put(Coin.TWENTY_CENTS, 1);
        cassette = new CoinCassette(coinsInMachine);

        displaySpy = spy(Display.class);
    }

    // listing shelve numbers

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        assertThat(vendingMachine.listShelveNumbers()).containsExactly(1, 2, 3);
    }

    // selecting shelve

    @Test
    public void shouldDisplayProductPriceWhenSelectedShelve()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);

        verify(displaySpy).displayMessage("cola drink 2.50");
    }

    @Test(expected = InvalidShelveException.class)
    public void shouldThrowExceptionWhenSelectedInvalidShelve()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(0);
    }

    @Test(expected = ProductNotAvailableException.class)
    public void shouldThrowExceptionWhenSelectedShelveAndProductIsNotAvailable()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(3);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveAfterInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.TWENTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        vendingMachine.selectShelve(2);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveManyTimesInRow()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = getVendingMachineWithSelectedShelve(1);

        vendingMachine.selectShelve(2);
    }

    // inserting coins

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInsertedCoinWithoutSelectingShelve() throws UnacceptableCoinException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

    @Test
    public void shouldDisplayAmountOfMoneyNeededToCoverProductPriceWhenInsertedCoin()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        vendingMachine.insertCoin(Coin.TWENTY_CENTS);

        verify(displaySpy).displayMessage("chocolate bar 0.10");
    }

    @Test
    public void shouldDisplayZeroWhenInsertedCoinAndThereIsEnoughMoneyToCoverProductPrice()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        verify(displaySpy).displayMessage("chocolate bar 0.00");
    }

    @Test(expected = UnacceptableCoinException.class)
    @Parameters({"ONE_CENT", "TWO_CENTS", "FIVE_CENTS"})
    public void shouldThrowExceptionWhenInsertedUnacceptableCoin(Coin unacceptableCoin)
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = getVendingMachineWithSelectedShelve(1);

        vendingMachine.insertCoin(unacceptableCoin);
    }

    // checking if inserted enough money

    @Test
    public void shouldShowThatInsertedNotEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        vendingMachine.insertCoin(Coin.TWENTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isFalse();
    }

    @Test
    public void shouldShowThatInsertedEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        vendingMachine.insertCoin(Coin.ONE_DOLLAR);

        assertThat(vendingMachine.insertedEnoughMoney()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCheckedInsertedMoneyWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.insertedEnoughMoney();
    }

    // canceling operation

    @Test
    public void shouldReturnNoCoinsWhenCanceledWithoutInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(2);

        assertThat(vendingMachine.cancel()).isEmpty();
    }

    @Test
    public void shouldReturnCoinsWhenCanceledAfterInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        vendingMachine.insertCoin(Coin.TEN_CENTS);

        assertThat(vendingMachine.cancel()).containsExactly(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS, Coin.TEN_CENTS);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCanceledAfterInsertingEnoughCoins()
        throws UnacceptableCoinException, ProductNotAvailableException, InvalidShelveException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        vendingMachine.cancel();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCanceledWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.cancel();
    }

    @Test
    public void shouldBeAbleToSelectShelveAgainWhenCanceled()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = getVendingMachineWithSelectedShelve(1);

        try {
            vendingMachine.cancel();
            vendingMachine.selectShelve(2);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }

    // dispensing product

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenDispensedProductWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.dispenseProduct();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenDispensedProductWithoutInsertingEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS, Coin.TWENTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        vendingMachine.dispenseProduct();
    }

    @Test
    public void shouldBeAbleToSelectShelveWhenDispensedWithoutProduct()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        try {
            vendingMachine.dispenseProduct();
            vendingMachine.selectShelve(2);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }

    @Test
    public void shouldBeAbleToSelectShelveWhenDispensedWithProduct()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR, Coin.FIFTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        try {
            vendingMachine.dispenseProduct();
            vendingMachine.selectShelve(1);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }

    @Test
    public void shouldGetMoneyAndNoProductWhenChangeCantBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        Purchase purchase = vendingMachine.dispenseProduct();

        assertThat(purchase.getProduct()).isNull();
        assertThat(purchase.getChange()).containsExactly(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
    }

    @Test
    public void shouldGetChangeAndProductWhenChangeCanBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR, Coin.FIFTY_CENTS);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(2, insertedCoins);

        Purchase purchase = vendingMachine.dispenseProduct();

        assertThat(purchase.getProduct()).isNotNull();
        assertThat(purchase.getChange()).containsExactly(Coin.TWENTY_CENTS);
    }

    @Test
    public void shouldDisplayWarningWhenChangeCantBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        List<Coin> insertedCoins = Arrays.asList(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
        VendingMachine vendingMachine = getVendingMachineWithSelectedShelveAndInsertedCoins(1, insertedCoins);

        vendingMachine.dispenseProduct();

        verify(displaySpy).displayWarning("No change!");
    }

    private VendingMachine getVendingMachineWithSelectedShelve(int shelveNumber)
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);
        vendingMachine.selectShelve(shelveNumber);
        return vendingMachine;
    }

    private VendingMachine getVendingMachineWithSelectedShelveAndInsertedCoins(int shelveNumber, List<Coin> insertedCoins)
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);
        vendingMachine.selectShelve(shelveNumber);
        for (Coin coin : insertedCoins) {
            vendingMachine.insertCoin(coin);
        }
        return vendingMachine;
    }

}
