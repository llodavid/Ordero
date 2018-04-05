package be.llodavid.domain.orders;

import be.llodavid.domain.items.Item;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemGroupUnitTest {
    Item mockItem;
    ItemGroup itemGroup;
    @Before
    public void setUp() throws Exception {
        mockItem = Mockito.mock(Item.class);
        Mockito.when(mockItem.getStock()).thenReturn(5);
        Mockito.when(mockItem.getName()).thenReturn("test");
        Mockito.when(mockItem.getDescription()).thenReturn("test");
        Mockito.when(mockItem.getPrice()).thenReturn(new BigDecimal(70));
//        Item = Item.ItemBuilder.buildItem()
//                .withName("Chair with two paws")
//                .withDescription("extra paws cost extra")
//                .withPrice(new BigDecimal(70))
//                .withStock(5)
//                .build();
    }

    @Test
    public void calculateShippingDate_givenSufficientStock_returnsDateTomorrow() {
        itemGroup = new ItemGroup(mockItem, 5);
        itemGroup.calculateShippingDate(LocalDate.of(2018,03,24));

        Assertions.assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.of(2018,03,25));
    }

    @Test
    public void calculateShippingDate_givenInSufficientStock_returnsDatePlus7Days() {
        itemGroup = new ItemGroup(mockItem, 7);
        itemGroup.calculateShippingDate(LocalDate.of(2018,03,24));

        Assertions.assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.of(2018,03,31));
    }

    @Test
    public void calculateItemGroupTotal() {
        itemGroup = new ItemGroup(mockItem, 5);
        Assertions.assertThat(itemGroup.calculateItemGroupTotal()).isEqualTo(new BigDecimal(350));
    }
}