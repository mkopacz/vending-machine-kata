package tdd.vendingMachine;

import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicVendingMachine implements VendingMachine {

    private final List<Shelve> shelves;
    private final Display display;

    private Optional<Shelve> selectedShelve = Optional.empty();

    public BasicVendingMachine(List<Shelve> shelves, Display display) {
        this.shelves = shelves;
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
        selectedShelve = shelves.stream()
            .filter(shelve -> shelve.getNumber() == shelveNumber)
            .findFirst();

        if (selectedShelve.isPresent()) {
            displaySelectedShelveMessage();
        } else {
            throw new InvalidShelveException(shelveNumber);
        }
    }

    private void displaySelectedShelveMessage() {
        String productName = selectedShelve.get().getProductName();
        String productPrice = selectedShelve.get().getProductPrice().toString();
        display.displayMessage(productName + " " + productPrice);
    }

}
