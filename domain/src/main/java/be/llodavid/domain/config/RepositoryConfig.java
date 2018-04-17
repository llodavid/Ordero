package be.llodavid.domain.config;

import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.OrderoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = OrderoRepository.class)
public class RepositoryConfig {
//    @Bean(name="ItemRepo")
//    public OrderoRepository<Item> itemRepository() {
//        return new OrderoRepository<>();
//    }

//    @Bean(name="CustomerRepo")
//    public OrderoRepository<Customer> customerRepository() {
//        return new OrderoRepository<Customer>();
//    }

//    @Bean(name="OrderRepo")
//    public OrderoRepository<Order> orderRepository() {
//        return new OrderoRepository<>();
//    }
}
