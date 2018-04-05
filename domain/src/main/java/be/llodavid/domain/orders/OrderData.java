package be.llodavid.domain.orders;

import be.llodavid.domain.items.Item;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class OrderData {
    public List<Order> getDefaultOrders() {
        Item item1, item2;
        item1 = Item.ItemBuilder.buildItem()
                .withName("LG 55 inch OLED TV")
                .withDescription("TV")
                .withPrice(new BigDecimal(1500))
                .withStock(6)
                .build();
        item2 = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        item1.setId(1);
        item2.setId(2);
        Order order1 = new Order(1,
                Arrays.asList(
                        new ItemGroup(item1, 2),
                        new ItemGroup(item2, 1)));
        order1.finishOrder(LocalDate.now().minusDays(1));
        Order order2 = new Order(2,
                Arrays.asList(
                        new ItemGroup(item1, 2),
                        new ItemGroup(item2, 1)));
        order2.finishOrder(LocalDate.now().minusDays(2));
        return Arrays.asList(order1,
                order2);

    }
}
