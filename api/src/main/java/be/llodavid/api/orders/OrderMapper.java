package be.llodavid.api.orders;

import be.llodavid.api.customers.CustomerMapper;
import be.llodavid.api.shopping.ItemGroupMapper;
import be.llodavid.domain.customers.Customer;
import be.llodavid.util.helperClasses.BelgianDateFormatter;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
public class OrderMapper {
    private ItemGroupMapper itemGroupMapper;
    private CustomerMapper customerMapper;

    //TODO ask Niels: is it ok to use another mapper here?
    @Inject
    public OrderMapper(ItemGroupMapper itemGroupMapper, CustomerMapper customerMapper) {
        this.itemGroupMapper = itemGroupMapper;
        this.customerMapper = customerMapper;
    }

    public OrderDTO orderToDTO(Order order) {
        return new OrderDTO()
                .withOrderId(order.getId())
                .withCustomerId(order.getCustomerId())
                .withOrderDate(BelgianDateFormatter.dateToString(order.getOrderDate()))
                .withOrderStatus(order.getOrderStatus())
                .withOrderItems(order.getOrderItems().stream()
                        .map(itemGroup -> itemGroupMapper.ItemGroupToDTO(itemGroup))
                        .collect(Collectors.toList()))
                .withTotalAmount(order.calculateOrderTotal());
    }

    public ItemsShippingTodayDTO toItemsShippingTodayDTO(Map.Entry<Customer, List<ItemGroup>> itemsShippingToday) {
        return new ItemsShippingTodayDTO()
                .withCustomerDTO(customerMapper.customerToDTO(itemsShippingToday.getKey()))
                .withItemGroupDTOS(itemsShippingToday.getValue().stream()
                        .map(itemGroup -> itemGroupMapper.ItemGroupToDTO(itemGroup))
                        .collect(Collectors.toList()));
    }
}
