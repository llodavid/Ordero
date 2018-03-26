package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.ShoppingCart;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.*;

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

    public Order createOrder(int customerId) {
        verifyIfCustomerExists(customerId);
        Order order = viewOrderBasedOnShoppingCart(customerId);
        itemService.modifyStock(order.getOrderItems());
//        verifyIfPaymentReceived(order.getTotalAmount(), payment);
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

    //Unnecessary, honestly the things I waste my time on....... Let's try and do it this way later on :-].
    //    private void verifyIfPaymentReceived(BigDecimal totalAmount, BigDecimal payment) {
    //        if (!payment.equals(totalAmount)) {
    //            throw new PaymentNotReceivedException(payment);
    //        }
    //    }
}
