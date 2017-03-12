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
        Shelve shelve = getShelveWithProductQuantity(1);

        assertThat(shelve.isProductAvailable()).isTrue();
    }

    @Test
    public void shouldShowThatProductIsNotAvailable() {
        Shelve shelve = getShelveWithProductQuantity(0);

        assertThat(shelve.isProductAvailable()).isFalse();
    }

    @Test
    public void shouldReleaseProductWhenItIsAvailable() {
        Shelve shelve = getShelveWithProductQuantity(1);

        assertThat(shelve.releaseProduct()).isPresent();
    }

    @Test
    public void shouldNotReleaseProductWhenItIsNotAvailable() {
        Shelve shelve = getShelveWithProductQuantity(0);

        assertThat(shelve.releaseProduct()).isEmpty();
    }

    @Test
    public void shouldNotReleaseProductWhenItIsNotAvailableAnymore() {
        Shelve shelve = getShelveWithProductQuantity(1);

        shelve.releaseProduct();

        assertThat(shelve.releaseProduct()).isEmpty();
    }

    private Shelve getShelveWithProductQuantity(int quantity) {
        return new Shelve(1, productMock, quantity);
    }

}
