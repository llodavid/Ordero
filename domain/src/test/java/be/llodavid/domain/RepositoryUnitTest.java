package be.llodavid.domain;

import be.llodavid.configuration.databaseconfig.DatabaseConfig;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerData;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.util.exceptions.OrderoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringJUnitConfig(DatabaseConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)

public class RepositoryUnitTest {

    private Customer customer1, customer2, customer3;
    private CustomerRepository customerRepository;

    @Autowired
    public RepositoryUnitTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setUp() throws Exception {
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
        customerRepository.save(customer1);
        assertThat(customerRepository.findById(customer1.getId())).isEqualTo(customer1);
    }

    @Test
    public void getAllRecords_happyPath() {
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        assertThat(customerRepository.findAll())
                .contains(customer1, customer2, customer3);
    }

    @Test
    public void save_happyPath() {
        customerRepository.save(customer1);
        assertThat(customerRepository.findAll()).contains(customer1);
    }
    @Test
    public void save_givenExistingRecord_throwsException() {
        customerRepository.save(customer1);
        assertThatExceptionOfType(OrderoException.class)
                .isThrownBy(()->customerRepository.save(customer1))
                .withMessage("This customer record already exists in the database.");
    }

//    @Test
//    public void assertThatRecordExists_givenNonExistingRecord_throwsException() {
//        customerRepository.save(customer2);
//        assertThatExceptionOfType(OrderoException.class)
//                .isThrownBy(()->customerRepository(customer1.getId()))
//                .withMessage("The record with ID: 0 couldn't be found.");
//    }

//    @Test
//    public void clear_happyPath() {
//        customerRepository.save(customer2);
//        customerRepository.deleteAll();
//        assertThat(customerRepository.findAll().size()).isEqualTo(0);
//    }

}