package tdd.vendingMachine.display;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleDisplayTest {

    private Display consoleDisplay;
    private ByteArrayOutputStream consoleOutput;

    @Before
    public void setUp() {
        consoleDisplay = new ConsoleDisplay();
        consoleOutput = new ByteArrayOutputStream();

        System.setOut(new PrintStream(consoleOutput));
    }

    @Test
    public void shouldDisplayMessage() {
        String message = "example message";

        consoleDisplay.displayMessage(message);

        assertThat(consoleOutput.toString()).isEqualTo(message + "\n");
    }

    @Test
    public void shouldDisplayWarning() {
        String warning = "example warning";

        consoleDisplay.displayWarning(warning);

        assertThat(consoleOutput.toString()).isEqualTo("WARNING! " + warning + "\n");
    }

}
