package tdd.vendingMachine.state;

import tdd.vendingMachine.BasicVendingMachine;
import tdd.vendingMachine.domain.Coin;
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

    private BigDecimal insertedMoney;

    public InsertingCoinsBasicVendingMachineState(BasicVendingMachine vendingMachine, Shelve selectedShelve) {
        this.vendingMachine = vendingMachine;
        this.selectedShelve = selectedShelve;
        this.insertedMoney = BigDecimal.ZERO;
    }

    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        throw new IllegalStateException("Shelve already selected!");
    }

    @Override
    public void insertCoin(Coin coin) throws UnacceptableCoinException {
        vendingMachine.getCassette().putCoin(coin);
        insertedMoney = insertedMoney.add(coin.getValue());
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
        goToSelectingProductState();
        return vendingMachine.getCassette().getCoins(insertedMoney)
            .orElseThrow(() -> new IllegalStateException("There should be coins to cover " + insertedMoney + "!"));
    }

    @Override
    public Purchase dispenseProduct() {
        throw new IllegalStateException("Inserted not enough money!");
    }

    private void displayInsertedCoinMessage() {
        String productName = selectedShelve.getProductName();
        BigDecimal productPrice = selectedShelve.getProductPrice();
        BigDecimal moneyLeft = productPrice.subtract(insertedMoney);
        vendingMachine.getDisplay().displayMessage(productName + " " + moneyLeft);
    }

    private void goToSelectingProductState() {
        BasicVendingMachineState insertingCoinsState =
            new SelectingProductBasicVendingMachineState(vendingMachine);
        vendingMachine.setState(insertingCoinsState);
    }

    private void goToDispensingProductState() {
        BasicVendingMachineState insertingCoinsState =
            new DispensingProductBasicVendingMachineState(vendingMachine, selectedShelve, insertedMoney);
        vendingMachine.setState(insertingCoinsState);
    }

}
