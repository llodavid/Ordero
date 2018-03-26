package be.llodavid.service;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.customer.CustomerData;
import be.llodavid.domain.Repository;
import be.llodavid.service.exceptions.UnknownResourceException;
import be.llodavid.service.exceptions.DoubleEntryException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class CustomerService {
    private Repository<Customer> customerRepository;

    @Inject @Named("CustomerRepo")
    public CustomerService(Repository<Customer> customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void injectDefaultData() {
        customerRepository.injectDefaultData(new CustomerData().getDefaultCustomers());
    }

    public Customer getCustomer(int customerID) throws UnknownResourceException {
        if (customerRepository.recordExists(customerID)) {
            return customerRepository.getRecordById(customerID);
        }
        throw new UnknownResourceException("customer", "customer ID: " + customerID);
    }

    public Customer addCustomer(Customer customer) {
        if (!customerRepository.recordAlreadyInRepository(customer)) {
            return customerRepository.addRecord(customer);
        }
        throw new DoubleEntryException("customer", String.format("%s %s", customer.getFirstName(), customer.getLastName()));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAllRecords();
    }

    public boolean customerExists(int customerId) {
        return customerRepository.recordExists(customerId);
    }
}