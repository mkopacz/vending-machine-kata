package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.domain.Shelve;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicVendingMachineTest {

    private List<Shelve> shelves;

    @Before
    public void setUp() {
        shelves = new ArrayList<>();
        shelves.add(new Shelve(1));
        shelves.add(new Shelve(2));
        shelves.add(new Shelve(3));
    }

    @Test
    public void shouldListShelveNumbers() {
        VendingMachine vendingMachine = new BasicVendingMachine(shelves);

        List<Integer> shelveNumbers = vendingMachine.listShelveNumbers();

        assertThat(shelveNumbers).containsExactly(1, 2, 3);
    }

}
