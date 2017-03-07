package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.CoinCassette;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicVendingMachine implements VendingMachine {

    private final List<Shelve> shelves;
    private final CoinCassette cassette;
    private final Display display;

    private Optional<Shelve> selectedShelve = Optional.empty();
    private Optional<BigDecimal> insertedMoney = Optional.empty();

    public BasicVendingMachine(List<Shelve> shelves, CoinCassette cassette, Display display) {
        this.shelves = shelves;
        this.cassette = cassette;
        this.display = display;
    }

    @Override
    public List<Integer> listShelveNumbers() {
        return shelves.stream()
            .map(Shelve::getNumber)
            .collect(Collectors.toList());
    }

    @Override
    public void selectShelve(int shelveNumber) throws InvalidShelveException {
        if (insertedMoney.isPresent()) {
            throw new IllegalStateException("Already inserted coins!");
        }

        selectedShelve = shelves.stream()
            .filter(shelve -> shelve.getNumber() == shelveNumber)
            .findFirst();

        if (selectedShelve.isPresent()) {
            insertedMoney = Optional.of(BigDecimal.ZERO);
            displaySelectedShelveMessage();
        } else {
            throw new InvalidShelveException(shelveNumber);
        }
    }

    @Override
    public void insertCoin(Coin coin) {
        if (!selectedShelve.isPresent()) {
            throw new IllegalStateException("No shelve is selected!");
        }

        cassette.putCoin(coin);
        addCoinToInsertedMoney(coin);
        displayInsertedCoinMessage();
    }

    @Override
    public boolean insertedEnoughMoney() {
        if (!selectedShelve.isPresent()) {
            throw new IllegalStateException("No shelve is selected!");
        }

        BigDecimal productPrice = selectedShelve.get().getProductPrice();
        return insertedMoney.get().compareTo(productPrice) >= 0;
    }

    private void addCoinToInsertedMoney(Coin coin) {
        BigDecimal coinValue = coin.getValue();
        insertedMoney = insertedMoney.map(
            currentMoney -> currentMoney.add(coinValue)
        );
    }

    private void displaySelectedShelveMessage() {
        String productName = selectedShelve.get().getProductName();
        String productPrice = selectedShelve.get().getProductPrice().toString();
        display.displayMessage(productName + " " + productPrice);
    }

    private void displayInsertedCoinMessage() {
        String productName = selectedShelve.get().getProductName();
        BigDecimal productPrice = selectedShelve.get().getProductPrice();
        BigDecimal moneyLeft = productPrice.subtract(insertedMoney.get());
        display.displayMessage(productName + " " + moneyLeft);
    }

}
