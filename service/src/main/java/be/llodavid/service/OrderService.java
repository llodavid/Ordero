package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.order.OrderData;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class OrderService {
    private Repository<Order> orderRepository;
    @Inject
    private ShoppingService shoppingService;
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

    public Order finishOrderInShoppingCart(int customerId) {
        verifyIfCustomerExists(customerId);
//        verifyIfPaymentReceived(order.getTotalAmount(), payment);
        return orderRepository.addRecord(getOrderFromShoppingCart(customerId));
    }

    private void verifyIfCustomerExists(int customerId) {
        if (!customerService.customerExists(customerId)) {
            throw new UnknownResourceException("customer", "customer ID: " + customerId);
        }
    }

    private Order getOrderFromShoppingCart(int customerId) {
        Order newOrder = shoppingService.createOrderFromShoppingCart(customerId);
        newOrder.finishOrder(LocalDate.now());
        shoppingService.clearShoppingCart(customerId);
        return newOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllRecords();
    }

    //Unnecessary, honestly the things I waste my time on....... Let's try and do it this way later on :-].
    //    private void verifyIfPaymentReceived(BigDecimal totalAmount, BigDecimal payment) {
    //        if (!payment.equals(totalAmount)) {
    //            throw new PaymentNotReceivedException(payment);
    //        }
    //    }

    public Order createOrderFromShoppingCart(int customerId) {
        return orderRepository.addRecord(
                shoppingService.createOrderFromShoppingCart(customerId));
    }

    public String createOrderReportForCustomer(int customerId) {
        List<Order> customerOrders = orderRepository.getRecordsForValueId(customerId);

        //TODO: Finish this and tidy up in here.
        //Also,  foreach loop is a lot better here performancewise...
        StringBuilder stringBuilder = addCustomerInfoToReport(customerId);
        stringBuilder.append(
                customerOrders.stream()
                        .map(order -> addOrderInfoToReport(order))
                        .collect(Collectors.joining("\n")));

        stringBuilder.append("-------------------------\nTOTAL OF ALL ORDERS: ")
                .append(customerOrders.stream()
                        .map(order -> order.getTotalAmount())
                        .reduce(BigDecimal.ZERO,
                                (allOrdersTotal, orderTotal) -> allOrdersTotal.add(orderTotal)));
        return stringBuilder.toString();
    }

    private StringBuilder addCustomerInfoToReport(int customerId) {
        Customer customer = customerService.getCustomer(customerId);
        return new StringBuilder("Customer: ")
                .append(customer.getFirstName())
                .append(" ")
                .append(customer.getLastName())
                .append("\n-------------------------------------\n");
    }

    private String addOrderInfoToReport(Order order) {
        return new StringBuilder("ORDER n° ")
                .append(order.getId())
                .append("\n----------------\n")
                .append(order.toString())
                .append("Order Total: ")
                .append(order.getTotalAmount().toPlainString())
                .append(" euro").toString();
    }
}
