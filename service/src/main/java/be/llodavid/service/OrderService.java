package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Named
public class OrderService {
    private Repository<Order> orderRepository;
    private CustomerService customerService;
    private ItemService itemService;

    @Inject
    public OrderService(@Qualifier("OrderRepo") Repository<Order> orderRepository, CustomerService customerService, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.itemService = itemService;
    }

    public void injectDefaultData() {
        orderRepository.injectDefaultData(new OrderData().getDefaultOrders());
    }

    public Order getOrder(int orderID) throws UnknownResourceException {
        verifyIfOrderExists(orderID);
        return orderRepository.getRecordById(orderID);
    }

    public void verifyIfOrderExists(int orderID) {
        if (!orderRepository.recordExists(orderID)) {
            throw new UnknownResourceException("order", "order ID: " + orderID);
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllRecords();
    }

    public List<Order> getAllOrdersForCustomer(int customerId) {
        customerService.verifyIfCustomerExists(customerId);
        //Todo: find a way to mock predicate for testing purposes.
//        return orderRepository.getFilteredRecords(order -> order.getCustomerId() == customerId);
        return orderRepository.getAllRecords().stream()
                .filter(order -> order.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public Order createOrder(Order order) {
        itemService.modifyStock(order.getOrderItems());
        order.finishOrder(LocalDate.now());
        return orderRepository.addRecord(order);
    }

    public Map<Customer, List<ItemGroup>> viewOrderItemsShippingToday() {
        return orderRepository.getAllRecords().stream()
                .filter(order -> isOneOfTheItemGroupsShippingToday(order))
                .collect(Collectors.toMap(
                        order -> getCustomer(order),
                        order -> itemsShippingToday(order)));
    }

    private boolean isOneOfTheItemGroupsShippingToday(Order order) {
        return order.getOrderItems().stream()
                .anyMatch(itemGroupShippingToday());
    }

    private Predicate<ItemGroup> itemGroupShippingToday() {
        return itemGroup -> itemGroup.getShippingDate().equals(LocalDate.now());
    }

    private Customer getCustomer(Order order) {
        return customerService.getCustomer(order.getCustomerId());
    }

    private List<ItemGroup> itemsShippingToday(Order order) {
        return order.getOrderItems().stream()
                .filter(itemGroupShippingToday())
                .collect(Collectors.toList());
    }
}