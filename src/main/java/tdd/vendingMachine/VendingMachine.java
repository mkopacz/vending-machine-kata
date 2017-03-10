package tdd.vendingMachine;

import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.exception.CoinNotAcceptableException;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;

import java.util.List;

public interface VendingMachine {

    List<Integer> listShelveNumbers();

    void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException;

    void insertCoin(Coin coin) throws CoinNotAcceptableException;

    boolean insertedEnoughMoney();

    List<Coin> cancel();

    Purchase dispenseProduct();

}
