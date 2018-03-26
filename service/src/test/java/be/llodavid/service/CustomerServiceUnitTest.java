package be.llodavid.service;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.helperClass.Address;
import be.llodavid.domain.Repository;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

public class CustomerServiceUnitTest {

    private Customer customer1, customer2, customer3;
    private Repository<Customer> customerRepository;
    private CustomerService customerService;

    @Before
    public void setUp() {
        customerRepository = Mockito.mock(Repository.class);
        customerService = new CustomerService(customerRepository);
        customer1 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("David")
                .withLastName("Van den Bergh")
                .withEmail("david@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("steenweg")
                        .withHousenumber("53")
                        .withCity("Welle")
                        .withZipcode("9473")
                        .build())
                .build();
        customer2 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Piet")
                .withLastName("HuysenTruyt")
                .withEmail("KortePiet2@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("steenweg")
                        .withHousenumber("53")
                        .withCity("Welle")
                        .withZipcode("9473")
                        .build())
                .build();
        customer3 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Bruce")
                .withLastName("Whiner")
                .withEmail("BATMAN@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("steenweg")
                        .withHousenumber("53")
                        .withCity("Welle")
                        .withZipcode("9473")
                        .build())
                .build();
    }

    @Test
    public void assertThatCustomerExists_givenNonExistingCustomer_throwsException() {
//        customerRepository.addCustomer(customer2);
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customerRepository.assertThatCustomerExists(customer1.getCustomerId())).withMessage("No such customer found.");
    }

    @Test
    public void getCustomer_happyPath() {
        Mockito.when(customerRepository.getRecordById(1)).thenReturn(customer1);
        Mockito.when(customerRepository.recordExists(1)).thenReturn(true);
        Assertions.assertThat(customerService.getCustomer(1)).isEqualTo(customer1);
    }

    @Test
    public void getCustomer_givenCustomerThatDoesNotExist_throwsException() {
        Mockito.when(customerRepository.getRecordById(1)).thenReturn(customer1);
        Mockito.when(customerRepository.recordExists(1)).thenReturn(true);
        Assertions.assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(()->customerService.getCustomer(15)).withMessage("The customer could not be found based on the provided customer ID: 15.");
    }

    @Test
    public void addCustomer_happyPath() {
        Mockito.when(customerRepository.addRecord(customer1)).thenReturn(customer1);
        Assertions.assertThat(customerService.addCustomer(customer1)).isEqualTo(customer1);
    }

    @Test
    public void addCustomer_givenCustomerThatAlreadyExists_throwsException() {
        Mockito.when(customerRepository.recordAlreadyInRepository(customer1)).thenReturn(true);
        Assertions.assertThatExceptionOfType(DoubleEntryException.class).isThrownBy(()->customerService.addCustomer(customer1)).withMessage("The customer David Van den Bergh is already present in the system.");
    }

    @Test
    public void getAllCustomers_happyPath() {
        Mockito.when(customerRepository.getAllRecords()).thenReturn(Arrays.asList(customer1,customer2,customer3));
        Assertions.assertThat(customerService.getAllCustomers()).isEqualTo(Arrays.asList(customer1,customer2,customer3));
    }
}