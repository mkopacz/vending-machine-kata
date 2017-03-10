package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.*;
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
        if (selectedShelve.isPresent() || insertedMoney.isPresent()) {
            throw new IllegalStateException("Either selected shelve or inserted money has been already initialized!");
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
            displaySelectedShelveMessage(foundShelve.get());
        }
    }

    @Override
    public void insertCoin(Coin coin) throws CoinNotAcceptableException {
        if (!selectedShelve.isPresent() || !insertedMoney.isPresent()) {
            throw new IllegalStateException("Either selected shelve or inserted money hasn't been yet initialized!");
        }

        cassette.putCoin(coin);
        addCoinToInsertedMoney(coin);
        displayInsertedCoinMessage(selectedShelve.get(), insertedMoney.get());
    }

    @Override
    public boolean insertedEnoughMoney() {
        if (!selectedShelve.isPresent() || !insertedMoney.isPresent()) {
            throw new IllegalStateException("Either selected shelve or inserted money hasn't been yet initialized!");
        }

        BigDecimal productPrice = selectedShelve.get().getProductPrice();
        return insertedMoney.get().compareTo(productPrice) >= 0;
    }

    @Override
    public List<Coin> cancel() {
        if (!selectedShelve.isPresent() || !insertedMoney.isPresent()) {
            throw new IllegalStateException("Either selected shelve or inserted money hasn't been yet initialized!");
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

    @Override
    public Purchase dispenseProduct() {
        if (!selectedShelve.isPresent() || !insertedMoney.isPresent()) {
            throw new IllegalStateException("Either selected shelve or inserted money hasn't been yet initialized!");
        } else if (!insertedEnoughMoney()) {
            throw new IllegalStateException("Not enough money has been inserted!");
        }

        BigDecimal changeAmount = calculateChange(selectedShelve.get(), insertedMoney.get());
        Optional<List<Coin>> changeInCoins = cassette.getCoins(changeAmount);

        if (!changeInCoins.isPresent()) {
            List<Coin> insertedMoneyInCoins = cancel();
            return new Purchase(null, insertedMoneyInCoins);
        } else {
            Optional<Product> product = selectedShelve.get().releaseProduct();
            if (!product.isPresent()) {
                throw new IllegalStateException("There should be product to release!");
            } else {
                resetMachineState();
                return new Purchase(product.get(), changeInCoins.get());
            }
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

    private void displaySelectedShelveMessage(Shelve selectedShelve) {
        String productName = selectedShelve.getProductName();
        String productPrice = selectedShelve.getProductPrice().toString();
        display.displayMessage(productName + " " + productPrice);
    }

    private void displayInsertedCoinMessage(Shelve selectedShelve, BigDecimal insertedMoney) {
        String productName = selectedShelve.getProductName();
        BigDecimal productPrice = selectedShelve.getProductPrice();
        BigDecimal moneyLeft = productPrice.subtract(insertedMoney);
        display.displayMessage(productName + " " + moneyLeft);
    }

    private BigDecimal calculateChange(Shelve selectedShelve, BigDecimal insertedMoney) {
        BigDecimal productPrice = selectedShelve.getProductPrice();
        return insertedMoney.subtract(productPrice);
    }

}
