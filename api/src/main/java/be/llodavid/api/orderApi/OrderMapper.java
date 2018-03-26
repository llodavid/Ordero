package be.llodavid.api.orderApi;

import be.llodavid.api.shoppingApi.ItemGroupMapper;
import be.llodavid.domain.order.Order;

import javax.inject.Named;
import java.util.stream.Collectors;

@Named
public class OrderMapper {
    ItemGroupMapper itemGroupMapper;
    //TODO ask Niels: is it ok to use another mapper here?
    public OrderMapper(ItemGroupMapper itemGroupMapper) {
        this.itemGroupMapper=itemGroupMapper;
    }

    public OrderDTO orderToDTO (Order order) {
        return new OrderDTO()
                .withOrderId(order.getId())
                .withCustomerId(order.getCustomerId())
                .withOrderDate(order.getOrderDate())
                .withOrderStatus(order.getOrderStatus())
                .withOrderItems(order.getOrderItems().stream()
                        .map(itemGroup->itemGroupMapper.ItemGroupToDTO(itemGroup))
                        .collect(Collectors.toList()))
                .withTotalAmount(order.getTotalAmount());
    }
}
