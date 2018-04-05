package be.llodavid.domain.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShoppingCart {

    private int customerId;
    private List<ItemGroup> orderItems;

    public ShoppingCart(int customerId) {
        this.customerId = customerId;
        orderItems=new ArrayList<>();
    }

    public void addItem(ItemGroup itemGroup) {
        orderItems.add(itemGroup);
    }

    public Order createOrder()  {
        return new Order(customerId,orderItems);
    }

    public List<ItemGroup> getShoppingCartContent() {
        return Collections.unmodifiableList(orderItems);
    }

    public BigDecimal calculateBasketTotal() {
        return orderItems.stream()
                .map(itemGroup -> itemGroup.calculateItemGroupTotal())
                .reduce(BigDecimal.ZERO,
                        (orderTotal, itemGroupTotal)-> orderTotal.add(itemGroupTotal));
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return customerId == that.customerId &&
                Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerId, orderItems);
    }
}
