package be.llodavid.domain.order;

import be.llodavid.domain.Repository;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class OrderRepository extends Repository<Order> {
    public List<Order> getRecordsForSpecificCustomer(int customerId) {
        return getAllRecords().stream()
        .filter(order->order.getCustomerId()==customerId)
        .collect(Collectors.toList());
    }
}
