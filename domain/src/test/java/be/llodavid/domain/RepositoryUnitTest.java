package be.llodavid.domain;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.customer.CustomerData;
import be.llodavid.domain.customer.Address;
import be.llodavid.util.exceptions.OrderoException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class RepositoryUnitTest {

    private Customer customer1, customer2, customer3;
    private Repository<Customer> customerRepository;

    @Before
    public void setUp() throws Exception {
        customerRepository = new Repository<>();
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
    public void getRecordById_happyPath() {
        customerRepository.addRecord(customer1);
        assertThat(customerRepository.getRecordById(1)).isEqualTo(customer1);
    }

    @Test
    public void getAllRecords_happyPath() {
        customerRepository.addRecord(customer1);
        customerRepository.addRecord(customer2);
        customerRepository.addRecord(customer3);
        assertThat(customerRepository.getAllRecords())
                .containsExactlyInAnyOrder(customer1, customer2, customer3);
    }

    @Test
    public void addRecord_happyPath() {
        customerRepository.addRecord(customer1);
        assertThat(customerRepository.getAllRecords()).contains(customer1);
    }
    @Test
    public void addRecord_givenExistingRecord_throwsException() {
        customerRepository.addRecord(customer1);
        assertThatExceptionOfType(OrderoException.class)
                .isThrownBy(()->customerRepository.addRecord(customer1))
                .withMessage("The customer already exists in the database.");
    }

    @Test
    public void assertThatRecordExists_givenNonExistingRecord_throwsException() {
        customerRepository.addRecord(customer2);
        assertThatExceptionOfType(OrderoException.class)
                .isThrownBy(()->customerRepository.assertThatRecordExists(customer1.getId()))
                .withMessage("The record with ID: 0 couldn't be found.");
    }

    @Test
    public void injectDefaultRecordData_happyPath() {
        customerRepository.injectDefaultData(new CustomerData().getDefaultCustomers());
        assertThat(customerRepository.getAllRecords().size()).isGreaterThan(0);
    }

    @Test
    public void clear_happyPath() {
        customerRepository.addRecord(customer2);
        customerRepository.clear();
        assertThat(customerRepository.getAllRecords().size()).isEqualTo(0);
    }

    @Test
    public void updateRecord() {
        customerRepository.addRecord(customer2);
        int customerId = customer2.getId();
        customerRepository.updateRecord(customer3, customerId);

        assertThat(customerRepository.getRecordById(customerId)).isEqualTo(customer3);
    }
}