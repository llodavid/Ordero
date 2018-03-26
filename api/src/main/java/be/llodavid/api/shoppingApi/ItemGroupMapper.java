package be.llodavid.api.shoppingApi;

import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;

import javax.inject.Named;

@Named
public class ItemGroupMapper {
    public ItemGroupDTO ItemGroupToDTO(ItemGroup itemGroup) {
        return new ItemGroupDTO()
                .withItemId(itemGroup.getItemId())
                .withName(itemGroup.getName())
                .withDescription(itemGroup.getDescription())
                .withAmount(itemGroup.getAmount())
                .withPrice(itemGroup.getPrice())
                .withShippingDays(itemGroup.getShippingDays())
                .withShippingDate(itemGroup.getShippingDate());
    }

    public ItemGroup ToItemGroup(ItemGroupDTO itemGroupDTO, Item item) {
        //We don't use Item from ItemGroup as to avoid manipulation from Front-End
        return new ItemGroup(item, itemGroupDTO.amount);
    }
}
