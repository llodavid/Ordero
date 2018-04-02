package be.llodavid.service;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.customer.CustomerData;
import be.llodavid.domain.Repository;
import be.llodavid.util.exceptions.DoubleEntryException;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class CustomerService {
    private Repository<Customer> customerRepository;

    @Inject
    public CustomerService(@Qualifier("CustomerRepo")Repository<Customer> customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void injectDefaultData() {
        customerRepository.injectDefaultData(new CustomerData().getDefaultCustomers());
    }

    public Customer getCustomer(int customerID) throws UnknownResourceException {
        verifyIfCustomerExists(customerID);
        return customerRepository.getRecordById(customerID);
    }

    public void verifyIfCustomerExists(int customerID) {
        if (!customerRepository.recordExists(customerID)) {
            throw new UnknownResourceException("customer", "customer ID: " + customerID);
        }
    }

    public Customer addCustomer(Customer customer) {
        verifyEntryDoesNotExistYet(customer);
        return customerRepository.addRecord(customer);

    }

    private void verifyEntryDoesNotExistYet(Customer customer) {
        if (customerRepository.recordAlreadyInRepository(customer)) {
            throw new DoubleEntryException("customer", String.format("%s %s", customer.getFirstName(), customer.getLastName()));
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAllRecords();
    }

    public boolean customerExists(int customerId) {
        return customerRepository.recordExists(customerId);
    }
}
