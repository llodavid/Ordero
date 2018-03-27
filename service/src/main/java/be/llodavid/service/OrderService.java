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

//    public Order createOrderFromShoppingCart(int customerId) {
//        verifyIfCustomerExists(customerId);
//        Order order = getShoppingCartContents(customerId);
//        itemService.modifyStock(order.getOrderItems());
////        verifyIfPaymentReceived(order.getTotalAmount(), payment);
//        return orderRepository.addRecord(order);
//    }
//
//    private void verifyIfCustomerExists(int customerId) {
//        if (!customerService.customerExists(customerId)) {
//            throw new UnknownResourceException("customer", "customer ID: " + customerId);
//        }
//    }
//
//    private Order getShoppingCartContents(int customerId) {
//        Order newOrder = shoppingService.viewOrderBasedOnShoppingCart(customerId);
//        newOrder.finishOrder(LocalDate.now());
//        shoppingService.clearShoppingCart(customerId);
//        return newOrder;
//    }

    //    public Order viewOrderBasedOnShoppingCart(int customerId) {
//        return orderRepository.addRecord(
//                shoppingService.viewOrderBasedOnShoppingCart(customerId));
//    }
}
