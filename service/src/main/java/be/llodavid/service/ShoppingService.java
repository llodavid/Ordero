package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.ShoppingCart;
import be.llodavid.service.exceptions.OrderoException;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class ShoppingService {
    private Map<Integer, ShoppingCart> shoppingCarts;
    private OrderService orderService;
    private CustomerService customerService;
    private ItemService itemService;

    //I use LinkedHashMap, so you always see the items in the same order.
    @Inject
    public ShoppingService(OrderService orderService, CustomerService customerService, ItemService itemService) {
        shoppingCarts = new LinkedHashMap<>();
        this.orderService = orderService;
        this.customerService = customerService;
        this.itemService = itemService;
    }

    public ItemGroup addItemToCart(ItemGroup itemGroup, int customerId) {
        ShoppingCart shoppingCart = getCustomerShoppingCartOrCreateNew(customerId);
        shoppingCart.addItem(itemGroup);
        shoppingCarts.put(customerId, shoppingCart);
        return itemGroup;
    }

    private ShoppingCart getCustomerShoppingCartOrCreateNew(int customerId) {
        return shoppingCarts.containsKey(customerId) ?
                shoppingCarts.get(customerId) :
                new ShoppingCart(customerId);
    }

    public Order viewOrderBasedOnShoppingCart(int customerId) {
        verifyIfCustomerHasShoppingCart(customerId);
        return shoppingCarts.get(customerId).createOrder();
    }

    private void verifyIfCustomerHasShoppingCart(int customerId) {
        if (shoppingCartIsEmpty(customerId)) {
            throw new UnknownResourceException("shopping cart",
                    String.format("customerID %s", String.valueOf(customerId)));
        }
    }

    public Order createOrderFromShoppingCart(int customerId) {
        customerService.verifyIfCustomerExists(customerId);
        Order order = orderService.createOrder(getShoppingCart(customerId));
        clearShoppingCart(customerId);
        return order;
    }

    public List<ItemGroup> getShoppingCartContent(int customerId) {
        return shoppingCartIsEmpty(customerId) ?
                new ArrayList<>() :
                shoppingCarts.get(customerId).getShoppingCartContent();
    }

    private boolean shoppingCartIsEmpty(int customerId) {
        return !shoppingCarts.containsKey(customerId);
    }

    public void clearShoppingCart(int customerId) {
        shoppingCarts.remove(customerId);
    }

    public Order reOrder(int customerId, int orderId) {
        ShoppingCart shoppingCart = new ShoppingCart(customerId);
        refreshItemData(retrieveCustomerOrder(customerId, orderId))
                .forEach(itemGroup -> shoppingCart.addItem(itemGroup));
        return orderService.createOrder(shoppingCart);
    }

    private List<ItemGroup> refreshItemData(Order order) {
        return order.getOrderItems().stream()
                .map(itemGroup -> itemService.createItemGroup(
                        itemGroup.getItemId(), order.getCustomerId()))
                .collect(Collectors.toList());
    }

    private Order retrieveCustomerOrder(int customerId, int orderId) {
        validateReorder(customerId, orderId);
        return orderService.getOrder(orderId);
    }

    private void validateReorder(int customerId, int orderId) {
        customerService.verifyIfCustomerExists(customerId);
        orderService.verifyIfOrderExists(orderId);
        verifyIfOrderIsOfCustomer(customerId, orderId);
    }

    private void verifyIfOrderIsOfCustomer(int customerId, int orderId) {
        if (orderId != customerId) {
            throw new OrderoException("A customer can only re-order one of their own orders");
        }
    }

    public ShoppingCart getShoppingCart(int customerId) {
        customerService.verifyIfCustomerExists(customerId);
        return shoppingCarts.get(customerId);
    }
}
