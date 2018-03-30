package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.item.Item;
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
    private Order order1, order2, order3;
    private Repository<Order> orderRepository;
    private OrderService orderService;
    private CustomerService customerService;
    private ItemService itemService;
    private OrderData orderData;
    private Customer customer;


    @Before
    public void setUp() throws Exception {
        orderRepository = mock(Repository.class);
        orderData = mock(OrderData.class);
        customerService = mock(CustomerService.class);
        itemService = mock(ItemService.class);
        orderService = new OrderService(orderRepository, customerService,itemService);
        customer = mock(Customer.class);

        order1 = mock(Order.class);
        order2 = mock(Order.class);
        order3= mock(Order.class);
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

    @Test
    public void getAllOrdersForCustomer_happyPath() {
        when(orderRepository.getAllRecords()).thenReturn(Arrays.asList(order1, order2, order3));
        when(order1.getCustomerId()).thenReturn(1);
        when(order2.getCustomerId()).thenReturn(2);
        when(order3.getCustomerId()).thenReturn(1);

        assertThat(orderService.getAllOrdersForCustomer(1)).containsOnly(order1, order3);
    }
}