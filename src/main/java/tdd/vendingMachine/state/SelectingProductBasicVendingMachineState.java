package tdd.vendingMachine.state;

import tdd.vendingMachine.BasicVendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.util.List;

public class SelectingProductBasicVendingMachineState implements BasicVendingMachineState {

    private final BasicVendingMachine vendingMachine;

    public SelectingProductBasicVendingMachineState(BasicVendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        Shelve foundShelve = vendingMachine.getShelves().stream()
            .filter(shelve -> shelve.getNumber() == shelveNumber).findFirst()
            .orElseThrow(() -> new InvalidShelveException(shelveNumber));

        if (!foundShelve.isProductAvailable()) {
            String productName = foundShelve.getProductName();
            throw new ProductNotAvailableException(productName);
        }

        displaySelectedShelveMessage(foundShelve);
        goToInsertingCoinsState(foundShelve);
    }

    @Override
    public void insertCoin(Coin coin) throws UnacceptableCoinException {
        throw new IllegalStateException("No shelve selected!");
    }

    @Override
    public boolean insertedEnoughMoney() {
        throw new IllegalStateException("No shelve selected!");
    }

    @Override
    public List<Coin> cancel() {
        throw new IllegalStateException("No shelve selected!");
    }

    @Override
    public Purchase dispenseProduct() {
        throw new IllegalStateException("No shelve selected!");
    }

    private void displaySelectedShelveMessage(Shelve selectedShelve) {
        String productName = selectedShelve.getProductName();
        String productPrice = selectedShelve.getProductPrice().toString();
        vendingMachine.getDisplay().displayMessage(productName + " " + productPrice);
    }

    private void goToInsertingCoinsState(Shelve selectedShelve) {
        BasicVendingMachineState insertingCoinsState =
            new InsertingCoinsBasicVendingMachineState(vendingMachine, selectedShelve);
        vendingMachine.setState(insertingCoinsState);
    }

}
