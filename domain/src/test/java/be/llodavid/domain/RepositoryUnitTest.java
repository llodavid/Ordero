package be.llodavid.domain.Customer;

import be.llodavid.domain.HelperClass.Address;
import be.llodavid.domain.Repository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class CustomerRepositoryUnitTest {

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
    public void getCustomerById_happyPath() {
        customerRepository.addValue(customer1);
        Assertions.assertThat(customerRepository.getValueById(1)).isEqualTo(customer1);
    }

    @Test
    public void getAllCustomers_happyPath() {
        customerRepository.addValue(customer1);
        customerRepository.addValue(customer2);
        customerRepository.addValue(customer3);
        Assertions.assertThat(customerRepository.getAllValues()).containsExactlyInAnyOrder(customer1, customer2, customer3);
    }

    @Test
    public void addCustomer_happyPath() {
        customerRepository.addValue(customer1);
        Assertions.assertThat(customerRepository.getAllValues()).contains(customer1);
    }
    @Test
    public void addCustomer_givenExistingCustomer_throwsException() {
        customerRepository.addValue(customer1);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->customerRepository.addValue(customer1)).withMessage("The customer already exists.");
    }

    @Test
    public void assertThatCustomerExists_givenNonExistingCustomer_throwsException() {
        customerRepository.addValue(customer2);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->customerRepository.assertThatValueExists(customer1.getCustomerId())).withMessage("No such customer found.");
    }

    @Test
    public void injectDefaultCustomerData_HappyPath() {
        customerRepository.injectDefaultValueData(new CustomerData().getDefaultCustomers());
        Assertions.assertThat(customerRepository.getAllValues().size()).isGreaterThan(0);
    }


}