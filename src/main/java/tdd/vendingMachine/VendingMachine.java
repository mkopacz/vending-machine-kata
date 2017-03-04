package tdd.vendingMachine;

import tdd.vendingMachine.exception.InvalidShelveException;

import java.util.List;

public interface VendingMachine {

    List<Integer> listShelveNumbers();

    void selectShelve(int shelveNumber) throws InvalidShelveException;

}
