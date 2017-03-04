package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelve;
import tdd.vendingMachine.exception.InvalidShelveException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BasicVendingMachineTest {

    private Display display;
    private List<Shelve> shelves;

    @Before
    public void setUp() {
        display = spy(Display.class);

        shelves = new ArrayList<>();
        shelves.add(new Shelve(1, new Product("cola drink", "2.44")));
        shelves.add(new Shelve(2, new Product("chocolate bar", "9.99")));
        shelves.add(new Shelve(3, new Product("mineral water", "109.90")));
    }

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, display);

        List<Integer> shelveNumbers = vendingMachine.listShelveNumbers();

        assertThat(shelveNumbers).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldDisplayProductPriceWhenSelectedShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, display);

        vendingMachine.selectShelve(2);

        verify(display).displayMessage("chocolate bar 9.99");
    }

    @Test(expected = InvalidShelveException.class)
    public void shouldThrowExceptionWhenSelectedInvalidShelve() throws InvalidShelveException {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves, display);

        vendingMachine.selectShelve(0);
    }

}
