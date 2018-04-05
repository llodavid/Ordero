package be.llodavid.api.shopping;

import be.llodavid.util.helperClasses.BelgianDateFormatter;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.orders.ItemGroup;

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
                .withItemGroupTotal(itemGroup.calculateItemGroupTotal())
                .withShippingDays(itemGroup.getShippingDays())
                .withShippingDate(BelgianDateFormatter.dateToString(itemGroup.getShippingDate()));
    }

    public ItemGroup ToItemGroup(ItemGroupDTO itemGroupDTO, Item item) {
        //We don't use Item from ItemGroup as to avoid manipulation from Front-End
        return new ItemGroup(item, itemGroupDTO.amount);
    }
}
