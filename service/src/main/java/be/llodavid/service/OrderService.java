package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;

import java.time.LocalDate;
import java.util.List;
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
        return orderRepository.getAllRecords().stream()
                .filter(order -> order.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public Order createOrder(Order order) {
        itemService.modifyStock(order.getOrderItems());
        order.finishOrder(LocalDate.now());
        return orderRepository.addRecord(order);
    }
}
