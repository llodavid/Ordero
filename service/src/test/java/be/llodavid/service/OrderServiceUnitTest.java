package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.orders.OrderData;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class OrderServiceUnitTest {
    private Order order1, order2, order3;
    private ItemGroup itemGroup1, itemGroup2, itemGroup3;
    private Repository<Order> orderRepository;
    private OrderService orderService;
    private CustomerService customerService;
    private ItemService itemService;
    private OrderData orderData;
    private Customer customer, customer2;


    @Before
    public void setUp() throws Exception {
        orderRepository = mock(Repository.class);
        orderData = mock(OrderData.class);
        customerService = mock(CustomerService.class);
        itemService = mock(ItemService.class);
        orderService = new OrderService(orderRepository, customerService, itemService);
        customer = mock(Customer.class);
        customer2 = mock(Customer.class);
        itemGroup1 = mock(ItemGroup.class);
        itemGroup2 = mock(ItemGroup.class);
        itemGroup3 = mock(ItemGroup.class);

        order1 = mock(Order.class);
        order2 = mock(Order.class);
        order3 = mock(Order.class);
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
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> orderService.getOrder(15)).withMessage("The orders could not be found based on the provided orders ID: 15.");
    }

    @Test
    public void getAllOrders_happyPath() {
        when(orderRepository.getAllRecords()).thenReturn(Arrays.asList(order1, order2));
        assertThat(orderService.getAllOrders()).isEqualTo(Arrays.asList(order1, order2));
    }

    @Test
    public void getAllOrdersForCustomer_happyPath() {
        //        when(orderRepository.getFilteredRecords(orders -> orders.getCustomerId() == 1)).thenReturn(Arrays.asList(order1, order2, order3));
        when(orderRepository.getAllRecords()).thenReturn(Arrays.asList(order1, order2, order3));
        when(order1.getCustomerId()).thenReturn(1);
        when(order2.getCustomerId()).thenReturn(2);
        when(order3.getCustomerId()).thenReturn(1);

        assertThat(orderService.getAllOrdersForCustomer(1)).containsOnly(order1, order3);
        verify(customerService, times(1)).verifyIfCustomerExists(1);
    }

    @Test
    public void createOrder_happyPath() {
        when(orderRepository.addRecord(order1)).thenReturn(order1);
        when(order1.getOrderItems()).thenReturn(Arrays.asList(itemGroup1));
        assertThat(orderService.addOrder(order1)).isEqualTo(order1);
        verify(itemService, times(1)).modifyStock(Arrays.asList(itemGroup1));
    }

    @Test
    public void viewOrderItemsShippingToday() {
        when(orderRepository.getAllRecords()).thenReturn(Arrays.asList(order1, order2));
        when(order1.getOrderItems()).thenReturn(Arrays.asList(itemGroup1, itemGroup2));
        when(order2.getOrderItems()).thenReturn(Arrays.asList(itemGroup1, itemGroup2, itemGroup3));
        when(itemGroup1.getShippingDate()).thenReturn(LocalDate.now());
        when(itemGroup2.getShippingDate()).thenReturn(LocalDate.now().minusDays(1));
        when(itemGroup3.getShippingDate()).thenReturn(LocalDate.now());

        when(order1.getCustomerId()).thenReturn(1);
        when(order2.getCustomerId()).thenReturn(2);

        when(customerService.getCustomer(1)).thenReturn(customer);
        when(customerService.getCustomer(2)).thenReturn(customer2);

        Map<Customer, List<ItemGroup>> ordersShippingToday = orderService.viewOrderItemsShippingToday();

        assertThat(ordersShippingToday.get(customer)).isEqualTo(Arrays.asList(itemGroup1));
        assertThat(ordersShippingToday.get(customer2)).containsOnly(itemGroup1, itemGroup3);

    }
}