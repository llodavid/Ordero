package be.llodavid.api.customers;

import be.llodavid.api.TestApplication;
import be.llodavid.domain.Repository;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Inject
    @Qualifier("CustomerRepo")
    private Repository<Customer> customerRepository;
    @Inject
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerDTO customerDTO;
//    @Inject
//    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
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
        customerRepository.addRecord(customer);
    }

    //TODO: discuss with Niels - first integration tests, are these OK?
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
        ResponseEntity<CustomerDTO> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "customers",customer.getId()), CustomerDTO.class);

        CustomerDTO customerDTO2 = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customerDTO2).isNotNull();
        assertThat(customerDTO2.customerId).isEqualTo(customer.getId());
        assertThat(customerDTO2.firstName).isEqualTo("David");
        assertThat(customerDTO2.lastName).isEqualTo("From the Mountain");
        assertThat(customerDTO2.eMail).isEqualTo("david@hotmail.com");
    }

    @Test
    public void getCustomers_happyPath() {
        ResponseEntity<CustomerDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s", port, "customers"), CustomerDTO[].class);

        List<CustomerDTO> customerList = Arrays.asList(response.getBody());
        assertThat(customerList).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomerDTO customerDTO2 = customerList.get(customerList.size()-1);
        assertThat(customerDTO2.customerId).isEqualTo(customerList.size());
        assertThat(customerDTO2.firstName).isEqualTo("David");
        assertThat(customerDTO2.lastName).isEqualTo("From the Mountain");
        assertThat(customerDTO2.eMail).isEqualTo("david@hotmail.com");
    }
}