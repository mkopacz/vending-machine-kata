package tdd.vendingMachine.exception;

public class InvalidShelveException extends Exception {

    public InvalidShelveException(int shelveNumber) {
        super("Shelve number " + shelveNumber + " is invalid!");
    }

}
