package be.llodavid.domain.orders;

import be.llodavid.domain.RepositoryRecord;
import be.llodavid.util.exceptions.OrderoException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name="ORDERS")
public class Order {
    @Id
    @SequenceGenerator(name = "order_generator", sequenceName = "order_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @Column(name = "ORDER_ID")
    private long orderId;
    @Column(name = "FK_CUSTOMER_ID")
    private long customerId;
    @Column(name = "ORDERSTATUS")
    private OrderStatus orderStatus;
    @Column(name = "ORDERDATE")
    private LocalDate orderDate;

    @OneToMany (cascade = {CascadeType.ALL, CascadeType.REMOVE})
    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name="FK_ORDER_ID", nullable=false)
    private List<ItemGroup> orderItems;

    public Order(long customerId, List<ItemGroup> orderItems) {
        this.customerId = customerId;
        verifyIfAtLeastOneItemGroup(orderItems);
        this.orderItems = orderItems;
        orderStatus = OrderStatus.CREATED;
    }
    public void addItemGroup(ItemGroup itemGroup) {
        this.orderItems.add(itemGroup);
    }

    public Order() {
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
    public long getId() {
        return orderId;
    }

    public long getCustomerId() {
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

    public void setId(long valueId) {
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
