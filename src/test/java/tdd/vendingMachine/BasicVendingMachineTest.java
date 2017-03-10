package tdd.vendingMachine;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.data.MapEntry;
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
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class BasicVendingMachineTest {

    private Display displaySpy;
    private CoinCassette cassetteMock;

    private List<Shelve> shelves;

    @Before
    public void setUp() {
        displaySpy = spy(Display.class);
        cassetteMock = mock(CoinCassette.class);

        shelves = new ArrayList<>();
        shelves.add(new Shelve(1, new Product("cola drink", "2.44"), 1));
        shelves.add(new Shelve(2, new Product("chocolate bar", "5.99"), 1));
        shelves.add(new Shelve(3, new Product("mineral water", "7.20"), 1));
    }

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        List<Integer> shelveNumbers = vendingMachine.listShelveNumbers();

        assertThat(shelveNumbers).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldDisplayProductPriceWhenSelectedShelve()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(2);

        verify(displaySpy).displayMessage("chocolate bar 5.99");
    }

    @Test(expected = InvalidShelveException.class)
    public void shouldThrowExceptionWhenSelectedInvalidShelve()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(0);
    }

    @Test
    public void shouldStoreCoinInsideMachineWhenSelectedShelveAndInsertedCoin()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        Map<Coin, Integer> coins = new HashMap<>();
        CoinCassette cassette = new CoinCassette(coins);
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.ONE_DOLLAR);

        assertThat(coins).containsExactly(MapEntry.entry(Coin.ONE_DOLLAR, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInsertedCoinWithoutSelectingShelve() throws UnacceptableCoinException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.insertCoin(Coin.TEN_CENTS);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveAfterInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.selectShelve(2);
    }

    @Test
    public void shouldDisplayAmountOfMoneyNeededToCoverProductPrice()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
        vendingMachine.insertCoin(Coin.TWENTY_CENTS);

        verify(displaySpy).displayMessage("mineral water 2.00");
    }

    @Test
    public void shouldShowThatInsertedNotEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isFalse();
    }

    @Test
    public void shouldShowThatInsertedEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);

        assertThat(vendingMachine.insertedEnoughMoney()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCheckedInsertedMoneyWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.insertedEnoughMoney();
    }

    @Test
    public void shouldReturnNoCoinsWhenCanceledWithoutInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(2);
        List<Coin> returnedCoins = vendingMachine.cancel();

        assertThat(returnedCoins).isEmpty();
    }

    @Test
    public void shouldReturnCoinsWhenCanceledAfterInsertingCoins()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.TWENTY_CENTS);
        vendingMachine.insertCoin(Coin.TEN_CENTS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);
        List<Coin> returnedCoins = vendingMachine.cancel();

        assertThat(returnedCoins).containsExactly(Coin.FIFTY_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenCanceledWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.cancel();
    }

    @Test
    public void shouldBeAbleToSelectShelveAgainWhenCanceled()
        throws InvalidShelveException, ProductNotAvailableException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        try {
            vendingMachine.selectShelve(1);
            vendingMachine.cancel();
            vendingMachine.selectShelve(2);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }


    @Test(expected = ProductNotAvailableException.class)
    public void shouldThrowExceptionWhenSelectedShelveAndProductIsNotAvailable()
        throws InvalidShelveException, ProductNotAvailableException {

        List<Shelve> shelves = Arrays.asList(new Shelve(1, mock(Product.class), 0));
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
    }

    @Test(expected = UnacceptableCoinException.class)
    @Parameters({"ONE_CENT", "TWO_CENTS", "FIVE_CENTS"})
    public void shouldThrowExceptionWhenInsertedCoinThatIsNotAcceptable(Coin unacceptableCoin)
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(unacceptableCoin);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSelectedShelveManyTimesInRow()
        throws InvalidShelveException, ProductNotAvailableException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.selectShelve(2);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenDispensedProductWithoutSelectingShelve() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.dispenseProduct();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenDispensedProductWithoutInsertingEnoughMoney()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassetteMock, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
        vendingMachine.dispenseProduct();
    }

    @Test
    public void shouldBeAbleToSelectShelveWhenDispensedWithoutProduct()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        try {
            vendingMachine.selectShelve(1);
            vendingMachine.insertCoin(Coin.TWO_DOLLARS);
            vendingMachine.insertCoin(Coin.ONE_DOLLAR);
            vendingMachine.dispenseProduct();
            vendingMachine.selectShelve(1);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }

    @Test
    public void shouldBeAbleToSelectShelveWhenDispensedWithProduct()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        Map<Coin, Integer> coins = new HashMap<>();
        coins.put(Coin.TWENTY_CENTS, 1);
        coins.put(Coin.TEN_CENTS, 1);

        CoinCassette cassette = new CoinCassette(coins);
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        try {
            vendingMachine.selectShelve(3);
            vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
            vendingMachine.insertCoin(Coin.TWO_DOLLARS);
            vendingMachine.insertCoin(Coin.FIFTY_CENTS);
            vendingMachine.dispenseProduct();
            vendingMachine.selectShelve(1);
        } catch (IllegalStateException e) {
            fail("Should be able to select shelve!", e);
        }
    }

    @Test
    public void shouldGetMoneyAndNoProductWhenChangeCantBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.ONE_DOLLAR);
        Purchase purchase = vendingMachine.dispenseProduct();

        assertThat(purchase.getProduct()).isNull();
        assertThat(purchase.getChange()).containsExactly(Coin.TWO_DOLLARS, Coin.ONE_DOLLAR);
    }

    @Test
    public void shouldGetChangeAndProductWhenChangeCanBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        Map<Coin, Integer> coins = new HashMap<>();
        coins.put(Coin.TWENTY_CENTS, 1);
        coins.put(Coin.TEN_CENTS, 1);

        CoinCassette cassette = new CoinCassette(coins);
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(3);
        vendingMachine.insertCoin(Coin.FIVE_DOLLARS);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);
        Purchase purchase = vendingMachine.dispenseProduct();

        assertThat(purchase.getProduct()).isNotNull();
        assertThat(purchase.getChange()).containsExactly(Coin.TWENTY_CENTS, Coin.TEN_CENTS);
    }

    @Test
    public void shouldDisplayWarningWhenChangeCantBeReturned()
        throws InvalidShelveException, ProductNotAvailableException, UnacceptableCoinException {

        CoinCassette cassette = new CoinCassette(new HashMap<>());
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, cassette, displaySpy);

        vendingMachine.selectShelve(1);
        vendingMachine.insertCoin(Coin.TWO_DOLLARS);
        vendingMachine.insertCoin(Coin.ONE_DOLLAR);
        vendingMachine.dispenseProduct();

        verify(displaySpy).displayWarning("No change!");
    }

}
