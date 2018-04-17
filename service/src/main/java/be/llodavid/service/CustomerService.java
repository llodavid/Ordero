package be.llodavid.service;

import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerData;
import be.llodavid.domain.OrderoRepository;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.util.exceptions.DoubleEntryException;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(long customerID) throws UnknownResourceException {
        verifyIfCustomerExists(customerID);
        return customerRepository.findById(customerID).get();
    }

    public void verifyIfCustomerExists(long customerID) {
        if (!customerRepository.existsById(customerID)) {
            throw new UnknownResourceException("customers", "customers ID: " + customerID);
        }
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    private void verifyEntryDoesNotExistYet(Customer customer) {
        if (customerRepository.existsById(customer.getId())) {
            throw new DoubleEntryException("customers", String.format("%s %s", customer.getFirstName(), customer.getLastName()));
        }
    }
    public List<Customer> getAllCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
}
