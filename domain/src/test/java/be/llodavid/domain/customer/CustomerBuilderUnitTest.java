package be.llodavid.domain.customer;

import be.llodavid.domain.helperClass.Address;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CustomerBuilderUnitTest {

    @Test
    public void build_givenAllRequiredFields_returnCustomerObject() {
        Customer customer = Customer.CustomerBuilder.buildCustomer()
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
        Assertions.assertThat(customer.toString()).isEqualTo("\tlastName='Van den Bergh', \n" +
                "\tfirstName='David', \n" +
                "\tcity='\tcity='Welle', \n" +
                "\tstreet='steenweg', \n" +
                "\thousenumber='53', \n" +
                "\tzipcode='9473'', \n" +
                "\temail='david@hotmail.com'");
    }

    @Test
    public void build_givenMissingRequiredFields_throwsException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->Customer.CustomerBuilder.buildCustomer()
                        .withFirstName("David")
                        .withLastName("Van den Bergh")
                        .build());
    }
}