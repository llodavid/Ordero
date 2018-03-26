package be.llodavid.domain.order;

import be.llodavid.domain.RepositoryRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static be.llodavid.domain.order.OrderStatus.*;

public class Order implements RepositoryRecord {

    private int orderId;
    private int customerId;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<ItemGroup> orderItems;
    private BigDecimal totalAmount;

    public Order(int customerId, List<ItemGroup> orderItems) {
        this.customerId = customerId;
        verifyIfAtLeastOneItemGroup(orderItems);
        this.orderItems = orderItems;
        orderStatus = CREATED;
    }

    private void verifyIfAtLeastOneItemGroup(List<ItemGroup> orderItems) {
        if (orderItems == null || orderItems.size()<1) {
            throw new IllegalArgumentException("Please add at least 1 item to the order.");
        }
    }

    //TODO: discuss this with Niels
    public void finishOrder(LocalDate orderDate) {
        verifyIfOrderAlreadyPaid();
        orderStatus = PAID;
        this.orderDate = orderDate;
        setShippingDate(orderDate);
        calculateOrderTotal();
    }

    private void verifyIfOrderAlreadyPaid() {
        if (orderStatus != CREATED) {
            throw new IllegalArgumentException("Order has already been paid.");
        }
    }

    private void calculateOrderTotal() {
        totalAmount = orderItems.stream()
                .map(itemGroup -> itemGroup.calculateItemGroupTotal())
                .reduce(BigDecimal.ZERO,
                        (orderTotal, itemGroupTotal) -> orderTotal.add(itemGroupTotal));
    }


    private void setShippingDate(LocalDate orderDate) {
        orderItems.forEach(itemGroup -> itemGroup.calculateShippingDate(orderDate));
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<ItemGroup> getOrderItems() {
        return orderItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void setId(int valueId) {
        this.orderId = valueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId);
    }
}
