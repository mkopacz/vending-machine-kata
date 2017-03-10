package tdd.vendingMachine.display;

public class ConsoleDisplay implements Display {

    private static final String WARNING_PREFIX = "WARNING! ";

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayWarning(String warning) {
        System.out.println(WARNING_PREFIX + warning);
    }

}
