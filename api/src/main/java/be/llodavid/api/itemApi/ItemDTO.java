package be.llodavid.api.CustomerApi;

import be.llodavid.domain.HelperClass.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {
    public int customerId;
    public String lastName;
    public String firstName;
    public String phonenumber;
    public String eMail;
    public String street;
    public String housenumber;
    public String zipcode;
    public String city;
    public String country;

    public CustomerDTO withCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerDTO withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerDTO withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerDTO withPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public CustomerDTO witheMail(String eMail) {
        this.eMail = eMail;
        return this;
    }

    CustomerDTO withAddress(Address address) {
        this.street = address.getStreet();
        this.housenumber = address.getHousenumber();
        this.zipcode = address.getZipcode();
        this.city = address.getCity();
        this.country = address.getCountry();
        return this;
    }

    Address getAddress() {
        return new Address.AddressBuilder()
                .withStreet(street)
                .withHousenumber(housenumber)
                .withZipcode(zipcode)
                .withCity(city)
                .withCountry(country)
                .build();
    }
}
