package be.llodavid.domain.orders;

import be.llodavid.domain.RepositoryRecord;
import be.llodavid.util.exceptions.OrderoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Order implements RepositoryRecord {

    private int orderId;
    private int customerId;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<ItemGroup> orderItems;

    public Order(int customerId, List<ItemGroup> orderItems) {
        this.customerId = customerId;
        verifyIfAtLeastOneItemGroup(orderItems);
        this.orderItems = orderItems;
        orderStatus = OrderStatus.CREATED;
    }

    private void verifyIfAtLeastOneItemGroup(List<ItemGroup> orderItems) {
        if (orderItems == null || orderItems.size()<1) {
            throw new OrderoException("Please add at least 1 items to the orders.");
        }
    }

    //TODO: discuss this with Niels
    public void finishOrder(LocalDate orderDate) {
        verifyIfOrderAlreadyPaid();
        orderStatus = OrderStatus.PAID;
        this.orderDate = orderDate;
        setShippingDate(orderDate);
    }

    private void verifyIfOrderAlreadyPaid() {
        if (orderStatus != OrderStatus.CREATED) {
            throw new OrderoException("Order has already been paid.");
        }
    }

    public BigDecimal calculateOrderTotal() {
        return orderItems.stream()
                .map(itemGroup -> itemGroup.calculateItemGroupTotal())
                .reduce(BigDecimal.ZERO,
                        (orderTotal, itemGroupTotal) -> orderTotal.add(itemGroupTotal));
    }


    private void setShippingDate(LocalDate orderDate) {
        orderItems.forEach(itemGroup -> itemGroup.calculateShippingDate(orderDate));
    }
    @Override
    public int getId() {
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

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
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

    @Override
    public String toString() {
        BigDecimal orderTotal = BigDecimal.ZERO;
        return new StringBuilder("ORDER n° ")
                .append(orderId)
                .append("\n----------------\n")
                .append(orderItems.stream()
                        .map(itemGroup ->  itemGroup.toString())
                        .collect(Collectors.joining("\n")))
                .append("Order Total: ")
                .append(calculateOrderTotal().toPlainString())
                .append(" euro")
                .toString();
    }
}
