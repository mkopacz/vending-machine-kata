package tdd.vendingMachine.display;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleDisplayTest {

    private ByteArrayOutputStream consoleOutput;

    @Before
    public void setUp() {
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
    }

    @Test
    public void shouldDisplayMessage() {
        Display display = new ConsoleDisplay();
        String message = "example message";

        display.displayMessage(message);

        assertThat(consoleOutput.toString()).isEqualTo(message + "\n");
    }

}
