package be.llodavid.service;

import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.ShoppingCart;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShoppingServiceUnitTest {

    ShoppingService shoppingService;
    ShoppingCart shoppingCart1;
    ItemGroup itemGroup1, itemGroup2, itemGroup3;
    @Before
    public void setUp() throws Exception {
        shoppingCart1 = mock(ShoppingCart.class);
        itemGroup1 = mock(ItemGroup.class);
        itemGroup2 = mock(ItemGroup.class);
        itemGroup3 = mock(ItemGroup.class);
        shoppingService = new ShoppingService();
    }

    //TODO: Discuss with Niels - can't unit test this because i cant' mock the Carts, only the items in that cart.
    @Test
    public void addItemToCart_happyPath() {
        shoppingService.addItemToCart(itemGroup1, 1);
        shoppingService.addItemToCart(itemGroup2, 2);
        shoppingService.addItemToCart(itemGroup3, 1);
        assertThat(shoppingService.getShoppingCartContent(1)).containsExactlyInAnyOrder(itemGroup1, itemGroup3);
    }

    @Test
    public void createOrderFromShoppingCart() {
        //implement when above problem is solved
    }

    @Test
    public void getShoppingCartContent() {
        //implement when above problem is solved
    }

    @Test
    public void clearShoppingCart() {
        //implement when above problem is solved
    }
}