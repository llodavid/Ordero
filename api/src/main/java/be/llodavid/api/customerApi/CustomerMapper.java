package be.llodavid.api.customerApi;

import be.llodavid.domain.customer.Customer;

import javax.inject.Named;

@Named
public class CustomerMapper {
    public CustomerDTO customerToDTO (Customer customer) {
        return new CustomerDTO()
                .withCustomerId(customer.getCustomerId())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .witheMail(customer.getEmail())
                .withPhonenumber(customer.getPhonenumber())
                .withAddress(customer.getAddress());
    }
    public Customer dtoToCustomer (CustomerDTO customerDTO) {
        return new Customer.CustomerBuilder()
                .withFirstName(customerDTO.firstName)
                .withLastName(customerDTO.lastName)
                .withPhonenumber(customerDTO.phonenumber)
                .withAddress(customerDTO.getAddress())
                .withEmail(customerDTO.eMail)
                .build();
    }
}
