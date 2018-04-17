package be.llodavid.service;

import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerData;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.OrderoRepository;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.util.exceptions.DoubleEntryException;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CustomerServiceUnitTest {

    private Customer customer1, customer2, customer3;
    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private CustomerData customerData;

    @Before
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerData = mock(CustomerData.class);
        customerService = new CustomerService(customerRepository);

        //TODO: write mocks for customers (damn this unit testing is a lot of work)
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

//    @Test
//    public void customerExists_givenExistingCustomer_returnTrue() {
//        when(customerRepository.existsById(1L)).thenReturn(true);
//        Assertions.assertThat(customerService.getCustomer(1)).isEqualTo(true);
//    }

//    @Test
//    public void injectDefaultData_happyPath() {
//        when(customerData.getDefaultCustomers()).thenReturn(new ArrayList<>());
//        customerService.injectDefaultData();
//        verify(customerRepository, times(1)).injectDefaultData(new CustomerData().getDefaultCustomers());
//    }

    @Test
    public void getCustomer_happyPath() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.existsById(1L)).thenReturn(true);
        Assertions.assertThat(customerService.getCustomer(1L)).isEqualTo(customer1);
    }

    @Test
    public void getCustomer_givenCustomerThatDoesNotExist_throwsException() {
//        when(customerRepository.findById(1L)).thenReturn(null);
        when(customerRepository.existsById(1L)).thenReturn(true);
        Assertions.assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> customerService.getCustomer(15)).withMessage("The customers could not be found based on the provided customers ID: 15.");
    }

    @Test
    public void addCustomer_happyPath() {
        when(customerRepository.save(customer1)).thenReturn(customer1);
        Assertions.assertThat(customerService.createCustomer(customer1)).isEqualTo(customer1);
    }

    @Test
    public void addCustomer_givenCustomerThatAlreadyExists_throwsException() {
//        when(customerRepository.recordAlreadyInRepository(customer1)).thenReturn(true);
//        Assertions.assertThatExceptionOfType(DoubleEntryException.class).isThrownBy(() -> customerService.createCustomer(customer1)).withMessage("The customers David Van den Bergh is already present in the system.");
    }

    @Test
    public void getAllCustomers_happyPath() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2, customer3));
        Assertions.assertThat(customerService.getAllCustomers()).isEqualTo(Arrays.asList(customer1, customer2, customer3));
    }
}