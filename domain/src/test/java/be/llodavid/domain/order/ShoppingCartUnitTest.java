package be.llodavid.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;

import static be.llodavid.domain.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartUnitTest {

    private ShoppingCart shoppingCart;
    private ItemGroup itemGroup1, itemGroup2;
    private Order order;

    @Before
    public void setUp() throws Exception {
        shoppingCart = new ShoppingCart(1);

        itemGroup1 = mock(ItemGroup.class);
        itemGroup2 = mock(ItemGroup.class);
        order = mock(Order.class);

        when(itemGroup1.calculateItemGroupTotal()).thenReturn(new BigDecimal(100));
        when(itemGroup2.calculateItemGroupTotal()).thenReturn(new BigDecimal(200));
    }

    @Test
    public void addItem() {
        shoppingCart.addItem(itemGroup1);
        shoppingCart.addItem(itemGroup2);

        assertThat(shoppingCart.getShoppingCartContent()).containsExactly(itemGroup1, itemGroup2);
    }

    @Test
    public void createOrder() {
        shoppingCart.addItem(itemGroup1);
        shoppingCart.addItem(itemGroup2);

        Mockito.when(order.getCustomerId()).thenReturn(1);
        Mockito.when(order.getOrderItems()).thenReturn(Arrays.asList(itemGroup1, itemGroup2));
        Mockito.when(order.getOrderStatus()).thenReturn(CREATED);

        Order cartOrder = shoppingCart.createOrder();
        //TODO discuss with Niels: is this still a decent unit test?
        assertThat(cartOrder.getCustomerId()).isEqualTo(order.getCustomerId());
        assertThat(cartOrder.getOrderItems()).isEqualTo(order.getOrderItems());
        assertThat(cartOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    public void getShoppingCartContent_happyPath() {
        shoppingCart.addItem(itemGroup1);
        shoppingCart.addItem(itemGroup2);

        assertThat(shoppingCart.getShoppingCartContent()).containsExactly(itemGroup1, itemGroup2);
    }

    @Test
    public void calculateBasketTotal_happyPath() {
        shoppingCart.addItem(itemGroup1);
        shoppingCart.addItem(itemGroup2);

        assertThat(shoppingCart.calculateBasketTotal()).isEqualTo(new BigDecimal(300));
    }
}