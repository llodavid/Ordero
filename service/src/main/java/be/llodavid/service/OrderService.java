package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.domain.order.OrderReport;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.List;

@Named
public class OrderService {
    private Repository<Order> orderRepository;
    @Inject
    private CustomerService customerService;

    @Inject
    @Named("OrderRepo")
    public OrderService(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    //TODO:ask Niels how to fix this constructor... Cannot get it to work!
//    @Inject
//    @Named("OrderRepo")
//    public OrderService(Repository<Order> orderRepository, ShoppingService shoppingService, CustomerService customerService) {
//        this.orderRepository = orderRepository;
//        this.shoppingService = shoppingService;
//        this.customerService = customerService;
//    }

    public void injectDefaultData() {
        orderRepository.injectDefaultData(new OrderData().getDefaultOrders());
    }

    public Order getOrder(int orderID) throws UnknownResourceException {
        if (orderRepository.recordExists(orderID)) {
            return orderRepository.getRecordById(orderID);
        }
        throw new UnknownResourceException("order", "order ID: " + orderID);
    }
    public List<Order> getAllOrders() {
        return orderRepository.getAllRecords();
    }

    public String createOrderReportForCustomer(int customerId) {
        OrderReport orderReportCreator = new OrderReport(
                customerService.getCustomer(customerId),
                orderRepository.getRecordsForValueId(customerId));
        return orderReportCreator.createOrderReport();
    }


}
