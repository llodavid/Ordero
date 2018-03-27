package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.ShoppingCart;
import be.llodavid.service.exceptions.OrderoException;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class ShoppingService {
    private Map<Integer, ShoppingCart> shoppingCarts;
    private Repository<Order> orderRepository;
    @Inject
    private CustomerService customerService;
    @Inject
    private ItemService itemService;

    //I use LinkedHashMap, so you always see the items in the same order.
    @Inject
    @Named("OrderRepo")
    public ShoppingService(Repository<Order> orderRepository) {
        shoppingCarts = new LinkedHashMap<>();
        this.orderRepository = orderRepository;
    }

    public ItemGroup addItemToCart(int itemId, int customerId) {
        ShoppingCart shoppingCart = getCustomerShoppingCartOrCreateNew(customerId);
        ItemGroup itemGroup = itemService.createItemGroup(itemId, customerId );
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

    public Order createOrder(int customerId) {
        verifyIfCustomerExists(customerId);
        Order order = viewOrderBasedOnShoppingCart(customerId);
        itemService.modifyStock(order.getOrderItems());
        clearShoppingCart(customerId);
        return orderRepository.addRecord(order);
    }

    private void verifyIfCustomerExists(int customerId) {
        if (!customerService.customerExists(customerId)) {
            throw new UnknownResourceException("customer", "customer ID: " + customerId);
        }
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
        Order order = retrieveCustomerOrder(customerId, orderId);
        itemService.modifyStock(order.getOrderItems());
        return orderRepository.addRecord(new Order(customerId,refreshItemData(order)));
    }

    private List<ItemGroup> refreshItemData(Order order) {
        return order.getOrderItems().stream()
                .map(itemGroup->
                        itemService.createItemGroup(itemGroup.getItemId(), order.getCustomerId()))
                .collect(Collectors.toList());
    }

    private Order retrieveCustomerOrder(int customerId, int orderId) {
        validateReorder(customerId, orderId);
        return orderRepository.getRecordById(orderId);
    }

    private void validateReorder(int customerId, int orderId) {
        verifyIfCustomerExists(customerId);
        verifyIfOrderExists(orderId);
        verifyIfOrderIsOfCustomer(customerId, orderId);
    }

    private void verifyIfOrderExists(int orderId) {
        if (!orderRepository.recordExists(orderId)) {
            throw new UnknownResourceException("order", "order ID: " + orderId);
        }
    }

    private void verifyIfOrderIsOfCustomer(int customerId, int orderId) {
        if (orderId != customerId) {
            throw new OrderoException("A customer can only re-order one of their own orders");
        }
    }
}
