package be.llodavid.domain.customer;

import be.llodavid.domain.helperClass.Address;
import be.llodavid.domain.helperClass.EmailValidation;
import be.llodavid.domain.RepositoryRecord;

import java.util.Objects;

public class Customer implements RepositoryRecord {

    private int customerId;
    private String lastName;
    private String firstName;
    private String phonenumber;
    private String email;
    private Address address;

    private Customer(CustomerBuilder customerBuilder) {
        this.lastName = customerBuilder.lastName;
        this.address = customerBuilder.address;
        this.firstName = customerBuilder.firstName;
        this.phonenumber = customerBuilder.phonenumber;
        this.email = customerBuilder.email;
    }

    public void setId(int valueId) {
        this.customerId = valueId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\tlastName='").append(lastName).append('\'');
        sb.append(", \n\tfirstName='").append(firstName).append('\'');
        sb.append(", \n\tcity='").append(address.toString()).append('\'');
        sb.append(", \n\temail='").append(email).append('\'');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    public static class CustomerBuilder {
        private String lastName;
        private String firstName;
        private Address address;
        private String phonenumber;
        private String email;

        public Customer build() {
            if (allFieldsSet()) {
                return new Customer(this);
            }
            throw new IllegalArgumentException("Please provide all the necessary arguments.");
        }

        public static CustomerBuilder buildCustomer() {
            return new CustomerBuilder();
        }

        private boolean allFieldsSet() {
            return (isFilledIn(lastName)
                    && isFilledIn(firstName)
                    && (isFilledIn(phonenumber)
                    || isFilledIn(email)))
                    && address!=null;
        }

        private boolean isFilledIn(String field) {
            return field != null && !field.trim().equals("");
        }

        public CustomerBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder withPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
            return this;
        }

        public CustomerBuilder withEmail(String email) {
            if (!EmailValidation.isValidEmail(email)) {
                throw new IllegalArgumentException("Please provide a valid E-mail address!");
            }
            this.email = email;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomerBuilder that = (CustomerBuilder) o;
            return Objects.equals(lastName, that.lastName) &&
                    Objects.equals(firstName, that.firstName) &&
                    Objects.equals(address, that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lastName, firstName, address);
        }
    }
}
