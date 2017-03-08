package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.CoinCassette;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.CoinNotAcceptableException;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;

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
    public void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException {
        if (insertedMoney.isPresent()) {
            throw new IllegalStateException("Already inserted coins!");
        }

        Optional<Shelve> foundShelve = shelves.stream()
            .filter(shelve -> shelve.getNumber() == shelveNumber)
            .findFirst();

        if (!foundShelve.isPresent()) {
            throw new InvalidShelveException(shelveNumber);
        } else if (!foundShelve.get().isProductAvailable()) {
            String productName = foundShelve.get().getProductName();
            throw new ProductNotAvailableException(productName);
        } else {
            initMachineState(foundShelve);
            displaySelectedShelveMessage();
        }
    }

    @Override
    public void insertCoin(Coin coin) throws CoinNotAcceptableException {
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

    @Override
    public List<Coin> cancel() {
        if (!selectedShelve.isPresent()) {
            throw new IllegalStateException("No shelve is selected!");
        }

        BigDecimal moneyToReturn = insertedMoney.get();
        Optional<List<Coin>> coinsToReturn = cassette.getCoins(moneyToReturn);

        if (!coinsToReturn.isPresent()) {
            throw new IllegalStateException("There should be coins to cover " + moneyToReturn + "!");
        } else {
            resetMachineState();
            return coinsToReturn.get();
        }
    }

    private void addCoinToInsertedMoney(Coin coin) {
        BigDecimal coinValue = coin.getValue();
        insertedMoney = insertedMoney.map(
            currentMoney -> currentMoney.add(coinValue)
        );
    }

    private void initMachineState(Optional<Shelve> selectedShelve) {
        this.selectedShelve = selectedShelve;
        insertedMoney = Optional.of(BigDecimal.ZERO);
    }

    private void resetMachineState() {
        selectedShelve = Optional.empty();
        insertedMoney = Optional.empty();
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
