package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class OrderServiceUnitTest {
    Order order1;
    Order order2;
    private Repository<Order> orderRepository;
    private OrderService orderService;
    private CustomerService customerService;
    private ShoppingService shoppingService;
    private OrderData orderData;


    @Before
    public void setUp() throws Exception {
        orderRepository = mock(Repository.class);
        orderData = mock(OrderData.class);
        orderService = new OrderService(orderRepository);
        customerService = mock(CustomerService.class);
        shoppingService = mock(ShoppingService.class);

        order1 = mock(Order.class);
        order2 = mock(Order.class);
    }

    //Todo: Discuss this test with Niels
    //can't do this test until I add dependencies in constructor (or use spring boot test suite)
    @Test
    public void finishOrderInShoppingCart_happyPath() {
//        when(customerService.customerExists(1)).thenReturn(true);
//        when(orderRepository.addRecord(order1)).thenReturn(order1);
//        when(shoppingService.viewOrderBasedOnShoppingCart(1)).thenReturn(order1);
//
//        assertThat(orderService.viewOrderBasedOnShoppingCart(1)).isEqualTo(order1);
//        verify(shoppingService, times(1)).clearShoppingCart(1);
//        verify(order1, times(1)).finishOrder(LocalDate.now());
    }

    @Test
    public void createOrderFromShoppingCart() {
        //can't do it without proper Constructor dependency injection
    }

    @Test
    public void injectDefaultData_happyPath() {
        when(orderData.getDefaultOrders()).thenReturn(new ArrayList<>());
        orderService.injectDefaultData();
        verify(orderRepository, times(1)).injectDefaultData(new OrderData().getDefaultOrders());
    }
    
    @Test
    public void getOrder_happyPath() {
        when(orderRepository.getRecordById(1)).thenReturn(order1);
        when(orderRepository.recordExists(1)).thenReturn(true);
        assertThat(orderService.getOrder(1)).isEqualTo(order1);
    }

    @Test
    public void getOrder_givenOrderThatDoesNotExist_throwsException() {
        when(orderRepository.getRecordById(1)).thenReturn(order1);
        when(orderRepository.recordExists(1)).thenReturn(true);
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> orderService.getOrder(15)).withMessage("The order could not be found based on the provided order ID: 15.");
    }
    
    @Test
    public void getAllOrders_happyPath() {
        when(orderRepository.getAllRecords()).thenReturn(Arrays.asList(order1, order2));
        assertThat(orderService.getAllOrders()).isEqualTo(Arrays.asList(order1, order2));
    }
}