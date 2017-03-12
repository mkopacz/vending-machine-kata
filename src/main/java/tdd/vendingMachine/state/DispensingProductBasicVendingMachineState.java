package tdd.vendingMachine.state;

import tdd.vendingMachine.BasicVendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DispensingProductBasicVendingMachineState implements BasicVendingMachineState {

    private final BasicVendingMachine vendingMachine;
    private final Shelve selectedShelve;
    private final BigDecimal insertedMoney;

    public DispensingProductBasicVendingMachineState(BasicVendingMachine vendingMachine, Shelve selectedShelve,
                                                     BigDecimal insertedMoney) {
        this.vendingMachine = vendingMachine;
        this.selectedShelve = selectedShelve;
        this.insertedMoney = insertedMoney;
    }


    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        throw new IllegalStateException("Shelve already selected!");
    }

    @Override
    public void insertCoin(Coin coin) throws UnacceptableCoinException {
        throw new IllegalStateException("Already inserted enough money!");
    }

    @Override
    public boolean insertedEnoughMoney() {
        return true;
    }

    @Override
    public List<Coin> cancel() {
        throw new IllegalStateException("Can't cancel while dispensing product!");
    }

    @Override
    public Purchase dispenseProduct() {
        goToSelectingProductState();

        return tryToGetChange().map(changeInCoins -> {
            Product product = selectedShelve.releaseProduct()
                .orElseThrow(() -> new IllegalStateException("There should be product to release!"));
            return new Purchase(product, changeInCoins);
        }).orElseGet(this::returnInsertedMoney);
    }

    private Optional<List<Coin>> tryToGetChange() {
        BigDecimal productPrice = selectedShelve.getProductPrice();
        BigDecimal changeAmount = insertedMoney.subtract(productPrice);
        return vendingMachine.getCassette().getCoins(changeAmount);
    }

    private Purchase returnInsertedMoney() {
        vendingMachine.getDisplay().displayWarning("No change!");
        List<Coin> insertedMoneyInCoins = vendingMachine.getCassette().getCoins(insertedMoney)
            .orElseThrow(() -> new IllegalStateException("There should be coins to cover " + insertedMoney + "!"));
        return new Purchase(null, insertedMoneyInCoins);
    }

    private void goToSelectingProductState() {
        BasicVendingMachineState insertingCoinsState =
            new SelectingProductBasicVendingMachineState(vendingMachine);
        vendingMachine.setState(insertingCoinsState);
    }

}
