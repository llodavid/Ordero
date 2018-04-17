package be.llodavid.service;

import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.orders.ShoppingCart;
import be.llodavid.util.exceptions.OrderoException;
import be.llodavid.util.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class ShoppingService {
    private Map<Long, ShoppingCart> shoppingCarts;
    private OrderService orderService;
    private CustomerService customerService;
    private ItemService itemService;

    //I use LinkedHashMap, so you always see the items in the same orders.
    @Inject
    public ShoppingService(OrderService orderService, CustomerService customerService, ItemService itemService) {
        shoppingCarts = new LinkedHashMap<>();
        this.orderService = orderService;
        this.customerService = customerService;
        this.itemService = itemService;
    }

    public ItemGroup addItemToShoppingCart(ItemGroup itemGroup, long customerId) {
        ShoppingCart shoppingCart = getCustomerShoppingCartOrCreateNew(customerId);
        shoppingCart.addItem(itemGroup);
        shoppingCarts.put(customerId, shoppingCart);
        return itemGroup;
    }

    private ShoppingCart getCustomerShoppingCartOrCreateNew(long customerId) {
        return shoppingCarts.containsKey(customerId) ?
                shoppingCarts.get(customerId) :
                new ShoppingCart(customerId);
    }

    private void verifyIfCustomerHasShoppingCart(long customerId) {
        if (shoppingCartIsEmpty(customerId)) {
            throw new UnknownResourceException("shopping cart",
                    String.format("customerID %s", String.valueOf(customerId)));
        }
    }

    public Order createOrderFromShoppingCart(long customerId) {
        customerService.verifyIfCustomerExists(customerId);
        verifyIfCustomerHasShoppingCart(customerId);
        Order order = orderService.addOrder(
                getShoppingCart(customerId).createOrder());
        clearShoppingCart(customerId);
        return order;
    }

    public List<ItemGroup> getShoppingCartContent(long customerId) {
        return shoppingCartIsEmpty(customerId) ?
                new ArrayList<>() :
                shoppingCarts.get(customerId).getShoppingCartContent();
    }

    private boolean shoppingCartIsEmpty(long customerId) {
        return !shoppingCarts.containsKey(customerId);
    }

    public void clearShoppingCart(long customerId) {
        shoppingCarts.remove(customerId);
    }

    public Order reOrder(long customerId, int orderId) {
        return orderService.addOrder(
                new Order(customerId,
                        refreshItemData(retrieveCustomerOrder(customerId, orderId))));
    }

    private List<ItemGroup> refreshItemData(Order order) {
        return order.getOrderItems().stream()
                .map(itemGroup -> itemService.createItemGroup(
                        itemGroup.getItemId(), itemGroup.getAmount()))
                .collect(Collectors.toList());
    }

    private Order retrieveCustomerOrder(long customerId, int orderId) {
        Order oldOrder = orderService.getOrder(orderId);
        validateReorder(customerId, oldOrder);
        return oldOrder;
    }

    private void validateReorder(long customerId, Order oldOrder) {
        customerService.verifyIfCustomerExists(customerId);
        verifyIfOrderIsOfCustomer(customerId, oldOrder.getCustomerId());
    }

    private void verifyIfOrderIsOfCustomer(long customerId, long orderCustomerId) {
        if (orderCustomerId != customerId) {
            throw new OrderoException("A customer can only re-orders one of their own orders");
        }
    }

    public ShoppingCart getShoppingCart(long customerId) {
        customerService.verifyIfCustomerExists(customerId);
        return shoppingCarts.get(customerId);
    }
}
