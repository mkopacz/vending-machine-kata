package tdd.vendingMachine.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

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

    @Test
    public void shouldReleaseProductWhenItIsAvailable() {
        Shelve shelve = new Shelve(1, new Product("product", "1.99"), 1);

        Optional<Product> product = shelve.releaseProduct();

        assertThat(product).isPresent();
    }

    @Test
    public void shouldNotReleaseProductWhenItIsNotAvailable() {
        Shelve shelve = new Shelve(1, new Product("product", "1.99"), 0);

        Optional<Product> product = shelve.releaseProduct();

        assertThat(product).isEmpty();
    }

    @Test
    public void shouldNotReleaseProductWhenItIsNotAvailableAnymore() {
        Shelve shelve = new Shelve(1, new Product("product", "1.99"), 1);

        shelve.releaseProduct();
        Optional<Product> product = shelve.releaseProduct();

        assertThat(product).isEmpty();
    }

}
