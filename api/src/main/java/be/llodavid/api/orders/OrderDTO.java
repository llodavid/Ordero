package be.llodavid.api.orders;

import be.llodavid.api.shopping.ItemGroupDTO;
import be.llodavid.domain.orders.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    public int orderId;
    public int customerId;
    public OrderStatus orderStatus;
    //@JsonFormat(pattern = "dd::MM::yyyy")
    public String orderDate;
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

    public OrderDTO withOrderDate(String orderDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return customerId == orderDTO.customerId &&
                orderStatus == orderDTO.orderStatus &&
                Objects.equals(orderDate, orderDTO.orderDate) &&
                Objects.equals(orderItems, orderDTO.orderItems) &&
                Objects.equals(totalAmount, orderDTO.totalAmount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerId, orderStatus, orderDate, orderItems, totalAmount);
    }


}
