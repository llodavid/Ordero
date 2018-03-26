package be.llodavid.api.orderApi;

import be.llodavid.api.shoppingApi.ItemGroupDTO;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    public int orderId;
    public int customerId;
    public OrderStatus orderStatus;
    public LocalDate orderDate;
    public List<ItemGroupDTO> orderItems;
    public BigDecimal totalAmount;

    public OrderDTO withOrderId(int orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderDTO withCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderDTO withOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderDTO withOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderDTO withOrderItems(List<ItemGroupDTO> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public OrderDTO withTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

}
