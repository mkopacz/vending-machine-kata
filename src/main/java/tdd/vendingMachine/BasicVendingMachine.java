package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicVendingMachine implements VendingMachine {

    private final List<Shelve> shelves;
    private final Map<Coin, Integer> coins;
    private final Display display;

    private Optional<Shelve> selectedShelve = Optional.empty();
    private Optional<BigDecimal> insertedMoney = Optional.empty();

    public BasicVendingMachine(List<Shelve> shelves, Map<Coin, Integer> coins, Display display) {
        this.shelves = shelves;
        this.coins = coins;
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

        coins.compute(coin, (k, v) -> v == null ? 1 : v + 1);

        BigDecimal coinValue = coin.getValue();
        insertedMoney = insertedMoney.map(
            currentMoney -> currentMoney.add(coinValue)
        );

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
