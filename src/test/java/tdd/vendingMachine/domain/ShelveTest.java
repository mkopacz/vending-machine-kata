package tdd.vendingMachine.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ShelveTest {

    private Product productMock;

    @Before
    public void setUp() {
        productMock = mock(Product.class);
    }

    @Test
    public void shouldShowThatProductIsAvailable() {
        Shelve shelve = new Shelve(1, productMock, 1);

        boolean isProductAvailable = shelve.isProductAvailable();

        assertThat(isProductAvailable).isTrue();
    }

    @Test
    public void shouldShowThatProductIsNotAvailable() {
        Shelve shelve = new Shelve(1, productMock, 0);

        boolean isProductAvailable = shelve.isProductAvailable();

        assertThat(isProductAvailable).isFalse();
    }

}
