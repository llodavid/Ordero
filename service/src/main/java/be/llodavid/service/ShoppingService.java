package be.llodavid.service;

import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.ShoppingCart;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class ShoppingService {
    private Map<Integer, ShoppingCart> shoppingCarts;

    public ShoppingService() {
        shoppingCarts = new HashMap<>();
    }

    public ItemGroup addItemToCart(ItemGroup itemGroup, int customerId) {
        ShoppingCart shoppingCart = getCustomerShoppingCartOrCreateNew(customerId);
        shoppingCart.addItem(itemGroup);
        shoppingCarts.put(customerId,shoppingCart);
        return itemGroup;
    }

    private ShoppingCart getCustomerShoppingCartOrCreateNew(int customerId) {
        return shoppingCarts.containsKey(customerId) ?
                shoppingCarts.get(customerId) :
                new ShoppingCart(customerId);
    }

    public Order createOrderFromShoppingCart(int customerId) {
        verifyIfCustomerHasShoppingCart(customerId);
        return shoppingCarts.get(customerId).createOrder(LocalDate.now());
    }

    private void verifyIfCustomerHasShoppingCart(int customerId) {
        if (shoppingCartIsEmpty(customerId)) {
            throw new UnknownResourceException("shopping cart",
                    String.format("customerID %s", String.valueOf(customerId)));
        }
    }

    public List<ItemGroup> getShoppingCartContent(int customerId) {
        return shoppingCartIsEmpty(customerId) ?
                new ArrayList<>():
                shoppingCarts.get(customerId).getShoppingCartContent();
    }

    private boolean shoppingCartIsEmpty(int customerId) {
        return !shoppingCarts.containsKey(customerId);
    }

    public void clearShoppingCart(int customerId) {
        shoppingCarts.remove(customerId);
    }
}
