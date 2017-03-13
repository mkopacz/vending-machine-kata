package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.CoinCassette;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;
import tdd.vendingMachine.state.BasicVendingMachineState;
import tdd.vendingMachine.state.SelectingProductBasicVendingMachineState;

import java.util.List;
import java.util.stream.Collectors;

public class BasicVendingMachine implements VendingMachine {

    private final List<Shelve> shelves;
    private final CoinCassette cassette;
    private final Display display;

    private BasicVendingMachineState state;

    public BasicVendingMachine(List<Shelve> shelves, CoinCassette cassette, Display display) {
        this.shelves = shelves;
        this.cassette = cassette;
        this.display = display;

        this.state = new SelectingProductBasicVendingMachineState(this);
    }

    @Override
    public List<Integer> listShelveNumbers() {
        return shelves.stream()
            .map(Shelve::getNumber)
            .collect(Collectors.toList());
    }

    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        state.selectShelve(shelveNumber);
    }

    @Override
    public void insertCoin(Coin coin) throws UnacceptableCoinException {
        state.insertCoin(coin);
    }

    @Override
    public boolean insertedEnoughMoney() {
        return state.insertedEnoughMoney();
    }

    @Override
    public List<Coin> cancel() {
        return state.cancel();
    }

    @Override
    public Purchase dispenseProduct() {
        return state.dispenseProduct();
    }

    // getters and setter required by state design pattern

    public List<Shelve> getShelves() {
        return shelves;
    }

    public CoinCassette getCassette() {
        return cassette;
    }

    public Display getDisplay() {
        return display;
    }

    public void setState(BasicVendingMachineState state) {
        this.state = state;
    }

}
