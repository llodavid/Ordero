package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.ShoppingCart;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingServiceUnitTest {

    private ShoppingService shoppingService;
    private ShoppingCart shoppingCart1;
    private ItemGroup itemGroup1, itemGroup2, itemGroup3;
    private OrderService orderService;
    private Order order;
    private ItemService itemService;
    private CustomerService customerService;
    private Repository<Order> orderRepository;

    @Before
    public void setUp() throws Exception {
        shoppingCart1 = mock(ShoppingCart.class);
        itemGroup1 = mock(ItemGroup.class);
        itemGroup2 = mock(ItemGroup.class);
        itemGroup3 = mock(ItemGroup.class);
        itemService = mock(ItemService.class);
        customerService = mock(CustomerService.class);
        orderService = mock(OrderService.class);
        orderRepository = mock(Repository.class);
        order = mock(Order.class);
        shoppingService = new ShoppingService(orderService, customerService, itemService);
    }

    @Test
    public void addItemToCart_happyPath() {
//        when(itemService.createItemGroup(1, 1)).thenReturn(itemGroup1);
//        when(itemService.createItemGroup(3, 1)).thenReturn(itemGroup3);
        shoppingService.addItemToCart(itemGroup1, 1);
        shoppingService.addItemToCart(itemGroup2, 2);
        shoppingService.addItemToCart(itemGroup3, 1);
        assertThat(shoppingService.getShoppingCartContent(1)).containsExactlyInAnyOrder(itemGroup1, itemGroup3);
    }

    //TODO: not a good unit test... improve code and test
    @Test
    public void createOrderFromShoppingCart_givenCustomerWithShoppingCart_createsOrder() {
        when(customerService.customerExists(1)).thenReturn(true);
        shoppingService.addItemToCart(itemGroup1, 1);
        when(orderService.createOrder(shoppingService.getShoppingCart(1))).thenReturn(order);

        assertThat(shoppingService.createOrderFromShoppingCart(1)).isEqualTo(order);
    }

    @Test
    public void getShoppingCartContent_givenCustomerWithShoppingCart_returnsShoppingCart() {
        when(itemService.createItemGroup(1, 1)).thenReturn(itemGroup1);
        shoppingService.addItemToCart(itemGroup1, 1);
        assertThat(shoppingService.getShoppingCartContent(1)).containsExactly(itemGroup1);
        assertThat(shoppingService.getShoppingCartContent(1).size()).isEqualTo(1);
    }

    @Test
    public void getShoppingCartContent_givenCustomerWithoutShoppingCart_returnsNewArrayList() {
        assertThat(shoppingService.getShoppingCartContent(1)).isEqualTo(new ArrayList<>());
        assertThat(shoppingService.getShoppingCartContent(1).size()).isEqualTo(0);
    }

    @Test
    public void clearShoppingCart() {
        when(itemService.createItemGroup(1, 1)).thenReturn(itemGroup1);
        shoppingService.addItemToCart(itemGroup1, 1);
        shoppingService.clearShoppingCart(1);
        assertThat(shoppingService.getShoppingCartContent(1).size()).isEqualTo(0);
    }

    //not really a unit tests as it uses shoppingCart.
    @Test
    public void getShoppingCart_givenCustomerAndItem_returnsCustomerShoppingCart() {
        when(customerService.customerExists(1)).thenReturn(true);
        when(itemService.createItemGroup(1, 1)).thenReturn(itemGroup1);

        shoppingService.addItemToCart(itemGroup1, 1);
        ShoppingCart shoppingCart = new ShoppingCart(1);
        shoppingCart.addItem(itemGroup1);
        assertThat(shoppingService.getShoppingCart(1)).isEqualTo(shoppingCart);
        assertThat(shoppingService.getShoppingCart(1).getShoppingCartContent()).containsExactly(itemGroup1);
    }
}