package tdd.vendingMachine.state;

import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Purchase;
import tdd.vendingMachine.exception.InvalidShelveException;
import tdd.vendingMachine.exception.ProductNotAvailableException;
import tdd.vendingMachine.exception.UnacceptableCoinException;

import java.util.List;

public interface BasicVendingMachineState {

    void selectShelve(int shelveNumber) throws InvalidShelveException, ProductNotAvailableException;

    void insertCoin(Coin coin) throws UnacceptableCoinException;

    boolean insertedEnoughMoney();

    List<Coin> cancel();

    Purchase dispenseProduct();

}
