package be.llodavid.api.orders;

import be.llodavid.api.customers.CustomerDTO;
import be.llodavid.api.shopping.ItemGroupDTO;

import java.util.List;

public class ItemsShippingTodayDTO {
    public CustomerDTO customerDTO;
    public List<ItemGroupDTO> itemGroupDTOS;

    public ItemsShippingTodayDTO withCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
        return this;
    }

    public ItemsShippingTodayDTO withItemGroupDTOS(List<ItemGroupDTO> itemGroupDTOS) {
        this.itemGroupDTOS = itemGroupDTOS;
        return this;
    }
}
