package tdd.vendingMachine.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void shouldRoundProductPrice() {
        Product product = new Product("product", "19.9876");

        BigDecimal price = product.getPrice();

        assertThat(price).isEqualTo(new BigDecimal("19.99"));
    }

}
