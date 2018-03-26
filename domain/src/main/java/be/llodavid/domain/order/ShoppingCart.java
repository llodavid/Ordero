package be.llodavid.domain.order;

import be.llodavid.domain.item.Item;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class    ShoppingCart {

    private int customerId;
    private List<ItemGroup> orderItems;

    public ShoppingCart(int customerId) {
        this.customerId = customerId;
        orderItems=new ArrayList<>();
    }

    public void addItem(ItemGroup itemGroup) {
        orderItems.add(itemGroup);
    }

    public Order createOrder(LocalDate orderDate)  {
        return new Order(customerId,orderItems);
    }

    public List<ItemGroup> getShoppingCartContent() {
        return orderItems;
    }

    public BigDecimal calculateBasketTotal() {
        return orderItems.stream()
                .map(itemGroup -> itemGroup.calculateItemGroupTotal())
                .reduce(BigDecimal.ZERO,
                        (orderTotal, itemGroupTotal)-> orderTotal.add(itemGroupTotal));
    }
}
