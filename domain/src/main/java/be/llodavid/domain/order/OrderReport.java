package be.llodavid.domain.order;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.order.Order;

import java.math.BigDecimal;
import java.util.List;

//TODO: Try to create XML report
public class OrderReport {
    Customer customer;
    List<Order> customerOrders;

    public OrderReport(Customer customer, List<Order> customerOrders) {
        this.customer = customer;
        this.customerOrders = customerOrders;
    }

    public String createOrderReport() {
        StringBuilder stringBuilder = addCustomerInfoToReport();
        return stringBuilder.append(createOrderReportBody()).toString();
    }

    private StringBuilder addCustomerInfoToReport() {
        return new StringBuilder("Customer: ")
                .append(customer.getFirstName())
                .append(" ")
                .append(customer.getLastName())
                .append("\n-------------------------------------\n");
    }

    private String createOrderReportBody() {
        StringBuilder stringBuilder = new StringBuilder();
        BigDecimal allOrdersTotal = BigDecimal.ZERO;
        for (Order order : customerOrders) {
            stringBuilder.append(addOrderInfoToReport(order)).append("\n");
            allOrdersTotal.add(order.getTotalAmount());
        }
        return stringBuilder.append(createOrderReportFooter(allOrdersTotal)).toString();
    }

    private String addOrderInfoToReport(Order order) {
        return new StringBuilder("ORDER n° ")
                .append(order.getId())
                .append("\n----------------\n")
                .append(order.toString())
                .append("Order Total: ")
                .append(order.getTotalAmount().toPlainString())
                .append(" euro\n").toString();
    }

    private String createOrderReportFooter(BigDecimal allOrdersTotal) {
        return new StringBuilder()
                .append("-------------------------\nTOTAL OF ALL ORDERS: ")
                .append(allOrdersTotal).append(" EURO").toString();
    }

}
