package tdd.vendingMachine.state;

import tdd.vendingMachine.BasicVendingMachine;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.CoinCassette;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.math.BigDecimal;
import java.util.List;

public class InsertingCoinsBasicVendingMachineState implements BasicVendingMachineState {

    private final BasicVendingMachine vendingMachine;
    private final Shelve selectedShelve;

    private final CoinCassette cassette;
    private final Display display;

    private BigDecimal insertedMoney;

    public InsertingCoinsBasicVendingMachineState(BasicVendingMachine vendingMachine, Shelve selectedShelve) {
        this.vendingMachine = vendingMachine;
        this.selectedShelve = selectedShelve;

        this.cassette = vendingMachine.getCassette();
        this.display = vendingMachine.getDisplay();

        this.insertedMoney = BigDecimal.ZERO;
    }

    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        throw new IllegalStateException("Shelve already selected!");
    }

    @Override
    public void insertCoin(Coin coin) throws UnacceptableCoinException {
        cassette.putCoin(coin);
        addCoinToInsertedMoney(coin);
        displayInsertedCoinMessage();

        if (insertedEnoughMoney()) {
            goToDispensingProductState();
        }
    }

    @Override
    public boolean insertedEnoughMoney() {
        BigDecimal productPrice = selectedShelve.getProductPrice();
        return insertedMoney.compareTo(productPrice) >= 0;
    }

    @Override
    public List<Coin> cancel() {
        List<Coin> insertedMoneyInCoins = cassette.getCoins(insertedMoney)
            .orElseThrow(() -> new IllegalStateException("There should be coins to cover " + insertedMoney + "!"));

        goToSelectingProductState();
        return insertedMoneyInCoins;
    }

    @Override
    public Purchase dispenseProduct() {
        throw new IllegalStateException("Inserted not enough money!");
    }

    private void addCoinToInsertedMoney(Coin coin) {
        BigDecimal coinValue = coin.getValue();
        insertedMoney = insertedMoney.add(coinValue);
    }

    private void displayInsertedCoinMessage() {
        String productName = selectedShelve.getProductName();
        BigDecimal productPrice = selectedShelve.getProductPrice();
        BigDecimal moneyLeft = productPrice.subtract(insertedMoney).max(BigDecimal.ZERO);
        display.displayMessage(productName + " " + moneyLeft.setScale(2));
    }

    private void goToSelectingProductState() {
        BasicVendingMachineState selectingProductState =
            new SelectingProductBasicVendingMachineState(vendingMachine);
        vendingMachine.setState(selectingProductState);
    }

    private void goToDispensingProductState() {
        BasicVendingMachineState dispensingProductState =
            new DispensingProductBasicVendingMachineState(vendingMachine, selectedShelve, insertedMoney);
        vendingMachine.setState(dispensingProductState);
    }

}
