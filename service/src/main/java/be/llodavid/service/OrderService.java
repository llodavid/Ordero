package be.llodavid.service;

import be.llodavid.domain.OrderoRepository;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.orders.OrderData;
import be.llodavid.domain.orders.OrderRepository;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class OrderService {
    private OrderRepository orderRepository;
    private CustomerService customerService;
    private ItemService itemService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.itemService = itemService;
    }

    public Order getOrder(long orderID) throws UnknownResourceException {
        return orderRepository.findById(orderID)
                .orElseThrow(()-> new UnknownResourceException("order", "order ID: " + orderID));
    }


    public List<Order> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrdersForCustomer(int customerId) {
        customerService.verifyIfCustomerExists(customerId);
//        return orderRepository.getFilteredRecords(orders -> orders.getCustomerId() == customerId);
        return getAllOrders().stream()
                .filter(order -> order.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public Order addOrder(Order order) {
        itemService.modifyStock(order.getOrderItems());
        order.finishOrder(LocalDate.now());
        return orderRepository.save(order);
    }

    public Map<Customer, List<ItemGroup>> viewOrderItemsShippingToday() {
        //TODO write a simple query for this.
        return getAllOrders().stream()
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