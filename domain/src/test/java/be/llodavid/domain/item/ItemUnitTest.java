package be.llodavid.domain.item;

import be.llodavid.util.exceptions.OrderoException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ItemUnitTest {
    private Item item1;
    @Before
    public void setUp() throws Exception {
        item1 = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();

    }

    @Test
    public void addItemsToStock_happyPath() {
        Item item = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();

        item.addItemsToStock(10);
        Assertions.assertThat(item.getStock()).isEqualTo(24);
    }

    @Test
    public void addItemsToStock_givenNegativeAmount_throwsException() {
        Assertions.assertThatExceptionOfType(OrderoException.class).isThrownBy(()-> item1.addItemsToStock(-10));
    }

    @Test
    public void correctStock_happyPath() {
        Item item = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();

        item.correctStock(10);
        Assertions.assertThat(item.getStock()).isEqualTo(10);
    }

    @Test
    public void correctStock_givenNegativeAmount_ThrowsException() {
        Assertions.assertThatExceptionOfType(OrderoException.class).isThrownBy(()-> item1.correctStock(-10));
    }

    @Test
    public void build_givenAllRequiredFields_returnsObject(){
        Item item = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        Assertions.assertThat(item.getDescription()).isEqualTo("extra paws cost extra");
        Assertions.assertThat(item.getName()).isEqualTo("Chair with two paws");
        Assertions.assertThat(item.getPrice()).isEqualTo(new BigDecimal(70));
        Assertions.assertThat(item.getStock()).isEqualTo(14);
    }

    @Test
    public void build_givenMissingRequiredFields_throwsException() {
        Assertions.assertThatExceptionOfType(OrderoException.class).isThrownBy(()->Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withStock(14)
                .build());
    }
}