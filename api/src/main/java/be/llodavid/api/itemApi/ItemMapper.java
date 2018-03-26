package be.llodavid.api.itemApi;

import be.llodavid.domain.item.Item;

import javax.inject.Named;

@Named
public class ItemMapper {
    public ItemDTO itemToDTO (Item item) {
        return new ItemDTO()
                .withCustomerId(item.getId())
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
}
