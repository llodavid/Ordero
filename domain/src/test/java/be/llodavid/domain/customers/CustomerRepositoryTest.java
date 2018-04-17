package be.llodavid.domain.customers;

import be.llodavid.configuration.databaseconfig.DatabaseConfig;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringJUnitConfig(DatabaseConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CustomerRepositoryTest {

    private CustomerRepository customerDBRepository;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository customerDBRepository) {
        this.customerDBRepository = customerDBRepository;
    }

    @Test
    public void createCustomer_happyPath(){
        Customer customer = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Piet")
                .withLastName("HuysenTruyt")
                .withEmail("KortePiet2@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("RockyRoqd")
                        .withHousenumber("69")
                        .withCity("FLANDERS")
                        .withZipcode("9473")
                        .build())
                .build();

        customerDBRepository.save(customer);

        assertThat(customer.getId()).isNotEqualTo(0);
    }
}