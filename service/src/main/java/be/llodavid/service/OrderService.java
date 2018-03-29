package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

@Named
public class OrderService {
    private Repository<Order> orderRepository;

    private CustomerService customerService;

    @Inject
    public OrderService(@Qualifier("OrderRepo") Repository<Order> orderRepository, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

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

//    public String createOrderReportForCustomer(int customerId) {
////        OrderReport orderReportCreator = new OrderReport(
////                customerService.getCustomer(customerId),
////                orderRepository.getRecordsForValueId(customerId));
////        return orderReportCreator.createOrderReport();
//    }


}
