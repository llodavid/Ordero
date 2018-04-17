package be.llodavid.api.customers;

import be.llodavid.api.TestApplication;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() throws Exception {
        clearAndFlushTables();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        customer = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("David")
                .withLastName("From the Mountain")
                .withEmail("david@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("BrickRoad")
                        .withHousenumber("53")
                        .withCity("Welle")
                        .withZipcode("9473")
                        .build())
                .build();
        customerDTO = customerMapper.customerToDTO(customer);

    }
    public void clearAndFlushTables() {
        customerRepository.deleteAll();
    }

    @Test
    public void createCustomer_happyPath() {
        CustomerDTO createdCustomer = new TestRestTemplate()
                .postForObject(String.format("http://localhost:%s/%s", port, "customers"), customerDTO, CustomerDTO.class);

        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.firstName).isEqualTo("David");
        assertThat(createdCustomer.lastName).isEqualTo("From the Mountain");
        assertThat(createdCustomer.eMail).isEqualTo("david@hotmail.com");
    }

    @Test
    public void getCustomer_happyPath() {
        CustomerDTO createdCustomer = new TestRestTemplate()
                .postForObject(String.format("http://localhost:%s/%s", port, "customers"), customerDTO, CustomerDTO.class);
        ResponseEntity<CustomerDTO> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "customers",createdCustomer.customerId), CustomerDTO.class);

        CustomerDTO customerDTO2 = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customerDTO2).isNotNull();
        assertThat(customerDTO2).isEqualTo(createdCustomer);
    }

    @Test
    public void getCustomers_happyPath() {
        //customerRepository.save(customer);
        CustomerDTO createdCustomer = new TestRestTemplate()
                .postForObject(String.format("http://localhost:%s/%s", port, "customers"), customerDTO, CustomerDTO.class);

        ResponseEntity<CustomerDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s", port, "customers"), CustomerDTO[].class);

        List<CustomerDTO> customerList = Arrays.asList(response.getBody());
        assertThat(customerList).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customerList).hasSize(1);
        assertThat(customerList).contains(createdCustomer);
    }
}