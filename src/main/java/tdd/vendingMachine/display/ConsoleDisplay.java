package tdd.vendingMachine.display;

public class ConsoleDisplay implements Display {

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

}
