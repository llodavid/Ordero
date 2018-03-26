package be.llodavid.api.config;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.Order;
import be.llodavid.domain.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Repository.class)
public class RepositoryConfig {
    @Bean(name="ItemRepo")
    public Repository<Item> itemRepository() {
        return new Repository<>();
    }

    @Bean(name="CustomerRepo")
    public Repository<Customer> customerRepository() {
        return new Repository<>();
    }

    @Bean(name="OrderRepo")
    public Repository<Order> orderRepository() {
        return new Repository<>();
    }

}
