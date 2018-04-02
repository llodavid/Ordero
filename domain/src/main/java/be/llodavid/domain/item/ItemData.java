package be.llodavid.domain.item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ItemData {
    public List<Item> getDefaultItems() {
        return Arrays.asList(Item.ItemBuilder.buildItem()
                        .withName("LG 55 inch OLED TV")
                        .withDescription("TV")
                        .withPrice(new BigDecimal(1500))
                        .withStock(6)
                        .build(),
                Item.ItemBuilder.buildItem()
                        .withName("Chair with two paws")
                        .withDescription("extra paws cost extra")
                        .withPrice(new BigDecimal(70))
                        .withStock(14)
                        .build(),
                Item.ItemBuilder.buildItem()
                        .withName("Lego blocks")
                        .withDescription("It's just simple blocks man, you can put them together and stuff.")
                        .withPrice(new BigDecimal(10))
                        .withStock(3)
                        .build());
    }
}
