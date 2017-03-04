package tdd.vendingMachine;

import tdd.vendingMachine.domain.Shelve;

import java.util.List;
import java.util.stream.Collectors;

public class BasicVendingMachine implements VendingMachine {

    private final List<Shelve> shelves;

    public BasicVendingMachine(List<Shelve> shelves) {
        this.shelves = shelves;
    }

    @Override
    public List<Integer> listShelveNumbers() {
        return shelves.stream()
            .map(Shelve::getNumber)
            .collect(Collectors.toList());
    }

}
