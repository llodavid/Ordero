package be.llodavid.api.items;

import be.llodavid.domain.items.Item;
import be.llodavid.domain.orders.StockSupplyLevel;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
public class ItemMapper {
    public ItemDTO itemToDTO (Item item) {
        return new ItemDTO()
                    .withItemId(item.getId())
                .withName(item.getName())
                .withDescription(item.getDescription())
                .withPrice(item.getPrice())
                .withStock(item.getStock());
    }
    public Item dtoToItem (ItemDTO itemDTO) {
        return new Item.ItemBuilder()
                .withName(itemDTO.name)
                .withDescription(itemDTO.description)
                .withPrice(itemDTO.price)
                .withStock(itemDTO.stock)
                .build();
    }

    public ItemStockLevelDTO toItemStockLevelDTO(Map.Entry<StockSupplyLevel, List<Item>> itemStockLevel) {
        return new ItemStockLevelDTO()
                .withStockSupplyLevel(itemStockLevel.getKey().toString())
                .withItemDTOS(itemStockLevel.getValue().stream()
                        .map(item -> itemToDTO(item))
                        .collect(Collectors.toList()));
    }
}
