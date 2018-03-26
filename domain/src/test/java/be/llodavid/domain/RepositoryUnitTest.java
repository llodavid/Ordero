package be.llodavid.domain;

import be.llodavid.domain.customer.Customer;
import be.llodavid.domain.customer.CustomerData;
import be.llodavid.domain.helperClass.Address;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class RepositoryUnitTest {

    Customer customer1, customer2, customer3;
    Repository<Customer> customerRepository;

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
        Assertions.assertThat(customerRepository.getRecordById(1)).isEqualTo(customer1);
    }

    @Test
    public void getAllRecords_happyPath() {
        customerRepository.addRecord(customer1);
        customerRepository.addRecord(customer2);
        customerRepository.addRecord(customer3);
        Assertions.assertThat(customerRepository.getAllRecords())
                .containsExactlyInAnyOrder(customer1, customer2, customer3);
    }

    @Test
    public void addRecord_happyPath() {
        customerRepository.addRecord(customer1);
        Assertions.assertThat(customerRepository.getAllRecords()).contains(customer1);
    }
    @Test
    public void addRecord_givenExistingRecord_throwsException() {
        customerRepository.addRecord(customer1);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->customerRepository.addRecord(customer1))
                .withMessage("The record already exists.");
    }

    @Test
    public void assertThatRecordExists_givenNonExistingRecord_throwsException() {
        customerRepository.addRecord(customer2);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->customerRepository.assertThatRecordExists(customer1.getCustomerId()))
                .withMessage("The record with ID: 0 couldn't be found.");
    }

    @Test
    public void injectDefaultRecordData_HappyPath() {
        customerRepository.injectDefaultData(new CustomerData().getDefaultCustomers());
        Assertions.assertThat(customerRepository.getAllRecords().size()).isGreaterThan(0);
    }


}