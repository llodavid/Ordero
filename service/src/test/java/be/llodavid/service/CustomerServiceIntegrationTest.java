package be.llodavid.service;

import be.llodavid.configuration.databaseconfig.DatabaseConfig;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.Customer;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringJUnitConfig(DatabaseConfig.class)
@DataJpaTest
//@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = NONE)
class CustomerServiceIntegrationTest {
    private Customer customer3, customer1, customer2;
    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customer1 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("David")
                .withLastName("Van den Bergh")
                .withEmail("david@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("steenweg")
                        .withHousenumber("65s")
                        .withCity("FLANDERS")
                        .withZipcode("1193")
                        .build())
                .build();
        customer2 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Bruce")
                .withLastName("Whiner")
                .withEmail("BATMAN@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("BatCave")
                        .withHousenumber("99")
                        .withCity("Gent")
                        .withZipcode("9000")
                        .build())
                .build();

        customer3 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Jack")
                .withLastName("Donaghy")
                .withEmail("KortePiet2@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("RockyRoad")
                        .withHousenumber("69")
                        .withCity("FLANDERS")
                        .withZipcode("9473")
                        .build())
                .build();
    }

    @Test
    void getCustomer() {
        customerService.createCustomer(customer3);
        assertThat(customerService.getCustomer(customer3.getId())).isNotEqualTo(Optional.empty());
    }

    @Test
    void verifyIfCustomerExists_givenNonExistingCustomer_throwsException() {
        assertThatExceptionOfType(UnknownResourceException.class)
        .isThrownBy(()->customerService.verifyIfCustomerExists(0));
    }

    @Test
    void createCustomer_happyPath() {
        customerService.createCustomer(customer3);
        assertThat(customer3.getId()).isNotEqualTo(0);
    }

    @Test
    void getAllCustomers() {
        customerService.createCustomer(customer1);
        customerService.createCustomer(customer2);
        customerService.createCustomer(customer3);

        assertThat(customerService.getAllCustomers()).contains(customer1,customer2,customer3);
    }

}