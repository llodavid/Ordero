package be.llodavid.api.customers;

import be.llodavid.domain.customers.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {
    public long customerId;
    public String lastName;
    public String firstName;
    public String phonenumber;
    public String eMail;
    public String street;
    public String housenumber;
    public String zipcode;
    public String city;
    public String country;

    public CustomerDTO withCustomerId(long customerId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return customerId == that.customerId &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(phonenumber, that.phonenumber) &&
                Objects.equals(eMail, that.eMail) &&
                Objects.equals(street, that.street) &&
                Objects.equals(housenumber, that.housenumber) &&
                Objects.equals(zipcode, that.zipcode) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerId, lastName, firstName, phonenumber, eMail, street, housenumber, zipcode, city, country);
    }
}
