package be.llodavid.domain.customer;

import java.util.Arrays;
import java.util.List;

public class CustomerData {
    public List<Customer> getDefaultCustomers() {
        return Arrays.asList(Customer.CustomerBuilder.buildCustomer()
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
                        .build(),
                Customer.CustomerBuilder.buildCustomer()
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
                        .build(),
                Customer.CustomerBuilder.buildCustomer()
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
                        .build());
    }
}
