package be.llodavid.api.items;

import be.llodavid.domain.items.Item;
import be.llodavid.domain.orders.StockSupplyLevel;
import be.llodavid.util.exceptions.OrderoException;

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
    public Item dtoToUpdatedItem(ItemDTO itemDTO, long itemId) {
        Item updatedItem = new Item.ItemBuilder()
                .withName(itemDTO.name)
                .withDescription(itemDTO.description)
                .withPrice(itemDTO.price)
                .withStock(itemDTO.stock)
                .build();

        if (itemDTO.itemId==0 || itemDTO.itemId==itemId) {
            updatedItem.setId(itemId);
            return  updatedItem;
        }
        else {
            throw new OrderoException("provided itemID does not correspond with the original item ID");
        }
    }

    public ItemStockLevelDTO toItemStockLevelDTO(Map.Entry<StockSupplyLevel, List<Item>> itemStockLevel) {
        return new ItemStockLevelDTO()
                .withStockSupplyLevel(itemStockLevel.getKey().toString())
                .withItemDTOS(itemStockLevel.getValue().stream()
                        .map(item -> itemToDTO(item))
                        .collect(Collectors.toList()));
    }

    public Item dtoToItem(ItemDTO item) {
        return this.dtoToUpdatedItem(item,0);
    }
}
